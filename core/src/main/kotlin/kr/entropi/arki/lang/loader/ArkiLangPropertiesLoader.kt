package kr.entropi.arki.lang.loader

import kr.entropi.arki.lang.ArkiLangFile
import kr.entropi.arki.lang.ArkiLangLoader
import java.io.File
import java.io.InputStream
import java.util.*
import kotlin.io.path.Path
import kotlin.io.path.extension

object ArkiLangPropertiesLoader: ArkiLangLoader {
    override fun isExtension(path: String): Boolean {
        return Path(path).extension == "properties"
    }

    override fun isLoadable(file: File): Boolean {
        return isExtension(file.path)
    }

    override fun loadFromStream(locale: Locale, stream: InputStream): ArkiLangFile {
        val properties = Properties()
        properties.load(stream.reader(Charsets.UTF_8))

        val stringMap = properties.toMap()
            .mapKeys { it.key.toString() }
            .mapValues { it.value.toString() }

        return ArkiLangFile(
            inherit = null,
            locale = locale,
            data = stringMap.toMutableMap()
        )
    }
}