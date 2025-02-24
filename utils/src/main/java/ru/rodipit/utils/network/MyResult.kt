package ru.rodipit.utils.network

sealed class MyResult<out T> {
    data class Success<out T>(val data: T) : MyResult<T>()
    data class Error(val exception: Throwable) : MyResult<Nothing>()
    data class ErrorWithContent<out T>(val data: T, val exception: Throwable) : MyResult<T>()
}