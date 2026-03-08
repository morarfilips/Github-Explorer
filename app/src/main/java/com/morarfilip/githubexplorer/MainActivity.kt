package com.morarfilip.githubexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.morarfilip.githubexplorer.ui.navigation.NavGraph
import com.morarfilip.githubexplorer.ui.theme.GithubExplorerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enables edge-to-edge drawing for a modern look
        enableEdgeToEdge()

        setContent {
            GithubExplorerTheme {
                // The NavGraph manages the screens (List and Details)
                // and handles the navigation logic
                NavGraph()
            }
        }
    }
}