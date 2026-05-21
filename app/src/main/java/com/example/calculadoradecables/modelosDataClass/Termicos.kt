package com.example.calculadoradecables.modelosDataClass

import com.google.gson.annotations.SerializedName

data class Termicos(
    @SerializedName("ID") val id: Int,
    @SerializedName("PROTECCIONSELECCIONADA") val proteccionSeleccionada: Int,
    @SerializedName("PROTECCIONTERMICA") val proteccionTermica: String,
    @SerializedName("SECCIONCONDUCTOR") val seccionConductor: Double,
    @SerializedName("TENSION") val tension: String,
    @SerializedName("ProteccionDiferencial") val proteccionDiferencial: String
)
