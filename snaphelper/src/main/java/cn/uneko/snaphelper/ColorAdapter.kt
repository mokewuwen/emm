package cn.uneko.snaphelper

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ColorAdapter(private val colors: List<Int>) : RecyclerView.Adapter<Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_color, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return colors.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.setBackgroundColor(colors[position])
    }

}

class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

}