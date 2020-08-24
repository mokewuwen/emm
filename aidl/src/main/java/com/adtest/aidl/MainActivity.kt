package com.adtest.aidl

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var binder: IPlayerCommand? = null
    private val conn = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            binder = IPlayerCommand.Stub.asInterface(service)
            binder?.registerProgress(object : IProgressListener.Stub() {
                override fun onProgress(progress: Int, total: Int) {
                    Log.d(TAG, "onProgress:${Thread.currentThread()} $total => $progress,data: ${Data.test}")
                }
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate: ${Thread.currentThread()}")

        bindService(Intent(this, MusicService::class.java), conn, BIND_AUTO_CREATE)

        play.setOnClickListener { binder?.command(PLAY) }
        pause.setOnClickListener { binder?.command(PAUSE) }
        resume.setOnClickListener {
            binder?.command(RESUME)
            Data.changeData()
        }
        stop.setOnClickListener { binder?.command(STOP) }
        reset.setOnClickListener { binder?.command(RESET) }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        unbindService(conn)
    }
}
