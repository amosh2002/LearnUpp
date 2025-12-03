package com.learnupp.ui.settings.language

import cafe.adriel.voyager.core.model.screenModelScope
import com.learnupp.domain.usecase.language.GetLanguagesUseCase
import com.learnupp.domain.usecase.language.ReloadLanguagesUseCase
import com.learnupp.domain.usecase.language.SelectLanguageUseCase
import com.learnupp.ui.base.BaseScreenModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LanguageSelectionScreenModel(
    private val getLanguages: GetLanguagesUseCase,
    private val reloadLanguages: ReloadLanguagesUseCase,
    private val selectLanguage: SelectLanguageUseCase
) : BaseScreenModel() {

    val languages = getLanguages()
        .stateIn(screenModelScope, SharingStarted.Eagerly, emptyList())

    init {
        screenModelScope.launch { reloadLanguages() }
    }

    fun choose(code: String) {
        screenModelScope.launch {
            selectLanguage(code)
        }
    }
}

