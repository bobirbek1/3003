package com.idrok.a3003.ui.dataFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.idrok.a3003.R
import com.idrok.a3003.data.readFromText.ReadFromText
import com.idrok.a3003.model.DocData
import com.idrok.a3003.model.DocDataChild
import com.idrok.a3003.ui.dataFragment.adapter.DATA_POS
import com.idrok.a3003.ui.dataFragment.adapter.POSITION
import kotlinx.android.synthetic.main.fragment_data_child.view.*


class DataChildFragment : Fragment() {

    private lateinit var rootView: View
    private lateinit var tvMazmuni:TextView
    private lateinit var tvDebet:TextView
    private lateinit var tvKredit:TextView
    private lateinit var tvYth:TextView
    private lateinit var tvTitle:TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_data_child, container, false)
        setViews()
        return rootView
    }

    private fun setViews() {
        tvMazmuni = rootView.tv_data_mazmuni
        tvDebet = rootView.tv_data_debet
        tvKredit = rootView.tv_data_kredit
        tvYth = rootView.tv_data_yth
        tvTitle = rootView.tv_data_title
        getArgs()
    }

    private fun getArgs() {
        val position = requireArguments().getInt(POSITION, 0)
        val dataPos = requireArguments().getInt(DATA_POS, 0)
        val dataChilds = ReadFromText.getDataChild(position)
        val data = ReadFromText.getData(position)
        if (dataChilds.size != 0) {
            setDataWithChild(dataChilds[dataPos],data)
        } else {
            setData(data)
        }

    }

    private fun setDataWithChild(docDataChild: DocDataChild, data: DocData) {
        tvMazmuni.text = docDataChild.title
        tvDebet.text = docDataChild.debet
        tvKredit.text = docDataChild.kredit
        tvYth.text = data.yth
        tvTitle.text = ReadFromText.Title
    }

    private fun setData(data: DocData) {
        tvMazmuni.text = data.title
        tvDebet.text = data.debet
        tvKredit.text = data.kredit
        tvYth.text = data.yth
        tvTitle.text = ReadFromText.Title
    }
}