package cn.uneko.xmlparse

import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream

class StackOverflowXmlParser {

    // namespace don't use
    private val ns: String? = null
    fun parse(inputStream: InputStream): List<Entry> {
        inputStream.use {
            val parser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(it, null)
            parser.nextTag()
            return readFeed(parser)
        }
    }

    private fun readFeed(parser: XmlPullParser): List<Entry> {
        val entries = mutableListOf<Entry>()
        parser.require(XmlPullParser.START_TAG, ns, "feed")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            if (parser.name == "entry") {
                entries.add(readEntry(parser))
            } else {
                skip(parser)
            }
        }
        return entries
    }

    private fun readEntry(parser: XmlPullParser): Entry {
        parser.require(XmlPullParser.START_TAG, ns, "entry")
        val entry = Entry()
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {
                "title" -> entry.title = readTitle(parser)
                "updated" -> entry.updated = readUpdated(parser)
                "link" -> entry.link = readLink(parser)
                else -> skip(parser)
            }
        }
        return entry
    }

    private fun readTitle(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "title")
        val title = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "title")
        return title
    }

    private fun readUpdated(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "updated")
        val updated = readText(parser)
        parser.require(XmlPullParser.END_TAG, ns, "updated")
        return updated
    }

    private fun readLink(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "link")
        var link = ""
        val rel = parser.getAttributeValue(null, "rel")
        if (rel == "alternate") {
            link = parser.getAttributeValue(null, "href")
            parser.nextTag()
        }
        parser.require(XmlPullParser.END_TAG, ns, "link")
        return link
    }

    private fun readText(parser: XmlPullParser): String {
        var text = ""
        if (parser.next() == XmlPullParser.TEXT) {
            text = parser.text
            parser.nextTag()
        }
        return text
    }

    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.START_TAG -> depth++
                XmlPullParser.END_TAG -> depth--
            }
        }
    }
}