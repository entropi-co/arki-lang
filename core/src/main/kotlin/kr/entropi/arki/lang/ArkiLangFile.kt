package kr.entropi.arki.lang

open class ArkiLangFile(
    val inherit: ArkiLangFile? = null,
        val values: MutableMap<String, String> = mutableMapOf()
) {
    fun resolve(key: String): String? {
        return values[key] ?: inherit?.resolve(key)
    }

    object Empty: ArkiLangFile()
}