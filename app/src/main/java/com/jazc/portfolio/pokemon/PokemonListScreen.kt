package com.jazc.portfolio.pokemon

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jazc.designsystem.component.JazcText
import com.jazc.designsystem.text.uiText
import com.jazc.designsystem.theme.JazcTextSize
import com.jazc.designsystem.theme.JazcTheme
import com.jazc.portfolio.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonListScreen(
    pokemon: List<PokemonItem>,
    isLoading: Boolean,
    hasError: Boolean,
    onRetry: () -> Unit,
    onPokemonClick: (Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.pokemon_list_title)) },
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
            } else LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(pokemon, key = { it.id }) { item ->
                    PokemonRow(pokemon = item, onClick = { onPokemonClick(item.id) })
                }
            }
        }
    }
}

@Composable
private fun PokemonRow(pokemon: PokemonItem, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier.size(80.dp).background(
                color = MaterialTheme.colorScheme.surfaceContainerHighest,
                shape = MaterialTheme.shapes.small,
            ),
            contentAlignment = Alignment.Center,
        ) {
            AsyncImage(
                model = pokemon.frontDefault,
                contentDescription = null,
                modifier = Modifier.fillMaxSize().padding(4.dp),
                contentScale = ContentScale.Fit,
                placeholder = painterResource(android.R.drawable.ic_menu_gallery),
                error = painterResource(android.R.drawable.ic_menu_gallery),
                fallback = painterResource(android.R.drawable.ic_menu_gallery),
            )
        }
        JazcText(uiText(pokemon.name),
            size = JazcTextSize.Lg20)
    }
}

@Preview(showBackground = true)
@Composable
private fun PokemonListPreview() {
    JazcTheme {
        PokemonListScreen(
            pokemon = listOf(PokemonItem(1, "bulbasaur", 7, 69, listOf("grass", "poison"), null)),
            isLoading = false,
            hasError = false,
            onRetry = {},
            onPokemonClick = {},
            onBack = {},
        )
    }
}
