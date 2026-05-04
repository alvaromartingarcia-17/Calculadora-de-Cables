package com.example.calculadoradecables.modelosDataClass

import com.google.gson.annotations.SerializedName

data class Usuario(
    @SerializedName("NOMBRE") val nombre : String,
    @SerializedName("CORREO") val correo : String,
    @SerializedName("CONTRASEÑA") val contraseña : String
)