package cn.uneko.xmlparse

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.uneko.xmlparse.databinding.ItemFeedBinding

class FeedAdapter(private val entries: List<Entry>) : RecyclerView.Adapter<FeedAdapter.Holder>() {
    private var click: ((link: String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_feed, parent, false)
    )

    override fun getItemCount() = entries.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind.apply {
            val color = if (position % 2 == 0) Color.LTGRAY else Color.GRAY
            llBg.setBackgroundColor(color)
            tvTitle.text = entries[position].title
            tvUpdated.text = entries[position].updated.substringAfterLast('T').dropLast(1)
            root.setOnClickListener { click?.invoke(entries[position].link) }
        }
    }


    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bind = ItemFeedBinding.bind(itemView)
    }

    fun setItemClickListener(click: (link: String) -> Unit) {
        this.click = click
    }
}