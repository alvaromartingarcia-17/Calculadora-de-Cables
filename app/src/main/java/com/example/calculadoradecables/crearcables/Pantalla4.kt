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
import com.example.calculadoradecables.viewmodel.CableViewModel
import androidx.navigation.fragment.findNavController
import com.example.calculadoradecables.R
import com.example.calculadoradecables.RetrofitClient
import com.example.calculadoradecables.databinding.PantallaDatosCable4Binding
import com.example.calculadoradecables.modelosDataClass.NuevoCable
import com.google.android.material.snackbar.Snackbar
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Pantalla4 : Fragment() {
    private var _binding: PantallaDatosCable4Binding? = null
    private val binding get() = _binding!!

    private val viewModel: CableViewModel by activityViewModels()

    private var listalocalizacion: MutableList<String> = mutableListOf()
    private var listaconductormaterial: MutableList<String> = mutableListOf()
    private var listaconductoraislamiento: MutableList<String> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PantallaDatosCable4Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cargarDatos()

        binding.botonSiguiente.setOnClickListener {
            if (validarPantalla4()) {
            if (!viewModel.vercable) {
                viewModel.meterdatos(
                    "tipoconductormaterial",
                    binding.spinner5.selectedItem?.toString().orEmpty()
                )
                viewModel.meterdatos(
                    "tipoconductoraislamiento",
                    binding.spinner6.selectedItem?.toString().orEmpty()
                )
                viewModel.meterdatos(
                    "localizacioncanalizacion",
                    binding.spinner7.selectedItem?.toString().orEmpty()
                )
                viewModel.meterdatos(
                    "factorcorreccionlugar",
                    binding.editTextFactorCorreccion.text.toString()
                )


                Log.d("nose", "Datos antes de crear cable: ${viewModel.depuraciondatos()}")
                val nuevocable: NuevoCable? = viewModel.CrearCable()

                if (nuevocable == null) {
                    Log.e("nose", "CrearCable devuelve null")
                    return@setOnClickListener
                }
                Log.d(
                    "nose",
                    "Datos del cable del view model Pantalla3 Siguiente: ${viewModel.depuraciondatos()}"
                )
                Log.d("nose", "Datos del cable del nuevocable: $nuevocable")

                RetrofitClient.instance.crearcable("nuevocable", nuevocable)
                    .enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            if (response.isSuccessful) {
                                val body = response.body()?.string()
                                Log.d("nose", "Respuesta body: $body")
                                viewModel.borrardatos()
                                findNavController().navigate(R.id.menuPrincipalFragmento)
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            Log.e("nose", "Error onfailure: ${t.message}")
                            mostrarSnackbar("Problemas de conexión")
                        }
                    })
            } else {
                findNavController().navigate(R.id.fragmentoVerCables)
            }
            }
        }

        binding.botonAtras.setOnClickListener {
            if (!viewModel.vercable) {
                viewModel.meterdatos(
                    "tipoconductormaterial",
                    binding.spinner5.selectedItem?.toString().orEmpty()
                )
                viewModel.meterdatos(
                    "tipoconductoraislamiento",
                    binding.spinner6.selectedItem?.toString().orEmpty()
                )
                viewModel.meterdatos(
                    "localizacioncanalizacion",
                    binding.spinner7.selectedItem?.toString().orEmpty()
                )
                viewModel.meterdatos(
                    "factorcorreccionlugar",
                    binding.editTextFactorCorreccion.text.toString()
                )
            }
            findNavController().navigate(R.id.pantalla3)
        }
    }

    // Comprobación datos
    fun validarPantalla4(): Boolean {

        val factorCorreccion =
            binding.editTextFactorCorreccion.text.toString().trim().toDoubleOrNull()
        if (binding.editTextFactorCorreccion.text.toString().isEmpty()) {
            binding.error9.text = "Introduce el factor corrección"
            binding.error9.visibility = View.VISIBLE
            return false
        } else {
            if (factorCorreccion == null || factorCorreccion <= 0 || factorCorreccion > 1) {
                binding.error9.text = "Valor inválido, máximo 1"
                binding.error9.visibility = View.VISIBLE
                return false
            } else {
                binding.error9.visibility = View.GONE
            }
        }
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

    private fun cargarDatos() {

        RetrofitClient.instance.localizacion("localizacion")
            .enqueue(object : Callback<MutableList<String>> {
                override fun onResponse(
                    call: Call<MutableList<String>>,
                    response: Response<MutableList<String>>
                ) {
                    Log.d("nose", "localizacion HTTP: ${response.code()}")
                    Log.d("nose", "localizacion Body: ${response.body()}")
                    Log.d("nose", "localizacion ErrorBody: ${response.errorBody()?.string()}")
                    if (response.isSuccessful) {
                        Log.d("nose", "Localizacion cargar datos ${response.body().toString()}")
                        listalocalizacion = response.body()!!
                        val adapter = ArrayAdapter(
                            requireContext(),
                            R.layout.spinner_layout_nuevo,
                            response.body()!!
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
                        binding.spinner7.adapter = adapter

                        if (viewModel.datoscable.value?.get("sensibilidadiferencial") != null) {
                            val posicion =
                                listalocalizacion.indexOf(viewModel.datoscable.value?.get("sensibilidadiferencial"))
                            Log.d("nose", "posicion array $posicion")
                            if (posicion >= 0) {
                                binding.spinner7.setSelection(posicion)
                                binding.editTextFactorCorreccion.setText(
                                    viewModel.datoscable.value?.get(
                                        "factorcorreccionlugar"
                                    )
                                )
                            }
                        }
                        intentarPonerdatoscable()
                    }
                }

                override fun onFailure(call: Call<MutableList<String>>, t: Throwable) {
                    Log.e("nose", "Error onfailure de localizacion: ${t.message}")
                    mostrarSnackbar("Problemas de conexión")
                }
            })

        RetrofitClient.instance.tipoconductoraislamiento("tipoconductoraislamiento")
            .enqueue(object : Callback<MutableList<String>> {
                override fun onResponse(
                    call: Call<MutableList<String>>,
                    response: Response<MutableList<String>>
                ) {
                    Log.d("nose", "aislamiento HTTP: ${response.code()}")
                    Log.d("nose", "aislamiento Body: ${response.body()}")
                    Log.d("nose", "aislamiento ErrorBody: ${response.errorBody()?.string()}")

                    if (response.isSuccessful) {
                        Log.d("nose", response.body().toString())

                        listaconductoraislamiento = response.body()!!
                        val adapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            listaconductoraislamiento
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.spinner6.adapter = adapter

                        if (viewModel.datoscable.value?.get("tipoconductor") != null) {
                            val posicion = listaconductoraislamiento.indexOf(
                                viewModel.datoscable.value?.get("tipoconductoraislamiento")
                            )

                            if (posicion >= 0) {
                                binding.spinner6.setSelection(posicion)
                            }
                        }
                        intentarPonerdatoscable()
                    }
                }

                override fun onFailure(call: Call<MutableList<String>>, t: Throwable) {
                    Log.e("nose", "Error onfailure: ${t.message}")
                    mostrarSnackbar("Problemas de conexión")
                }
            })

        RetrofitClient.instance.tipoconductormaterial("tipoconductormaterial")
            .enqueue(object : Callback<MutableList<String>> {
                override fun onResponse(
                    call: Call<MutableList<String>>,
                    response: Response<MutableList<String>>
                ) {
                    Log.d("nose", "material HTTP: ${response.code()}")
                    Log.d("nose", "material Body: ${response.body()}")
                    Log.d("nose", "material ErrorBody: ${response.errorBody()?.string()}")

                    if (response.isSuccessful) {
                        Log.d("nose", response.body().toString())

                        listaconductormaterial = response.body()!!
                        val adapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            listaconductormaterial
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.spinner5.adapter = adapter

                        if (viewModel.datoscable.value?.get("tipoconductor") != null) {
                            val posicion =
                                listaconductormaterial.indexOf(viewModel.datoscable.value?.get("tipoconductormaterial"))

                            if (posicion >= 0) {
                                binding.spinner5.setSelection(posicion)
                            }
                        }
                        intentarPonerdatoscable()
                    }
                }

                override fun onFailure(call: Call<MutableList<String>>, t: Throwable) {
                    Log.e("nose", "Error onfailure: ${t.message}")
                    mostrarSnackbar("Problemas de conexión")
                }
            })
    }

    fun modificacionedittext() {

        binding.editTextFactorCorreccion.isFocusable = false
        binding.editTextFactorCorreccion.isClickable = false

        binding.spinner5.isClickable = false
        binding.spinner5.isEnabled = false

        binding.spinner6.isClickable = false
        binding.spinner6.isEnabled = false

        binding.spinner7.isClickable = false
        binding.spinner7.isEnabled = false
    }

    fun ponerdatoscable() {
        binding.editTextFactorCorreccion.setText(viewModel.cable.factorcorreccionlugar)

        val valorSpinner = viewModel.cable.tipoconductormaterial

        val posicion = (binding.spinner5.adapter as ArrayAdapter<String>)
            .getPosition(valorSpinner)

        binding.spinner5.setSelection(posicion)

        val valorSpinner2 = viewModel.cable.tipoconductorasilamiento

        val posicion2 = (binding.spinner6.adapter as ArrayAdapter<String>)
            .getPosition(valorSpinner2)

        binding.spinner6.setSelection(posicion2)

        val valorSpinner3 = viewModel.cable.localizacioncanalizacion

        val posicion3 = (binding.spinner7.adapter as ArrayAdapter<String>)
            .getPosition(valorSpinner3)

        binding.spinner7.setSelection(posicion3)
    }

    private fun intentarPonerdatoscable() {
        if (listalocalizacion.isNotEmpty() &&
            listaconductormaterial.isNotEmpty() &&
            listaconductoraislamiento.isNotEmpty() &&
            viewModel.vercable
        ) {
            modificacionedittext()
            ponerdatoscable()
            binding.botonSiguiente.text = "Volver"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Liberamos el binding
        _binding = null
    }
}