package com.example.expirationdateapp.add.ocr;

import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

public class TextExtractionResponse {
    public TextExtractionResult result;

    public List<Rect> getRects(){
        ArrayList<Rect> rects = new ArrayList<Rect>();
        for (List<List<Integer>> box : result.boxes){
            List<Integer> leftTop = box.get(0);
            List<Integer> rightBottom = box.get(2);
            rects.add(new Rect(leftTop.get(0), leftTop.get(1), rightBottom.get(0), rightBottom.get(1)));
        }

        return rects;
    }
}
