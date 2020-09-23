package com.idrok.a3003.ui.sliderAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.idrok.a3003.R
import com.idrok.a3003.model.ImageSlider
import kotlinx.android.synthetic.main.slider_item.view.*

class SliderAdapter(
    private val listImages: ArrayList<ImageSlider>,
    private val viewPager2: ViewPager2,
    private val itemClick: ((ImageSlider) -> Unit)
) : RecyclerView.Adapter<SliderAdapter.VH>() {
    private lateinit var context:Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.slider_item, parent, false)
        context = parent.context
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.itemView.setOnClickListener {
            itemClick.invoke(listImages[position])
        }
        holder.itemView.iv_slider_item.setImageResource(listImages[position].image)
        if (position == listImages.size - 2){
            viewPager2.post(runnable())
        }
    }

    override fun getItemCount(): Int = listImages.size

    class VH(view: View) : RecyclerView.ViewHolder(view) {

    }
    private fun runnable() = object : Runnable{
        override fun run() {
            listImages.addAll(listImages)
            notifyDataSetChanged()
        }

    }

}