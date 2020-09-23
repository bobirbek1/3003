package com.idrok.a3003.ui

import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.idrok.a3003.R
import com.idrok.a3003.model.ImageSlider
import com.idrok.a3003.ui.sliderAdapter.SliderAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private val sliderHandler = Handler()
    private lateinit var viewPager2: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val listImages = getImages()
        setImageSlider(listImages)
    }

    override fun onResume() {
        super.onResume()
        setLayoutParams()
//        sliderHandler.postDelayed(sliderRunnable(), 2000)
    }

    //    Main activityda buttonlarni widthni berish
    private fun setLayoutParams() {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val screenHeight = metrics.heightPixels

        val height = (screenHeight*3)/20

        cv_main_1.layoutParams.width = height
        cv_main_2.layoutParams.width = height
        cv_main_3.layoutParams.width = height
        cv_main_4.layoutParams.width = height
        cv_main_5.layoutParams.width = height
        cv_main_6.layoutParams.width = height
    }

    private fun setImageSlider(listImages: ArrayList<ImageSlider>) {
        viewPager2 = vp_ads_top
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