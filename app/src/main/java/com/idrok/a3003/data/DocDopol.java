package com.idrok.a3003.data;

import android.content.Context;
import android.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.IPdfTextLocation;
import com.itextpdf.kernel.pdf.canvas.parser.listener.RegexBasedLocationExtractionStrategy;
import com.itextpdf.layout.Canvas;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DocDopol {
    private static final String TAG = "myLogs";

    private Context context;
    private PDFView pdfView;
    private InputStream inPDF;
    private ArrayMap<Integer, Collection<IPdfTextLocation>> collection;
    private SearchState searchState;
    private ExecutorService pool;
    private int countPdfPage;
    private ArrayList<PdfOnlyPage> list;

    public static DocDopol getInstance(Context context, PDFView pdfView, String pdfName) {
        try {
            InputStream in = context.getAssets().open(pdfName);
            return new DocDopol(context, pdfView, in);
        } catch (Exception e) {
            Log.e("DocDopol", "exception:" + e.getMessage());
        }
        return new DocDopol(context, pdfView, null);
    }

//    public static DocDopol getInstanceZakon(Context context, PDFView pdfView) {
//        InputStream in = context.getResources().openRawResource(R.raw.zakon);
//        return new DocDopol(context, pdfView, in);
//    }

    private DocDopol(Context context, PDFView pdfView, InputStream in) {
        this.context = context;
        this.pdfView = pdfView;
        inPDF = in;

        try {
            if (inPDF != null) {
                initialization();
                inPDF.reset();
            }
        } catch (IOException e) {
            Log.e("DocDopol", "exception:" + e.getMessage());
        }
        setSearchState(SearchState.None);
    }

    public void Destroy() throws IOException {
        if (inPDF != null) {
            stop();
            list.clear();
            inPDF.close();
        }
    }

    public void stop() {
        if (inPDF != null) {
            if (getSearchState() == SearchState.SearchStart) {
                pool.shutdownNow();
            }
            setSearchState(SearchState.SearchCanceled);
            collection.clear();
        }
    }

    public void reset() throws IOException {
        if (inPDF != null)
            this.inPDF.reset();
    }

    public void open() {
        if (inPDF != null) {
            pdfView.fromStream(inPDF)
                    .nightMode(true)
                    .load();
        }
        else
            Toast.makeText(context, "No PDF found", Toast.LENGTH_SHORT).show();
    }

    public void search(String value) throws IOException {

        if (inPDF != null) {
            setSearchState(SearchState.SearchStart);

            pool = Executors.newFixedThreadPool(10);

            Log.d(TAG, "pageCount:" + countPdfPage);
            for (int i = 0; i < countPdfPage; i++) {
                PdfOnlyPage pdfOnlyPage = list.get(i);
                pool.submit(getRunnable(pdfOnlyPage, value, i));
            }

            pool.shutdown();

            try {
                pool.awaitTermination(1, TimeUnit.DAYS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (getSearchState() != SearchState.SearchCanceled) {
                File nowPdfFile = PdfOnlyPage.getEndFile(context, list);
                pdfView.fromFile(nowPdfFile)
                        .nightMode(true)
                        .load();
                inPDF.reset();
            }
            setSearchState(SearchState.SearchEnd);
        }

        /*
        for (int i = 0; i < countPdfPage; i++) {
            Collection<IPdfTextLocation> resultCollection = collection.valueAt(i);
            for (IPdfTextLocation result : resultCollection) {
                Log.d(TAG, "страница : " + String.valueOf(i));
                Log.d(TAG, "text : " + result.getText());
            }
        }
         */

    }

    private void initialization() throws IOException {

        PdfDocument pdfDocument = new PdfDocument(new PdfReader(inPDF));

        countPdfPage = pdfDocument.getNumberOfPages();

        collection = new ArrayMap<>();

        list = new ArrayList<>();

        for (int i = 0; i < countPdfPage; i++) {
            list.add(new PdfOnlyPage(context, pdfDocument, i));
        }

        pdfDocument.close();
    }

    private Runnable getRunnable(final PdfOnlyPage pdfOnlyPage, final String value, final int index) {
        return () -> {

            File filePdf = pdfOnlyPage.getPdfDocument();
            File file = pdfOnlyPage.getFile();

            PdfDocument pdfDocument;
            try {
                pdfDocument = new PdfDocument(new PdfReader(filePdf), new PdfWriter(file));

                RegexBasedLocationExtractionStrategy strategy = new RegexBasedLocationExtractionStrategy(value);

                PdfCanvasProcessor proc = new PdfCanvasProcessor(strategy);

                PdfPage page = pdfDocument.getFirstPage();

                proc.processPageContent(page);

                Collection<IPdfTextLocation> resultCollection = strategy.getResultantLocations();

                collection.put(index, resultCollection);

                for (IPdfTextLocation result : resultCollection) {
                    Log.d(TAG, "result:" + result.getText());
                    PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDocument);

                    Rectangle rectangle = result.getRectangle();

                    DeviceRgb rgb = new DeviceRgb(235, 200, 75);

                    pdfCanvas.setFillColor(rgb).setStrokeColor(rgb).rectangle(rectangle).fillStroke();

                    Canvas canvas = new Canvas(pdfCanvas, rectangle);

                    canvas.close();
                }

                pdfDocument.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    private static class PdfOnlyPage {
        private File pdfDocument;
        private File file;
        private Context context;

        private PdfOnlyPage(Context context, PdfDocument pdfDocument, int index) throws FileNotFoundException {
            this.context = context;
            this.pdfDocument = getNowPdfFile(pdfDocument, index);
            this.file = getNowFile(index);
        }

        private File getNowPdfFile(PdfDocument pdfDocument, int index) throws FileNotFoundException {
            File file = new File(context.getExternalFilesDir(null), "tempNow" + index + ".pdf");
            PdfDocument newPdf = new PdfDocument(new PdfWriter(file));
            pdfDocument.copyPagesTo(index + 1, index + 1, newPdf);
            newPdf.close();
            return file;
        }

        private File getNowFile(int index) {
            return new File(context.getExternalFilesDir(null), "temp" + index + ".pdf");
        }

        public File getPdfDocument() {
            return pdfDocument;
        }

        public File getFile() {
            return file;
        }

        public static File getEndFile(Context context, ArrayList<PdfOnlyPage> list) throws IOException {
            int count = list.size();
            File file = new File(context.getExternalFilesDir(null), "tempNow.pdf");
            PdfDocument newPdf = new PdfDocument(new PdfWriter(file));

            for (int i = 0; i < count; i++) {
                PdfDocument pdfDocument = new PdfDocument(new PdfReader(list.get(i).getFile()));
                pdfDocument.copyPagesTo(1, 1, newPdf, i + 1);
                pdfDocument.close();
            }
            newPdf.close();
            return file;
        }
    }

    public SearchState getSearchState() {
        return searchState;
    }

    public void setSearchState(SearchState searchState) {
        this.searchState = searchState;
    }
}
