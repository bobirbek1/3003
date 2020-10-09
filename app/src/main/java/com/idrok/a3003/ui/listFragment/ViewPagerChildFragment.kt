package com.idrok.a3003.ui.listFragment


import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.idrok.a3003.R
import com.idrok.a3003.data.GetData
import com.idrok.a3003.ui.customizeSearch.CustomizeSearch
import com.idrok.a3003.ui.listFragment.adapter.ViewPagerChildAdapter
import kotlinx.android.synthetic.main.fragment_viewpager_child.view.*


const val DATA_SECTION = "data_name"

class ViewPagerChildFragment : Fragment(R.layout.fragment_viewpager_child) {

    private lateinit var rootView: View
    private lateinit var getData: GetData
    private lateinit var adapter: ViewPagerChildAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        rootView = view
        setRv()
        getData = GetData(requireContext())
        getArgs()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
       CustomizeSearch(requireActivity()).customizeSearch(menu,searchView){ _, changed ->
           if (changed != null)
           adapter.filter.filter(changed)
       }
    }

    private fun getArgs() {
        val type = requireArguments().getInt("type", 0)
        val segment = requireArguments().getString("segment", "")
        if (type != 0) {
            when (type) {
                1 -> {
                    if (segment.isNotEmpty()) {
                        if (segment == requireContext().getString(R.string.activeSegment1)) {
                            val list = getData.getActiveSchetList1()
                            adapter.setData(list)
                        } else if (segment == requireContext().getString(R.string.activeSegment2)) {
                            val list = getData.getActiveSchetList2()
                            adapter.setData(list)
                        }
                    }
                }
                2 -> {
                    if (segment.isNotEmpty()) {
                        if (segment == requireContext().getString(R.string.passiveSegment1)) {
                            val list = getData.getPassiveSchetList1()
                            adapter.setData(list)
                        } else if (segment == requireContext().getString(R.string.passiveSegment2)) {
                            val list = getData.getPassiveSchetList2()
                            adapter.setData(list)
                        }
                    }
                }
                3 -> {
                    val list = getData.getTranzitniyeSchetList()
                    adapter.setData(list)

                }
                4 -> {
                    val list = getData.getZabalansoviyeSchet()
                    adapter.setData(list)

                }
            }
        }

    }


    private fun setRv() {
        adapter = ViewPagerChildAdapter { section ->
            // RecyclerView itemi bosilganda ListChilds'ni ListChildFragmentga yuboramiz
            val bundle = Bundle()
            bundle.putInt(DATA_SECTION, section)
            findNavController().navigate(R.id.dataFragment, bundle)
        }

        rootView.rv_list.adapter = adapter

        rootView.rv_list.layoutManager?.scrollToPosition(0)
        rootView.rv_list.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
    }

}