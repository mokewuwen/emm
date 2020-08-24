package cn.uneko.study

import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.LevelListDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_drawable.*

class DrawableActivity : AppCompatActivity() {

    private val handler = Handler()
    private var clipDrawable: ClipDrawable? = null
    private var clipLevel = 0
    private var levelListDrawable: LevelListDrawable? = null
    private var wifiLevel = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawable)

        // TransitionDrawable
        (iv_course.drawable as TransitionDrawable).startTransition(1000)

        // ClipDrawable
        clipDrawable = iv_cctv_anime.drawable as ClipDrawable
        handler.post(task)

        levelListDrawable = iv_wifi.drawable as LevelListDrawable
        btn_level.setOnClickListener {
            wifiLevel = if (wifiLevel >= 4) 0 else ++wifiLevel
            levelListDrawable?.level = wifiLevel
        }
    }

    private val task = object : Runnable {
        override fun run() {
            if (clipLevel >= 10000) {
                return
            }
            clipLevel += 100
            clipDrawable?.level = clipLevel
            handler.postDelayed(this, 16)

            // levelListDrawable
            wifiLevel = if (wifiLevel >= 4) 0 else ++wifiLevel
            levelListDrawable?.level = wifiLevel
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(task)
    }
}