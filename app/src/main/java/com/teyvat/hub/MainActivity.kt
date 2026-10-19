package com.teyvat.hub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.teyvat.hub.presentation.navigation.TeyvatHubScaffold
import com.teyvat.hub.presentation.theme.TeyvatHubTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Single activity hosting the Compose navigation graph.
 *
 * The activity only wires framework setup; every screen and navigation rule
 * lives under [com.teyvat.hub.presentation] so the UI can be evolved without
 * touching this entry point.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            TeyvatHubTheme {
                TeyvatHubApp()
            }
        }
    }
}

/** Root composable of the application. */
@Composable
fun TeyvatHubApp() {
    TeyvatHubScaffold()
}
