package com.peter.thelandlord.utils

import java.util.concurrent.Executors

object Executors {
    val executor = Executors.newSingleThreadExecutor()

    fun ioExecutor(f: () -> Unit){
        executor.execute(f)
    }
}