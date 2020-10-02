package com.idrok.a3003.ui.listChildFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.idrok.a3003.R
import com.idrok.a3003.ui.listChildFragment.adapter.ViewPagerAdapter
import com.idrok.a3003.ui.listFragment.DATA_NAME
import kotlinx.android.synthetic.main.custom_tab_item.view.*
import kotlinx.android.synthetic.main.fragment_data.view.*

class ListChildFragment : Fragment(R.layout.fragment_data) {

    private lateinit var rootView: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView = view
        setOnClicks()
        setViewPager()
    }

    private fun setViewPager() {
        val list = arrayListOf("title1", "title2", "title3","title","title","title")
        val adapter = ViewPagerAdapter(this, list)
        val tabLayout = rootView.tab_data
        val viewPager2 = rootView.vp_data

        viewPager2.adapter = adapter
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.customView?.cv_data?.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorPrimary
                    )
                )
                tab?.customView?.tv_tab_item?.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorWhite
                    )
                )
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.customView?.cv_data?.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorWhite
                    )
                )
                tab?.customView?.tv_tab_item?.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorPrimary
                    )
                )
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            val view = LayoutInflater.from(requireContext())
                .inflate(R.layout.custom_tab_item, null)
            view.tv_tab_item.text = (position+1).toString()
            tab.customView = view
        }.attach()

    }

    private fun setOnClicks() {
        rootView.ib_bookmark.setOnClickListener {

        }
        rootView.ib_share.setOnClickListener { }

        rootView.tv_home.setOnClickListener { }
        rootView.tv_bookmark.setOnClickListener { }
    }

    private fun getNameFromArguments(): String {
        return requireArguments().getString(DATA_NAME, "")
    }

}