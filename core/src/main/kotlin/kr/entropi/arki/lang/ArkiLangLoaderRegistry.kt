package kr.entropi.arki.lang

import kr.entropi.arki.lang.loader.ArkiLangPropertiesLoader
import java.io.File

object ArkiLangLoaderRegistry: List<ArkiLangLoader> by mutableListOf(
    ArkiLangPropertiesLoader
) {
    fun findLoaderByPath(path: String): ArkiLangLoader? {
        return find { it.isExtension(path) }
    }

    fun findLoaderForFile(file: File): ArkiLangLoader? {
        return find { it.isLoadable(file) }
    }
}