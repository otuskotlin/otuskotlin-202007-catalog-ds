import kotlinx.coroutines.runBlocking
import ru.otus.otuskotlin.catalogue.backend.handlers.cor.IExec
import ru.otus.otuskotlin.catalogue.backend.handlers.cor.corProc
import kotlin.test.Test
import kotlin.test.assertEquals

internal class CorTest {

    @Test
    fun testCor1(){
        val someCor: IExec<SomeContext> = corProc {
            exec {
                println("Start test.")
                someInt = 5
            }

            handler {
                isMatchable { someInt == 5 }
                exec {
                    someString = "Five"
                    println("Five checking is passed!")
                }
            }

                handler {
                    isMatchable {
                        someString.length < 5
                    }
                    exec {
                        println(someString)
                        someString += " is $someInt"
                    }
                }
                handler {
                    isMatchable {
                        someString.length > 5
                    }
                    exec {
                        println("Full string: $someString")
                    }
                }


        }

        val ctx = SomeContext()

        runBlocking {
            someCor.exec(ctx)
            assertEquals(5, ctx.someInt)
        }
    }

    data class SomeContext(
        var someInt: Int = 0,
        var someString: String = ""
    )
}