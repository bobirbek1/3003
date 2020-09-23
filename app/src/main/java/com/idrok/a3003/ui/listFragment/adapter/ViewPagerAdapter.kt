package com.idrok.a3003.ui.listFragment.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.idrok.a3003.ui.listFragment.ListFragmentChild

class ViewPagerAdapter (private val listTitles:ArrayList<String>,fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = listTitles.size

    override fun createFragment(position: Int): Fragment {
        val fragment = ListFragmentChild()
        Log.d("ViewPagerAdapter","fragment open")
        return fragment
    }
}