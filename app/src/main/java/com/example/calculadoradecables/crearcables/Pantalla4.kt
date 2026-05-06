package com.example.calculadoradecables.crearcables

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.calculadoradecables.viewmodel.CableViewModel
import androidx.navigation.fragment.findNavController
import com.example.calculadoradecables.MainActivity
import com.example.calculadoradecables.R
import com.example.calculadoradecables.RetrofitClient
import com.example.calculadoradecables.databinding.PantallaDatosCable4Binding
import com.example.calculadoradecables.modelosDataClass.Cable
import com.google.android.material.snackbar.Snackbar
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Pantalla4 : Fragment() {
    private var _binding: PantallaDatosCable4Binding? = null
    private val binding get() = _binding!!

    var esValido = false

    private val viewModel: CableViewModel by activityViewModels()

    private var listalocalizacion: MutableList<String> = mutableListOf()
    private var listaconductor: MutableList<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PantallaDatosCable4Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        RetrofitClient.instance.localizacion("localizacion")
            .enqueue(object : Callback<MutableList<String>> {
                override fun onResponse(
                    call: Call<MutableList<String>>,
                    response: Response<MutableList<String>>
                ) {
                    if (response.isSuccessful) {
                        Log.d("nose", response.body().toString())
                        listalocalizacion = response.body()!!
                        val adapter = ArrayAdapter(
                            requireContext(),
                            R.layout.spinner_layout_nuevo,
                            response.body()!!
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
                        binding.spinner7.adapter = adapter

                        if (viewModel.datoscable.value?.get("sensibilidadiferencial") != null) {
                            val posicion =
                                listalocalizacion.indexOf(viewModel.datoscable.value?.get("sensibilidadiferencial"))
                            Log.d("nose","posicion array $posicion")
                            if (posicion >= 0) {
                                binding.spinner7.setSelection(posicion)
                                binding.editTextFactorCorreccion.setText(
                                    viewModel.datoscable.value?.get(
                                        "factorcorreccionlugar"
                                    )
                                )
                                binding.editTextFactorCorreccion2.setText(
                                    viewModel.datoscable.value?.get(
                                        "factorcorrecciontipo"
                                    )
                                )
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<MutableList<String>>, t: Throwable) {
                    Log.e("nose", "Error onfailure: ${t.message}")
                    mostrarSnackbar("Problemas de conexión")
                }
            })

        RetrofitClient.instance.localizacion("tipoconductor")
            .enqueue(object : Callback<MutableList<String>> {
                override fun onResponse(
                    call: Call<MutableList<String>>,
                    response: Response<MutableList<String>>
                ) {
                    if (response.isSuccessful) {
                        Log.d("nose", response.body().toString())
                        val lista1 = response.body()!!.take(2)
                        val lista2 = response.body()!!.takeLast(2)

                        listaconductor = response.body()!!
                        val adapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            lista1
                        )
                        val adapter2 = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            lista2
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.spinner5?.adapter = adapter
                        binding.spinner6?.adapter = adapter2

                        if (viewModel.datoscable.value?.get("tipoconductor") != null) {
                            val posicion = listaconductor.indexOf(viewModel.datoscable.value?.get("tipoconductor"))
                            val posicion2 = listaconductor.indexOf(viewModel.datoscable.value?.get("tipoconductor2")) - 2
                            Log.d("nose","posicion array $posicion")
                            Log.d("nose","posicion2 array $posicion2")

                            if (posicion >= 0) {
                                binding.spinner5?.setSelection(posicion)
                                binding.spinner6?.setSelection(posicion2)
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<MutableList<String>>, t: Throwable) {
                    Log.e("nose", "Error onfailure: ${t.message}")
                    mostrarSnackbar("Problemas de conexión")
                }
            })

        binding.botonSiguiente.setOnClickListener {

            validarPantalla4()
            if (esValido) {
                viewModel.meterdatos("tipoconductor", binding.spinner5?.selectedItem.toString())
                viewModel.meterdatos("tipoconductor2", binding.spinner6?.selectedItem.toString())
                viewModel.meterdatos("localizacioncanalizacion", binding.spinner7.selectedItem.toString())
                viewModel.meterdatos("factorcorreccionlugar", binding.editTextFactorCorreccion.text.toString())
                viewModel.meterdatos("factorcorrecciontipo", binding.editTextFactorCorreccion2.text.toString())

                val nuevocable : Cable = viewModel.cable()!!
                Log.d("nose", "Datos del cable del view model Pantalla3 Siguiente: ${viewModel.depuraciondatos()}")
                Log.d("nose", "Datos del cable del nuevocable: $nuevocable")

                RetrofitClient.instance.crearcable("nuevocable",nuevocable.usuario, nuevocable)
                    .enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            if (response.isSuccessful) {
                                val body = response.body()?.string()
                                Log.d("nose", "Respuesta body: $body")
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Log.e("nose", "Error onfailure: ${t.message}")
                            mostrarSnackbar("Problemas de conexión")
                        }
                    })
            }
        }

        binding.botonAtras.setOnClickListener {
            viewModel.meterdatos("tipoconductor", binding.spinner5?.selectedItem.toString())
            viewModel.meterdatos("tipoconductor2", binding.spinner6?.selectedItem.toString())
            viewModel.meterdatos("localizacioncanalizacion", binding.spinner7.selectedItem.toString())
            viewModel.meterdatos("factorcorreccionlugar", binding.editTextFactorCorreccion.text.toString())
            viewModel.meterdatos("factorcorrecciontipo", binding.editTextFactorCorreccion2.text.toString())

            Log.d("nose", "Datos del cable del view model Pantalla3 Siguiente: ${viewModel.depuraciondatos()}")
            findNavController().navigate(R.id.pantalla3)
        }
    }

    // Comprobación datos
    fun validarPantalla4() {
        val factorCorreccionStr = binding.editTextFactorCorreccion.text.toString().trim()
        val factorCorreccionStr2 = binding.editTextFactorCorreccion2.text.toString().trim()

        esValido = true

        // FACTOR CORRECCIÓN LUGAR
        val factorCorreccion = factorCorreccionStr.toDoubleOrNull()
        if (factorCorreccionStr.isEmpty()) {
            binding.error9.text = "Introduce el factor corrección del lugar"
            binding.error9.visibility = View.VISIBLE
            esValido = false
        } else {
            if (factorCorreccion == null || factorCorreccion <= 0) {
                binding.error9.text = "Valor inválido"
                binding.error9.visibility = View.VISIBLE
                esValido = false
            } else {
                binding.error9.visibility = View.GONE
            }
        }

        // FACTOR CORRECCIÓN TIPO
        val factorCorreccion2 = factorCorreccionStr2.toDoubleOrNull()
        if (factorCorreccionStr2.isEmpty()) {
            binding.error10.text = "Introduce el factor de correción del tipo"
            binding.error10.visibility = View.VISIBLE
            esValido = false
        } else {
            if (factorCorreccion2 == null || factorCorreccion2 <= 0) {
                binding.error10.text = "Valor inválido"
                binding.error10.visibility = View.VISIBLE
                esValido = false
            } else {
                binding.error10.visibility = View.GONE
            }
        }
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

        // Liberamos el binding
        _binding = null
    }
}