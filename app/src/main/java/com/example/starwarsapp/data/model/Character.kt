package com.example.starwarsapp.data.model

import com.google.gson.annotations.SerializedName

// Modelo de personaje de Star Wars
data class Character(
    @SerializedName("name")       val name: String,
    @SerializedName("height")     val height: String,
    @SerializedName("mass")       val mass: String,
    @SerializedName("hair_color") val hairColor: String,
    @SerializedName("skin_color") val skinColor: String,
    @SerializedName("gender")     val gender: String,
    @SerializedName("birth_year") val birthYear: String,
    @SerializedName("url")        val url: String
) {

    val id: Int
        get() = url.trimEnd('/').split('/').last().toIntOrNull() ?: 1

    val imageUrl: String
        get() = "https://api.dicebear.com/9.x/lorelei/svg?seed=${id}&backgroundColor=1a1a2e&radius=50"


    val genderLabel: String
        get() = when (gender.lowercase()) {
            "male"          -> "Masculino"
            "female"        -> "Femenino"
            "hermaphrodite" -> "Hermafrodita"
            "none", "n/a", "unknown"   -> "N/A"
            else            -> gender.replaceFirstChar { it.uppercase() }
        }

    val birthYearLabel: String
        get() = if (birthYear.lowercase() == "unknown") "s.f." else birthYear

    val heightLabel: String
        get() = if (height.lowercase() == "unknown") "N/A" else "$height cm"

    val massLabel: String
        get() = if (mass.lowercase() == "unknown") "N/A" else "$mass kg"
}