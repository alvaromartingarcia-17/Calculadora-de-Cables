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
import androidx.navigation.fragment.findNavController
import com.example.calculadoradecables.R
import com.example.calculadoradecables.RetrofitClient
import com.example.calculadoradecables.databinding.PantallaDatosCable1Binding
import com.example.calculadoradecables.modelosDataClass.Termicos
import com.example.calculadoradecables.viewmodel.CableViewModel
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.sqrt

class Pantalla1 : Fragment() {
    private var _binding: PantallaDatosCable1Binding? = null
    private val binding get() = _binding!!

    private val viewModel: CableViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PantallaDatosCable1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val lista: MutableList<String> = mutableListOf("Trifasica tetrapolar", "Monofasica bipolar")
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            lista
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner1.adapter = adapter

        if (viewModel.vercable) {
            modificacionedittext()
            ponerdatoscable()
        } else {
            binding.botonAtras.text = "Cancelar"
            if (viewModel.datoscable.value?.get("potencia") != null) {
                binding.editTextNombreCircuito.setText(viewModel.datoscable.value?.get("nombre"))
                binding.editTextPotencia.setText(viewModel.datoscable.value?.get("potencia"))
            }
        }

        binding.botonSiguiente.setOnClickListener {
            if (validarPantalla1()) {
                if (viewModel.vercable) {
                    findNavController().navigate(R.id.pantalla2)
                } else {
                    viewModel.meterdatos("potencia", binding.editTextPotencia.text.toString())
                    viewModel.meterdatos("nombre", binding.editTextNombreCircuito.text.toString())

                    cargartermicos(
                        calcularproteccionSeleccionada(),
                        binding.spinner1.selectedItem.toString()
                    )
                    Log.d(
                        "nose",
                        "Datos del cable del view model Pantalla1 alante: ${viewModel.depuraciondatos()}"
                    )
                }
            }
        }

        binding.botonAtras.setOnClickListener {
            viewModel.borrardatos()
            Log.d(
                "nose",
                "Datos del cable del view model Pantalla1 atras: ${viewModel.depuraciondatos()}"
            )
            if (viewModel.vercable) {
                findNavController().navigate(R.id.fragmentoVerCables)
            } else {
                findNavController().navigate(R.id.menuPrincipalFragmento)
            }
        }
    }

    // Comprobación datos
    fun validarPantalla1(): Boolean {

        // NOMBRE
        if (binding.editTextNombreCircuito.text.isEmpty()) {
            binding.error1.text = "Introduce el nombre del circuito"
            binding.error1.visibility = View.VISIBLE
            return false
        } else {
            binding.error1.visibility = View.GONE
        }

        // POTENCIA
        val potencia = binding.editTextPotencia.text.toString()
        if (potencia.isEmpty()) {
            binding.error2.text = "Introduce la potencia"
            binding.error2.visibility = View.VISIBLE
            return false
        } else {
            val potenciaInt = potencia.toIntOrNull()
            if (potenciaInt == null || potenciaInt <= 0) {
                binding.error2.text = "Valor inválido"
                binding.error2.visibility = View.VISIBLE
                return false
            } else {
                binding.error2.visibility = View.GONE
            }
        }

        if (binding.spinner1.selectedItem.toString() == "Monofasica bipolar") {
            val intensidad = calcularproteccionSeleccionada()
            if (intensidad > 63) {
                binding.error2.text = "La intensidad ($intensidad A) supera el límite monofásico de 63 A"
                binding.error2.visibility = View.VISIBLE
                return false
            }
        }
        return true

        return true
    }

    fun cargartermicos(proteccionseleccionada: Int, tipotension: String) {
        RetrofitClient.instance.termicos("termicos", proteccionseleccionada, tipotension)
            .enqueue(object : Callback<Termicos> {
                override fun onResponse(
                    call: Call<Termicos>,
                    response: Response<Termicos>
                ) {
                    // Log del código HTTP
                    Log.d("nose", "Código HTTP: ${response.code()}")

                    // Leer el body real
                    val responseBody = response.body()?.toString()
                    Log.d("nose", "Body de respuesta: $responseBody")
                    Log.d(
                        "nose",
                        "ErrorBody: ${response.errorBody()?.string()}"
                    )  // <-- aquí estará el error SQL

                    if (response.isSuccessful) {
                        Log.d("nose", "Tabla termicos: ${response.body()!!}")
                        viewModel.termicos = response.body()!!
                        viewModel.meterdatos("idtablatermicos", viewModel.termicos.id.toString())
                        findNavController().navigate(R.id.pantalla2)
                    }
                }

                override fun onFailure(call: Call<Termicos>, t: Throwable) {
                    Log.e("nose", "Error onfailure: ${t.message}")
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

        binding.editTextNombreCircuito.isFocusable = false
        binding.editTextNombreCircuito.isClickable = false

        binding.editTextPotencia.isFocusable = false
        binding.editTextPotencia.isClickable = false


        binding.spinner1.isClickable = false
        binding.spinner1.isEnabled = false
    }

    fun ponerdatoscable() {
        binding.botonAtras.text = "Volver"
        binding.editTextNombreCircuito.setText(viewModel.cable.nombre)
        binding.editTextPotencia.setText(viewModel.cable.potencia)

        val valorSpinner = viewModel.cable.tipotension

        val posicion = (binding.spinner1.adapter as ArrayAdapter<String>)
            .getPosition(valorSpinner)

        binding.spinner1.setSelection(posicion)
        binding.spinner1.setSelection(posicion)
    }

    fun calcularproteccionSeleccionada(): Int {
        var intensidad = 0
        val potencia = binding.editTextPotencia.text.toString().toInt()
        val tension = if (binding.spinner1.selectedItem.toString() == "Trifasica tetrapolar"){400}else{230}

        if (binding.spinner1.selectedItem.toString() == "Trifasica tetrapolar") {
            intensidad = (potencia / ((sqrt(3.0) * tension))).toInt()
            viewModel.meterdatos("tension", binding.editTextNombreCircuito.text.toString())

            Log.d("nose", "intensidad trifasica tetrapolar $intensidad")
        } else {
            intensidad = potencia / tension
            Log.d("nose", "intensidad monofasica $intensidad")
        }
        return intensidad
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Liberamos el binding
        _binding = null
    }
}