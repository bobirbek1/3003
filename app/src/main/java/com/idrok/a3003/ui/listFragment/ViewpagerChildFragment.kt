package com.idrok.a3003.ui.listFragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.idrok.a3003.R
import com.idrok.a3003.data.GetData
import com.idrok.a3003.model.ITEM_TYPE
import com.idrok.a3003.model.ListChilds
import com.idrok.a3003.model.ListItems
import com.idrok.a3003.model.TITLE_TYPE
import com.idrok.a3003.ui.listFragment.adapter.ListChildAdapter
import kotlinx.android.synthetic.main.fragment_viewpager_child.view.*

const val DATA_NAME = "data_name"
class ViewpagerChildFragment : Fragment(R.layout.fragment_viewpager_child) {

    private lateinit var rootView: View
    private lateinit var getData: GetData

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView = view

    }

    private fun setRv(list:ArrayList<ListItems>) {
        Log.d("ListFragmentChild", "list:$list")

        val adapter = ListChildAdapter(list) {name ->
            // RecyclerView itemi bosilganda ListChilds'ni ListChildFragmentga yuboramiz
            val bundle = Bundle()
            bundle.putString(DATA_NAME,name)
            findNavController().navigate(R.id.listChildFragment,bundle)
        }

        rootView.rv_list.adapter = adapter
    }

}