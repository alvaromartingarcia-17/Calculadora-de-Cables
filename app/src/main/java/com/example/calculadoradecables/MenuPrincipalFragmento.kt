package com.example.calculadoradecables

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.calculadoradecables.databinding.PantallaMenuPrincipalBinding

class MenuPrincipalFragmento : Fragment() {
    private var _binding: PantallaMenuPrincipalBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PantallaMenuPrincipalBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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