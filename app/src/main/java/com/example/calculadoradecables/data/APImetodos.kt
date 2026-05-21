package com.example.calculadoradecables.data

import com.example.calculadoradecables.modelosDataClass.Cable
import com.example.calculadoradecables.modelosDataClass.NuevoCable
import com.example.calculadoradecables.modelosDataClass.Termicos
import com.example.calculadoradecables.modelosDataClass.Usuario
import com.example.calculadoradecables.modelosDataClass.UsuarioRespuesta
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
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
        @Body NuevoCable: NuevoCable
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
    fun termicos(
        @Query("metodoAPI") metodo: String,
        @Query("proteccion") proteccionseleccionada: Double,
        @Query("tension") tension: String
    ): Call<Termicos>

    @GET("API_CalculadoraCables.php")
    fun caidatensionmax(
        @Query("metodoAPI") metodo: String,
    ): Call<MutableList<Double>>

    @GET("API_CalculadoraCables.php")
    fun seccionconductor(
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
    fun tipoconductoraislamiento(
        @Query("metodoAPI") metodo: String,
    ): Call<MutableList<String>>

    @GET("API_CalculadoraCables.php")
    fun tipoconductormaterial(
        @Query("metodoAPI") metodo: String,
    ): Call<MutableList<String>>


    @POST("API_CalculadoraCables.php")
    fun olvidarContrasena(
        @Query("metodoAPI") metodo: String,
        @Body body: RequestBody): Call<ResponseBody>


    @POST("API_CalculadoraCables.php")
    fun resetearContrasena(
        @Query("metodoAPI") metodo: String,
        @Body body: RequestBody ): Call<ResponseBody>

}