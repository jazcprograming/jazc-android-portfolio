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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jazc.designsystem.theme.JazcTheme
import com.jazc.portfolio.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(pokemonId: Int, onBack: () -> Unit, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Pokemon $pokemonId") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.pokemon_back))
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).consumeWindowInsets(padding)
                .verticalScroll(rememberScrollState()).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            Box(
                modifier = Modifier.widthIn(max = 240.dp).fillMaxWidth().aspectRatio(1f)
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest, MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center,
            ) {
                Text("P", style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Row(Modifier.widthIn(max = 400.dp).fillMaxWidth()) {
                Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(stringResource(R.string.pokemon_height), style = MaterialTheme.typography.labelLarge)
                    Text("100", style = MaterialTheme.typography.headlineSmall)
                }
                Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(stringResource(R.string.pokemon_weight), style = MaterialTheme.typography.labelLarge)
                    Text("100", style = MaterialTheme.typography.headlineSmall)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PokemonDetailPreview() {
    JazcTheme { PokemonDetailScreen(pokemonId = 3, onBack = {}) }
}
