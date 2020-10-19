package com.idrok.a3003.data.pdfHelper;
import android.os.Build;
import android.util.ArrayMap;
import androidx.annotation.RequiresApi;
import com.itextpdf.kernel.pdf.canvas.parser.listener.IPdfTextLocation;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class PdfNavigation {

    private ArrayMap<Integer, ArrayList<IPdfTextLocation>> collection;
    private int countArrayMap;
    private int countArrayMapChild;
    private int countAllArrayMapChild;
    private Position position;

    public PdfNavigation() {
        this.collection = new ArrayMap<>();

    }

    public void add(int index,ArrayList<IPdfTextLocation> list){
        collection.put(index, list);
        setCountArrayMap(collection.size());
        setCountArrayMapChild(countArrayMapChild());
        position = new Position(-1,-1);
    }

    public void Destroy(){
        if (collection.size() > 0) {
            collection.clear();
        }
    }

    public IPdfTextLocation getElement(){
        return collection.valueAt(position.indexArrayMap).get(position.indexArrayMapChild);
    }

    public ArrayList<IPdfTextLocation> getArrayElement(){
        return collection.valueAt(position.indexArrayMap);
    }

    public ArrayList<IPdfTextLocation> getArrayElement(int index){
        return collection.valueAt(index);
    }

    public boolean first(){

        int count = getCountArrayMap();

        if (count > 0) {
            for (int i = 0; i < count; i++) {
                int countChild = collection.valueAt(i).size();
                if (countChild > 0) {
                    position.setIndexArrayMap(i);
                    position.setIndexArrayMapChild(0);
                    setCountAllArrayMapChild(1);
                    return true;
                }
            }
        }

        return false;
    }

    public boolean next(){

        boolean isBool = false;

        int index = position.getIndexArrayMap();
        int indexChild = position.getIndexArrayMapChild() + 1;

        int count = getCountArrayMap();

        if (count > index) {
            for (int i = index; i < count; i++) {

                int countChild = collection.valueAt(i).size();

                for (int j = indexChild; j < countChild; j++){
                    position.setIndexArrayMap(i);
                    position.setIndexArrayMapChild(j);
                    setCountAllArrayMapChild(getCountAllArrayMapChild() + 1);
                    isBool = true;
                    break;
                }

                if (!isBool) {
                    position.setIndexArrayMapChild(0);
                    indexChild = 0;
                }else {
                    return isBool;
                }
            }
        }

        return isBool;
    }

    public boolean preed(){

        boolean isBool = false;

        int index = position.getIndexArrayMap();
        int indexChild = position.getIndexArrayMapChild() - 1;

        if (0 <= index) {
            for (int i = index; i >= 0; i--) {

                for (int j = indexChild; j >= 0; j--){
                    position.setIndexArrayMap(i);
                    position.setIndexArrayMapChild(j);
                    setCountAllArrayMapChild(getCountAllArrayMapChild() - 1);
                    isBool = true;
                    break;
                }

                if (!isBool) {
                    if (i > 0) {
                        int countChild = collection.valueAt(i - 1).size();
                        position.setIndexArrayMapChild(countChild - 1);
                        indexChild = countChild - 1;
                    }
                }else {
                    return isBool;
                }
            }
        }

        return isBool;
    }

    private int countArrayMapChild(){
        int count = getCountArrayMap();
        int result = 0;
        if (count > 0){
            for (int i = 0; i < count; i++){
                int countChild = collection.valueAt(i).size();
                if (countChild > 0){
                    for (int j = 0; j < countChild; j++){
                        result++;
                    }
                }
            }
        }
        return  result;
    }

    public Position getPosition(){
        return position;
    }

    public int getCountArrayMap() {
        return countArrayMap;
    }

    public int getCountArrayMapChild() {
        return countArrayMapChild;
    }

    private void setCountArrayMap(int countArrayMap) {
        this.countArrayMap = countArrayMap;
    }

    private void setCountArrayMapChild(int countArrayMapChild) {
        this.countArrayMapChild = countArrayMapChild;
    }

    public int getCountAllArrayMapChild() {
        return countAllArrayMapChild;
    }

    private void setCountAllArrayMapChild(int countAllArrayMapChild) {
        this.countAllArrayMapChild = countAllArrayMapChild;
    }

    public class Position{
        private int indexArrayMap;
        private int indexArrayMapChild;

        public Position(int indexArrayMap, int indexArrayMapChild) {
            this.indexArrayMap = indexArrayMap;
            this.indexArrayMapChild = indexArrayMapChild;
        }

        public int getIndexArrayMap() {
            return indexArrayMap;
        }

        public void setIndexArrayMap(int indexArrayMap) {
            this.indexArrayMap = indexArrayMap;
        }

        public int getIndexArrayMapChild() {
            return indexArrayMapChild;
        }

        public void setIndexArrayMapChild(int indexArrayMapChild) {
            this.indexArrayMapChild = indexArrayMapChild;
        }
    }

}

