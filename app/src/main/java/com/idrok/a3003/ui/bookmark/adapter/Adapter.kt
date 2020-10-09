package com.idrok.a3003.ui.bookmark.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.idrok.a3003.R
import kotlinx.android.synthetic.main.rv_bookmark_item.view.*

class Adapter(private val list: ArrayList<String>, private val callback: (String) -> Unit) :
    RecyclerView.Adapter<Adapter.VH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_bookmark_item, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.itemView.setOnClickListener {
            callback.invoke(list[position])
        }
        holder.itemView.tv_title_bookmark.text = list[position]
    }

    override fun getItemCount(): Int = list.size

    class VH(view: View) : RecyclerView.ViewHolder(view) {

    }

}