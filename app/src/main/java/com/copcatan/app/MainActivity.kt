package com.copcatan.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.copcatan.app.ui.CopCatanApp
import com.copcatan.app.ui.theme.CopCatanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CopCatanTheme {
                CopCatanApp()
            }
        }
    }
}
