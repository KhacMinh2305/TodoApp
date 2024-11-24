package data.result


sealed class Result {

    enum class ResultState {SUCCESS, FAILURE}

    data object Success : Result()
    data class Failure(val cause : String) : Result()
    data class Error(val message : String?) : Result()
}