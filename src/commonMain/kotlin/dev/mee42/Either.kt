package dev.mee42

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

class Either<A,B> private constructor(private val a: A?, private val b: B?) {
    fun <R> mapA(mapper: (A) -> R):Either<R,B> {
        return if(a != null) ofA(mapper(a)) else ofB(b!!)
    }
    fun <R> mapB(mapper: (B) -> R):Either<A,R> {
        return if(b != null) ofB(mapper(b)) else ofA(a!!)
    }
    fun isA() = a != null
    fun isB() = b != null

    fun aOrElse(a1: A) = a ?: a1
    fun aOrThrow(throwable: () -> Throwable) = a ?: throw throwable()
    fun getAUnsafe() = aOrThrow { IllegalStateException("Can't get A object when none exist") }
    fun getA() = a

    fun bOrElse(aMapper: (A) -> B) = b ?: aMapper(a!!)
    fun bOrElse(b1: B) = b ?: b1
    fun bOrThrow(throwable: () -> Throwable): B = b ?: throw throwable()
    fun getBUnsafe() = bOrThrow { IllegalStateException("Can't get A object when none exist") }
    fun getB() = b



    companion object {
        fun <A,B> ofA(a: A): Either<A,B>{
            return Either(a,null)
        }
        fun <A,B> ofB(b: B): Either<A,B> {
            return Either(null, b)
        }
    }
    fun <A,B> Either<A,B>.aOrElse(bMapper: (B) -> A):A{
        return getA() ?: bMapper(getBUnsafe())
    }

    inline fun <R> getFromEither(aMapper: (A) -> R, bMapper: (B) -> R):R {
        return getA()?.let(aMapper) ?: bMapper(getBUnsafe())
    }
}

fun <A> Either<A,A>.getOne(): A {
    return getA() ?: getBUnsafe()
}
