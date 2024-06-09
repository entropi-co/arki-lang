package kr.entropi.arki.lang

import kr.entropi.arki.lang.preprocessor.ArkiLangPlaceholderPreprocessor
import java.util.Locale

object ArkiLangRegistry {
    var localeDefault = Locale.getDefault()
    val locales = mutableListOf<Locale>()
    private val _files = mutableListOf<ArkiLangFile>()

    var formatter: ArkiLangFormatter = ArkiLangFormatter.Default
    var preprocessor: ArkiLangPreprocessor = ArkiLangPlaceholderPreprocessor()

    val files
        get() = _files.toList()

    fun addFilePreprocessing(file: ArkiLangFile, adjustPriority: Boolean = true) {
        val preprocessed = preprocessor.preprocess(file)
        addFile(file, adjustPriority)
    }

    fun addFile(file: ArkiLangFile, adjustPriority: Boolean = true) {
        val existingFile = fileByLocaleOrNull(file.locale)
        _files += if(existingFile != null && adjustPriority) {
            file.copy(priority = existingFile.priority - 1)
        } else {
            file
        }
    }

    fun removeFile(file: ArkiLangFile): Boolean {
        return _files.remove(file)
    }

    fun removeFile(locale: Locale): Boolean {
        return _files.removeIf { it.locale == locale }
    }

    fun clearFiles() {
        _files.clear()
    }

    fun fileByLocaleOrNull(locale: Locale): ArkiLangFile? {
        return _files
            .sortedBy { it.priority }
            .firstOrNull { it.locale == locale }
    }

    fun fileByLocaleOrFallback(locale: Locale): ArkiLangFile {
        return fileByLocaleOrNull(locale)
            ?: fileByLocaleOrNull(localeDefault)
            ?: _files.first()
    }

    fun filesByLocale(locale: Locale): List<ArkiLangFile> {
        return _files
            .filter { it.locale == locale }
            .sortedBy { it.priority }
    }
}