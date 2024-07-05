package kr.entropi.arki.lang

import java.util.Locale

data class ArkiLangFile(
    val inherit: ArkiLangFile? = null,
    val priority: Int = 0,
    val locale: Locale = Locale.getDefault(),
    val data: Map<String, String> = mapOf()
) {
    fun resolve(key: String): String? {
        return data[key] ?: inherit?.resolve(key)
    }

    companion object {
        val Empty = ArkiLangFile()
    }
}