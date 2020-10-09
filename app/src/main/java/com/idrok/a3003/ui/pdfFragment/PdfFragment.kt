package com.idrok.a3003.ui.pdfFragment

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.idrok.a3003.R
import com.idrok.a3003.data.DocDopol
import com.idrok.a3003.data.SearchState
import com.idrok.a3003.ui.bookmark.TITLE
import com.idrok.a3003.ui.customizeSearch.CustomizeSearch
import com.idrok.a3003.ui.main.PDFNAME
import kotlinx.android.synthetic.main.fragment_pdf_reader.view.*

class PdfFragment : Fragment(R.layout.fragment_pdf_reader) {

    private lateinit var rootView: View
    private lateinit var docDopol: DocDopol

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView = view
        setHasOptionsMenu(true)
        setViews()
    }

    private fun setViews() {
        (requireActivity() as AppCompatActivity).setSupportActionBar(rootView.pdf_toolbar)
        rootView.pdf_toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        getArgs()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.pdf_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val searchView = menu.findItem(R.id.pdf_search).actionView as SearchView
        CustomizeSearch(requireActivity()).customizeSearch(menu, searchView) { submitted, _ ->
            if (!submitted.isNullOrEmpty()) {
                if (docDopol.searchState == SearchState.SearchStart) {
                    docDopol.stop()
                }
                docDopol.search(submitted)
            }
        }
        searchView.setOnCloseListener {
            docDopol.reset()
            docDopol.open()
            searchView.onActionViewCollapsed()
            return@setOnCloseListener true
        }
    }

    private fun getArgs(): String? {
        val pdfName = requireArguments().getString(PDFNAME, "")
        val title = requireArguments().getString(TITLE, "")
        if (!title.isNullOrEmpty()) {
            rootView.pdf_toolbar.title = title
        } else {
            rootView.pdf_toolbar.title = requireContext().getString(R.string.app_name)
        }
        if (!pdfName.isNullOrEmpty()) {
            setPdfWithSearch(pdfName)
            return pdfName
        } else {
            Toast.makeText(requireContext(), "No PDF found", Toast.LENGTH_SHORT).show()
        }
        return ""
    }

    private fun setPdfWithSearch(pdfName: String) {
        val pdfReader = rootView.pdf_reader
        Log.d("PdfFragment", "setPdf:$pdfReader")
        docDopol = DocDopol.getInstance(requireContext(), pdfReader, pdfName)
        docDopol.open()
    }


}