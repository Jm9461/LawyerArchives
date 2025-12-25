package com.lawyer_archives

import android.app.Application
import android.util.Log

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("APP_CRASH", "Crash in thread: ${thread.name}", throwable)
            throwable.printStackTrace()
            android.os.Process.killProcess(android.os.Process.myPid())
        }
    }
}