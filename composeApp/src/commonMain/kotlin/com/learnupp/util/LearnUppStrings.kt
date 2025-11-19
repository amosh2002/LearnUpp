package com.learnupp.util

import org.koin.mp.KoinPlatform.getKoin

enum class LearnUppStrings(
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
    SETTINGS_TITLE("Settings", "Կարգավորումներ", "Настройки"),
    NOTIFICATIONS("Notifications", "Ծանուցումներ", "Уведомления"),
    PRIVACY("Privacy", "Գաղտնիություն", "Конфиденциальность"),
    LANGUAGE("Language", "Լեզու", "Язык"),
    DOWNLOAD_SETTINGS(
        "Download Settings",
        "Ներբեռնումների կարգավորումներ",
        "Настройки загрузок"
    ),
    HELP_SUPPORT("Help & Support", "Օգնություն և աջակցություն", "Помощь и поддержка"),
    VIDEOS_LABEL("Videos", "Տեսանյութեր", "Видео"),
    SHORTS_LABEL("Shorts", "Կարճ տեսանյութեր", "Шорты"),
    COURSES_LABEL("Courses", "Դասընթացներ", "Курсы"),
    STUDENTS_LABEL("Students", "Ուսանողներ", "Студенты"),
    FOLLOWERS_LABEL("Followers", "Հետևորդներ", "Подписчики"),
    FOLLOWING_LABEL("Following", "Բաժանորդագրություններ", "Подписки"),
    RATING_LABEL("Rating", "Վարկանիշ", "Рейтинг"),
    POSTS_LABEL("Posts", "Հրապարակումներ", "Посты"),
    ABOUT_ME("About me", "Իմ մասին", "Обо мне"),
    EDIT_ABOUT("Edit About", "Խմբագրել «Իմ մասին»", "Изменить «Обо мне»"),
    SAVE("Save", "Պահպանել", "Сохранить"),
    COURSES_LOCKED_MESSAGE(
        "Courses are available for Lecturers. Upgrade to start publishing.",
        "Դասընթացները հասանելի են միայն դասախոսներին։ Թարմացրեք կարգավիճակը՝ հրապարակելու համար։",
        "Курсы доступны только преподавателям. Повышайте статус, чтобы публиковать."
    ),
    PROFILE_AVATAR("Profile avatar", "Պրոֆիլի նկար", "Аватар профиля"),
    MESSAGES_TITLE("Messages", "Հաղորդագրություններ", "Сообщения"),
    SEARCH_CHATS_PLACEHOLDER("Search chats...", "Որոնել զրույցներ...", "Поиск чатов..."),
    TAB_ALL("All", "Բոլորը", "Все"),
    TAB_DIRECT("Direct", "Անձնական", "Личные"),
    TAB_GROUPS("Groups", "Խմբեր", "Группы"),
    MEMBERS_LABEL("members", "մասնակից", "участников");
}

fun LearnUppStrings.getValue(): String {
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
