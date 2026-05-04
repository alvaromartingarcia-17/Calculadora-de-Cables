package com.example.calculadoradecables.modelosDataClass

import com.google.gson.annotations.SerializedName

data class Cable(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("potencia") val potencia: String,
    @SerializedName("tension") val tension: String,
    @SerializedName("longitud") val longitud: String,
    @SerializedName("caidatensionmax") val caidatensionmax: String,
    @SerializedName("proteccionseleccionada") val proteccionseleccionada: String,
    @SerializedName("protecciontermica") val protecciontermica: String,
    @SerializedName("protecciondiferencial") val protecciondiferencial: String,
    @SerializedName("tipodiferencial") val tipodiferencial: String,
    @SerializedName("sensibilidaddiferencial") val sensibilidadiferencial: String,
    @SerializedName("seccionconductor") val seccionconductor: String,
    @SerializedName("tipoconductor") val tipoconductor: String,
    @SerializedName("tipoconductor2") val tipoconductor2: String,
    @SerializedName("localizacioncanalizacion") val localizacioncanalizacion: String,
    @SerializedName("factorcorreccionlugar") val factorcorreccionlugar: String,
    @SerializedName("factorcorrecciontipo") val factorcorrecciontipo: String,
    @SerializedName("usuario") val usuario: String
)