package com.huanhuan.codeeditor.models

import androidx.compose.ui.graphics.vector.ImageVector

data class StatusBarItem(
    val text: String = "",
    val icon: ImageVector? = null,
    val onClick: (() -> Unit)? = null
)

data class MenuItem(
    val title: String,
    val subItems: List<SubMenuItem>
)

data class SubMenuItem(
    val title: String,
    val onClick: () -> Unit
)

data class FileItem(
    val name: String,
    val path: String,
    val isDirectory: Boolean,
    val children: List<FileItem>? = null
)