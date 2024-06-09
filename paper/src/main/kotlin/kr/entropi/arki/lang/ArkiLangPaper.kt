package kr.entropi.arki.lang

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import java.util.*

object ArkiLangPaper {
    var globalDefault = Locale.getDefault()
    var consoleLocale = Locale.getDefault()
}

fun Audience.resolveLocale(): Locale {
    return when(this) {
        is Player -> this.locale()
        is ConsoleCommandSender -> ArkiLangPaper.consoleLocale
        else -> ArkiLangPaper.globalDefault
    }
}

fun Audience.translate(key: String, vararg args: Any): String {
    val locale = resolveLocale()
    val formatted = ArkiLang.formatted(locale, key, *args) ?: "$${key}:${args.joinToString(",")}$"
    return formatted
}

fun Audience.sendTranslated(key: String, vararg args: Any) {
    this.sendMessage(Component.text(translate(key, *args)))
}

fun Audience.sendRichTranslated(key: String, vararg args: Any) {
    this as CommandSender

    this.sendRichMessage(translate(key, *args))
}

fun Audience.sendActionBarTranslated(key: String, vararg args: Any) {
    this.sendActionBar(Component.text(translate(key, *args)))
}

fun Audience.sendRichActionBarTranslated(key: String, vararg args: Any) {
    this as CommandSender

    this.sendActionBar(MiniMessage.miniMessage().deserialize(translate(key, *args)))
}