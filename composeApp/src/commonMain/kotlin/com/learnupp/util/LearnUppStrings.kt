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
    SIGN_IN_TITLE("Sign In", "Մուտք գործել", "Войти"),
    SIGN_IN_SUBTITLE(
        "Access your account to continue.",
        "Մուտք գործեք ձեր հաշիվ՝ շարունակելու համար։",
        "Войдите в свой аккаунт, чтобы продолжить."
    ),
    EMAIL_LABEL("Email", "Էլ. հասցե", "Эл. почта"),
    EMAIL_PLACEHOLDER("you@example.com", "you@example.com", "you@example.com"),
    PASSWORD_LABEL("Password", "Գաղտնաբառ", "Пароль"),
    FORGOT_PASSWORD("Forgot Password?", "Մոռացե՞լ եք գաղտնաբառը?", "Забыли пароль?"),
    SIGN_IN_BUTTON("Sign In", "Մուտք գործել", "Войти"),
    OR_CONTINUE_WITH("or continue with", "կամ շարունակեք", "или продолжите через"),
    GOOGLE_LABEL("Google", "Google", "Google"),
    APPLE_LABEL("Apple", "Apple", "Apple"),
    DONT_HAVE_ACCOUNT("Don't have an account?", "Չունե՞ք հաշիվ?", "Нет аккаунта?"),
    SIGN_UP_CTA("Sign Up", "Գրանցվել", "Зарегистрироваться"),
    SHOW_PASSWORD("Show password", "Ցույց տալ գաղտնաբառը", "Показать пароль"),
    HIDE_PASSWORD("Hide password", "Թաքցնել գաղտնաբառը", "Скрыть пароль"),
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
    EARNINGS("Earnings", "Եկամուտներ", "Заработок"),
    PAYMENT_METHODS("Payment Methods", "Վճարման եղանակներ", "Методы оплаты"),
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
    MEMBERS_LABEL("members", "մասնակից", "участников"),
    SEARCH_COURSES_PLACEHOLDER(
        "Search courses or creators",
        "Փնտրեք դասընթացներ կամ հեղինակների",
        "Ищите курсы или авторов"
    ),
    SEE_ALL("See all...", "Տեսնել բոլորը...", "Смотреть все..."),
    ENROLL_NOW("Enroll Now", "Գրանցվել հիմա", "Записаться"),
    LECTURES_LABEL("Lectures", "Դասախոսություններ", "Лекций"),
    FITNESS_LABEL("Fitness", "Ֆիթնես", "Фитнес"),
    DESIGN_LABEL("Design", "Դիզայն", "Дизайн"),
    MARKETING_LABEL("Marketing", "Մարքեթինգ", "Маркетинг"),

    // Screen titles
    SCREEN_VIDEOS("Videos", "Տեսանյութեր", "Видео"),
    SCREEN_REELS("Shorts", "Կարճ տեսանյութեր", "Шорты"),
    SCREEN_COURSES("Courses", "Դասընթացներ", "Курсы"),
    SCREEN_MESSAGES("Messages", "Հաղորդագրություններ", "Сообщения"),
    SCREEN_MORE("More", "Ավելին", "Еще"),
    SCREEN_EDIT_PROFILE("Edit Profile", "Խմբագրել պրոֆիլը", "Редактировать профиль"),
    SCREEN_NOTIFICATIONS("Notifications", "Ծանուցումներ", "Уведомления"),
    SCREEN_EARNINGS("Earnings", "Եկամուտներ", "Заработок"),
    SCREEN_PAYMENTS("Payment Methods", "Վճարման եղանակներ", "Методы оплаты"),
    SCREEN_LANGUAGE("Language", "Լեզու", "Язык"),
    SCREEN_HELP("Help & Support", "Օգնություն և աջակցություն", "Помощь и поддержка"),
    SCREEN_WITHDRAW_FUNDS("Withdraw Funds", "Հանել միջոցներ", "Снять средства"),
    SCREEN_WITHDRAW_METHODS("Select Payout Method", "Ընտրեք վճարման եղանակ", "Выберите способ выплаты"),
    SCREEN_WITHDRAW_SUCCESS("Withdrawal Successful", "Հանելն ավարտված է", "Успешный вывод"),
    SCREEN_AUTH_START("Sign In / Sign Up", "Մուտք / Գրանցում", "Вход / Регистрация"),
    SCREEN_VERIFY_OTP("Verification Code", "Վավերացման կոդ", "Код подтверждения"),
    SCREEN_PROFILE_SETUP("Complete Profile", "Լրացրեք պրոֆիլը", "Заполните профиль"),
    // Legacy screen titles kept for older flows
    SCREEN_LOGIN("Login", "Մուտք", "Логин"),
    SCREEN_SIGN_UP("Create Account", "Գրանցել հաշիվ", "Создать аккаунт"),

    // Auth start
    AUTH_WELCOME_TITLE("Welcome to LearnUpp", "Բարի գալուստ LearnUpp", "Добро пожаловать в LearnUpp"),
    AUTH_WELCOME_SUBTITLE(
        "Sign in with email, Google, or Apple to continue.",
        "Մուտք գործեք email-ով, Google-ով կամ Apple-ով՝ շարունակելու համար։",
        "Войдите с email, Google или Apple, чтобы продолжить."
    ),
    AUTH_CONTINUE_WITH_EMAIL("Continue with Email", "Շարունակել էլփոստով", "Продолжить с email"),
    AUTH_SEND_CODE("Send Code", "Ուղարկել կոդը", "Отправить код"),
    AUTH_SOCIAL_COMING_SOON(
        "Google/Apple sign-in is coming soon.",
        "Google/Apple մուտքը շուտով կլինի։",
        "Вход через Google/Apple скоро будет."
    ),

    // OTP
    OTP_TITLE("Verification Code", "Վավերացման կոդ", "Код подтверждения"),
    OTP_SUBTITLE(
        "Enter the 6-digit code we sent to your email.",
        "Մուտքագրեք ձեր էլ. հասցեին ուղարկված 6-նիշ կոդը։",
        "Введите 6-значный код, отправленный на вашу почту."
    ),
    OTP_DIDNT_RECEIVE("Didn't receive the code?", "Չե՞ք ստացել կոդը։", "Не получили код?"),
    OTP_RESEND("Resend Code", "Կրկին ուղարկել կոդը", "Отправить код еще раз"),
    OTP_VERIFY_BUTTON("Verify Code", "Ստուգել կոդը", "Подтвердить код"),
    OTP_GO_BACK("Go Back to Sign In", "Վերադառնալ մուտք", "Вернуться ко входу"),

    // Profile completion
    PROFILE_SETUP_TITLE("Complete your profile", "Լրացրեք ձեր պրոֆիլը", "Заполните профиль"),
    PROFILE_SETUP_SUBTITLE(
        "Choose a username so others can find you.",
        "Ընտրեք օգտանուն, որպեսզի մյուսները գտնեն ձեզ։",
        "Выберите имя пользователя, чтобы вас могли найти."
    ),
    USERNAME_LABEL("Username", "Օգտանուն", "Имя пользователя"),
    USERNAME_PLACEHOLDER("@username", "@օգտանուն", "@username"),
    USERNAME_TAKEN("That username is already taken", "Այդ օգտանունը զբաղված է", "Это имя уже занято"),
    USERNAME_AVAILABLE("Username is available", "Օգտանունը ազատ է", "Имя доступно"),
    USERNAME_CHECKING("Checking availability...", "Ստուգում է հասանելիությունը...", "Проверяем доступность..."),
    FULL_NAME_OPTIONAL("Full name (optional)", "Անուն Ազգանուն (ըստ ցանկության)", "Полное имя (необязательно)"),
    COMPLETE_PROFILE_BUTTON("Finish", "Ավարտել", "Готово"),
    EDIT_PROFILE_TITLE("Edit Profile", "Խմբագրել պրոֆիլը", "Редактировать профиль"),
    CHANGE_PHOTO("Change Photo", "Փոխել լուսանկարը", "Изменить фото"),
    PROFILE_FULL_NAME_LABEL("Full Name", "Անուն Ազգանուն", "Полное имя"),
    PROFILE_EMAIL_LABEL("Email Address", "Էլ. հասցե", "Эл. адрес"),
    PROFILE_BIO_LABEL("Bio", "Կենսագրություն", "Биография"),
    PROFILE_BIO_PLACEHOLDER(
        "Share a short intro about yourself.",
        "Պատմեք կարճ ձեր մասին։",
        "Расскажите кратко о себе."
    ),
    SAVE_CHANGES("Save Changes", "Պահպանել փոփոխությունները", "Сохранить изменения"),
    PROFILE_UPDATE_FAILED(
        "Failed to update profile. Please try again.",
        "Չհաջողվեց թարմացնել պրոֆիլը։ Կրկին փորձեք։",
        "Не удалось обновить профиль. Повторите попытку."
    ),
    PHOTO_CHANGE_COMING_SOON(
        "Photo updates are coming soon.",
        "Լուսանկարի թարմացումները շուտով կլինեն։",
        "Обновление фото будет доступно позже."
    ),

    // Account / login methods
    ACCOUNT_AND_LOGIN("Account & Login", "Հաշիվ և մուտք", "Аккаунт и вход"),
    CHANGE_EMAIL("Change Email", "Փոխել էլ. հասցեն", "Сменить email"),
    LINK_GOOGLE("Link Google", "Կապել Google-ը", "Привязать Google"),
    LINK_APPLE("Link Apple", "Կապել Apple-ը", "Привязать Apple"),
    SET_AS_PRIMARY("Set as primary", "Դարձնել հիմնական", "Сделать основным"),
    COMING_SOON("Coming soon", "Շուտով", "Скоро"),
    EMAIL_UPDATE_OTP_SENT("We’ll send an OTP to your new email.", "OTP կուղարկենք ձեր նոր էլփոստին։", "Мы отправим OTP на ваш новый email."),
    ONLY_ONE_LOGIN_ACTIVE("Only one login method can be active at a time.", "Միայն մեկ մուտքի մեթոդ կարող է լինել ակտիվ։", "Только один способ входа может быть активным."),

    // Videos / course previews
    COURSE_PREVIEW_LABEL("Course Preview", "Դասընթացի նախադիտում", "Превью курса"),
    VIEW_FULL_COURSE("View Full Course", "Դիտել ամբողջական դասընթացը", "Смотреть полный курс"),

    // Legacy (kept for reference in older screens)
    REGISTER_TITLE("Create Account", "Գրանցել հաշիվ", "Создать аккаунт"),
    REGISTER_FULL_NAME("Full Name", "Անուն Ազգանուն", "Полное имя"),
    REGISTER_FULL_NAME_PLACEHOLDER("Enter your full name", "Մուտքագրեք ձեր անունը", "Введите ваше имя"),
    REGISTER_EMAIL_ADDRESS("Email Address", "Էլ. հասցե", "Эл. адрес"),
    REGISTER_EMAIL_PLACEHOLDER("Enter your email", "Մուտքագրեք ձեր էլ. հասցեն", "Введите ваш email"),
    REGISTER_PASSWORD_PLACEHOLDER("Create a password", "Ստեղծեք գաղտնաբառ", "Придумайте пароль"),
    REGISTER_CONFIRM_PASSWORD("Confirm Password", "Հաստատել գաղտնաբառը", "Подтвердите пароль"),
    REGISTER_CONFIRM_PASSWORD_PLACEHOLDER("Re-enter your password", "Կրկին մուտքագրեք գաղտնաբառը", "Введите пароль еще раз"),
    REGISTER_PASSWORD_HINT("Must be at least 8 characters", "Պետք է լինի առնվազն 8 նիշ", "Должен быть не менее 8 символов"),
    REGISTER_TERMS_TEXT(
        "I agree to the Terms of Service and Privacy Policy",
        "Համաձայն եմ ծառայության պայմաններին և գաղտնիության քաղաքականությանը",
        "Я соглашаюсь с Условиями использования и Политикой конфиденциальности"
    ),
    REGISTER_BUTTON("Create Account", "Գրանցել հաշիվ", "Создать аккаунт"),
    REGISTER_HAVE_ACCOUNT("Already have an account?", "Արդեն ունե՞ք հաշիվ:", "Уже есть аккаунт?");
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
