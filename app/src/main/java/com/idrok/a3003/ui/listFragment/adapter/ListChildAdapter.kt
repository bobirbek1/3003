package com.idrok.a3003.ui.listFragment.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.idrok.a3003.R
import com.idrok.a3003.model.ITEM_TYPE
import com.idrok.a3003.model.ListItems
import com.idrok.a3003.model.TITLE_TYPE
import kotlinx.android.synthetic.main.rv_list_items.view.*
import kotlinx.android.synthetic.main.rv_list_items_title.view.*

class ListChildAdapter(
    private val list: ArrayList<ListItems>,
    private val callback: ((String) -> Unit)
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        Log.d("ListChildAdapter","enter onCreateViewHolder")
        when(viewType){
            TITLE_TYPE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.rv_list_items_title,parent,false)
                return VH1(view)
            }
            ITEM_TYPE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.rv_list_items,parent,false)
                return VH2(view)
            }
        }
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_list_items, parent, false)
        return VH2(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (list[position].type == TITLE_TYPE){
            holder as VH1
            holder.onBind(list[position].body)
        } else{
            holder as VH2
            holder.onBind(list[position].body)
        }

    }

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return list[position].type
    }


    class VH1(view: View) : RecyclerView.ViewHolder(view) {
        fun onBind(body: String) {
            itemView.tv_list_title.text = body
        }
    }
    class VH2(view: View) : RecyclerView.ViewHolder(view) {
        fun onBind(body: String) {
            itemView.tv_list_items.text = body
        }
    }

}