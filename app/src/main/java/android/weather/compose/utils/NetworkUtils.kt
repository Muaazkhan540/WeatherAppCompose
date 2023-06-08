package android.weather.compose.utils

import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

val handler = CoroutineExceptionHandler { _, throwable ->
    Log.d("TAG", "CoroutineExceptionHandler: ${throwable.message}")
}
val scope = CoroutineScope(handler + SupervisorJob())

sealed class ResponseResult<out H> {
    class Success<out T>(val data: T) : ResponseResult<T>()
    class Failure(val error: Throwable) : ResponseResult<Nothing>()
    class Loading<T> : ResponseResult<T>()
}

suspend inline fun <T> safeApiCall(
    crossinline body: suspend () -> T,
): ResponseResult<T> {
    return try {
        // blocking block
        val users = withContext(Dispatchers.IO + handler + SupervisorJob()) {
            body()
        }
        ResponseResult.Success(users)
    } catch (e: Exception) {
        ResponseResult.Failure(e)
    }
}

inline fun <T : Any> liveResponse(crossinline body: suspend () -> ResponseResult<T>) =
    flow {
        emit(ResponseResult.Loading())
        val result = body()
        emit(result)
    }

const val BASE_URL = "https://api.weatherapi.com/v1"