package cn.uneko.tiktokmagic

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

/**
 * 数据来源
 * https://zh.wikipedia.org/wiki/%E7%A7%BB%E5%8A%A8%E8%AE%BE%E5%A4%87%E7%BD%91%E7%BB%9C%E4%BB%A3%E7%A0%81
 */
class MainActivity : AppCompatActivity() {
    private lateinit var curSim: Sim

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        saveOriginSimData()

        val data = Gson().fromJson(getJson(), Array<Sim>::class.java)

        data?.let { it ->
            val list = arrayListOf<String>()
            it.forEach {
                list.add(it.country)
            }
            val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list)
            mSpinner.adapter = adapter
            mSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    curSim = data[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        }

        mBtnSet.setOnClickListener {
            val cmd = "setprop gsm.sim.operator.alpha \"${curSim.alpha}\" \n" +
                    "setprop gsm.sim.operator.iso-country \"${curSim.abbre}\" \n" +
                    "setprop gsm.sim.operator.numeric \"${curSim.numeric}\" \n" + getQueryCmd()
            exeCommand(cmd) { data -> mTvPanel.text = data }
        }

        mBtnReset.setOnClickListener {
            val cmd = "setprop gsm.sim.operator.alpha \"China Mobile,中国电信\"\n" +
                    "setprop gsm.sim.operator.iso-country cn,cn\n" +
                    "setprop gsm.sim.operator.numeric 46002,46011\n" + getQueryCmd()
            exeCommand(cmd) { data -> mTvPanel.text = data }
        }

        mBtnQuery.setOnClickListener {
            exeCommand(getQueryCmd()) { data -> mTvPanel.text = data }
        }

        mBtnTest.setOnClickListener {
            val origin = Utils.instance.get()
            if (origin != null) {
                val cmd = "setprop gsm.sim.operator.alpha \"${origin["alpha"]}\"\n" +
                        "setprop gsm.sim.operator.iso-country \"${origin["abbre"]}\"\n" +
                        "setprop gsm.sim.operator.numeric \"${origin["numeric"]}\"\n" + getQueryCmd()
                exeCommand(cmd) { data -> mTvPanel.text = data }
            } else {
                mTvPanel.text = "未找到原始数据"
            }
        }
    }

    private fun getQueryCmd(): String {
        return "echo alpha:`getprop gsm.sim.operator.alpha`\n" +
                "echo abbre:`getprop gsm.sim.operator.iso-country`\n" +
                "echo numeric:`getprop gsm.sim.operator.numeric`"
    }

    private fun exeCommand(
        cmd: String,
        eachLine: ((line: String) -> Unit)? = null,
        result: ((data: String) -> Unit)? = null
    ) {
        Thread(Runnable {
            val process = Runtime.getRuntime().exec("su")
            val write = BufferedWriter(OutputStreamWriter(process.outputStream))
            val input = BufferedReader(InputStreamReader(process.inputStream))

            try {
                write.write("$cmd\nexit\n")
                write.flush()
                val builder = StringBuilder()
                input.forEachLine {
                    eachLine?.invoke(it)
                    builder.appendln(it)
                }
                runOnUiThread {
                    result?.invoke(builder.toString())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                write.close()
                input.close()
            }
        }).start()
    }

    private fun getJson(): String? {
        val reader = BufferedReader(InputStreamReader(assets.open("sim.json")))
        val builder = StringBuilder()
        var line: String?
        do {
            line = reader.readLine()
            if (line != null) {
                builder.append(line)
            }
        } while (line != null)
        return if (builder.isNotEmpty()) builder.toString() else null
    }

    private fun saveOriginSimData() {
        //todo 加入判断root
        if (Utils.instance.isFirst()) {
            Utils.instance.removeAll()
            val map = hashMapOf<String, Any>()
            exeCommand(getQueryCmd(),
                { line ->
                    //fixme，不必要的函数，在result函数中写正则比较好
                    val origin = line.split(":")
                    map[origin[0]] = origin[1]
                },
                {
                    Utils.instance.save(map)
                    Utils.instance.setNotFirst()
                })
            //Log.e("t", map.toString())
        }
    }
}
