package com.example.starwarsapp.data.repository

import com.example.starwarsapp.data.api.RetrofitClient
import com.example.starwarsapp.data.model.Character

/**
 * Repositorio que actúa como única fuente de verdad para los datos de personajes.
 *
 * ⚠️ CAMBIO: swapi.info devuelve todos los personajes de una vez (sin paginación).
 * La búsqueda ahora se hace localmente en memoria, filtrando la lista completa.
 * Esto es más eficiente que hacer una petición por cada búsqueda.
 */
class CharacterRepository {

    private val api = RetrofitClient.swapiService

    // Cache en memoria para no repetir la petición de red en cada búsqueda
    private var cachedCharacters: List<Character>? = null

    /**
     * Obtiene todos los personajes.
     * Usa el cache si ya se descargaron previamente.
     */
    suspend fun getCharacters(): List<Character> {
        cachedCharacters?.let { return it }
        return api.getCharacters().also { cachedCharacters = it }
    }

    /**
     * Filtra personajes por nombre localmente.
     * Si el query está vacío, devuelve todos.
     */
    suspend fun searchCharacters(query: String): List<Character> {
        val all = getCharacters()
        return if (query.isBlank()) all
        else all.filter { it.name.contains(query, ignoreCase = true) }
    }
}
