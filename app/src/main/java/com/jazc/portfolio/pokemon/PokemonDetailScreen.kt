package com.jazc.portfolio.pokemon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jazc.designsystem.theme.JazcTheme
import com.jazc.portfolio.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(
    pokemon: PokemonItem?,
    isLoading: Boolean,
    hasError: Boolean,
    onRetry: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(pokemon?.name ?: stringResource(R.string.pokemon_list_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.pokemon_back))
                    }
                },
            )
        },
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding).consumeWindowInsets(padding)) {
            if (isLoading || hasError) {
                PokemonLoading(isLoading, onRetry)
            } else if (pokemon == null) {
                Text(stringResource(R.string.pokemon_not_found), modifier = Modifier.align(Alignment.Center))
            } else Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp),
            ) {
                Box(
                    modifier = Modifier.widthIn(max = 240.dp).fillMaxWidth().aspectRatio(1f)
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest, MaterialTheme.shapes.medium),
                    contentAlignment = Alignment.Center,
                ) {
                    AsyncImage(
                        model = pokemon.frontDefault,
                        contentDescription = pokemon.name,
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        contentScale = ContentScale.Fit,
                        placeholder = painterResource(android.R.drawable.ic_menu_gallery),
                        error = painterResource(android.R.drawable.ic_menu_gallery),
                        fallback = painterResource(android.R.drawable.ic_menu_gallery),
                    )
                }
                Row(Modifier.widthIn(max = 400.dp).fillMaxWidth()) {
                    Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(stringResource(R.string.pokemon_height), style = MaterialTheme.typography.labelLarge)
                        Text(stringResource(R.string.pokemon_height_value, pokemon.height / 10.0), style = MaterialTheme.typography.headlineSmall)
                    }
                    Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(stringResource(R.string.pokemon_weight), style = MaterialTheme.typography.labelLarge)
                        Text(stringResource(R.string.pokemon_weight_value, pokemon.weight / 10.0), style = MaterialTheme.typography.headlineSmall)
                    }
                }
                Column(Modifier.widthIn(max = 400.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(stringResource(R.string.pokemon_types), style = MaterialTheme.typography.titleMedium)
                    pokemon.types.forEach { type ->
                        Text(type, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PokemonDetailPreview() {
    JazcTheme {
        PokemonDetailScreen(
            pokemon = PokemonItem(1, "bulbasaur", 7, 69, listOf("grass", "poison"), null),
            isLoading = false, hasError = false, onRetry = {}, onBack = {},
        )
    }
}
