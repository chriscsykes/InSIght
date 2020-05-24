package edu.dartmouth.cs.finalproject.activities.audio;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class TextToSpeechEngine {
    private static final String TAG = TextToSpeechEngine.class.getName();
    private TextToSpeech mTextToSpeech;

    public TextToSpeechEngine(Context context){
        mTextToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    mTextToSpeech.setLanguage(Locale.UK);
                    // try to get Local language specific to user
                    Locale defaultLocale = Locale.getDefault();
                    if (mTextToSpeech.isLanguageAvailable(defaultLocale) == TextToSpeech.LANG_AVAILABLE){
                        mTextToSpeech.setLanguage(defaultLocale);
                    }
                }
            }
        });
    }

    /*
     * Speaks the text using the QUEUE_FLUSH as default queue strategy
     * QUEUE_FLASH will cause request to interrupt whatever was currently being synthesized:
     * the queue is flushed and the new utterance is queued, which places it at the head of the queue
     * @params utteranceId: uniquely identifies the text to be spoken
     */
    public void speakText(String text, String utteranceId){
        mTextToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId );
    }

    /*
     * Speaks the text using the specified queue strategy
     * Using TextToSpeech.QUEUE_ADD as queue strategy will cause this utterance to be queued
     * played after any pending utterance has completed
     * @params utteranceId: uniquely identifies the text to be spoken
     */
    public void speakText(String text, String utteranceId, int queueStrategy ){
        mTextToSpeech.speak(text, queueStrategy, null, utteranceId );
    }

    /*
     * Tries to change the language of the textToSpeechEngine
     * if language is supported, else return false
     */
    public boolean changeLanguage(Locale newLanguage){
        if (mTextToSpeech.isLanguageAvailable(newLanguage) == TextToSpeech.LANG_AVAILABLE){
            mTextToSpeech.setLanguage(newLanguage);
            return true;
        }else{
            Log.d(TAG, "changeLanguage: " + newLanguage.getDisplayLanguage() +" is not supported");
            return false;
        }
    }
    /*
     * Shuts down the textToSpeech Engine to release device resources
     * Ideally, called in Activity onDestroy() Lifecycle method
     */
    public void closeTextToSpeechEngine(){
        mTextToSpeech.shutdown();
    }
}
