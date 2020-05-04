package com.peter.thelandlord

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.Test

import org.junit.Assert.*
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.Executors


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
    fun test_timestamp(){
        println(Thread.currentThread().name)
        val coroutineScope = CoroutineScope(Dispatchers.IO)

        coroutineScope.launch {
            println("Hello world")
            println(Thread.currentThread().name)
        }

    }

    fun ioExecutor(f: () -> Unit){
        val executor = Executors.newSingleThreadExecutor()
        executor.execute(f)

    }

}
