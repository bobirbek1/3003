package com.idrok.a3003.ui.main

import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.idrok.a3003.R
import com.idrok.a3003.model.ImageSlider
import com.idrok.a3003.ui.sliderAdapter.SliderAdapter
import kotlinx.android.synthetic.main.fragment_main.view.*
import kotlin.math.abs

class MainFragment:Fragment(R.layout.fragment_main){
    private lateinit var rootView:View
    private val sliderHandler = Handler()
    private lateinit var viewPager2: ViewPager2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView = view
        setLayoutParams()
        setOnClicks()
        val listImages = getImages()
        setImageSlider(listImages)
    }

    // Buttonlarga SetOnClickListener beramiz
    private fun setOnClicks() {
        //1-button
        rootView.cv_main_1.setOnClickListener {
            findNavController().navigate(R.id.listFragment)
        }
        //2-button
        rootView.cv_main_2.setOnClickListener {

        }
        //3-button
        rootView.cv_main_3.setOnClickListener {

        }
        //4-button
        rootView.cv_main_4.setOnClickListener {

        }
        //5-button
        rootView.cv_main_5.setOnClickListener {

        }
        //6-button
        rootView.cv_main_6.setOnClickListener {

        }
    }

    //    Main fragmentda buttonlarni widthni heightiga tenglab berish berish
    private fun setLayoutParams() {
        val metrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(metrics)
        val screenHeight = metrics.heightPixels

        val height = (screenHeight * 3) / 20

        rootView.cv_main_1.layoutParams.width = height
        rootView.cv_main_2.layoutParams.width = height
        rootView.cv_main_3.layoutParams.width = height
        rootView.cv_main_4.layoutParams.width = height
        rootView.cv_main_5.layoutParams.width = height
        rootView.cv_main_6.layoutParams.width = height
    }


    private fun setImageSlider(listImages: ArrayList<ImageSlider>) {
        viewPager2 = rootView.vp_ads_top
        val adapter = SliderAdapter(listImages, viewPager2) {

        }
        viewPager2.adapter = adapter
        viewPager2.clipToPadding = false
        viewPager2.clipChildren = false
        viewPager2.offscreenPageLimit = 3
        viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        val pageTransformer = CompositePageTransformer()
        pageTransformer.addTransformer(MarginPageTransformer(40))
        pageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.15f
        }
        viewPager2.setPageTransformer(pageTransformer)
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRunnable())
                sliderHandler.postDelayed(sliderRunnable(), 2000)
            }
        })
    }

    private fun getImages(): ArrayList<ImageSlider> {
        val listImages = arrayListOf<ImageSlider>()
        (0..5).forEach { _ ->
            listImages.add(ImageSlider(R.drawable.ic_image))
        }
        return listImages
    }

    private fun sliderRunnable(): Runnable =
        Runnable { viewPager2.currentItem = viewPager2.currentItem + 1 }

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable())
    }
}