package com.huanhuan.codeeditor.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.huanhuan.codeeditor.models.SubMenuItem
import com.huanhuan.codeeditor.models.MenuItem
import kotlin.system.exitProcess

@Composable
fun VSCodeStyleTopAppBar(
    modifier: Modifier = Modifier
) {
    var currentFile by remember { mutableStateOf("MainActivity.kt") }

    val menuItems = listOf(
        MenuItem("文件", listOf(
            SubMenuItem("新建文件") { /* 新建文件逻辑 */ },
            SubMenuItem("打开文件") { /* 打开文件逻辑 */ },
            SubMenuItem("保存") { /* 保存逻辑 */ },
            SubMenuItem("另存为") { /* 另存为逻辑 */ },
            SubMenuItem("退出") { exitProcess(0); /* 退出逻辑 */ }
        )),
        MenuItem("编辑", listOf(
            SubMenuItem("撤销") { /* 撤销逻辑 */ },
            SubMenuItem("重做") { /* 重做逻辑 */ },
            SubMenuItem("剪切") { /* 剪切逻辑 */ },
            SubMenuItem("复制") { /* 复制逻辑 */ },
            SubMenuItem("粘贴") { /* 粘贴逻辑 */ }
        )),
        MenuItem("选择", listOf(
            SubMenuItem("全选") { /* 全选逻辑 */ },
            SubMenuItem("选择行") { /* 选择行逻辑 */ },
            SubMenuItem("选择所有匹配项") { /* 选择所有匹配项逻辑 */ }
        )),
        MenuItem("查看", listOf(
            SubMenuItem("命令面板") { /* 命令面板逻辑 */ },
            SubMenuItem("放大") { /* 放大逻辑 */ },
            SubMenuItem("缩小") { /* 缩小逻辑 */ },
            SubMenuItem("切换侧边栏") { /* 切换侧边栏逻辑 */ }
        )),
        MenuItem("运行", listOf(
            SubMenuItem("运行项目") { /* 运行项目逻辑 */ },
            SubMenuItem("调试") { /* 调试逻辑 */ },
            SubMenuItem("终止") { /* 终止逻辑 */ }
        ))
    )

    Column(modifier = modifier) {
        // 顶部菜单栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp) // 增加到40dp，更符合标准工具栏高度
                .background(Color(0xFFF3F3F3)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 菜单项
            menuItems.forEach { menuItem ->
                DropdownMenuButton(
                    title = menuItem.title,
                    subMenuItems = menuItem.subItems
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // 文件名称
            Text(
                text = currentFile,
                color = Color(0xFF333333),
                fontSize = 14.sp, // 增加到14sp，提高可读性
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        // 标签栏（类似VSCode的文件标签）
        VSCodeStyleTabBar(currentFile = currentFile)
    }
}

@Composable
fun DropdownMenuButton(
    title: String,
    subMenuItems: List<SubMenuItem>
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .height(40.dp) // 与父容器高度一致
            .wrapContentWidth(Alignment.CenterHorizontally)
    ) {
        // 菜单按钮
        Text(
            text = title,
            color = Color(0xFF333333),
            fontSize = 14.sp, // 增加到14sp
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .height(40.dp)
                .padding(horizontal = 16.dp) // 增加水平内边距
                .clickable { expanded = true }
                .wrapContentWidth(Alignment.CenterHorizontally)
                .wrapContentHeight(Alignment.CenterVertically)
                .background(
                    if (expanded) Color(0xFFE0E0E0) else Color.Transparent
                )
        )

        // 下拉菜单
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color(0xFFFFFFFF))
                .widthIn(min = 160.dp) // 设置最小宽度
        ) {
            subMenuItems.forEach { subMenuItem ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = subMenuItem.title,
                            color = Color(0xFF333333),
                            fontSize = 14.sp, // 增加到14sp
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    onClick = {
                        subMenuItem.onClick()
                        expanded = false
                    },
                    modifier = Modifier
                        .background(Color(0xFFFFFFFF))
                        .height(32.dp) // 设置菜单项高度
                )
            }
        }
    }
}

@Composable
fun VSCodeStyleTabBar(
    currentFile: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp) // 增加到40dp，与顶部菜单栏一致
            .background(Color(0xFFF9F9F9)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 当前文件标签
        Box(
            modifier = Modifier
                .height(40.dp)
                .background(Color(0xFFFFFFFF))
                .padding(horizontal = 20.dp), // 增加水平内边距
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = currentFile,
                color = Color(0xFF333333),
                fontSize = 14.sp, // 增加到14sp
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // 标签栏操作按钮
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "新建文件",
            tint = Color(0xFF666666),
            modifier = Modifier
                .size(24.dp) // 增加到24dp
                .padding(12.dp) // 增加到12dp
                .clickable { /* 新建文件逻辑 */ }
        )
    }
}