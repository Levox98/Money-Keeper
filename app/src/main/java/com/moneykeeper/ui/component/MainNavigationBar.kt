package com.moneykeeper.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.moneykeeper.R
import com.moneykeeper.ui.screen.Screen

@Composable
fun MainNavigationBar(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = currentScreen == Screen.TRANSACTIONS,
            onClick = { onScreenSelected(Screen.TRANSACTIONS) },
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text(stringResource(R.string.transactions)) }
        )
        NavigationBarItem(
            selected = currentScreen == Screen.STATISTICS,
            onClick = { onScreenSelected(Screen.STATISTICS) },
            icon = { Icon(Icons.Default.Info, contentDescription = null) },
            label = { Text(stringResource(R.string.statistics)) }
        )
        NavigationBarItem(
            selected = currentScreen == Screen.AI_ANALYSIS,
            onClick = { onScreenSelected(Screen.AI_ANALYSIS) },
            icon = { Icon(Icons.Default.Star, contentDescription = null) },
            label = { Text(stringResource(R.string.ai_analysis)) }
        )
    }
}
