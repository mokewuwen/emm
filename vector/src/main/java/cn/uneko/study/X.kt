package cn.uneko.study

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlin.math.roundToInt


fun Context.startActivity(clz: Class<*>, func: (Intent.() -> Unit)? = null) {
    val intent = Intent(this, clz).apply {
        if (func != null) {
            func()
        }
    }
    if (this !is Activity) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}

val DEBUG = BuildConfig.DEBUG
const val TAG_PREFIX = "emm"

fun Any.log(msg: String) {
    if (DEBUG) {
        Log.e("$TAG_PREFIX-${javaClass.simpleName}", msg)
    }
}

fun Any.log(tag: String, msg: String) {
    if (DEBUG) {
        Log.e("$TAG_PREFIX-$tag", msg)
    }
}

val Int.dp: Int
    get() = (App.getInstance().resources.displayMetrics.density * this).roundToInt()

val Int.sp: Int
    get() = (App.getInstance().resources.displayMetrics.scaledDensity * this).roundToInt()
