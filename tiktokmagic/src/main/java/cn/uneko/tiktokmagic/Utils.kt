package cn.uneko.tiktokmagic

import android.content.Context
import android.content.SharedPreferences
import java.util.*

class Utils private constructor() {

    private val sharedPreference: SharedPreferences =
        App.getContext().getSharedPreferences("sim", Context.MODE_PRIVATE)

    companion object {
        val instance by lazy { Utils() }
    }

    fun save(map: HashMap<String, Any>) {
        val editor = sharedPreference.edit()
        map.forEach {
            when (it.value) {
                is String -> editor.putString(it.key, it.value as String)
                is Boolean -> editor.putBoolean(it.key, it.value as Boolean)
                is Float -> editor.putFloat(it.key, it.value as Float)
                is Int -> editor.putInt(it.key, it.value as Int)
                is Long -> editor.putLong(it.key, it.value as Long)
            }
        }
        editor.apply()
    }

    fun get(): MutableMap<String, *>? {
        return sharedPreference.all
    }

    fun get(str: String): Boolean {
        return sharedPreference.getBoolean(str, false)
    }

    fun isFirst(): Boolean {
        return sharedPreference.getBoolean("isFirst", true)
    }

    fun setNotFirst() {
        sharedPreference.edit()
                .putBoolean("isFirst", false)
                .apply()
    }

    fun removeAll() {
        sharedPreference.edit().clear().apply()
    }
}