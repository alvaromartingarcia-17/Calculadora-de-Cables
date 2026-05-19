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
import com.example.calculadoradecables.databinding.PantallaDatosCable3Binding
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Pantalla3 : Fragment() {
    private var _binding: PantallaDatosCable3Binding? = null
    private val binding get() = _binding!!

    private val viewModel: CableViewModel by activityViewModels()

    private var listatipodiferencial: MutableList<String> = mutableListOf()
    private var listasensibilidaddiferencial: MutableList<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PantallaDatosCable3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cargardatos()

        binding.botonSiguiente.setOnClickListener {
            if (!viewModel.vercable) {
                viewModel.meterdatos(
                    "tipodiferencial",
                    binding.spinner3.selectedItem.toString()
                )
                viewModel.meterdatos(
                    "sensibilidadiferencial",
                    binding.spinner4.selectedItem.toString()
                )

                Log.d(
                    "nose",
                    "Datos del cable del view model Pantalla3 Siguiente: ${viewModel.depuraciondatos()}"
                )
            }
            findNavController().navigate(R.id.pantalla4)

        }

        binding.botonAtras.setOnClickListener {
            if (!viewModel.vercable) {

                viewModel.meterdatos("tipodiferencial", binding.spinner3.selectedItem.toString())
                viewModel.meterdatos(
                    "sensibilidadiferencial",
                    binding.spinner4.selectedItem.toString()
                )
                Log.d(
                    "nose",
                    "Datos del cable del view model Pantalla3 Siguiente: ${viewModel.depuraciondatos()}"
                )
            }
            findNavController().navigate(R.id.pantalla2)
        }
    }
    private fun cargardatos() {
        RetrofitClient.instance.tipodiferencial("tipodiferencial")
            .enqueue(object : Callback<MutableList<String>> {
                override fun onResponse(
                    call: Call<MutableList<String>>,
                    response: Response<MutableList<String>>
                ) {
                    if (response.isSuccessful) {
                        Log.d("nose", response.body().toString())
                        listatipodiferencial = response.body()!!
                        val adapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            response.body()!!
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.spinner3.adapter = adapter

                        if (viewModel.datoscable.value?.get("tipodiferencial") != null) {
                            val posicion =
                                listatipodiferencial.indexOf(viewModel.datoscable.value?.get("tipodiferencial"))
                            Log.d("nose", "posicion array $posicion")
                            if (posicion >= 0) {
                                binding.spinner3.setSelection(posicion)
                            }
                        }
                        intentarPonerdatoscable()
                    }
                }

                override fun onFailure(call: Call<MutableList<String>>, t: Throwable) {
                    Log.e("nose", "Error onfailure1: ${t.message}")
                    mostrarSnackbar("Problemas de conexión")
                }
            })

        RetrofitClient.instance.sensibilidad("sensibilidad")
            .enqueue(object : Callback<MutableList<String>> {
                override fun onResponse(
                    call: Call<MutableList<String>>,
                    response: Response<MutableList<String>>
                ) {
                    Log.d("nose", "Código HTTP: ${response.code()}")
                    Log.d("nose", "Body: ${response.body()}")
                    Log.d("nose", "ErrorBody: ${response.errorBody()?.string()}")
                    if (response.isSuccessful) {
                        Log.d("nose", response.body().toString())
                        listasensibilidaddiferencial = response.body()!!
                        val adapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            response.body()!!
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.spinner4.adapter = adapter

                        if (!viewModel.vercable) {
                            binding.textViewProteccionDiferencialMensaje.setText(viewModel.termicos.proteccionDiferencial)
                            binding.textViewSeccionConductorMensaje.setText(viewModel.termicos.seccionConductor)
                        }
                        if (viewModel.datoscable.value?.get("sensibilidadiferencial") != null) {
                            val posicion = listasensibilidaddiferencial.indexOf(
                                viewModel.datoscable.value?.get("sensibilidadiferencial")
                            )
                            Log.d("nose", "posicion array $posicion")
                            if (posicion >= 0) {
                                binding.spinner4.setSelection(posicion)
                            }
                        }
                        intentarPonerdatoscable()
                    }
                }

                override fun onFailure(call: Call<MutableList<String>>, t: Throwable) {
                    Log.e("nose", "Error onfailure2: ${t.message}")
                    mostrarSnackbar("Problemas de conexión")
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

    fun modificacionedittext() {

        binding.textViewSeccionConductorMensaje.isFocusable = false
        binding.textViewSeccionConductorMensaje.isClickable = false


        binding.textViewProteccionDiferencialMensaje.isFocusable = false
        binding.textViewProteccionDiferencialMensaje.isClickable = false

        binding.spinner4.isClickable = false
        binding.spinner4.isEnabled = false

        binding.spinner3.isClickable = false
        binding.spinner3.isEnabled = false
    }

    fun ponerdatoscable() {
        binding.textViewSeccionConductorMensaje.setText(viewModel.cable.seccionconductor)
        binding.textViewProteccionDiferencialMensaje.setText(viewModel.cable.protecciondiferencial)

        val valorSpinner = viewModel.cable.tipodiferencial

        val posicion = (binding.spinner3.adapter as ArrayAdapter<String>)
            .getPosition(valorSpinner)

        binding.spinner3.setSelection(posicion)

        val valorSpinner2 = viewModel.cable.sensibilidadiferencial

        val posicion2 = (binding.spinner4.adapter as ArrayAdapter<String>)
            .getPosition(valorSpinner2)

        binding.spinner4.setSelection(posicion2)
    }

    private fun intentarPonerdatoscable() {
        if (listatipodiferencial.isNotEmpty() &&
            listasensibilidaddiferencial.isNotEmpty() &&
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