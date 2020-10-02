package com.idrok.a3003.ui.listFragment

import android.app.SearchManager
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.idrok.a3003.R
import com.idrok.a3003.data.GetData
import com.idrok.a3003.ui.listFragment.adapter.ViewPagerChildAdapter
import kotlinx.android.synthetic.main.fragment_viewpager_child.view.*


const val DATA_NAME = "data_name"

class ViewPagerChildFragment : Fragment(R.layout.fragment_viewpager_child) {

    private lateinit var rootView: View
    private lateinit var getData: GetData
    private lateinit var adapter: ViewPagerChildAdapter
    private lateinit var searchView: SearchView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        rootView = view
        setRv()
        getData = GetData(requireContext())
        getArgs()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val searchManager =
            requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.action_search)
            .actionView as SearchView
        searchView.setSearchableInfo(
            searchManager
                .getSearchableInfo(requireActivity().componentName)
        )
        searchView.maxWidth = Int.MAX_VALUE

        cusomizeSearchView()

        searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("ViewPagerChildFragment", "onTextSubmut: $query")
                adapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                Log.d("ViewPagerChildFragment", "onTextChange: $query")
                adapter.filter.filter(query)
                return false
            }
        })

    }

    private fun cusomizeSearchView() {
        val searchButton = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_button)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//            searchButton.imageTintList =
//                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.colorWhite))
        searchButton.setColorFilter(Color.WHITE)
        val editText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        editText.setTextColor(Color.BLACK)

        val searchPlate = searchView.findViewById<View>(androidx.appcompat.R.id.search_plate)
        searchPlate.setBackgroundResource(R.drawable.search_bg_rounded)

        val closeButton =
            searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        closeButton.setColorFilter(
            ContextCompat.getColor(requireContext(), R.color.colorPrimary),
            PorterDuff.Mode.SRC_IN
        )


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
        adapter = ViewPagerChildAdapter() { name ->
            // RecyclerView itemi bosilganda ListChilds'ni ListChildFragmentga yuboramiz
            val bundle = Bundle()
            bundle.putString(DATA_NAME, name)
            findNavController().navigate(R.id.listChildFragment, bundle)
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