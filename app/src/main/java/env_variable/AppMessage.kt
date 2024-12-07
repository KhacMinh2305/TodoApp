package env_variable

class AppMessage {
    companion object {
        const val NOT_ALLOW_DELETE_TASK = "Not allow to delete finished or expired task."
        const val INVALID_DATE = "Invalid date."
        const val INVALID_TIME = "Invalid time."
        const val EMPTY_INPUT = "Fields are empty."
        const val USER_NOT_FOUND = "User not found."
        const val EMPTY_AUTHENTICATION_FIELD = "Username or password is empty."
        const val USER_EXISTED = "This user existed."
        const val UNDEFINED_ERROR = "Something went wrong. Try again."
        const val RESULT_ADD_TASK_SUCCESS = "Adding task successfully."
        const val RESULT_ADD_TASK_FAILED = "Adding task failed."
    }
}