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

class IntroducirCorreo : AppCompatActivity() {

    private lateinit var etCorreo: EditText
    private lateinit var btnEnviar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.introducircorreo)

        etCorreo  = findViewById(R.id.etCorreo)
        btnEnviar = findViewById(R.id.btnEnviar)

        btnEnviar.setOnClickListener {
            val correo = etCorreo.text.toString().trim()
            if (correo.isEmpty()) {
                Toast.makeText(this, "Introduce tu correo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            enviarCorreo(correo)
        }
    }

    private fun enviarCorreo(correo: String) {
        btnEnviar.isEnabled = false

        val body = RequestBody.create(
            "application/json".toMediaType(),
            JSONObject().apply { put("CORREO", correo) }.toString()
        )

        RetrofitClient.instance.olvidarContrasena("borrarcontraseña", body)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    btnEnviar.isEnabled = true
                    Toast.makeText(this@IntroducirCorreo,
                        "Si el correo existe recibirás instrucciones",
                        Toast.LENGTH_LONG).show()
                    finish()
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    btnEnviar.isEnabled = true
                    Toast.makeText(this@IntroducirCorreo,
                        "Error de red: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
    }
}