package cn.uneko.study

import android.app.Application
import android.content.Context
import android.hardware.display.DisplayManager
import android.os.Handler
import android.util.Log

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        refreshRateListener()
    }

    private fun refreshRateListener() {
        val displayManager = getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
        displayManager.registerDisplayListener(object : DisplayManager.DisplayListener {
            override fun onDisplayChanged(displayId: Int) {
                refreshRate = displayManager.getDisplay(displayId).refreshRate
                Log.d("aaa", "onDisplayChanged: $refreshRate")
            }

            override fun onDisplayAdded(displayId: Int) {

            }

            override fun onDisplayRemoved(displayId: Int) {

            }
        }, Handler())
        Log.d("aaa", "refreshRateListener: " + displayManager.displays.map { it.refreshRate })
    }


    companion object {
        private lateinit var INSTANCE: Application
        private var refreshRate = 60F
        public fun getInstance(): Application {
            return INSTANCE
        }
    }
}