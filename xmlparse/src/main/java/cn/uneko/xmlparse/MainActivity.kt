package cn.uneko.xmlparse

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.net.URL

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        launch {
            val entries = withContext(Dispatchers.Default) { getEntries() }
            entries?.let {
                val adapter = FeedAdapter(it)
                adapter.setItemClickListener { link -> viewLink(link) }
                mRvFeed.adapter = adapter
                mRvFeed.startLayoutAnimation()
            }
        }
    }

    private fun getEntries(): List<Entry>? {
        return try {
            StackOverflowXmlParser().parse(URL("https://stackoverflow.com/feeds/").openStream())
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun viewLink(link: String) {
        val uri = Uri.parse(link)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }
}

