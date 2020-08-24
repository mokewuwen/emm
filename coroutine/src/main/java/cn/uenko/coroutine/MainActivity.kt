package cn.uenko.coroutine

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.qitech.coroutine.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnTest.setOnClickListener {
            runBlocking {

                coroutineScope {
                    val job = async {
                        var last = System.currentTimeMillis()
                        var i = 0
                        while (true) {
                            if (System.currentTimeMillis() > last) {
                                log("running...")
                                last += 200
                                i++
                            }
                        }

                    }
                    delay(700)
                    job.cancelAndJoin()
                    log("quit")
                }
            }
        }
    }

    private fun log(msg: String) {
        Log.e("aaa", msg)
    }
}
