package kr.entropi.arki.lang

abstract class ArkiLangPreprocessor {
    open var next: ArkiLangPreprocessor? = null

    open fun preprocess(file: ArkiLangFile): ArkiLangFile {
        return this.next?.preprocess(file) ?: file
    }

    infix fun withNext(next: ArkiLangPreprocessor?) {
        this.next = next
    }
}