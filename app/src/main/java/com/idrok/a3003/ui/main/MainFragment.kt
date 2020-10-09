package com.idrok.a3003.ui.main

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView
import com.idrok.a3003.PREFERENCE_KEY
import com.idrok.a3003.R
import com.idrok.a3003.model.ImageSlider
import com.idrok.a3003.data.LANGUAGE
import com.idrok.a3003.ui.sliderAdapter.SliderAdapter
import kotlinx.android.synthetic.main.content_main.view.*
import kotlinx.android.synthetic.main.fragment_main.view.*
import java.util.*
import kotlin.math.abs

const val PDFNAME = "pdfName"
const val IS_BUTTON6 = "isButton6"

class MainFragment : Fragment(R.layout.fragment_main),
    NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private lateinit var rootView: View
    private lateinit var viewPager2: ViewPager2
    private lateinit var timer: Timer


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView = view
        setViews()
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        findNavController().navigate(R.id.bookmarkFragment)
        return true
    }

    private fun setViews() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(rootView.home_toolbar)
        setLayoutParams()
        setOnClicks()
        val listImages = getImages()
        setImageSlider(listImages)
    }


    // Buttonlarga SetOnClickListener beramiz
    private fun setOnClicks() {
        rootView.home_toolbar.setNavigationOnClickListener {
            rootView.drawer_layout.openDrawer(GravityCompat.START)
            requireActivity()
        }

        rootView.nav_view.setNavigationItemSelectedListener(this)
//        rootView.btn_telegram.setOnClickListener(this)
//        rootView.btn_facebook.setOnClickListener(this)
//        rootView.btn_instagram.setOnClickListener(this)
//        rootView.btn_twitter.setOnClickListener(this)
//        rootView.btn_vk.setOnClickListener(this)

        //1-main button
        rootView.cv_main_1.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("type", 1)
            findNavController().navigate(R.id.listFragment, bundle)
        }
        //2-main button
        rootView.cv_main_2.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("type", 2)
            findNavController().navigate(R.id.listFragment, bundle)
        }
        //3-main button
        rootView.cv_main_3.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("type", 3)
            findNavController().navigate(R.id.listFragment, bundle)
        }
        //4-main button
        rootView.cv_main_4.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("type", 4)
            findNavController().navigate(R.id.listFragment, bundle)
        }
        //5-main button
        rootView.cv_main_5.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(PDFNAME, getPdfName(0))
            findNavController().navigate(R.id.pdfFragment, bundle)
        }
        //6-main button
        rootView.cv_main_6.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(PDFNAME,getPdfName(1))
            findNavController().navigate(R.id.pdfFragment,bundle)
        }
    }

    private fun getPdfName(i: Int): String {
        val prefs = requireActivity().getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE)
        val lang = prefs.getString(LANGUAGE, "ru")

        if (!lang.isNullOrEmpty()) {
            if (i == 0) {
                return when (lang) {
                    "uz" -> {
                        "qonun.uz.pdf"
                    }
                    "cyrl" -> {
                        "qonun.uz-Cyrl.pdf"
                    }
                    "ru" -> {
                        "qonun.ru.pdf"
                    }
                    else -> {
                        "qonun.ru.pdf"
                    }
                }
            } else {
                return when (lang) {
                    "uz" -> {
                        "konseptual.uz.pdf"
                    }
                    "cyrl" -> {
                        "konseptual.uz-Cyrl.pdf"
                    }
                    "ru" -> {
                        "konseptual.ru.pdf"
                    }
                    else -> {
                        "konseptual.ru.pdf"
                    }
                }
            }
        } else {
            return if (i == 0){"qonun.ru.pdf"}
            else{
                "konseptual.ru.pdf"
            }
        }

    }

    //    Main fragmentda buttonlarni widthni heightiga tenglab berish berish
    private fun setLayoutParams() {
        val metrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(metrics)
        val density = requireActivity().resources.displayMetrics.density
        var screenHeight = metrics.heightPixels

        screenHeight -= (56 * density).toInt()

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
        requireActivity()
    }

    override fun onResume() {
        super.onResume()
        timer = Timer()
        slider()
    }

    private fun slider() {
        timer.schedule(object : TimerTask() {
            override fun run() {
                viewPager2.currentItem = viewPager2.currentItem + 1
                slider()
            }

        }, 2000)
    }

    private fun getImages(): ArrayList<ImageSlider> {
        val listImages = arrayListOf<ImageSlider>()
        (0..5).forEach { _ ->
            listImages.add(ImageSlider(R.drawable.ic_image))
        }
        return listImages
    }

    override fun onPause() {
        timer.cancel()
        super.onPause()
        Log.d("MainFragment", "onPause work")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("MainFragment", "onDestroyView work")
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_language -> {
                findNavController().navigate(R.id.languageFragment)
            }

            R.id.nav_share -> {
            }

            R.id.nav_grade -> {
            }
        }
        return false
    }

    override fun onClick(view: View?) {
        when (view) {
//            rootView.btn_telegram -> {
//            }
//            rootView.btn_facebook -> {
//            }
//            rootView.btn_instagram -> {
//            }
//            rootView.btn_twitter -> {
//            }
//            rootView.btn_vk -> {
//            }
        }
    }
}