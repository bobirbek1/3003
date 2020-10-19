package com.idrok.a3003.data.pdfHelper;

import android.content.Context;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
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
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DocDopol {
//    private static final String TAG = "myLogs";

    private Context context;
    private PDFView pdfView;
    private InputStream inPDF;
    private File nowPdfFile;
    private SearchState searchState;
    private ExecutorService pool;
    private int countPdfPage;
    private ArrayList<PdfOnlyPage> list;


    private PdfNavigation pdfNavigation;

    public static DocDopol getInstance(Context context, PDFView pdfView, String pdfName) {
        InputStream in = null;
        try {
            in = context.getAssets().open(pdfName);
        } catch (IOException e) {
            Toast.makeText(context, "No pdf found", Toast.LENGTH_SHORT).show();
        }
        return new DocDopol(context, pdfView, in);
    }

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
            e.printStackTrace();
        }
        setSearchState(SearchState.None);
    }

    public void Destroy() {
        if (inPDF != null) {
            stop();
            list.clear();
            try {
                inPDF.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            pdfNavigation.Destroy();
        }
    }

    public void stop() {

        if (getSearchState() == SearchState.SearchStart) {
            pool.shutdownNow();
        }
        setSearchState(SearchState.SearchCanceled);
    }

    public void reset() {
        try {
            if (inPDF != null)
                this.inPDF.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void open() {
//        int page = pdfNavigation.getPosition().getIndexArrayMap();
//        pdfView.fromStream(inPDF).defaultPage(page).load();
        if (inPDF != null)
            pdfView.fromStream(inPDF).load();
    }

    public void reopen() {
        int page = pdfNavigation.getPosition().getIndexArrayMap();
        pdfView.fromFile(nowPdfFile).defaultPage(page).load();
        reset();
    }

    public boolean search(String value) {


        setSearchState(SearchState.SearchStart);

        pool = Executors.newFixedThreadPool(10);

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
        boolean result = false;
        try {
            result = firstCollection();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void initialization() throws IOException {

        PdfReader reader = new PdfReader(inPDF);

        PdfDocument pdfDocument = new PdfDocument(reader);

        countPdfPage = pdfDocument.getNumberOfPages();

        pdfNavigation = new PdfNavigation();

        list = new ArrayList<>();

        for (int i = 0; i < countPdfPage; i++) {
            list.add(new PdfOnlyPage(context, pdfDocument, i));
        }

        pdfDocument.close();
        reader.close();

    }

    private Runnable getRunnable(final PdfOnlyPage pdfOnlyPage, final String value, final int index) {
        return () -> {

            File filePdf = pdfOnlyPage.getCopyPdfDocument();
            File file = pdfOnlyPage.getDrawPdfDocument();

            PdfDocument pdfDocument;
            try {

                PdfReader reader = new PdfReader(filePdf);
                PdfWriter writer = new PdfWriter(file);

                pdfDocument = new PdfDocument(reader, writer);

                RegexBasedLocationExtractionStrategy strategy = new RegexBasedLocationExtractionStrategy(value);

                PdfCanvasProcessor proc = new PdfCanvasProcessor(strategy);

                PdfPage page = pdfDocument.getFirstPage();

                proc.processPageContent(page);

                proc.reset();

                Collection<IPdfTextLocation> resultCollection = strategy.getResultantLocations();

                ArrayList<IPdfTextLocation> array = new ArrayList<>(resultCollection);

                Collections.reverse(array);

                pdfNavigation.add(index, array);

                pdfDocument.close();
                writer.close();
                reader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    private Runnable getRunnable_nav(final PdfOnlyPage pdfOnlyPage, final int index) {
        return () -> {

            File filePdf = pdfOnlyPage.getCopyPdfDocument();
            File file = pdfOnlyPage.getDrawPdfDocument();

            PdfDocument pdfDocument;
            try {

                PdfReader reader = new PdfReader(filePdf);
                PdfWriter writer = new PdfWriter(file);

                pdfDocument = new PdfDocument(reader, writer);

                PdfPage page = pdfDocument.getFirstPage();

                ArrayList<IPdfTextLocation> array = pdfNavigation.getArrayElement(index);
                IPdfTextLocation result = pdfNavigation.getElement();

                paintRectangle(pdfDocument, page, array, result);

                pdfDocument.close();
                writer.close();
                reader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    private void paintRectangle(PdfDocument pdfDocument, PdfPage page, ArrayList<IPdfTextLocation> arrayList, IPdfTextLocation result) {

        int count = arrayList.size();
        PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDocument);
        DeviceRgb rgb;
        Rectangle rectangle;
        if (count > 0) {
            for (int i = 0; i < count; i++) {

                if (result == arrayList.get(i)) {
                    rgb = new DeviceRgb(12, 148, 99);
                    rectangle = result.getRectangle();
                } else {
                    rgb = new DeviceRgb(235, 200, 75);
                    rectangle = arrayList.get(i).getRectangle();
                }

                pdfCanvas.setFillColor(rgb).setStrokeColor(rgb).rectangle(rectangle).fillStroke();

                Canvas canvas = new Canvas(pdfCanvas, rectangle);

                canvas.close();

            }
        }
        pdfCanvas.release();
    }

    private void setCollection() throws IOException, InterruptedException {
        pool = Executors.newFixedThreadPool(10);

        for (int i = 0; i < countPdfPage; i++) {
            PdfOnlyPage pdfOnlyPage = list.get(i);
            pool.submit(getRunnable_nav(pdfOnlyPage, i));
        }

        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.DAYS);

        nowPdfFile = PdfOnlyPage.getEndFile(context, list);
        reopen();
    }

    private boolean firstCollection() throws IOException, InterruptedException {

        boolean result = false;

        if (getSearchState() != SearchState.SearchCanceled) {

            if (pdfNavigation.first()) {
                setCollection();
                result = true;
            }

            setSearchState(SearchState.SearchEnd);
            return result;
        }

        return result;
    }

//    public boolean lastCollection(){
//
//        return false;
//    }

    public boolean nextCollection() {

        boolean result = false;

        if (getSearchState() != SearchState.SearchCanceled) {

            if (pdfNavigation.next()) {
                try {
                    setCollection();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                result = true;
            }

            setSearchState(SearchState.SearchEnd);
            return result;
        }

        return result;
    }

    public boolean preedCollection() {

        boolean result = false;

        if (getSearchState() != SearchState.SearchCanceled) {

            if (pdfNavigation.preed()) {
                try {
                    setCollection();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                result = true;
            }

            setSearchState(SearchState.SearchEnd);
            return result;
        }

        return result;
    }

    public PdfNavigation getPdfNavigation() {
        return pdfNavigation;
    }

    private static class PdfOnlyPage {

        private File copy_pdf_Document;
        private File draw_pdf_Document;
        private Context context;

        private PdfOnlyPage(Context context, PdfDocument pdfDocument, int index) throws IOException {
            this.context = context;
            getNowPdfFile(pdfDocument, index);
            getNowFile(index);
        }

        private void getNowPdfFile(PdfDocument pdfDocument, int index) throws IOException {
            File nowPdfFile = new File(context.getExternalFilesDir(null), "tempNow" + index + ".pdf");
            PdfWriter writer = new PdfWriter(nowPdfFile);
            PdfDocument newPdf = new PdfDocument(writer);
            pdfDocument.copyPagesTo(index + 1, index + 1, newPdf);
            newPdf.close();
            writer.close();
            setCopy_pdf_Document(nowPdfFile);
        }

        private void getNowFile(int index) {
            File drawfileFile = new File(context.getExternalFilesDir(null), "temp" + index + ".pdf");
            setDraw_pdf_Document(drawfileFile);
        }

        public File getCopyPdfDocument() {
            return copy_pdf_Document;
        }

        public File getDrawPdfDocument() {
            return draw_pdf_Document;
        }

        public void setCopy_pdf_Document(File copy_pdf_Document) {
            this.copy_pdf_Document = copy_pdf_Document;
        }

        public void setDraw_pdf_Document(File draw_pdf_Document) {
            this.draw_pdf_Document = draw_pdf_Document;
        }

        public static File getEndFile(Context context, ArrayList<PdfOnlyPage> list) throws IOException {

            int count = list.size();

            File file = new File(context.getExternalFilesDir(null), "tempNow.pdf");
            PdfWriter writer = new PdfWriter(file);
            PdfDocument newPdf = new PdfDocument(writer);

            for (int i = 0; i < count; i++) {
                File filePDF = list.get(i).getDrawPdfDocument();

                PdfReader reader = new PdfReader(filePDF);
                PdfDocument pdfDocument = new PdfDocument(reader);
                pdfDocument.copyPagesTo(1, 1, newPdf, i + 1);

                reader.close();
                pdfDocument.close();
            }

            newPdf.close();
            writer.close();

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
