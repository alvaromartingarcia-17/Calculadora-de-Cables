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

    private var MutableListSeccionConductor: MutableList<Double> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PantallaDatosCable2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cargarseccionconductor()
        binding.botonAtras.isEnabled = false
        binding.botonSiguiente.isEnabled = false
        if (!viewModel.vercable) {
            binding.textViewProteccionTermicaMensaje.text = viewModel.termicos.proteccionTermica
            binding.textViewProteccionSelecMensaje.text = viewModel.termicos.proteccionSeleccionada.toString() + " A"
            binding.textViewProteccionDiferencialMensaje.text = viewModel.termicos.proteccionDiferencial

        } else {
            ponerdatoscable()
        }

        binding.botonSiguiente.setOnClickListener {
            viewModel.meterdatos(
                "seccionconductor",
                binding.spinner2.selectedItem.toString()
            )
            findNavController().navigate(R.id.pantalla3)
        }

        binding.botonAtras.setOnClickListener {
            findNavController().navigate(R.id.pantalla1)
        }
    }

    fun ponerdatoscable() {
        binding.spinner2.isClickable = false
        binding.spinner2.isEnabled = false

        binding.textViewProteccionSelecMensaje.text = "${viewModel.cable.proteccionseleccionada} A"
        binding.textViewProteccionTermicaMensaje.text = viewModel.cable.protecciontermica
        binding.textViewProteccionDiferencialMensaje.text = viewModel.cable.protecciondiferencial
    }

    fun cargarseccionconductor() {
        RetrofitClient.instance.seccionconductor("seccionconductor")
            .enqueue(object : Callback<MutableList<Double>> {
                override fun onResponse(
                    call: Call<MutableList<Double>>,
                    response: Response<MutableList<Double>>
                ) {

                    if (response.isSuccessful) {
                        Log.d("nose", "Tabla termicos: ${response.body()!!}")
                        MutableListSeccionConductor = response.body()!!
                        val adapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            response.body()!!
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.spinner2.adapter = adapter

                        if (viewModel.datoscable.value?.get("seccionconductor") != null) {
                            val posicion =
                                MutableListSeccionConductor.indexOf<Double>(
                                    viewModel.datoscable.value?.get(
                                        "seccionconductor"
                                    )!!.toDouble()
                                )
                            Log.d("nose", "posicion array $posicion")
                            if (posicion >= 0) {
                                binding.spinner2.setSelection(posicion)
                            }
                        } else {
                            val posicion =
                                MutableListSeccionConductor.indexOf<Double>(viewModel.termicos.seccionConductor)
                            Log.d("nose", "posicion array $posicion")
                            if (posicion >= 0) {
                                binding.spinner2.setSelection(posicion)
                            }
                        }
                        if (viewModel.vercable) intentarponerdatos()
                        binding.botonAtras.isEnabled = true
                        binding.botonSiguiente.isEnabled = true
                    }
                }

                override fun onFailure(call: Call<MutableList<Double>>, t: Throwable) {
                    Log.e("nose", "Error onfailure: ${t.message}")
                    mostrarSnackbar("Problemas de conexión")
                }
            })
    }

    private fun intentarponerdatos(){
        val valorSpinner = viewModel.cable.seccionconductor.toDouble()

        val posicion = (binding.spinner2.adapter as ArrayAdapter<Double>)
            .getPosition(valorSpinner)

        binding.spinner2.setSelection(posicion)
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