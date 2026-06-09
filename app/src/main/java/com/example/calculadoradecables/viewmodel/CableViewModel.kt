package com.example.calculadoradecables.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.calculadoradecables.modelosDataClass.Cable
import com.example.calculadoradecables.modelosDataClass.NuevoCable
import com.example.calculadoradecables.modelosDataClass.Termicos

class CableViewModel : ViewModel() {

    private val _datoscable = MutableLiveData<MutableMap<String, String>>(mutableMapOf())
    val datoscable: LiveData<MutableMap<String, String>> = _datoscable

    var vercable: Boolean = false

    lateinit var cable : Cable
    var termicos: Termicos = Termicos(0, 0, "", 0.0, "", "")
    fun meterdatos(key: String, value: String) {
        val current = _datoscable.value ?: mutableMapOf()
        current[key] = value
        _datoscable.value = current
    }

    fun CrearCable(): NuevoCable? {
        val data = _datoscable.value ?: return null
        Log.d("nose", "nombre: ${data["nombre"]}")
        Log.d("nose", "potencia: ${data["potencia"]}")
        Log.d("nose", "tension: ${data["tension"]}")
        Log.d("nose", "longitud: ${data["longitud"]}")
        Log.d("nose", "idtablatermicos: ${data["idtablatermicos"]}")
        Log.d("nose", "caidatensionmax: ${data["caidatensionmax"]}")
        Log.d("nose", "tipodiferencial: ${data["tipodiferencial"]}")
        Log.d("nose", "sensibilidadiferencial: ${data["sensibilidadiferencial"]}")
        Log.d("nose", "tipoconductormaterial: ${data["tipoconductormaterial"]}")
        Log.d("nose", "tipoconductoraislamiento: ${data["tipoconductoraislamiento"]}")
        Log.d("nose", "localizacioncanalizacion: ${data["localizacioncanalizacion"]}")
        Log.d("nose", "factorcorreccionlugar: ${data["factorcorreccionlugar"]}")
        Log.d("nose", "factorcorrecciontipo: ${data["factorcorrecciontipo"]}")

        return try {
            NuevoCable(
                nombre = data["nombre"] ?: return null,
                potencia = data["potencia"] ?: return null,
                longitud = data["longitud"] ?: return null,
                idtablatermicos = data["idtablatermicos"] ?: return null,
                seccionconductor = data["seccionconductor"] ?: return null,
                caidatensionmax = data["caidatensionmax"] ?: return null,
                tipodiferencial = data["tipodiferencial"] ?: return null,
                sensibilidadiferencial = data["sensibilidadiferencial"] ?: return null,
                tipoconductormaterial = data["tipoconductormaterial"] ?: return null,
                tipoconductoraislamiento = data["tipoconductoraislamiento"] ?: return null,
                localizacioncanalizacion = data["localizacioncanalizacion"] ?: return null,
                factorcorreccionlugar = data["factorcorreccionlugar"] ?: return null,
                usuario = data["usuario"] ?: "",
                numerocable = data["numerocable"]?.toIntOrNull() ?: return null            )
        } catch (e: Exception) {
            Log.d("nose",e.toString())
            null }
    }

    fun depuraciondatos(): String {
        val data = _datoscable.value ?: return "No hay datos"
        if (data.isEmpty()) return "El mapa está vacío"

        val builder = StringBuilder()
        builder.appendLine("=== DATOS DEL CABLE ===")
        for ((key, value) in data) {
            builder.appendLine("$key: $value")
        }
        return builder.toString()
    }

    fun borrardatos() {
        val correo = _datoscable.value?.get("usuario")
        _datoscable.value = mutableMapOf()
        if (correo != null) {
            _datoscable.value?.put("usuario", correo)
        }
    }
}