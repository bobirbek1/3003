package com.idrok.a3003.ui.listFragment

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.idrok.a3003.R
import com.idrok.a3003.data.GetData
import com.idrok.a3003.ui.listFragment.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_list.view.*

class ListDocumentsFragment : Fragment(R.layout.fragment_list) {
    private lateinit var rootView: View
    private lateinit var getData: GetData

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        rootView = view
        (requireActivity() as AppCompatActivity).setSupportActionBar(rootView.list_toolbar)
        getData = GetData(requireContext())
        setViews()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu,menu)
    }


    private fun setViews() {
        val type = requireArguments().getInt("type", 0)
        val toolbar = rootView.list_toolbar
        toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.mainFragment)
        }
        if (type != 0) {
            when (type) {
                1 -> {
                    rootView.tab_layout.visibility = View.VISIBLE
                    rootView.list_toolbar.setTitle(R.string.button1)
                    setViewPager(getListActivesTab(), type)
                }
                2 -> {
                    rootView.tab_layout.visibility = View.VISIBLE
                    rootView.list_toolbar.setTitle(R.string.button2)
                    setViewPager(getListPassivesTab(), type)
                }
                3 -> {
                    rootView.tab_layout.visibility = View.GONE
                    rootView.list_toolbar.setTitle(R.string.button3)
                    setViewPager(arrayListOf("noTitle"), type)
                }
                4 -> {
                    rootView.tab_layout.visibility = View.GONE
                    rootView.list_toolbar.setTitle(R.string.button4)
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
