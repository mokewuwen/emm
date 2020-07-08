package cn.uneko.study

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mIvTest.setOnTouchListener { _, event ->
            val state = when (event.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> android.R.attr.state_pressed
                MotionEvent.ACTION_UP -> {
                    mIvTest.performClick()
                    0
                }
                else -> 0
            }
            mIvTest.setImageState(intArrayOf(state), true)
            false
        }

        mIvTest.setOnClickListener {
            mIvTest.drawable.apply {
                if (this is Animatable) {
                    start()
                    Log.e("aaa", "click")
                }
            }
        }

        mBtnTest.setOnClickListener {
        }

    }
}
