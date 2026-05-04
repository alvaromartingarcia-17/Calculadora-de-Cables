package com.example.calculadoradecables.data

import com.example.calculadoradecables.modelosDataClass.Cable
import com.example.calculadoradecables.modelosDataClass.Usuario
import com.example.calculadoradecables.modelosDataClass.UsuarioRespuesta
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.Call

interface APImetodos {
    @POST("API_CalculadoraCables.php")
    fun iniciarsesion(
        @Query("metodoAPI") metodo: String,
        @Body usuario: Usuario
        ): Call<UsuarioRespuesta>

    @POST("API_CalculadoraCables.php")
    fun crearcable(
        @Query("metodoAPI") metodo: String,
        @Query("CORREO") correo: String,
        @Body Cable: Cable
    ): Call<ResponseBody>

    @POST("API_CalculadoraCables.php")
    fun crearusuario(
        @Query("metodoAPI") metodo: String,
        @Body usuario: Usuario
    ): Call<ResponseBody>

    @GET("API_CalculadoraCables.php")
    fun cablesusuario(
        @Query("metodoAPI") metodo: String,
        @Query("CORREO") usuario: String
        ): Call<MutableList<Cable>>

    @GET("API_CalculadoraCables.php")
    fun caidatensionmax(
        @Query("metodoAPI") metodo: String,
    ): Call<MutableList<Double>>

    @GET("API_CalculadoraCables.php")
    fun tipodiferencial(
        @Query("metodoAPI") metodo: String,
    ): Call<MutableList<String>>

    @GET("API_CalculadoraCables.php")
    fun sensibilidad(
        @Query("metodoAPI") metodo: String,
    ): Call<MutableList<String>>

    @GET("API_CalculadoraCables.php")
    fun localizacion(
        @Query("metodoAPI") metodo: String,
    ): Call<MutableList<String>>

    @GET("API_CalculadoraCables.php")
    fun proteccioncable(
        @Query("metodoAPI") metodo: String,
        @Query("proteccionseleccionada") proteccionseleccionada: String,
        @Query("tension") tension: String
        ): Call<ResponseBody>

    @GET("API_CalculadoraCables.php")
    fun seccionconductor(
        @Query("metodoAPI") metodo: String,
        @Query("proteccionseleccionada") proteccionseleccionada: String,
        @Query("tension") tension: String
        ): Call<ResponseBody>
}