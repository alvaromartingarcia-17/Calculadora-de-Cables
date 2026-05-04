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
            binding.editTextLongitud.setText(viewModel.datoscable.value?.get("longitud"))
        }
        binding.botonSiguiente.setOnClickListener {
            viewModel.meterdatos("potencia", binding.editTextPotencia.text.toString())
            viewModel.meterdatos("tension", binding.editTextTension.text.toString())
            viewModel.meterdatos("longitud", binding.editTextLongitud.text.toString())
            viewModel.meterdatos("nombre", binding.editTextNombreCircuito.text.toString())

            Log.d(
                "nose",
                "Datos del cable del view model Pantalla1 alante: ${viewModel.depuraciondatos()}"
            )
            findNavController().navigate(R.id.pantalla2)
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

    override fun onDestroyView() {
        super.onDestroyView()

        // Liberamos el binding
        _binding = null
    }
}