package com.huanhuan.codeeditor.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import kotlinx.coroutines.launch

private const val DEFAULT_CODE = """fun main() {
    println("Hello, Code Editor!")
}
"""

// 统一行高和字体设置的常量
private val CODE_FONT_SIZE = 14.sp
private val LINE_HEIGHT = 20.sp // 调整为更合适的行高
private val VERTICAL_PADDING = 2.dp
private val LINE_HEIGHT_DP = 20.dp // 以dp为单位的行高

@Composable
fun CodeEditorContent(modifier: Modifier = Modifier) {
    var codeText by remember { mutableStateOf(DEFAULT_CODE) }
    val lineNumberScrollState = rememberLazyListState()
    val codeEditorScrollState = rememberLazyListState()
    val horizontalScrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    // 同步两个滚动状态
    LaunchedEffect(remember { derivedStateOf { lineNumberScrollState.firstVisibleItemIndex } }) {
        if (lineNumberScrollState.isScrollInProgress) {
            codeEditorScrollState.scrollToItem(lineNumberScrollState.firstVisibleItemIndex)
        }
    }

    LaunchedEffect(remember { derivedStateOf { codeEditorScrollState.firstVisibleItemIndex } }) {
        if (codeEditorScrollState.isScrollInProgress) {
            lineNumberScrollState.scrollToItem(codeEditorScrollState.firstVisibleItemIndex)
        }
    }

    // 插入特殊字符的函数
    val onSpecialCharInsert: (String) -> Unit = { char ->
        // 这里可以实现插入特殊字符的逻辑
        // 暂时简单地在代码末尾添加
        codeText += char
    }

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .background(Color(0xFFFFFFFF))
        ) {
            // 行号区域
            LineNumberColumn(
                lineCount = codeText.lines().size,
                scrollState = lineNumberScrollState,
                modifier = Modifier
                    .width(60.dp)
                    .fillMaxHeight()
                    .background(Color(0xFFF5F5F5))
            )

            // 代码编辑区域
            CodeEditorArea(
                codeText = codeText,
                onCodeTextChange = { codeText = it },
                verticalScrollState = codeEditorScrollState,
                horizontalScrollState = horizontalScrollState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
        }

        // 状态栏（现在支持特殊字符快捷输入）
        VSCodeStyleStatusBar(
            onSpecialCharInsert = onSpecialCharInsert,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun LineNumberColumn(
    lineCount: Int,
    scrollState: LazyListState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = scrollState,
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 0.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp) // 确保行之间没有额外间距
    ) {
        itemsIndexed(List(lineCount) { it + 1 }) { index, lineNumber ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(LINE_HEIGHT_DP) // 使用固定的dp高度
            ) {
                Text(
                    text = lineNumber.toString(),
                    color = Color(0xFF6E7681),
                    fontSize = CODE_FONT_SIZE,
                    lineHeight = LINE_HEIGHT,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 12.dp)
                )
            }
        }
    }
}

@Composable
fun CodeEditorArea(
    codeText: String,
    onCodeTextChange: (String) -> Unit,
    verticalScrollState: LazyListState,
    horizontalScrollState: androidx.compose.foundation.ScrollState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
    ) {
        // 水平滚动容器
        Box(
            modifier = Modifier
                .fillMaxSize()
                .horizontalScroll(horizontalScrollState)
        ) {
            // 垂直滚动容器
            LazyColumn(
                state = verticalScrollState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 0.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                itemsIndexed(codeText.lines()) { index, line ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(LINE_HEIGHT_DP)
                    ) {
                        BasicTextField(
                            value = line,
                            onValueChange = { newLine ->
                                val lines = codeText.lines().toMutableList()
                                lines[index] = newLine
                                onCodeTextChange(lines.joinToString("\n"))
                            },
                            textStyle = TextStyle(
                                color = Color(0xFF24292E),
                                fontSize = CODE_FONT_SIZE,
                                lineHeight = LINE_HEIGHT,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Normal
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            decorationBox = { innerTextField ->
                                Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    innerTextField()
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}