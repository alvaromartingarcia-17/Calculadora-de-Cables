package com.example.calculadoradecables

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.calculadoradecables.databinding.PantallaMenuPrincipalBinding
import com.example.calculadoradecables.viewmodel.CableViewModel
import kotlin.getValue

class MenuPrincipalFragmento : Fragment() {
    private var _binding: PantallaMenuPrincipalBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CableViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PantallaMenuPrincipalBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.vercable = false
        binding.botonCrearCable.setOnClickListener {
            findNavController().navigate(R.id.pantalla1)
        }
        binding.botonVerCable.setOnClickListener {
            findNavController().navigate(R.id.fragmentoVerCables)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()

        // Liberamos el binding
        _binding = null
    }
}