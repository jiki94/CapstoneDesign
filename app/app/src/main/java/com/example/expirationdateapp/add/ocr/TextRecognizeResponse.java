package com.example.expirationdateapp.add.ocr;

import java.util.List;

class TextRecognizeResponse {
    TextRecognizeResult result;
}

class TextRecognizeResult {
    List<String> recognition_words;
}