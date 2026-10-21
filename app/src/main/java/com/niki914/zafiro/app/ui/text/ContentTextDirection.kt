package com.niki914.zafiro.app.ui.text

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection

private val FencedCodeBlock = Regex("(?s)```.*?```")
private val InlineCodeSpan = Regex("`[^`]*`")

internal fun resolveContentTextDirection(
    text: String,
    ignoreCode: Boolean = false,
): TextDirection {
    val content = if (ignoreCode) {
        InlineCodeSpan.replace(FencedCodeBlock.replace(text, " "), " ")
    } else {
        text
    }

    var arabic = 0
    var latin = 0
    for (character in content) {
        when {
            character in '\u0600'..'\u06FF' ||
                character in '\u0750'..'\u077F' ||
                character in '\u08A0'..'\u08FF' ||
                character in '\uFB50'..'\uFDFF' ||
                character in '\uFE70'..'\uFEFF' -> arabic++

            character.isLetter() -> latin++
        }
    }

    return if (arabic > 0 && arabic * 3 >= latin) {
        TextDirection.Rtl
    } else {
        TextDirection.Ltr
    }
}

internal fun resolveContentTextAlign(direction: TextDirection): TextAlign {
    return if (direction == TextDirection.Rtl) TextAlign.Right else TextAlign.Left
}
