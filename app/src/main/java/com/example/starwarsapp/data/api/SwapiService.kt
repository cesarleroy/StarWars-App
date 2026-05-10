package com.example.starwarsapp.data.api

import com.example.starwarsapp.data.model.Character
import retrofit2.http.GET


interface SwapiService {

    @GET("people/")
    suspend fun getCharacters(): List<Character>
}
