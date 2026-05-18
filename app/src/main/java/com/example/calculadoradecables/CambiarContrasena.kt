package com.example.calculadoradecables

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CambiarContrasena : AppCompatActivity() {
    private lateinit var etPassword: EditText
    private lateinit var etPasswordConfirm: EditText
    private lateinit var btnReset: Button
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recuperar_contrasena)

        etPassword        = findViewById(R.id.etPassword)
        etPasswordConfirm = findViewById(R.id.etPasswordConfirm)
        btnReset          = findViewById(R.id.btnReset)

        token = intent?.data?.getQueryParameter("token")

        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Token inválido", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        btnReset.setOnClickListener {
            val pass1 = etPassword.text.toString().trim()
            val pass2 = etPasswordConfirm.text.toString().trim()

            when {
                pass1.isEmpty() || pass2.isEmpty() ->
                    Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                pass1.length < 6 ->
                    Toast.makeText(this, "Mínimo 6 caracteres", Toast.LENGTH_SHORT).show()
                pass1 != pass2 ->
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                else ->
                    resetPassword(token!!, pass1)
            }
        }
    }

    private fun resetPassword(token: String, nuevaContrasena: String) {
        btnReset.isEnabled = false

        val body = RequestBody.create(
            "application/json".toMediaType(),
            JSONObject().apply {
                put("token", token)
                put("CONTRASEÑA", nuevaContrasena)
            }.toString()
        )

        RetrofitClient.instance.resetearContrasena("resetear", body)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    val json = JSONObject(response.body()!!.string())
                    if (json.optBoolean("success")) {
                        Toast.makeText(this@CambiarContrasena,
                            "Contraseña actualizada, ya puedes iniciar sesión",
                            Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        btnReset.isEnabled = true
                        Toast.makeText(this@CambiarContrasena,
                            json.optString("error", "Error desconocido"),
                            Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    btnReset.isEnabled = true
                    Toast.makeText(this@CambiarContrasena,
                        "Error de red", Toast.LENGTH_LONG).show()
                }
            })
    }
}