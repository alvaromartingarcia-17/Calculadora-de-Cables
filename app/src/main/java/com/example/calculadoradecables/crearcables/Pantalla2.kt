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
import com.example.calculadoradecables.modelosDataClass.Termicos
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Pantalla2 : Fragment() {
    private var _binding: PantallaDatosCable2Binding? = null
    private val binding get() = _binding!!

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
        if (!viewModel.vercable) {
            binding.textViewProteccionTermicaMensaje.setText(viewModel.termicos.proteccionTermica)
            binding.textViewProteccionSelecMensaje.setText(viewModel.termicos.proteccionSeleccionada.toString() + " A")
        }
        binding.botonSiguiente.isEnabled = false

        cargardatos()
        binding.botonSiguiente.setOnClickListener {
            if (validarPantalla2()) {
                if (!viewModel.vercable) {
                    viewModel.meterdatos("longitud", binding.editTextLongitud.text.toString())
                    viewModel.meterdatos("caidatensionmax", binding.spinner2.selectedItem.toString())
                    Log.d(
                        "nose",
                        "Datos del cable del view model Pantalla2 Siguiente: ${viewModel.depuraciondatos()}"
                    )
                }
                findNavController().navigate(R.id.pantalla3)
            }
        }

        binding.botonAtras.setOnClickListener {
            if (!viewModel.vercable) {
                viewModel.meterdatos("longitud", binding.editTextLongitud.text.toString())
                viewModel.meterdatos("caidatensionmax", binding.spinner2.selectedItem.toString())
                 Log.d(
                    "nose",
                    "Datos del cable del view model Pantalla2 atras: ${viewModel.depuraciondatos()}"
                )
            }
            findNavController().navigate(R.id.pantalla1)
        }
    }

    private fun cargardatos() {
        RetrofitClient.instance.caidatensionmax("caidatensionmaxima")
            .enqueue(object : Callback<MutableList<Double>> {
                override fun onResponse(
                    call: Call<MutableList<Double>>,
                    response: Response<MutableList<Double>>
                ) {
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

                        if (viewModel.vercable) {
                            intentarPonerdatoscable()
                        } else {
                            Log.d("nose","tabla termicos ${viewModel.termicos}")

                            if (viewModel.datoscable.value?.get("caidatensionmax") != null) {
                                val posicion = listatensionmax.indexOf(
                                    viewModel.datoscable.value?.get("caidatensionmax")?.toDouble()
                                )
                                Log.d("nose", "posicion array $posicion")
                                if (posicion >= 0) {
                                    binding.editTextLongitud.setText(
                                        viewModel.datoscable.value?.get("longitud").orEmpty()
                                    )
                                    binding.spinner2.setSelection(posicion)
                                }
                            }
                        }
                        binding.botonSiguiente.isEnabled = true
                    }
                }

                override fun onFailure(call: Call<MutableList<Double>>, t: Throwable) {
                    Log.e("nose", "Error onfailure: ${t.message}")
                    mostrarSnackbar("Problemas de conexión")
                }
            })
    }

    private fun validarPantalla2(): Boolean {
        val longitudStr = binding.editTextLongitud.text.toString().trim()

        if (longitudStr.isEmpty()) {
            binding.error4.text = "Introduce la longitud"
            binding.error4.visibility = View.VISIBLE
            return false
        }else binding.error4.visibility = View.GONE

        return true
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

    fun modificacionedittext() {

        binding.editTextLongitud.isFocusable = false
        binding.editTextLongitud.isClickable = false


        binding.textViewProteccionSelecMensaje.isFocusable = false
        binding.textViewProteccionSelecMensaje.isClickable = false


        binding.textViewProteccionTermicaMensaje.isFocusable = false
        binding.textViewProteccionTermicaMensaje.isClickable = false


        binding.spinner2.isClickable = false
        binding.spinner2.isEnabled = false
    }

    fun ponerdatoscable() {
        binding.editTextLongitud.setText(viewModel.cable.longitud)
        binding.textViewProteccionSelecMensaje.setText(viewModel.cable.proteccionseleccionada + " A")
        binding.textViewProteccionTermicaMensaje.setText(viewModel.cable.protecciontermica)

        val valorSpinner = viewModel.cable.caidatensionmax.toDouble()

        val posicion = (binding.spinner2.adapter as ArrayAdapter<Double>)
            .getPosition(valorSpinner)

        binding.spinner2.setSelection(posicion)
    }

    private fun intentarPonerdatoscable() {
        if (listatensionmax.isNotEmpty() &&
            viewModel.vercable
        ) {
            modificacionedittext()
            ponerdatoscable()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Liberamos el binding
        _binding = null
    }
}