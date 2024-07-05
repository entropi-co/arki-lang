package kr.entropi.arki.lang.preprocessor

import kr.entropi.arki.lang.ArkiLangFile
import kr.entropi.arki.lang.ArkiLangPreprocessor
import org.slf4j.LoggerFactory

class ArkiLangPlaceholderPreprocessor : ArkiLangPreprocessor() {
    private val logger = LoggerFactory.getLogger(ArkiLangPlaceholderPreprocessor::class.java)

    override var next: ArkiLangPreprocessor? = null

    companion object {
        val placeholderPattern = Regex("""(?<!\\)\$\{([_.a-zA-Z0-9]+)}""")
        val escapePattern = Regex("""\\\$\{(\w+)}""")

        const val MAX_MATCH_ITERATIONS = 500
    }

    override fun preprocess(file: ArkiLangFile): ArkiLangFile {
        val originMap = file.data
        val processingMap = originMap.toMutableMap()

        for ((key, value) in processingMap) {
            var newValue = value

            // Replace placeholders with their corresponding values
            var iter = 0
            while(true) {
                if(iter > MAX_MATCH_ITERATIONS) {
                    logger.warn("Match iteration exceed the limit ${MAX_MATCH_ITERATIONS}, causes performance issues. The language file may be wrong")
                    break
                }
                iter++

                val matchResults = placeholderPattern.findAll(newValue)
                if(!matchResults.iterator().hasNext()) break
                matchResults.forEach { matchResult ->
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
        }

        val processedFile = file.copy(
            data = processingMap
        )

        return super.preprocess(processedFile)
    }
}