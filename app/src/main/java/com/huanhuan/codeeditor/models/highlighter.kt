// SyntaxHighlighter.kt
package com.huanhuan.codeeditor.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

// 支持的语言类型
enum class ProgrammingLanguage {
    KOTLIN, JAVA, JAVASCRIPT, PYTHON, CPP, C, XML, JSON, HTML, CSS, SQL
}

// 语法规则
data class SyntaxRule(
    val pattern: Regex,
    val style: SpanStyle
)

// 语言配置
data class LanguageConfig(
    val keywords: Set<String>,
    val types: Set<String> = emptySet(),
    val builtins: Set<String> = emptySet(),
    val singleLineComment: String = "//",
    val multiLineCommentStart: String = "/*",
    val multiLineCommentEnd: String = "*/",
    val stringDelimiters: Set<Char> = setOf('"', '\''),
    val numberPattern: Regex = Regex("\\b\\d+\\.?\\d*\\b")
)

// 语法高亮器
class SyntaxHighlighter {

    fun highlight(code: String, language: ProgrammingLanguage): AnnotatedString {
        return buildAnnotatedString {
            val config = getLanguageConfig(language)
            val lines = code.lines()

            lines.forEachIndexed { lineIndex, line ->
                if (lineIndex > 0) append("\n")
                highlightLine(line, config)
            }
        }
    }

    // 改为 AnnotatedString.Builder 的扩展函数
    private fun AnnotatedString.Builder.highlightLine(line: String, config: LanguageConfig) {
        var currentIndex = 0
        val tokens = mutableListOf<Pair<IntRange, SpanStyle>>()

        // 处理注释
        if (config.singleLineComment.isNotEmpty() && line.trim().startsWith(config.singleLineComment)) {
            withStyle(SpanStyle(color = Color(0xFF6A9955))) {
                append(line)
            }
            return
        }

        // 匹配各种语法元素
        val patterns = listOf(
            // 字符串
            Regex("[\"'].*?[\"']") to SpanStyle(color = Color(0xFFCE9178)),
            // 数字
            config.numberPattern to SpanStyle(color = Color(0xFFB5CEA8)),
            // 关键字
            Regex("\\b(${config.keywords.joinToString("|")})\\b") to SpanStyle(color = Color(0xFF569CD6)),
            // 类型
            Regex("\\b(${config.types.joinToString("|")})\\b") to SpanStyle(color = Color(0xFF4EC9B0)),
            // 内置函数
            Regex("\\b(${config.builtins.joinToString("|")})\\b") to SpanStyle(color = Color(0xFFDCDCAA))
        )

        while (currentIndex < line.length) {
            var found = false

            for ((pattern, style) in patterns) {
                val match = pattern.find(line, currentIndex)
                if (match != null && match.range.first == currentIndex) {
                    tokens.add(match.range to style)
                    currentIndex = match.range.last + 1
                    found = true
                    break
                }
            }

            if (!found) {
                // 普通文本
                val nextChar = if (currentIndex < line.length) line[currentIndex] else ' '
                append(nextChar.toString())
                currentIndex++
            }
        }

        // 应用样式
        var pos = 0
        tokens.sortedBy { it.first.first }.forEach { (range, style) ->
            if (pos < range.first) {
                append(line.substring(pos, range.first))
            }
            withStyle(style) {
                append(line.substring(range.first, range.last + 1))
            }
            pos = range.last + 1
        }

        // 添加剩余文本
        if (pos < line.length) {
            append(line.substring(pos))
        }
    }

    private fun getLanguageConfig(language: ProgrammingLanguage): LanguageConfig {
        return when (language) {
            ProgrammingLanguage.KOTLIN -> kotlinConfig
            ProgrammingLanguage.JAVA -> javaConfig
            ProgrammingLanguage.JAVASCRIPT -> javascriptConfig
            ProgrammingLanguage.PYTHON -> pythonConfig
            ProgrammingLanguage.CPP -> cppConfig
            ProgrammingLanguage.C -> cConfig
            ProgrammingLanguage.XML -> xmlConfig
            ProgrammingLanguage.JSON -> jsonConfig
            ProgrammingLanguage.HTML -> htmlConfig
            ProgrammingLanguage.CSS -> cssConfig
            ProgrammingLanguage.SQL -> sqlConfig
        }
    }

    // 各种语言的配置
    private val kotlinConfig = LanguageConfig(
        keywords = setOf(
            "fun", "val", "var", "class", "interface", "object", "typealias",
            "if", "else", "when", "for", "while", "do", "try", "catch", "finally",
            "return", "throw", "break", "continue", "this", "super", "is", "in", "!in", "as", "!is",
            "package", "import", "get", "set", "by", "where", "init", "constructor",
            "abstract", "open", "final", "enum", "sealed", "annotation", "data",
            "override", "private", "protected", "public", "internal", "const",
            "operator", "infix", "inline", "noinline", "crossinline", "tailrec",
            "external", "suspend", "expect", "actual", "reified", "lateinit",
            "companion", "inner", "out", "in", "vararg", "dynamic"
        ),
        types = setOf(
            "String", "Int", "Long", "Double", "Float", "Boolean", "Char", "Byte", "Short",
            "List", "Set", "Map", "Array", "MutableList", "MutableSet", "MutableMap",
            "Any", "Unit", "Nothing", "null"
        ),
        builtins = setOf(
            "println", "print", "readLine", "main", "listOf", "setOf", "mapOf",
            "mutableListOf", "mutableSetOf", "mutableMapOf", "arrayOf",
            "with", "apply", "also", "let", "run", "repeat", "require", "check",
            "assert", "error", "TODO", "lazy", "use"
        )
    )

    private val javaConfig = LanguageConfig(
        keywords = setOf(
            "class", "interface", "enum", "public", "private", "protected", "static",
            "final", "abstract", "void", "int", "long", "double", "float", "boolean",
            "char", "byte", "short", "if", "else", "for", "while", "do", "switch",
            "case", "default", "break", "continue", "return", "try", "catch", "finally",
            "throw", "new", "this", "super", "extends", "implements", "import", "package",
            "native", "synchronized", "volatile", "transient", "instanceof", "assert"
        ),
        types = setOf("String", "Object", "Integer", "Long", "Double", "Float", "Boolean"),
        builtins = setOf("System", "out", "println", "main", "Math")
    )

    private val pythonConfig = LanguageConfig(
        keywords = setOf(
            "def", "class", "if", "elif", "else", "for", "while", "try", "except",
            "finally", "with", "as", "from", "import", "return", "yield", "pass",
            "break", "continue", "lambda", "in", "is", "and", "or", "not", "None",
            "True", "False", "async", "await", "global", "nonlocal", "del", "assert"
        ),
        builtins = setOf(
            "print", "len", "range", "list", "dict", "set", "str", "int", "float",
            "bool", "type", "isinstance", "enumerate", "zip", "map", "filter"
        ),
        singleLineComment = "#"
    )

    private val javascriptConfig = LanguageConfig(
        keywords = setOf(
            "function", "var", "let", "const", "if", "else", "for", "while", "do",
            "switch", "case", "default", "break", "continue", "return", "try", "catch",
            "finally", "throw", "new", "this", "class", "extends", "import", "export",
            "from", "as", "async", "await", "yield", "typeof", "instanceof", "in",
            "of", "delete", "void", "with", "debugger"
        ),
        builtins = setOf(
            "console", "log", "alert", "document", "window", "setTimeout", "setInterval",
            "fetch", "Promise", "Array", "Object", "String", "Number", "Boolean", "Date"
        )
    )

    private val cConfig = LanguageConfig(
        keywords = setOf(
            "if", "else", "for", "while", "do", "switch", "case", "default",
            "break", "continue", "return", "goto", "sizeof", "typedef",
            "struct", "union", "enum", "void", "char", "short", "int", "long",
            "float", "double", "signed", "unsigned", "const", "volatile",
            "static", "extern", "register", "auto", "inline", "restrict"
        )
    )

    private val cppConfig = LanguageConfig(
        keywords = setOf(
            "class", "struct", "namespace", "template", "typename", "using",
            "public", "private", "protected", "virtual", "override", "final",
            "const", "static", "extern", "mutable", "volatile", "register",
            "auto", "decltype", "sizeof", "alignof", "typeid", "dynamic_cast",
            "static_cast", "reinterpret_cast", "const_cast", "new", "delete",
            "this", "operator", "friend", "inline", "explicit", "noexcept"
        ) + cConfig.keywords
    )



    private val xmlConfig = LanguageConfig(
        keywords = emptySet(),
        singleLineComment = "<!--",
        multiLineCommentEnd = "-->"
    )

    private val jsonConfig = LanguageConfig(
        keywords = setOf("true", "false", "null")
    )

    private val htmlConfig = LanguageConfig(
        keywords = setOf(
            "html", "head", "body", "div", "span", "p", "a", "img", "script", "style",
            "title", "meta", "link", "br", "hr", "input", "button", "form", "table",
            "tr", "td", "th", "ul", "ol", "li", "h1", "h2", "h3", "h4", "h5", "h6"
        )
    )

    private val cssConfig = LanguageConfig(
        keywords = setOf(
            "margin", "padding", "border", "color", "background", "font", "display",
            "position", "width", "height", "top", "right", "bottom", "left", "flex",
            "grid", "animation", "transition", "transform", "media", "import"
        )
    )

    private val sqlConfig = LanguageConfig(
        keywords = setOf(
            "SELECT", "FROM", "WHERE", "INSERT", "UPDATE", "DELETE", "CREATE", "ALTER",
            "DROP", "TABLE", "VIEW", "INDEX", "JOIN", "LEFT", "RIGHT", "INNER", "OUTER",
            "GROUP BY", "ORDER BY", "HAVING", "UNION", "DISTINCT", "LIKE", "IN", "BETWEEN",
            "AND", "OR", "NOT", "NULL", "AS", "ON", "SET", "VALUES", "INTO"
        )
    )
}