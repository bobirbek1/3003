package com.idrok.a3003.ui.listFragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.idrok.a3003.R
import com.idrok.a3003.model.ITEM_TYPE
import com.idrok.a3003.model.ListChilds
import com.idrok.a3003.model.ListItems
import com.idrok.a3003.model.TITLE_TYPE
import com.idrok.a3003.ui.listFragment.adapter.ListChildAdapter
import kotlinx.android.synthetic.main.fragment_viewpager_child.view.*

const val DATA_NAME = "data_name"
class ViewpagerChildFragment : Fragment(R.layout.fragment_viewpager_child) {

    private lateinit var rootView: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView = view
        setRv()
    }

    private fun setRv() {
        val list = getList()
        Log.d("ListFragmentChild", "list:$list")

        val adapter = ListChildAdapter(list) {name ->
            // RecyclerView itemi bosilganda ListChilds'ni ListChildFragmentga yuboramiz
            val bundle = Bundle()
            bundle.putString(DATA_NAME,name)
            findNavController().navigate(R.id.listChildFragment,bundle)
        }

        rootView.rv_list.adapter = adapter
    }

    private fun getList(): ArrayList<ListItems> {
        return arrayListOf(
            ListItems("6000 schet k oplate postavshikam i podryadchikam", TITLE_TYPE),
            ListItems(
                "6010 scheta k oplate postavshikam i podryadchikam", ITEM_TYPE,
                ListChilds(
                    arrayListOf(
                        "001 osnavnie sredstva, poluchenniye po operativnoy arende",
                        "002 tovarno-materialniye sennosti, prinyatiye na otvetstvennoy xraneniye"
                    )
                )
            ),
            ListItems("6020 vekselya vidanniye", ITEM_TYPE)
        )
    }

}