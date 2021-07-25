package ru.otus.otuskotlin

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.*
import kotlin.test.Test

class SerialTest {

    @Test
    fun serialTest(){
        val dto: Base = Child("1")
        val dto2 = Another("2", 2, Model("Harry"))
        val json = Json {
            serializersModule = SerializersModule {
                polymorphic(Base::class){
                    subclass(Child::class)
                    subclass(Another::class)
                }
            }
            classDiscriminator = "type"
            prettyPrint = true
        }
        val serString = json.encodeToString(dto)
        println(serString)
        val model = json.decodeFromString<Base>(serString)
        println(model)
        val dto2String = json.encodeToString<Base>(dto2)
        val dto2String2 = json.encodeToString(dto2)
        println(dto2String)
        println(dto2String2)
        val model2 = json.decodeFromString<Base>(dto2String)
        println(model2)
    }

    interface Base {
        val id: String?
    }

    @Serializable
    @SerialName("child")
    data class Child(
        override val id: String? = null,
    ): Base

    @Serializable
    @SerialName("Another")
    data class Another(
        override val id: String? = null,
        val age: Int? = null,
        val model: Model? = null,
    ) : Base

    @Serializable
    data class Model(
        val name: String = "Paul"
    )
}
