package com.example.testapp.data.network.callAdapter

import arrow.core.Either
import arrow.core.Option
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonToken
import java.lang.reflect.ParameterizedType

class OptionTypeAdapter<T>(private val adapter: TypeAdapter<T>) : TypeAdapter<Either<Unit, T>>() {

    companion object {

        fun getFactory() = object : TypeAdapterFactory {
            override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
                val rawType = type.rawType as Class<*>
                if (rawType != Option::class.java) {
                    return null
                }
                val parameterizedType = type.type as ParameterizedType
                val actualType = parameterizedType.actualTypeArguments[0]
                val adapter = gson.getAdapter(TypeToken.get(actualType))
                return OptionTypeAdapter(adapter) as TypeAdapter<T>
            }
        }
    }

    override fun write(out: com.google.gson.stream.JsonWriter?, value: Either<Unit, T>?) {
        if (value?.isRight() == true){
            value.fold(ifLeft = {
                out?.nullValue()
            }, ifRight = {
                adapter.write(out, it)
            })

        }

    }

    override fun read(input: com.google.gson.stream.JsonReader?): Either<Unit, T> {
        val peek = input?.peek()
        return if (peek != JsonToken.NULL) {
            Either.fromNullable(adapter.read(input))
        } else {
            input.nextNull()
            Either.fromNullable(adapter.read(input))
        }
    }

}