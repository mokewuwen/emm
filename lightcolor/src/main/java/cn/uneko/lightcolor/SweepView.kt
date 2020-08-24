package cn.uneko.lightcolor

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.Interpolator
import kotlin.math.abs
import kotlin.math.pow

class SweepView(context: Context, attributeSet: AttributeSet) :
    View(context, attributeSet) {

    private val strokePaint = Paint()
    private var colors = intArrayOf()
    private var positions = floatArrayOf()
    private var corner = 0F
    private val size = PointF()
    private val rect = RectF()
    private var gradient: SweepGradient? = null
    private var strokeWidth = 0F
    private val localMatrix = Matrix()
    private val animator = ValueAnimator.ofFloat(0F, 359F)

    init {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.SweepView)
        corner = typedArray.getDimensionPixelOffset(R.styleable.SweepView_corner, 0).toFloat()
        strokeWidth =
            typedArray.getDimensionPixelOffset(R.styleable.SweepView_strokeWidth, 0).toFloat()
        val colorStr = typedArray.getString(R.styleable.SweepView_colors) ?: ""
        val positionStr = typedArray.getString(R.styleable.SweepView_positions) ?: ""
        try {
            colors = colorStr.split(",").map { Color.parseColor(it) }.toIntArray()
            positions = positionStr.split(",").map { it.toFloat() }.toFloatArray()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        typedArray.recycle()


        strokePaint.style = Paint.Style.STROKE
        strokePaint.strokeWidth = strokeWidth
        strokePaint.isAntiAlias = true
        animator.interpolator = MyInterpolator()
        animator.duration = 2500
        animator.repeatCount = ValueAnimator.INFINITE
        animator.addUpdateListener {
//            Log.e("aaa", "${it.animatedFraction} ==>> ${it.animatedValue} ")
            localMatrix.setRotate(it.animatedValue as Float, size.x / 2, size.y / 2)
            strokePaint.shader?.setLocalMatrix(localMatrix)
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawRoundRect(rect, corner, corner, strokePaint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        size.set(w.toFloat(), h.toFloat())
        val offset = strokeWidth / 2
        rect.set(offset, offset, size.x - offset, size.y - offset)
        gradient = SweepGradient(size.x / 2, size.y / 2, colors, positions)
        strokePaint.shader = gradient
    }

    fun start() {
        animator.start()
    }

    fun release() {
        animator.cancel()
    }

    class MyInterpolator : Interpolator {
        override fun getInterpolation(input: Float): Float {
            return input*input
        }

        private fun calcFraction(input: Float): Float {
            return when {
                input < 0.25 -> { // 0 ~ +
                    getOffset(input, 0F, true)
                }
                input < 0.5 -> { // 0 ~ -
                    getOffset(input, 0.25F, false)
                }
                input < 0.75 -> { // 0 ~ +
                    getOffset(input, 0.5F, true)
                }
                input < 1 -> { // 0 ~ - END  0.875
                    getOffset(input, 0.75F, false)
                }
                else -> 1F
            }.apply {
//                Log.e("aaa", "calcFraction: $input ==>> $this")
            }
        }

        private fun getOffset(
            input: Float,
            start: Float,
            isIncrement: Boolean //  开始慢,后续快
        ): Float {
            var scale = abs((input % 0.25)) / 0.25
            scale = scale.pow(2) // 开始慢,后续快
            scale = if (isIncrement) {
                scale.pow(2) // 开始慢,后续快
            } else {
                1 - (1 - scale) * (1 - scale)
            }
            var offset = (scale.toFloat() * 0.25F)
            offset = if (offset in 0.0..1.0) offset else input
            return (start + offset).also {
                Log.e("aaa", "getOffset:$it \t $scale \t $input ")
            }
        }
    }
}