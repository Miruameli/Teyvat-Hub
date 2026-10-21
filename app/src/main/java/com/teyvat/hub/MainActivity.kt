package com.teyvat.hub

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import com.teyvat.hub.presentation.core.navigation.TeyvatHubScaffold
import com.teyvat.hub.presentation.core.theme.TeyvatHubTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Single activity hosting the Compose navigation graph.
 *
 * The activity only wires framework setup; every screen and navigation rule
 * lives under [com.teyvat.hub.presentation] so the UI can be evolved without
 * touching this entry point.
 *
 * It extends AppCompatActivity rather than ComponentActivity because the
 * compatible per-app locale API only reaches activities that AppCompat
 * manages. With a plain ComponentActivity the language choice would silently
 * do nothing below Android 13.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

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
