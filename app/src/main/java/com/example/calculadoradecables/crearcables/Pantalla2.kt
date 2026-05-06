package com.example.calculadoradecables.crearcables

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.example.calculadoradecables.viewmodel.CableViewModel
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.calculadoradecables.R
import com.example.calculadoradecables.RetrofitClient
import com.example.calculadoradecables.databinding.PantallaDatosCable2Binding
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Pantalla2 : Fragment() {
    private var _binding: PantallaDatosCable2Binding? = null
    private val binding get() = _binding!!

    var esValido = false

    private val viewModel: CableViewModel by activityViewModels()

    private var listatensionmax: MutableList<Double> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PantallaDatosCable2Binding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        RetrofitClient.instance.caidatensionmax("caidatensionmaxima")
            .enqueue(object : Callback<MutableList<Double>> {
                override fun onResponse(
                    call: Call<MutableList<Double>>,
                    response: Response<MutableList<Double>>
                ){
                    if (response.isSuccessful) {
                        Log.d("nose", response.body().toString())
                        listatensionmax = response.body()!!
                        val adapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            response.body()!!
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.spinner2.adapter = adapter

                        if (viewModel.datoscable.value?.get("caidatensionmax") != null) {
                            val posicion = listatensionmax.indexOf(viewModel.datoscable.value?.get("caidatensionmax")?.toDouble())
                            Log.d("nose","posicion array $posicion")
                            if (posicion >= 0){
                                binding.editTextLongitud.setText(viewModel.datoscable.value?.get("longitud").orEmpty())
                                binding.spinner2.setSelection(posicion)
                                binding.editTextProteccionSelec.setText(viewModel.datoscable.value?.get("proteccionseleccionada").orEmpty())
                                binding.editTextProteccionTermica.setText(viewModel.datoscable.value?.get("protecciontermica").orEmpty())
                                // CAMBIADO A LA PANTALLA 3
                                // binding.editTextProteccionDiferencial.setText(viewModel.datoscable.value?.get("protecciondiferencial").orEmpty())
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<MutableList<Double>>, t: Throwable) {
                    Log.e("nose", "Error onfailure: ${t.message}")
                    mostrarSnackbar("Problemas de conexión")
                }
            })

        binding.botonSiguiente.setOnClickListener {
            validarPantalla2()
            if (esValido) {
                viewModel.meterdatos("longitud", binding.editTextLongitud.text.toString())
                viewModel.meterdatos("caidatensionmax",binding.spinner2.selectedItem.toString())
                viewModel.meterdatos("proteccionseleccionada",binding.editTextProteccionSelec.text.toString())
                viewModel.meterdatos("protecciontermica",binding.editTextProteccionTermica.text.toString())
                // CAMBIADO A LA PANTALLA 3
                // viewModel.meterdatos("protecciondiferencial",binding.editTextProteccionDiferencial.text.toString())

                Log.d(
                    "nose",
                    "Datos del cable del view model Pantalla2 Siguiente: ${viewModel.depuraciondatos()}"
                )

                findNavController().navigate(R.id.pantalla3)
            }
        }

        binding.botonAtras.setOnClickListener {
            viewModel.meterdatos("longitud", binding.editTextLongitud.text.toString())
            viewModel.meterdatos("caidatensionmax",binding.spinner2.selectedItem.toString())
            viewModel.meterdatos("proteccionseleccionada",binding.editTextProteccionSelec.text.toString())
            viewModel.meterdatos("protecciontermica",binding.editTextProteccionTermica.text.toString())
            // CAMBIADO A LA PANTALLA 3
            // viewModel.meterdatos("protecciondiferencial",binding.editTextProteccionDiferencial.text.toString())
            Log.d("nose","Datos del cable del view model Pantalla2 atras: ${viewModel.depuraciondatos()}")
            findNavController().navigate(R.id.pantalla1)
        }
    }

    // Comprobación datos
    fun validarPantalla2() {
        val longitudStr = binding.editTextLongitud.text.toString().trim()
        val proteccionSelecStr = binding.editTextProteccionSelec.text.toString().trim()
        val proteccionTermicaStr = binding.editTextProteccionTermica.text.toString().trim()
        // val proteccionDiferencialStr = binding.editTextProteccionDiferencial.text.toString().trim()

        esValido = true

        // LONGITUD
        val longitud = longitudStr.toDoubleOrNull()
        if (longitudStr.isEmpty()) {
            binding.error4.text = "Introduce la longitud"
            binding.error4.visibility = View.VISIBLE
            esValido = false
        } else {
            if (longitud == null || longitud <= 0) {
                binding.error4.text = "Valor inválido"
                binding.error4.visibility = View.VISIBLE
                esValido = false
            } else {
                binding.error4.visibility = View.GONE
            }
        }

        // PROTECCIÓN SELECCIONADA
        val proteccionSelec = proteccionSelecStr.toDoubleOrNull()
        if (proteccionSelecStr.isEmpty()) {
            binding.error5.text = "Introduce la protección seleccionada (A)"
            binding.error5.visibility = View.VISIBLE
            esValido = false
        } else {
            if (proteccionSelec == null || proteccionSelec <= 0) {
                binding.error5.text = "Valor inválido"
                binding.error5.visibility = View.VISIBLE
                esValido = false
            } else {
                binding.error5.visibility = View.GONE
            }
        }

        // PROTECCIÓN TÉRMICA
        if (proteccionTermicaStr.isEmpty()) {
            binding.error6.text = "Introduce la protección térmica"
            binding.error6.visibility = View.VISIBLE
            esValido = false
        } else {
            binding.error6.visibility = View.GONE
        }
        // CAMBIADO A LA PANTALLA 2
//        // PROTECCIÓN DIFERENCIAL
//        if (proteccionDiferencialStr.isEmpty()) {
//            binding.error7.text = "Introduce la protección diferencial"
//            binding.error7.visibility = View.VISIBLE
//            esValido = false
//        } else {
//            binding.error7.visibility = View.GONE
//        }
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