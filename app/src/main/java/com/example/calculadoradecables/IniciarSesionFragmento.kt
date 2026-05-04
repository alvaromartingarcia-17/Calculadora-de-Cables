package com.example.calculadoradecables

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.calculadoradecables.databinding.PantallaInicioSesionBinding
import com.example.calculadoradecables.modelosDataClass.Usuario
import com.example.calculadoradecables.modelosDataClass.UsuarioRespuesta
import com.example.calculadoradecables.viewmodel.CableViewModel
import com.google.android.material.snackbar.Snackbar
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.getValue

class IniciarSesionFragmento : Fragment() {
    private var _binding: PantallaInicioSesionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CableViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PantallaInicioSesionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.botonInicioSesion.setOnClickListener {
            comprobacionInciarSesion(false)
        }
        binding.botonCrearUsuario.setOnClickListener {
            comprobacionInciarSesion(true)
        }
    }

    private fun comprobacionInciarSesion(crear: Boolean) {
        val nombreUsuario = binding.editTextNombre.text.toString().trim()
        val contraseña = binding.editTextContrasenia.text.toString().trim()
        val correo = binding.editTextCorreo.text.toString().trim()


        // Validar campos vacíos
        if (nombreUsuario.isEmpty()) {
            binding.errorNombre.text = "Ingrese su Usuario"
            binding.errorNombre.visibility = View.VISIBLE
            return
        } else {
            binding.errorNombre.visibility = View.GONE
        }

        if (contraseña.isEmpty()) {
            binding.errorContrasenia.text = "Ingrese su contraseña"
            binding.errorContrasenia.visibility = View.VISIBLE
            return
        } else {
            binding.errorContrasenia.visibility = View.GONE
        }

        if (correo.isEmpty()) {
            binding.errorCorreo.text = "Ingrese su correo"
            binding.errorCorreo.visibility = View.VISIBLE
            return
        } else {
            binding.errorCorreo.visibility = View.GONE
        }

        // Login admin
        if (nombreUsuario == "admin" && contraseña == "admin" && correo == "admin") {
            if (crear) {
                binding.errorNombre.text = "No puedes crear esta cuenta"
                binding.errorNombre.visibility = View.VISIBLE
            }
            return
        }

        if (!correo.matches(Regex("^[a-z|0-9]+@(gmail.com|hotmail.com)$"))) {
            binding.errorCorreo.text = "Formato de correo incorrecto"
            binding.errorCorreo.visibility = View.VISIBLE
            return
        }
        buscarUsuario(correo, nombreUsuario, crear, contraseña)
    }

    private fun buscarUsuario(
        correo: String,
        nombreUsuario: String,
        crear: Boolean,
        contraseña: String,
    ) {
        val usuario = Usuario(nombreUsuario, correo, contraseña)
        RetrofitClient.instance.iniciarsesion("iniciarsesion", usuario)
            .enqueue(object : Callback<UsuarioRespuesta> {

                override fun onResponse(
                    call: Call<UsuarioRespuesta>,
                    response: Response<UsuarioRespuesta>
                ) {
                    if (response.isSuccessful) {

                        if (response.body()!!.success) {
                            if (crear){
                                binding.errorCorreo.text = "Correo ya usado"
                                binding.errorCorreo.visibility = View.VISIBLE
                            }else {
                                binding.errorNombre.visibility = View.GONE
                                binding.errorCorreo.visibility = View.GONE
                                binding.errorContrasenia.visibility = View.GONE
                                viewModel.meterdatos("usuario",usuario.correo)
                                view?.post {
                                    findNavController().navigate(R.id.menuPrincipalFragmento)
                                }
                            }
                        } else {
                            when (response.body()!!.error) {
                                "usuario_no_encontrado" -> {
                                    if (crear) {
                                        usuarioNoEncontrado(
                                            correo,
                                            nombreUsuario,
                                            contraseña
                                        )
                                    } else {
                                        binding.errorNombre.text = "Usuario no encontrado"
                                        binding.errorNombre.visibility = View.VISIBLE
                                    }
                                }

                                "contraseña_incorrecta" -> {
                                    binding.errorContrasenia.text = "Contraseña incorrecta"
                                    binding.errorContrasenia.visibility = View.VISIBLE
                                }

                                "usuario_erroneo" -> {
                                    binding.errorNombre.text = "Nombre de usuario incorrecto"
                                    binding.errorNombre.visibility = View.VISIBLE
                                }

                                else -> {
                                    Log.d(
                                        "nose",
                                        "Error responde: ${response.errorBody().toString()}"
                                    )
                                    Log.d("nose", "Error body: ${response.body().toString()}")
                                    Log.d("nose", "Error algo: ${response.raw()}")
                                    mostrarSnackbar("Error, intentelo de nuevo")
                                }
                            }
                        }

                    } else {
                        mostrarSnackbar("Error intentelo de nuevo")
                    }
                }


                override fun onFailure(call: Call<UsuarioRespuesta>, t: Throwable) {
                    Log.d("nose", "Error responde: ${t.message}")
                    Log.d("nose", "Error responde 2: ${t.cause}")

                    mostrarSnackbar("Error de conexión")
                }
            })
    }

    private fun usuarioNoEncontrado(
        correo: String,
        nombreUsuario: String,
        contraseña: String,
    ) {
        val nuevoUsuario = Usuario(nombreUsuario, correo, contraseña)
        RetrofitClient.instance.crearusuario("crearusuario", nuevoUsuario)
            .enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Log.d("nose", "Usuario creado correctamente")
                    viewModel.meterdatos("usuario",nuevoUsuario.correo)
                    view?.post {
                        findNavController().navigate(R.id.menuPrincipalFragmento)
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    mostrarSnackbar("Error de conexión")
                    Log.e("nose", "Error al crear usuario: ${t.message}")
                }
            })
    }

    private fun mostrarSnackbar(mensaje: String) {
        val snackbar = Snackbar.make(binding.root, mensaje, Snackbar.LENGTH_LONG)
        val snackbarText =
            snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        snackbarText.textSize = 24f
        snackbarText.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        snackbar.view.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.black
            )
        )
        snackbar.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}