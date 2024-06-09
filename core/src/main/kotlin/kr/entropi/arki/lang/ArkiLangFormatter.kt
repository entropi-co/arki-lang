package kr.entropi.arki.lang

import java.text.MessageFormat

interface ArkiLangFormatter {
    fun format(message: String, vararg args: Any): String

    object Default: ArkiLangFormatter {
        override fun format(message: String, vararg args: Any): String {
            return MessageFormat.format(message, *args)
        }
    }
}