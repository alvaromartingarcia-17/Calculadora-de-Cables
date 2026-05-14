package com.example.calculadoradecables.modelosDataClass

import com.google.gson.annotations.SerializedName

data class NuevoCable(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("potencia") val potencia: String,
    @SerializedName("longitud") val longitud: String,
    @SerializedName("termicos") val idtablatermicos: String,
    @SerializedName("caidatensionmax") val caidatensionmax: String,
    @SerializedName("tipodiferencial") val tipodiferencial: String,
    @SerializedName("sensibilidaddiferencial") val sensibilidadiferencial: String,
    @SerializedName("tipoconductormaterial") val tipoconductormaterial: String,
    @SerializedName("tipoconductoraislamiento") val tipoconductoraislamiento: String,
    @SerializedName("localizacioncanalizacion") val localizacioncanalizacion: String,
    @SerializedName("factorcorreccionlugar") val factorcorreccionlugar: String,
    @SerializedName("usuario") val usuario: String
)