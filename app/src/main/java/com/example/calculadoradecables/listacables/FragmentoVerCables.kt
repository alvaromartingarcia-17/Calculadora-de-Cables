package com.example.calculadoradecables.listacables

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calculadoradecables.R
import com.example.calculadoradecables.RetrofitClient
import com.example.calculadoradecables.databinding.PantallaListadoCablesBinding
import com.example.calculadoradecables.modelosDataClass.Cable
import com.example.calculadoradecables.viewmodel.CableViewModel
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.getValue

class FragmentoVerCables : Fragment() {
    private var _binding: PantallaListadoCablesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CableViewModel by activityViewModels()

    private var cablesMutableList: MutableList<Cable> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PantallaListadoCablesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("nose", "Usuario que se manda: ${viewModel.datoscable.value?.get("usuario")}")
        // Configurar el RecyclerView
        val recyclerReservas = view.findViewById<RecyclerView>(R.id.recyclerListadoCables)
        recyclerReservas.layoutManager = LinearLayoutManager(requireContext())
        RetrofitClient.instance.cablesusuario("cablesusuario", viewModel.datoscable.value?.get("usuario").toString())
            .enqueue(object : Callback<MutableList<Cable>> {
                override fun onResponse(
                    call: Call<MutableList<Cable>>,
                    response: Response<MutableList<Cable>>
                ){
                    // Log del código HTTP
                    Log.d("nose", "Código HTTP: ${response.code()}")

                    // Leer el body real
                    val responseBody = response.body()?.toString()
                    Log.d("nose", "Body de respuesta: $responseBody")
                    Log.d("nose", "ErrorBody: ${response.errorBody()?.string()}")  // <-- aquí estará el error SQL

                    if (response.isSuccessful) {
                        Log.d("nose","Hola caracola"+response.body().toString())
                        cablesMutableList = response.body()!!
                        val adapter = cablesAdapter(cablesMutableList)
                        adapter.onItemClick = { cable ->
                            viewModel.vercable = true
                            viewModel.cable = cable
                            findNavController().navigate(R.id.pantalla1)
                        }
                        recyclerReservas.adapter = adapter
                    }
                }

                override fun onFailure(call: Call<MutableList<Cable>>, t: Throwable) {
                    Log.e("nose", "Error onfailure: ${t.message}")
                    mostrarSnackbar("Problemas de conexión")
                }
            })

        binding.botonAtras.setOnClickListener {
            findNavController().navigate(R.id.menuPrincipalFragmento)
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