# Buscador de Personajes de Star Wars ⭐
Aplicación Android desarrollada con **Jetpack Compose** para la Unidad 5 del curso de Programación de Dispositivos Móviles.

## Características
* **Retrofit + OkHttp**: Petición a `https://swapi.info/api/people/` para obtener los 82 personajes del universo Star Wars, con interceptor de logging para depuración.
* **Coil con soporte SVG**: `SubcomposeAsyncImage` para cargar avatares desde DiceBear, con estados de carga y fallback a inicial del nombre si la imagen no está disponible.
* **Tres estados de UI**: Arquitectura basada en una `sealed class` (`CharacterUiState`) que maneja de forma explícita los estados `Loading`, `Success` y `Error`, manteniendo la UI siempre en un estado predecible.
* **Patrón Repositorio**: `CharacterRepository` desacopla el acceso a datos del `ViewModel`, con cache en memoria para evitar peticiones repetidas durante la búsqueda.
* **Buscador con Debounce**: Filtrado local en tiempo real con un delay de 500ms para no disparar búsquedas en cada tecla presionada.

## 📄 Archivos principales
* [StarWarsApp.kt](app/src/main/java/com/example/starwarsapp/MainActivity.kt) — Inicializa Coil con soporte SVG
* [CharacterViewModel.kt](app/src/main/java/com/example/starwarsapp/ui/viewmodel/CharacterViewModel.kt) — Lógica de negocio, Coroutines y StateFlow
* [CharacterScreen.kt](app/src/main/java/com/example/starwarsapp/ui/screen/CharacterScreen.kt) — UI completa en Jetpack Compose
* [CharacterRepository.kt](app/src/main/java/com/example/starwarsapp/data/repository/CharacterRepository.kt) — Patrón Repositorio con cache en memoria
* [SwapiService.kt](app/src/main/java/com/example/starwarsapp/data/api/SwapiService.kt) — Interfaz Retrofit con los endpoints de SWAPI

## 📸 Vista Previa
> ![Preview](./assets/StarWarsApp_Preview.gif)

## 🛠️ Tecnologías
* Kotlin
* Jetpack Compose (StateFlow, ViewModel, LazyColumn)
* Retrofit 2 + Gson
* Coil 2 (coil-compose + coil-svg)
* Coroutines
* Material Design 3

---
*Programación de Dispositivos Móviles 5CV51*

*Carmona González César Leroy - IPN UPIICSA*