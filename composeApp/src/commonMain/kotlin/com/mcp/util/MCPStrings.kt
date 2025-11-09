package com.mcp.util

import org.koin.mp.KoinPlatform.getKoin

enum class MCPStrings(
    val valueEN: String,
    val valueAM: String,
    val valueRU: String
) {

    OK("OK", "Լավ", "Хорошо"),
    CANCEL("Cancel", "Չեղարկել", "Отмена"),
    GO_BACK("Go Back", "Վերադառնալ", "Назад"),
    CONFIRM("Confirm", "Հաստատել", "Подтвердить"),
    LEAVE("Leave", "Դուրս գալ", "Выйти"),
    LEAVE_TITLE(
        "Are you sure you want to leave?",
        "Վստա՞հ եք, որ ցանկանում եք դուրս գալ",
        "Вы уверены, что хотите выйти?"
    ),
    LEAVE_TEXT(
        "All changes will be lost.",
        "Բոլոր փոփոխությունները կկորչեն։",
        "Все изменения будут потеряны."
    ),
    TRY_AGAIN("Try Again", "Փորձել կրկին", "Попробовать снова"),
    SOMETHING_WENT_WRONG(
        "Something Went Wrong...",
        "Անսպասելի սխալ...",
        "Что-то пошло не так..."
    ),
    CHECK_YOUR_CONNECTION(
        "Please check your internet connection and try again.",
        "Խնդրում ենք ստուգել ինտերնետ կապը և կրկին փորձել:",
        "Пожалуйста, проверьте подключение к интернету и повторите попытку."
    ),
    FAILED_TO_AUTHENTICATE(
        "Failed to authenticate",
        "Չհաջողվեց նունայնացնել",
        "Не удалось аутентифицировать"
    ),
    PLEASE_LOG_IN_AGAIN(
        "Please log in again",
        "Խնդրում ենք կրկին մուտք գործել",
        "Пожалуйста, войдите снова"
    ),
    FAILED_TO_LOG_IN(
        "Failed to log in",
        "Չհաջողվեց մուտք գործել",
        "Не удалось войти"
    ),
    LOGIN_ERROR_INVALID_CREDENTIALS(
        "Invalid username or password.",
        "Սխալ օգտանուն կամ գաղտնաբառ։",
        "Неверное имя пользователя или пароль."
    ),
    LOG_OUT_TITLE(
        "Are you sure you want to log out?",
        "Վստա՞հ եք, որ ցանկանում եք դուրս գալ։",
        "Вы уверены, что хотите выйти?"
    ),
    LOG_OUT("Log Out", "Դուրս գալ", "Выйти"),
    ERROR("Error!", "Սխալ", "Ошибка!"),
    FAILED_TO_LOG_OUT(
        "Failed to log out",
        "Չհաջողվեց դուրս գալ",
        "Не удалось выйти из системы"
    ),
}

fun MCPStrings.getValue(): String {
    val localizationService: LocalizationService = getKoin().get()
    val currentLanguage = localizationService.getCurrentLanguage()

    return when (currentLanguage) {
        LanguageEnum.English.iso -> this.valueEN
        LanguageEnum.Armenian.iso -> this.valueAM
        LanguageEnum.Russian.iso -> this.valueRU
        else -> {
            this.valueEN
        }
    }
}
