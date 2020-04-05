package com.example.expirationdateapp.add.ocr;

import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

class TextExtractionResponse {
    TextExtractionResult result;
}

class TextExtractionResult {
    List<List<List<Integer>>> boxes;
}