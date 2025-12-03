package com.learnupp.data.settings.language

import com.learnupp.domain.model.settings.LanguageOption

class MockLanguageOptionsApi : LanguageOptionsApi {
    override suspend fun fetchLanguages(): List<LanguageOption> = listOf(
        LanguageOption("en", "English", "\uD83C\uDDEC\uD83C\uDDE7", selected = true),
        LanguageOption("ru", "Русский", "\uD83C\uDDF7\uD83C\uDDFA"),
        LanguageOption("hy", "Հայերեն", "\uD83C\uDDED\uD83C\uDDF2")
    )
}

