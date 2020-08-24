package com.adtest.aidl

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Handler
import android.os.IBinder
import android.os.Process
import android.util.Log


class MusicService : Service() {
    private val handler = Handler()
    private val listenerSet = HashSet<IProgressListener>()

    private val musicPlayer: MediaPlayer by lazy {
        Log.d(TAG, "lazy")
        MediaPlayer.create(this, R.raw.music)
            .apply {
                isLooping = true
            }
    }


    private val task = object : Runnable {
        override fun run() {
            Log.d(TAG, "${Thread.currentThread()} run, data: ${Data.test}")
            for (listener in listenerSet) {
                listener.onProgress(musicPlayer.currentPosition, musicPlayer.duration)
            }
            handler.postDelayed(this, 500)
        }
    }


    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG, "onBind: ${Process.myUid()}")
        return object : IPlayerCommand.Stub() {
            override fun registerProgress(listener: IProgressListener?) {
                listener?.let { listenerSet.add(it) }
            }

            override fun command(command: Int) {
                when (command) {
                    PLAY -> {
                        musicPlayer.start()
                        isListenerRunning(true)
                    }
                    PAUSE -> {
                        musicPlayer.pause()
                        isListenerRunning(false)
                    }
                    RESUME -> {
                        musicPlayer.start()
                        isListenerRunning(true)
                    }
                    STOP -> {
                        musicPlayer.pause()
                        musicPlayer.seekTo(0)
                        isListenerRunning(false)
                    }
                }
            }

            override fun unRegisterProgress(listener: IProgressListener?) {
                listenerSet.remove(listener)
            }
        }
    }

    private fun isListenerRunning(isOn: Boolean) {
        if (isOn) {
            handler.post(task)
        } else {
            handler.removeCallbacks(task)
        }
    }

    override fun onRebind(intent: Intent?) {
        Log.d(TAG, "onRebind: ${Process.myUid()}")
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "onUnbind: ${Process.myUid()}")
        return super.onUnbind(intent)
    }
}
