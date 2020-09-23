package com.idrok.a3003.ui.listFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import com.google.android.material.tabs.TabLayoutMediator
import com.idrok.a3003.R
import com.idrok.a3003.ui.listFragment.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_list.view.*

class ListFragment : Fragment(R.layout.fragment_list) {
    private lateinit var rootView: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView = view
        val listTitles = getListTitles()
        setViewPager(listTitles)
    }

    //TabLayoutni titlelarini olib olamiz
    private fun getListTitles(): ArrayList<String> {
        return arrayListOf("Tekushiy obyazatelstva","Sobstvenniy kapital")
    }


    //ViewPagerni set qilib olamiz
    private fun setViewPager(listTitles: ArrayList<String>) {
        val viewPagerAdapter = ViewPagerAdapter( listTitles,this)
        val tabLayout = rootView.tab_layout
        val viewPager = rootView.vp_list
        viewPager.adapter = viewPagerAdapter
        Log.d("setViewPager", "2:$listTitles")
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = listTitles[position]
        }.attach()
    }
}
