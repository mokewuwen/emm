package cn.uneko.tiktokmagic

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        applicationContext
        application = this
    }

    companion object {
        var application: Application? = null
        fun getContext(): Application {
            return application!!
        }
    }


}