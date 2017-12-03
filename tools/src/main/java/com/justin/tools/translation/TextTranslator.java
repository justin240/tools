package com.justin.tools.translation;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

public class TextTranslator {

	//private static String MY_PRIVATE_API_KEY = "AIzaSyD9Tdf4h70boLyFotr09M4S4eO6AaQb77s";
	public static void main(String[] args) {
		
		// Instantiates a client
	    Translate translate = TranslateOptions.getDefaultInstance().getService();

	    // The text to translate
	    String text = "Hello, world!";

	    // Translates some text into Russian
	    Translation translation =
	        translate.translate(
	            text,
	            TranslateOption.sourceLanguage("en"),
	            TranslateOption.targetLanguage("ru"));
	    

	    System.out.printf("Text: %s%n", text);
	    System.out.printf("Translation: %s%n", translation.getTranslatedText());

	}

}
