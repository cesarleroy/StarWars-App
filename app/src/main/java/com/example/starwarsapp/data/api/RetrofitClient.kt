package com.example.starwarsapp.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton que provee la instancia de Retrofit configurada.
 */
object RetrofitClient {

    private const val BASE_URL = "https://swapi.info/api/"

    // Interceptor para ver las peticiones/respuestas en Logcat (solo debug)
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // Instancia perezosa (se crea solo cuando se necesita)
    val swapiService: SwapiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SwapiService::class.java)
    }
}
