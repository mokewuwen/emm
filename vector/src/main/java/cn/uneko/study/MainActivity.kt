package cn.uneko.study

import android.animation.ObjectAnimator
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.MotionEvent
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var isChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ObjectAnimator.ofFloat(iv_loading, "rotation", 0F, 359F).apply {
            duration = 700
            repeatCount = ObjectAnimator.INFINITE
            interpolator = LinearInterpolator()
            start()
        }


        mIvTest.setOnTouchListener { _, event ->
            val state = when (event.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    android.R.attr.state_pressed
                }
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
                }
            }
        }

        btn_toDrawable.setOnClickListener {
            startActivity(DrawableActivity::class.java)
        }

        iv_test.setOnClickListener {
            val state = if (isChecked) android.R.attr.state_checked else 0
            isChecked = !isChecked
            iv_test.setImageState(intArrayOf(state), true)
        }


        ivHexagonal.setOnClickListener {
            ivHexagonal.drawable.apply {
                if (this is Animatable) {
                    start()
                    ivHexagonal.pivotX
                }
            }
        }

    }
}
