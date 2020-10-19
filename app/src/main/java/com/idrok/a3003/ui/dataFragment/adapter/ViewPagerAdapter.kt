package com.idrok.a3003.ui.dataFragment.adapter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.idrok.a3003.ui.dataFragment.DataChildFragment

const val POSITION = "position"
const val DATA_POS = "data_pos"
class ViewPagerAdapter(
    private val childsCount: Int,
    private val pos: Int,
    fragment: Fragment
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = childsCount

    override fun createFragment(position: Int): Fragment {
        val fragment = DataChildFragment()
        Log.d("ViewPagerAdapter", "fragment open")
        val bundle = Bundle()
        bundle.putInt(POSITION,pos)
        bundle.putInt(DATA_POS,position)
        fragment.arguments = bundle
        return fragment
    }
}