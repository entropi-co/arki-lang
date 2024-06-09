package kr.entropi.arki.lang.preprocessor

import kr.entropi.arki.lang.ArkiLangFile
import kr.entropi.arki.lang.ArkiLangPreprocessor

class ArkiLangPlaceholderPreprocessor(
    override var next: ArkiLangPreprocessor? = null
) : ArkiLangPreprocessor() {
    companion object {
        val placeholderPattern = Regex("""(?<!\\)\$\{(\w+)}""")
        val escapePattern = Regex("""\\\$\{(\w+)}""")
    }

    override fun preprocess(file: ArkiLangFile): ArkiLangFile {
        val originMap = file.data
        val processingMap = originMap.toMutableMap()

        for ((key, value) in processingMap) {
            var newValue = value

            // Replace placeholders with their corresponding values
            placeholderPattern.findAll(value).forEach { matchResult ->
                val referenceName = matchResult.groupValues[1]
                if (originMap.containsKey(referenceName)) {
                    val referenceValue = originMap[referenceName]
                    newValue = newValue.replace(matchResult.value, referenceValue ?: "")
                }
            }

            // Unescape the escaped placeholders without replacing them
            newValue = escapePattern.replace(newValue) { matchResult ->
                matchResult.value.drop(1) // Remove only the escape character
            }

            processingMap[key] = newValue
        }

        val processedFile = file.copy(
            data = processingMap
        )

        return super.preprocess(processedFile)
    }
}