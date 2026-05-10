package com.example.starwarsapp.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.SubcomposeAsyncImage
import com.example.starwarsapp.data.model.Character
import com.example.starwarsapp.ui.state.CharacterUiState
import com.example.starwarsapp.ui.viewmodel.CharacterViewModel

// Colores temáticos de Star Wars
private val StarWarsYellow = Color(0xFFFFE81F)
private val DarkBackground = Color(0xFF0A0A0F)
private val CardBackground = Color(0xFF1A1A2E)
private val CardBorder = Color(0xFF2D2D4A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterScreen(viewModel: CharacterViewModel = viewModel()) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            // Barra superior con gradiente amarillo Star Wars
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color(0xFF1A1A00), Color(0xFF333300))
                        )
                    )
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Column {
                    Text(
                        text = "⭐ Star Wars",
                        color = StarWarsYellow,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = "Buscador de Personajes",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 13.sp
                    )
                }
            }
        },
        containerColor = DarkBackground
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // ── Barra de búsqueda ──────────────────────────────────────────
            SearchBar(
                query = searchQuery,
                onQueryChange = viewModel::onSearchQueryChange
            )

            // ── Contenido según el estado de la UI ────────────────────────
            when (val state = uiState) {

                is CharacterUiState.Loading -> LoadingContent()

                is CharacterUiState.Success -> CharacterList(characters = state.characters)

                is CharacterUiState.Error -> ErrorContent(
                    message = state.message,
                    onRetry = viewModel::loadCharacters
                )
            }
        }
    }
}

// ── Barra de búsqueda ─────────────────────────────────────────────────────────
@Composable
private fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        placeholder = {
            Text("Buscar personaje...", color = Color.Gray)
        },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Buscar", tint = StarWarsYellow)
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = StarWarsYellow,
            unfocusedBorderColor = CardBorder,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = StarWarsYellow,
            focusedContainerColor = CardBackground,
            unfocusedContainerColor = CardBackground
        )
    )
}

// ── Estado de Carga ───────────────────────────────────────────────────────────
@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                color = StarWarsYellow,
                strokeWidth = 4.dp,
                modifier = Modifier.size(56.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Conectando a la Fuerza...",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 14.sp
            )
        }
    }
}

// ── Estado de Error ───────────────────────────────────────────────────────────
@Composable
private fun ErrorContent(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Error",
                tint = Color(0xFFFF6B6B),
                modifier = Modifier.size(56.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 15.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = StarWarsYellow),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Reintentar", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ── Lista de personajes ───────────────────────────────────────────────────────
@Composable
private fun CharacterList(characters: List<Character>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "${characters.size} personaje(s) encontrado(s)",
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
        items(characters, key = { it.url }) { character ->
            CharacterCard(character = character)
        }
    }
}

// ── Tarjeta de personaje ──────────────────────────────────────────────────────
@Composable
private fun CharacterCard(character: Character) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ── Imagen del personaje con Coil ──────────────────────────────
            SubcomposeAsyncImage(
                model = character.imageUrl,
                contentDescription = character.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2D2D4A)),
                contentScale = ContentScale.Crop,
                loading = {
                    // Placeholder mientras carga la imagen
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            color = StarWarsYellow,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                error = {
                    // Imagen de respaldo si falla la carga
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF2D2D4A))
                    ) {
                        Text(
                            text = character.name.first().toString(),
                            color = StarWarsYellow,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.width(14.dp))

            // ── Información del personaje ──────────────────────────────────
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = character.name,
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))

                // Detalles en chips pequeños
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    InfoChip(label = "📅 ${character.birthYearLabel}")
                    InfoChip(label = "⚧ ${character.genderLabel}")
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    InfoChip(label = "📏 ${character.heightLabel}")
                    InfoChip(label = "⚖️ ${character.massLabel}")
                }
            }

            // Badge con el ID del personaje
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(StarWarsYellow),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "#${character.id}",
                    color = Color.Black,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

// ── Chip de información ───────────────────────────────────────────────────────
@Composable
private fun InfoChip(label: String) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = Color(0xFF2D2D4A)
    ) {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.75f),
            fontSize = 11.sp,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
        )
    }
}
