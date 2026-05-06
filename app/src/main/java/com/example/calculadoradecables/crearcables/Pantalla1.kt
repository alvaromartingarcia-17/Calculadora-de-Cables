package com.example.calculadoradecables.crearcables

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.calculadoradecables.R
import com.example.calculadoradecables.databinding.PantallaDatosCable1Binding
import com.example.calculadoradecables.viewmodel.CableViewModel

class Pantalla1 : Fragment() {
    private var _binding: PantallaDatosCable1Binding? = null
    private val binding get() = _binding!!

    var esValido = false

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
        if (viewModel.datoscable.value?.get("potencia") != null) {
            binding.editTextNombreCircuito.setText(viewModel.datoscable.value?.get("nombre"))
            binding.editTextPotencia.setText(viewModel.datoscable.value?.get("potencia"))
            binding.editTextTension.setText(viewModel.datoscable.value?.get("tension"))
            binding.editTextTipoTension.setText(viewModel.datoscable.value?.get("tipotension"))
            // CAMBIADO A LA PANTALLA 2
            // Falta el spinner de tipo tensión
            // binding.editTextLongitud.setText(viewModel.datoscable.value?.get("longitud"))
        }

        binding.botonSiguiente.setOnClickListener {
            validarPantalla1()
            if (esValido) {
                viewModel.meterdatos("potencia", binding.editTextPotencia.text.toString())
                viewModel.meterdatos("tension", binding.editTextTension.text.toString())
                viewModel.meterdatos("tipotension", binding.editTextTipoTension.text.toString())
                // CAMBIADO A LA PANTALLA 2
                // viewModel.meterdatos("longitud", binding.editTextLongitud.text.toString())
                // Falta el spinner de tipo tensión
                viewModel.meterdatos("nombre", binding.editTextNombreCircuito.text.toString())

                Log.d(
                    "nose",
                    "Datos del cable del view model Pantalla1 alante: ${viewModel.depuraciondatos()}"
                )

                findNavController().navigate(R.id.pantalla2)
            }
        }

        binding.botonAtras.setOnClickListener {
            viewModel.borrardatos()
            Log.d(
                "nose",
                "Datos del cable del view model Pantalla1 atras: ${viewModel.depuraciondatos()}"
            )
            findNavController().navigate(R.id.menuPrincipalFragmento)
        }
    }

    // Comprobación datos
    fun validarPantalla1() {
        val nombre = binding.editTextNombreCircuito.text.toString().trim()
        val potenciaStr = binding.editTextPotencia.text.toString().trim()
        val tensionStr = binding.editTextTension.text.toString().trim()
        // val tipoTensionStr = binding.editTextTipoTension.text.toString().trim()
        // CAMBIADO A LA PANTALLA 2
        // val longitudStr = binding.editTextLongitud.text.toString().trim()

        esValido = true

        // NOMBRE
        if (nombre.isEmpty()) {
            binding.error1.text = "Introduce el nombre del circuito"
            binding.error1.visibility = View.VISIBLE
            esValido = false
        } else {
            binding.error1.visibility = View.GONE
        }

        // POTENCIA
        val potencia = potenciaStr.toDoubleOrNull()
        if (potenciaStr.isEmpty()) {
            binding.error2.text = "Introduce la potencia"
            binding.error2.visibility = View.VISIBLE
            esValido = false
        } else {
            if (potencia == null || potencia <= 0) {
                binding.error2.text = "Valor inválido"
                binding.error2.visibility = View.VISIBLE
                esValido = false
            } else {
                binding.error2.visibility = View.GONE
            }
        }

        // TENSIÓN
        val tension = tensionStr.toDoubleOrNull()
        if (tensionStr.isEmpty()) {
            binding.error3.text = "Introduce la tensión"
            binding.error3.visibility = View.VISIBLE
            esValido = false
        } else {
            if (tension == null || tension <= 0) {
                binding.error3.text = "Valor inválido"
                binding.error3.visibility = View.VISIBLE
                esValido = false
            } else {
                binding.error3.visibility = View.GONE
            }
        }

//        TIPO TENSIÓN
//        val tipoTension = tipoTensionStr.toDoubleOrNull()
//        if (tipoTensionStr.isEmpty()) {
//            binding.error4.text = "Introduce el tipo de tensión"
//            binding.error4.visibility = View.VISIBLE
//            esValido = false
//        } else {
//            if (tipoTension == null || tipoTension <= 0) {
//                binding.error4.text = "Valor inválido"
//                binding.error4.visibility = View.VISIBLE
//                esValido = false
//            } else {
//                binding.error4.visibility = View.GONE
//            }
//        }

        // CAMBIADO A LA PANTALLA 2
//        LONGITUD
//        val longitud = longitudStr.toDoubleOrNull()
//        if (longitudStr.isEmpty()) {
//            binding.error4.text = "Introduce la longitud"
//            binding.error4.visibility = View.VISIBLE
//            esValido = false
//        } else {
//            if (longitud == null || longitud <= 0) {
//                binding.error4.text = "Valor inválido"
//                binding.error4.visibility = View.VISIBLE
//                esValido = false
//            } else {
//                binding.error4.visibility = View.GONE
//            }
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Liberamos el binding
        _binding = null
    }
}