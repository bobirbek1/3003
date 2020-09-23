package com.idrok.a3003.ui.listFragment.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ListChildAdapter(
    private val listTitles: ArrayList<String>,
    private val listItems: ArrayList<String>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)

    }

    class ViewHolder1(view: View) : RecyclerView.ViewHolder(view) {

    }

    class ViewHolder2(view: View) : RecyclerView.ViewHolder(view) {

    }
}