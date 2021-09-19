package com.alurwa.berkelas

import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import org.junit.Assert.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun flowException() = runBlocking {
        flow<Boolean> {
            try {
                throw IllegalStateException("Huhi")
            } catch (e: Exception) {
                println("Ui uee ring")
            }

        }.collect {  }
    }

    @Test
    fun suspendCoroutine() = runBlocking {
        launch {
            launch {
                try {
                    suspendCancellableCoroutine {
                        throw Exception("Error nih bos")
                    }
                } catch (e: Exception) {
                    println("Tak cekel kowe")
                }

            }
        }

        println("ramping")
    }

    @Test
    fun sortArray() {
        val arrayNum = arrayOf(
            78, 82, 75, 76, 76, 88, 75, 76, 74, 46, 72, 45, 88, 48, 89, 92, 53, 48, 90, 62,
            80, 58, 65, 84, 99, 81, 87, 74, 64, 65, 79, 68, 75, 66, 80, 74, 62, 83, 77, 71
        )

        arrayNum.sort()

        var ssw = 49
        var str = 0
        arrayNum.forEach {
            if (it >= ssw) {
                ssw += 5
                println(str)
                str = 0
            }
            str += 1

        }
        println(arrayNum.joinToString())
    }
}