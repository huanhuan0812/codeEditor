package com.huanhuan.codeeditor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.huanhuan.codeeditor.ui.theme.CodeEditorTheme
import com.huanhuan.codeeditor.components.*
import android.view.View
import androidx.core.view.WindowCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 设置沉浸式全屏
        setImmersiveMode()

        setContent {
            CodeEditorTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        VSCodeStyleTopAppBar()
                    },
                    bottomBar = {
                        VSCodeStyleStatusBar()
                    }
                ) { innerPadding ->
                    CodeEditorContent(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun setImmersiveMode() {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    }

    override fun onResume() {
        super.onResume()
        setImmersiveMode()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            setImmersiveMode()
        }
    }
}