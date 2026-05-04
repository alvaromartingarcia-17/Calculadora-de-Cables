package com.example.calculadoradecables.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.calculadoradecables.modelosDataClass.Cable

class CableViewModel : ViewModel() {

    private val _datoscable = MutableLiveData<MutableMap<String, String>>(mutableMapOf())
    val datoscable: LiveData<MutableMap<String, String>> = _datoscable

    var vercable: Boolean = false
    fun meterdatos(key: String, value: String) {
        val current = _datoscable.value ?: mutableMapOf()
        current[key] = value
        _datoscable.value = current
    }

    fun cable(): Cable? {
        val data = _datoscable.value ?: return null
        return try {
            Cable(
                nombre = data["nombre"] ?: return null,
                potencia = data["potencia"] ?: return null,
                tension = data["tension"] ?: return null,
                longitud = data["longitud"] ?: return null,
                caidatensionmax = data["caidatensionmax"] ?: return null,
                proteccionseleccionada = data["proteccionseleccionada"] ?: return null,
                protecciontermica = data["protecciontermica"] ?: return null,
                protecciondiferencial = data["protecciondiferencial"] ?: return null,
                tipodiferencial = data["tipodiferencial"] ?: return null,
                sensibilidadiferencial = data["sensibilidadiferencial"] ?: return null,
                seccionconductor = data["seccionconductor"] ?: return null,
                tipoconductor = data["tipoconductor"] ?: return null,
                tipoconductor2 = data["tipoconductor2"] ?: return null,
                localizacioncanalizacion = data["localizacioncanalizacion"] ?: return null,
                factorcorreccionlugar = data["factorcorreccionlugar"] ?: return null,
                factorcorrecciontipo = data["factorcorrecciontipo"] ?: return null,
                usuario = data["usuario"] ?: return null
            )
        } catch (e: Exception) { null }
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
        _datoscable.value = mutableMapOf()
    }
}