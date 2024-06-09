package kr.entropi.arki.lang

import org.slf4j.LoggerFactory
import java.io.File
import java.util.Locale

object ArkiLang {
    private val logger = LoggerFactory.getLogger("ArkiLang")

    fun resolveFallback(locale: Locale, key: String): String? {
        val files = ArkiLangRegistry.files.sortedBy { it.priority }
        for (file in files) {
            val resolved = file.resolve(key) ?: continue
            return resolved
        }
        return null
    }

    fun resolve(locale: Locale, key: String): String? {
        val files = ArkiLangRegistry.filesByLocale(locale)
        for (file in files) {
            val resolved = file.resolve(key) ?: continue
            return resolved
        }
        return resolveFallback(locale, key)
    }

    fun formatted(locale: Locale, key: String, vararg args: Any): String? {
        val resolved = resolve(locale, key) ?: return null
        return ArkiLangRegistry.formatter.format(resolved, *args)
    }

    /**
     * @param baseClass Base class to load the file
     * @param path Path to the file
     * @param locale Locale of the file
     * @param loader Loader to use at loading. Default to Automatic
     *
     * @return Whether the load was success or not
     */
    fun loadIncluded(baseClass: Class<*>, path: String, locale: Locale, loader: ArkiLangLoader = ArkiLangLoader.Automatic): Boolean {
        if(baseClass.getResource(path) == null)
            return false

        val loaderImpl = ArkiLangLoader.getLoaderImpl(path, loader)
        if(loaderImpl == null) {
            logger.warn("Failed to load locale ${locale.displayName} from resource of base class ${baseClass.canonicalName}: Failed at resolving loader implementation")
            return false
        }

        val stream = baseClass.getResourceAsStream(path)
        if(stream == null) {
            logger.warn("Failed to load locale ${locale.displayName} from resource of base class ${baseClass.canonicalName}: Failed at resource")
            return false
        }

        val langFile = loaderImpl.loadFromStream(locale, stream)
        ArkiLangRegistry.addFilePreprocessing(langFile)

        return true
    }

    /**
     * @param baseClass Base class to load the file
     * @param path Function to resolve path for a locale
     * @param locales Locales to load. Default to the registry
     * @param loader Loader to use at loading. Default to Automatic
     *
     * @return Whether the load was success or not
     */
    fun loadIncluded(baseClass: Class<*>, path: (Locale) -> String, locales: List<Locale> = ArkiLangRegistry.locales, loader: ArkiLangLoader = ArkiLangLoader.Automatic): Int {
        return locales.count { locale ->
            val targetPath = path.invoke(locale)

            loadIncluded(baseClass, targetPath, locale, loader)
        }
    }

    /**
     * @param T Type of base class to load the file
     * @param path Path from the base class to resolve resource
     * @param locale Locale of the file
     * @param loader Loader to use at loading. Default to Automatic
     * @return Whether the load was success or not
     */
    inline fun <reified T> loadIncluded(path: String, locale: Locale, loader: ArkiLangLoader = ArkiLangLoader.Automatic): Boolean {
        return loadIncluded(T::class.java, path, locale, loader)
    }

    /**
     * @param T Type of base class to load the file
     * @param path Function to resolve path for a locale
     * @param locales Locales to load. Default to the registry
     * @param loader Loader to use at loading. Default to Automatic
     *
     * @return Whether the load was success or not
     */
    inline fun <reified T> loadIncluded(noinline path: (Locale) -> String, locales: List<Locale> = ArkiLangRegistry.locales, loader: ArkiLangLoader = ArkiLangLoader.Automatic): Int {
        return loadIncluded(T::class.java, path, locales, loader)
    }

    /**
     * @param file File to load
     * @param locale Locale of the file
     * @param loader Loader to use at loading. Default to Automatic
     * @return Whether the load was success or not
     */
    fun loadExternal(file: File, locale: Locale, loader: ArkiLangLoader = ArkiLangLoader.Automatic): Boolean {
        if(!file.exists()) return false

        val loaderImpl = ArkiLangLoader.getLoaderImpl(file, loader)
        if(loaderImpl == null) {
            logger.warn("Failed to load locale ${locale.displayName} from file \"${file.path}\": Failed at resolving loader implementation")
            return false
        }

        val stream = file.inputStream()

        val langFile = loaderImpl.loadFromStream(locale, stream)
        ArkiLangRegistry.addFilePreprocessing(langFile)

        return true
    }

    /**
     * @param path Path to the file
     * @param locale Locale of the file
     * @param loader Loader to use at loading. Default to Automatic
     * @return Whether the load was success or not
     */
    fun loadExternal(path: String, locale: Locale, loader: ArkiLangLoader = ArkiLangLoader.Automatic): Boolean {
        return loadExternal(File(path), locale, loader)
    }

    /**
     * @param path Function to resolve path for a locale
     * @param locales Locales to load. Default to the registry
     * @param loader Loader to use at loading. Default to Automatic
     * @return Count of success loads
     */
    fun loadExternal(path: (Locale) -> String, locales: List<Locale> = ArkiLangRegistry.locales, loader: ArkiLangLoader = ArkiLangLoader.Automatic): Int {
        return locales.count { locale ->
            val targetPath = path.invoke(locale)
            loadExternal(targetPath, locale, loader)
        }
    }
}