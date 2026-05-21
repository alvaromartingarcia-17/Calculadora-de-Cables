package com.example.calculadoradecables.modelosDataClass

import com.google.gson.annotations.SerializedName

data class Cable(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("potencia") val potencia: String,
    @SerializedName("longitud") val longitud: String,
    @SerializedName("SECCIONCONDUCTOR") val seccionconductor: String,
    @SerializedName("caidatensionmax") val caidatensionmax: String,
    @SerializedName("tipotension") val tipotension: String,
    @SerializedName("proteccionseleccionada") val proteccionseleccionada: String,
    @SerializedName("protecciontermica") val protecciontermica: String,
    @SerializedName("protecciondiferencial") val protecciondiferencial: String,
    @SerializedName("tipodiferencial") val tipodiferencial: String,
    @SerializedName("sensibilidaddiferencial") val sensibilidadiferencial: String,
    @SerializedName("tipoconductormaterial") val tipoconductormaterial: String,
    @SerializedName("tipoconductoraislamiento") val tipoconductorasilamiento: String,
    @SerializedName("localizacioncanalizacion") val localizacioncanalizacion: String,
    @SerializedName("factorcorreccionlugar") val factorcorreccionlugar: String,
    @SerializedName("usuario") val usuario: String
)