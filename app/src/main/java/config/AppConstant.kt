package config

sealed class AppConstant {
    companion object {
        const val TASK_ID_TAG = "task_id"
        const val TASK_STATE_NOT_FINISHED = 0
        const val TASK_STATE_FINISHED = 1
        const val TASK_STATE_EXPIRED = 2
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
        const val PRIORITY_HIGH = 1
        const val PRIORITY_MEDIUM = 2
        const val PRIORITY_LOW = 3
        const val PATTERN_DATE = "dd/MM/yyyy"
        const val PATTERN_TIME = "HH:mm"
        const val MILLISECOND_IN_DAY = 86400000L
    }
}