package com.handcontrol.api

import java.util.*

enum class Command(val russianCommand: String, val englishCommand: String) {
    CLENCH("сжать", "clench"),
    UNCLENCH("разжать", "unclench"), // TODO заменить разжать на более простое слово
    OKAY("окей", "okay"),
    CLASS("класс", "class"),
    KEYBOARD("клавиатура", "keyboard"),
    HI("привет", "hi"),
    GOAT("коза", "goat");

    companion object {
        fun getRussianCommandIgnoreCase(command: String): Command? {
            val locale = Locale("ru", "RU")
            return values().find {
                it.russianCommand.toLowerCase(locale) == command.toLowerCase(
                    locale
                )
            }
        }

        fun getEnglishCommandIgnoreCase(command: String): Command? {
            val locale = Locale.US
            return values().find {
                it.englishCommand.toLowerCase(locale) == command.toLowerCase(
                    locale
                )
            }
        }
    }
}