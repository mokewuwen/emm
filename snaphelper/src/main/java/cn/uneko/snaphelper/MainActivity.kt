package cn.uneko.snaphelper

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.PagerSnapHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rgb = (1..255 step 50).toList()
        val colors = arrayListOf<Int>()
        for (r in rgb) {
            for (g in rgb) {
                for (b in rgb) {
                    colors.add(Color.argb(255, r, g, b))
                }
            }
        }
        Log.d("aaa", "onCreate: ${colors.size}")

        rv.adapter = ColorAdapter(colors)
        rv.layoutManager = GridLayoutManager(this, 2)
//        PagerSnapHelper().attachToRecyclerView(rv)
        LinearSnapHelper().attachToRecyclerView(rv)
    }
}
