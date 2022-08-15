package com.example.testapp.data.network.callAdapter

import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.Either.Right
import com.example.testapp.data.network.response.Error
import com.example.testapp.data.network.response.ResponseTest
import com.google.gson.Gson
import okhttp3.Request
import okio.Timeout
import retrofit2.*
import timber.log.Timber
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

internal class EitherCallAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) return null
        check(returnType is ParameterizedType) { "Return type must be a parameterized type." }

        val responseType = getParameterUpperBound(0, returnType)
        if (getRawType(responseType) != Either::class.java) return null
        check(responseType is ParameterizedType) { "Response type must be a parameterized type." }

        val leftType = getParameterUpperBound(0, responseType)
        if (getRawType(leftType) != Error::class.java) return null

        val rightType = getParameterUpperBound(1, responseType)
        return EitherCallAdapter<Any>(rightType)
    }
}

private class EitherCallAdapter<R>(
    private val successType: Type
) : CallAdapter<R, Call<Either<Error, R>>> {

    override fun adapt(call: Call<R>): Call<Either<Error, R>> = EitherCall(call, successType)

    override fun responseType(): Type = successType
}

private class EitherCall<R>(
    private val delegate: Call<R>,
    private val successType: Type
) : Call<Either<Error, R>> {

    override fun enqueue(callback: Callback<Either<Error, R>>) = delegate.enqueue(
        object : Callback<R> {

            override fun onResponse(call: Call<R>, response: Response<R>) {
                callback.onResponse(this@EitherCall, Response.success(response.toEither()))
            }

            private fun Response<R>.toEither(): Either<Error, R> {
                Timber.e(this.message())
                if (!isSuccessful) {//if network error 400+
                    val gson = Gson()
                    val errorObject = gson.fromJson(errorBody()?.string() ?: "", ResponseTest::class.java)
                    return Left(errorObject.error ?: Error(0, "Test error"))
                }

                body()?.let { body ->
                    return Right(body)
                }

                // if we defined Unit as success type it means we expected no response body
                // e.g. in case of 204 No Content
                return if (successType == Unit::class.java) {
                    @Suppress("UNCHECKED_CAST")
                    Right(Unit) as Either<Error, R>
                } else {
                    @Suppress("UNCHECKED_CAST")
                    Left(UnknownError("Response body was null")) as Either<Error, R>
                }
            }

            override fun onFailure(call: Call<R>, throwable: Throwable) {
                val response: Left<Error>?
                when(throwable){
//                    is SessionValidationException -> {
//                        response = Left(Error(401, throwable.throwMessage))
//                    }
                    else -> {
                        response = Left(Error(1, throwable.message ?: ""))
                    }
                }
                callback.onResponse(this@EitherCall, Response.success(response))
            }
        }
    )

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun clone(): Call<Either<Error, R>> = EitherCall(delegate.clone(), successType)

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): Response<Either<Error, R>> = throw UnsupportedOperationException()

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}
