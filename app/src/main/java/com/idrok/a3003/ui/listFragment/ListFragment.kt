package com.idrok.a3003.ui.listFragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.idrok.a3003.R
import com.idrok.a3003.data.GetData
import com.idrok.a3003.ui.listFragment.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_list.view.*

class ListFragment : Fragment(R.layout.fragment_list) {
    private lateinit var rootView: View
    private lateinit var getData: GetData

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView = view
        getData = GetData(requireContext())
        setViews()

    }

    private fun setViews() {
        val type = requireArguments().getInt("type", 0)
        if (type != 0) {
            when (type) {
                1 -> {
                    rootView.tab_layout.visibility = View.VISIBLE
                    setViewPager(getListActivesTab(), type)
                }
                2 -> {
                    rootView.tab_layout.visibility = View.VISIBLE
                    setViewPager(getListPassivesTab(), type)
                }
                3 -> {
                    rootView.tab_layout.visibility = View.GONE
                    setViewPager(arrayListOf("noTitle"), type)
                }
                4 -> {
                    rootView.tab_layout.visibility = View.GONE
                    setViewPager(arrayListOf("noTitle"), type)
                }
            }
        }
    }

    //TabLayoutni titlelarini olib olamiz
    private fun getListActivesTab(): ArrayList<String> = arrayListOf(
        requireContext().getString(R.string.activeSegment1),
        requireContext().getString(R.string.activeSegment2)
    )


    private fun getListPassivesTab() = arrayListOf(
        requireContext().getString(R.string.passiveSegment1),
        requireContext().getString(R.string.passiveSegment2)
    )


    //ViewPagerni set qilib olamiz
    private fun setViewPager(listTitles: ArrayList<String>, type: Int) {
        val viewPagerAdapter = ViewPagerAdapter(listTitles, this,type)
        val tabLayout = rootView.tab_layout
        val viewPager = rootView.vp_list
        viewPager.adapter = viewPagerAdapter
        Log.d("setViewPager", "2:$listTitles")
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = listTitles[position]
        }.attach()
    }
}
