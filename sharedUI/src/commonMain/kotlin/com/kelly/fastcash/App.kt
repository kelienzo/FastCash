package com.kelly.fastcash

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kelly.fastcash.presentation.navigation.FastCashNavGraph

@Composable
fun App() {
    MaterialTheme {
        Scaffold { padding ->
            FastCashNavGraph(
                modifier = Modifier.fillMaxSize()
                    .padding(padding)
                    .consumeWindowInsets(padding)
            )
        }
    }
}