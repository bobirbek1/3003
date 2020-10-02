package com.idrok.a3003.ui.listChildFragment.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.idrok.a3003.ui.dataChild.DataChildFragment

class ViewPagerAdapter(fragment: Fragment, private val list: ArrayList<String>) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = list.size

    override fun createFragment(position: Int): Fragment {
        val fragment = DataChildFragment()

        return fragment
    }

}