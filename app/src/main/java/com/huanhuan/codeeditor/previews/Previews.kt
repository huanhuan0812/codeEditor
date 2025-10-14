package com.huanhuan.codeeditor.previews

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.huanhuan.codeeditor.components.*
import com.huanhuan.codeeditor.ui.theme.CodeEditorTheme

@Preview(showBackground = true)
@Composable
fun CodeEditorPreview() {
    CodeEditorTheme {
        Scaffold(
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

@Preview(showBackground = true)
@Composable
fun StatusBarPreview() {
    CodeEditorTheme {
        VSCodeStyleStatusBar()
    }
}

@Preview(showBackground = true)
@Composable
fun TopAppBarPreview() {
    CodeEditorTheme {
        VSCodeStyleTopAppBar()
    }
}