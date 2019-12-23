package dev.mee42

import kotlin.contracts.contract

class ErrorOrSuccess<E> private constructor(private val error_: E?) {

    object Value
    companion object {
        fun <E> success() = ErrorOrSuccess<E>(null)
        fun <E> error(e: E) = ErrorOrSuccess(e)
    }

    val isError = error_ != null
    val isSuccess = error_ == null
    inline fun <R> onError(block: (E) -> R) = if(isError) block(getError()) else null
    fun <R> onSuccess(block: () -> R)= if(isSuccess) block() else null


    fun <R> process(onError: (E) -> R, onSuccess: () -> R): R {
        return if (isError) onError(getError()) else onSuccess()
    }
    fun getError(): E = error_!!

    fun <R> map(op: (E) -> R): ErrorOrSuccess<R> {
        return if(isError) ErrorOrSuccess(op(getError())) else ErrorOrSuccess(null)
    }
}

fun <T> T.asError() = ErrorOrSuccess.error(this)