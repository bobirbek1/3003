package com.idrok.a3003.ui.pdfFragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.github.barteksc.pdfviewer.util.FitPolicy
import com.idrok.a3003.R
import kotlinx.android.synthetic.main.fragment_pdf_reader.view.*

class PdfFragment:Fragment(R.layout.fragment_pdf_reader){

    private lateinit var rootView: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView = view
        setPdf()
    }

    private fun setPdf() {
        val pdfReader = rootView.pdf_reader
        Log.d("PdfFragment","setPdf:$pdfReader")
        pdfReader.fromAsset("pdf_ru.pdf")
            .enableSwipe(true)
            .swipeHorizontal(true)
            .enableDoubletap(true)
            .defaultPage(0)
            .enableAnnotationRendering(false)
            .password(null)
            .scrollHandle(null)
            .enableAntialiasing(true)
            .spacing(0)
            .pageFitPolicy(FitPolicy.WIDTH)
            .load()
    }


}