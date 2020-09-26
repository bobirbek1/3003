package com.idrok.a3003.ui.listChildFragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.idrok.a3003.R
import com.idrok.a3003.ui.listFragment.DATA_NAME

class ListChildFragment : Fragment(R.layout.fragment_viewpager_child){

    private lateinit var rootView:View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView = view
        val name = getNameFromArguments()
        if (name.isNotEmpty()){
            setRv()
        } else{
            Toast.makeText(requireContext(), "No data found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setRv() {

    }

    private fun getNameFromArguments(): String {
        return requireArguments().getString(DATA_NAME,"")
    }

}