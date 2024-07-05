import kr.entropi.arki.lang.ArkiLangFile
import kr.entropi.arki.lang.preprocessor.ArkiLangPlaceholderPreprocessor
import kotlin.test.Test

class TestPlaceholderPreprocessor {
    val mockData = mutableMapOf(
        "s.prefix" to "PREFIX",
        "s.test" to "\${s.prefix} test"
    )

    @Test
    fun testOne() {
        val mockFile = ArkiLangFile(
            data = mockData
        )

        val processed = ArkiLangPlaceholderPreprocessor().preprocess(mockFile)
        println(processed)
    }

    @Test
    fun testAlternativeImplementation() {
        val placeholderPattern = Regex("""(?<!\\)\$\{([_.a-zA-Z0-9]+)}""")
        val resolvedMap = mockData.toMutableMap()
        resolvedMap.forEach { (k, v) ->
            println(v)
            placeholderPattern.findAll(v).forEach {
                println(it)
            }
        }
    }
}
