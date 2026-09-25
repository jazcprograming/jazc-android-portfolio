package com.jazc.portfolio.pokemon

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jazc.portfolio.R

@Composable
internal fun PokemonLoading(isLoading: Boolean, onRetry: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Text(stringResource(R.string.pokemon_load_error))
            Button(onClick = onRetry) { Text(stringResource(R.string.pokemon_retry)) }
        }
    }
}
