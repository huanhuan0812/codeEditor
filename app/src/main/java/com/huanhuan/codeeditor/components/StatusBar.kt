package com.huanhuan.codeeditor.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.huanhuan.codeeditor.models.StatusBarItem

// 特殊字符快捷输入数据类
data class SpecialCharItem(
    val char: String,
    val description: String,
    val onClick: () -> Unit
)

@Composable
fun VSCodeStyleStatusBar(
    modifier: Modifier = Modifier,
    isKeyboardVisible: Boolean = false, // 从外部传入键盘状态
    onToggleKeyboard: (() -> Unit)? = null, // 切换键盘状态的回调
    onSpecialCharInsert: (String) -> Unit = {} // 插入特殊字符的回调
) {
    // 如果键盘可见，显示特殊字符快捷输入栏，否则显示正常状态栏
    if (isKeyboardVisible) {
        SpecialCharQuickInputBar(
            specialChars = getSpecialChars(onSpecialCharInsert),
            onHideKeyboard = { onToggleKeyboard?.invoke() },
            modifier = modifier
        )
    } else {
        NormalStatusBar(
            onShowKeyboard = { onToggleKeyboard?.invoke() },
            modifier = modifier
        )
    }
}

// 获取特殊字符列表
private fun getSpecialChars(onInsert: (String) -> Unit): List<SpecialCharItem> {
    return listOf(
        SpecialCharItem("{", "左大括号", { onInsert("{") }),
        SpecialCharItem("}", "右大括号", { onInsert("}") }),
        SpecialCharItem("[", "左中括号", { onInsert("[") }),
        SpecialCharItem("]", "右中括号", { onInsert("]") }),
        SpecialCharItem("(", "左小括号", { onInsert("(") }),
        SpecialCharItem(")", "右小括号", { onInsert(")") }),
        SpecialCharItem("<", "左尖括号", { onInsert("<") }),
        SpecialCharItem(">", "右尖括号", { onInsert(">") }),
        SpecialCharItem("\"", "双引号", { onInsert("\"") }),
        SpecialCharItem("'", "单引号", { onInsert("'") }),
        SpecialCharItem(";", "分号", { onInsert(";") }),
        SpecialCharItem("=", "等号", { onInsert("=") }),
        SpecialCharItem(":", "冒号", { onInsert(":") }),
        SpecialCharItem(",", "逗号", { onInsert(",") }),
        SpecialCharItem(".", "点", { onInsert(".") }),
        SpecialCharItem("\\", "反斜杠", { onInsert("\\") }),
        SpecialCharItem("/", "斜杠", { onInsert("/") }),
        SpecialCharItem("!", "感叹号", { onInsert("!") }),
        SpecialCharItem("?", "问号", { onInsert("?") }),
        SpecialCharItem("$", "美元符", { onInsert("$") }),
        SpecialCharItem("&", "与符号", { onInsert("&") }),
        SpecialCharItem("|", "或符号", { onInsert("|") }),
        SpecialCharItem("~", "波浪号", { onInsert("~") }),
        SpecialCharItem("Tab", "制表符", { onInsert("\t") }),
        SpecialCharItem("Space", "空格", { onInsert(" ") })
    )
}

@Composable
fun NormalStatusBar(
    onShowKeyboard: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentBranch by remember { mutableStateOf("main") }
    var lineCount by remember { mutableStateOf(100) }
    var currentLine by remember { mutableStateOf(1) }
    var currentColumn by remember { mutableStateOf(1) }
    var scrollPosition by remember { mutableStateOf("Top") }

    val leftStatusItems = listOf(
        StatusBarItem(icon = Icons.Default.Code, text = "UTF-8"),
        StatusBarItem(icon = Icons.Default.Language, text = "LF"),
        StatusBarItem(text = "Kotlin"),
        StatusBarItem(icon = Icons.Default.AccountTree, text = currentBranch)
    )

    val rightStatusItems = listOf(
        StatusBarItem(text = "Ln $currentLine, Col $currentColumn"),
        StatusBarItem(text = "Spaces: 4"),
        StatusBarItem(text = "$lineCount lines"),
        StatusBarItem(icon = Icons.Default.Warning, text = "0"),
        StatusBarItem(icon = Icons.Default.Error, text = "0"),
        StatusBarItem(text = scrollPosition),
        StatusBarItem(
            icon = Icons.Default.Keyboard,
            text = "键盘",
            onClick = onShowKeyboard
        )
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(28.dp)
            .background(Color(0xFF007ACC)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 左侧项目
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            leftStatusItems.forEachIndexed { index, item ->
                StatusBarItemWithDivider(
                    item = item,
                    showDivider = index < leftStatusItems.size - 1
                )
            }
        }

        // 右侧项目
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            rightStatusItems.forEachIndexed { index, item ->
                StatusBarItemWithDivider(
                    item = item,
                    showDivider = index < rightStatusItems.size - 1
                )
            }
        }
    }
}

@Composable
fun SpecialCharQuickInputBar(
    specialChars: List<SpecialCharItem>,
    onHideKeyboard: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(36.dp) // 增高以容纳更多内容
            .background(Color(0xFF2D2D30)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 隐藏键盘按钮
        Icon(
            imageVector = Icons.Default.KeyboardHide,
            contentDescription = "隐藏键盘",
            tint = Color.White,
            modifier = Modifier
                .size(20.dp)
                .padding(horizontal = 12.dp)
                .clickable { onHideKeyboard() }
        )

        Divider(
            modifier = Modifier
                .width(1.dp)
                .height(20.dp),
            color = Color(0xFF555555)
        )

        // 特殊字符快捷输入项 - 支持水平滚动
        Row(
            modifier = Modifier
                .weight(1f)
                .horizontalScroll(rememberScrollState()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            specialChars.forEach { charItem ->
                SpecialCharButton(
                    charItem = charItem,
                    modifier = Modifier.padding(horizontal = 2.dp)
                )
            }
        }

        Divider(
            modifier = Modifier
                .width(1.dp)
                .height(20.dp),
            color = Color(0xFF555555)
        )

        // 常用操作按钮
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Undo,
                contentDescription = "撤销",
                tint = Color.White,
                modifier = Modifier
                    .size(20.dp)
                    .padding(horizontal = 8.dp)
                    .clickable { /* 撤销逻辑 */ }
            )

            Icon(
                imageVector = Icons.Default.Redo,
                contentDescription = "重做",
                tint = Color.White,
                modifier = Modifier
                    .size(20.dp)
                    .padding(horizontal = 8.dp)
                    .clickable { /* 重做逻辑 */ }
            )

            Icon(
                imageVector = Icons.Default.ContentCopy,
                contentDescription = "复制",
                tint = Color.White,
                modifier = Modifier
                    .size(20.dp)
                    .padding(horizontal = 8.dp)
                    .clickable { /* 复制逻辑 */ }
            )
        }
    }
}

@Composable
fun SpecialCharButton(
    charItem: SpecialCharItem,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Box(
        modifier = modifier
            .height(28.dp)
            .background(
                if (isPressed) Color(0xFF005A9E) else Color(0xFF3C3C3C)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                charItem.onClick()
            }
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = charItem.char,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1
        )
    }
}

@Composable
fun StatusBarItemWithDivider(
    item: StatusBarItem,
    showDivider: Boolean = true
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        InteractiveStatusBarItemView(item = item)
        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier
                    .width(1.dp)
                    .height(12.dp),
                color = Color(0xFF005A9E)
            )
        }
    }
}

@Composable
fun InteractiveStatusBarItemView(item: StatusBarItem) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Box(
        modifier = Modifier
            .height(28.dp)
            .background(
                if (isPressed) Color(0xFF005A9E) else Color.Transparent
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                item.onClick?.invoke()
            }
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (item.icon != null) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.text,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
            if (item.text.isNotEmpty()) {
                Text(
                    text = item.text,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}