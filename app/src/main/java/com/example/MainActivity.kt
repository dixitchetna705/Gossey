package com.example

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.MainScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.SocialViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val socialViewModel: SocialViewModel = viewModel()
            val themeMode by socialViewModel.themeMode.collectAsStateWithLifecycle()
            val isSystemDark = androidx.compose.foundation.isSystemInDarkTheme()
            val darkTheme = when (themeMode) {
                "Light" -> false
                "Dark" -> true
                else -> isSystemDark
            }

            // Check if there is an incoming ACTION_SEND intent
            LaunchedEffect(intent) {
                handleIncomingIntent(intent, socialViewModel)
            }

            MyApplicationTheme(darkTheme = darkTheme) {
                MainScreen(viewModel = socialViewModel)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    private fun handleIncomingIntent(intent: Intent?, vm: SocialViewModel) {
        if (intent == null) return
        if (intent.action == Intent.ACTION_SEND) {
            val type = intent.type
            val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
            var sharedMediaUri: String? = null
            var isVideo = false

            if (type != null) {
                if (type.startsWith("image/")) {
                    val uri = intent.getParcelableExtra<android.net.Uri>(Intent.EXTRA_STREAM)
                    sharedMediaUri = uri?.toString()
                } else if (type.startsWith("video/")) {
                    val uri = intent.getParcelableExtra<android.net.Uri>(Intent.EXTRA_STREAM)
                    sharedMediaUri = uri?.toString()
                    isVideo = true
                }
            }
            vm.handleSharedContent(sharedText, sharedMediaUri, isVideo)
        }
    }
}
