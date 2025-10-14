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
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_FULLSCREEN
                )
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