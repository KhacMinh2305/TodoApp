package config

sealed class AppConstant {
    companion object {
        const val LOCAL_FILE_NAME = "local_file"
        const val USER_ID = "id"
        const val HASH_ALGORITHM = "MD5"
        const val DATABASE_NAME = "todo_database"
        const val UI_MODE = "UiMode"
        const val MODE_DARK = 1
        const val MODE_LIGHT = 0
        const val LANG_MODE = "langMode"
        const val LANG_EN = "en"
        const val LANG_VI = "vi"
        const val LANG_DEFAULT = "default"
    }
}