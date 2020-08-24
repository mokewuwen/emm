package cn.uneko.ui.wareview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class WareView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private lateinit var centerF: PointF
    private lateinit var center: Point
    private val path = Path()
    private val waveHeight = 50f
    private val step = 4
    private var progress: Float = 0f
    private val paint by lazy {
        val paint = Paint()
        paint.color = Color.BLUE
        paint.style = Paint.Style.FILL
        paint
    }

    private val animator by lazy {
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 1000
            addUpdateListener {
                progress = it.animatedValue as Float
                invalidate()
            }
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return
        path.reset()
        val maxWidth = width - 40
        val maxHeight = height - 20
        val space = maxWidth / step
        path.moveTo(-space * (progress - 1), height/2f)


        for (i in -space..width step space) {
            val step = space.toFloat()
            path.rQuadTo(step, -waveHeight, step * 2, 0f)
            path.rQuadTo(step, waveHeight, step * 2, 0f)
        }
        path.lineTo(maxWidth.toFloat() + 20f, maxHeight.toFloat())
        path.lineTo(20f, maxHeight.toFloat())
        path.close()
        canvas.drawPath(path, paint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        center = Point(w / 2, h / 2)
        centerF = PointF(w / 2.toFloat(), h / 2.toFloat())
    }

    fun start() {
        animator.start()
    }

    fun cancel() {
        animator.cancel()
    }
}