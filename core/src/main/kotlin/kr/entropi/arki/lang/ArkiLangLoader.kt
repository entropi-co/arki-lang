package kr.entropi.arki.lang

import org.slf4j.LoggerFactory
import java.io.File
import java.io.InputStream
import java.util.*

interface ArkiLangLoader {
    fun isExtension(path: String): Boolean
    fun isLoadable(file: File): Boolean
    fun loadFromStream(locale: Locale, stream: InputStream): ArkiLangFile

    companion object {
        fun getLoaderImpl(path: String, loader: ArkiLangLoader): ArkiLangLoader? {
            return if(loader == Automatic) ArkiLangLoaderRegistry.findLoaderByPath(path)
            else loader
        }

        fun getLoaderImpl(file: File, loader: ArkiLangLoader): ArkiLangLoader? {
            return if(loader == Automatic) ArkiLangLoaderRegistry.findLoaderForFile(file)
            else loader
        }
    }

    object Automatic: ArkiLangLoader {
        private val logger = LoggerFactory.getLogger("ArkiLangLoader.Automatic")

        private fun warnMistakenMethodCalls(method: String, cause: String) {
            logger.warn("$method was called which should not be called on Automatic loader")
            logger.warn("$cause, contact the developer")
        }

        override fun isExtension(path: String): Boolean {
            warnMistakenMethodCalls("isExtension(path)", "This will always return true")

            return true
        }

        override fun isLoadable(file: File): Boolean {
            warnMistakenMethodCalls("isLoadable(path)", "This will always return true")

            return true
        }

        override fun loadFromStream(locale: Locale, stream: InputStream): ArkiLangFile {
            warnMistakenMethodCalls("loadFromStream(path)", "This will always load an empty lang file")

            return ArkiLangFile.Empty
        }
    }
}