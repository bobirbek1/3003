package com.idrok.a3003.ui.listFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.idrok.a3003.R

class ListFragmentChild : Fragment(R.layout.fragment_list_child) {

private lateinit var rootView: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView = view
        val hashMap = hashMapOf<String,Any>(
            "dsccd" to arrayListOf<String>("zdcsv","sdesfcsefe")
        )

    }

}