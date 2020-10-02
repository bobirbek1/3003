package com.idrok.a3003.ui.listFragment.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.idrok.a3003.R
import com.idrok.a3003.model.ITEM_TYPE
import com.idrok.a3003.model.ListItems
import com.idrok.a3003.model.TITLE_TYPE
import kotlinx.android.synthetic.main.rv_list_items.view.*
import kotlinx.android.synthetic.main.rv_list_items_title.view.*
import java.util.*


class ViewPagerChildAdapter(
    private val callback: ((String) -> Unit)
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    private var listFiltered = arrayListOf<ListItems>()
    private var list = arrayListOf<ListItems>()
    private var lastPosition = -1

    fun setData(list: ArrayList<ListItems>) {
        this.list = list
        this.listFiltered = list
        notifyItemChanged(0, listFiltered.size)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d("ListChildAdapter", "enter onCreateViewHolder")
        when (viewType) {
            TITLE_TYPE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.rv_list_items_title, parent, false)
                return VH1(view)
            }
            ITEM_TYPE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.rv_list_items, parent, false)
                return VH2(view)
            }
        }
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_list_items, parent, false)
        return VH2(view)
    }

    // RecyclerView elemenlarini bin qilish
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (list[position].type == TITLE_TYPE) {
            holder as VH1
            holder.onBind(listFiltered[position].body)
            setAnimation(holder.itemView, position)
        } else {
            holder as VH2
            //RecyclerViewni elementi bosilganda callback bilan ListChildni ListFragmentChildga otish
            holder.itemView.setOnClickListener {
                callback.invoke("string")
            }
            holder.onBind(listFiltered[position].body)
            setAnimation(holder.itemView, position)
        }

    }

    private fun setAnimation(itemView: View, position: Int) {
        if (position > lastPosition) {
            val anim = ScaleAnimation(
                0.0f,
                1.0f,
                0.0f,
                1.0f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            )
            anim.duration =
                1000 //to make duration random number between [0,501)

            itemView.startAnimation(anim)
            lastPosition = position
        }
    }


    override fun getItemCount(): Int = listFiltered.size

    override fun getItemViewType(position: Int): Int {
        return list[position].type
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                Log.d("ViewPagerChildAdapter", "charString:$charString")
                listFiltered = if (charString.isEmpty()) {
                    list
                } else {
                    val filteredList = arrayListOf<ListItems>()
                    for (item in list) {
                        if (item.body.toLowerCase(Locale.getDefault()).trim().contains(
                                charString.toLowerCase(
                                    Locale.getDefault()
                                ).trim()
                            )
                        ) {
                            filteredList.add(item)
                        }
                    }
                    filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = listFiltered
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listFiltered = if (results != null)
                    results.values as ArrayList<ListItems>
                else
                    arrayListOf()

                notifyDataSetChanged()
            }
        }

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
