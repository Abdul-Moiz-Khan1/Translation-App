package com.example.translation_app

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.translation_app.databinding.ActivityMainBinding
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import com.google.mlkit.nl.languageid.LanguageIdentifier
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
//    lateinit var lannn:LanguageIdentifier
    lateinit var return_langauge:String
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        return_langauge = "en"

        binding.button.setOnClickListener {
            val getsentence = findViewById<EditText>(R.id.sentence).text.toString()
            val detected_language = detect_Language(getsentence)
            Log.d("check lan" , detected_language)
            val options = TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.fromLanguageTag(detected_language).toString())
                .setTargetLanguage(TranslateLanguage.ENGLISH)
                .build()
            val englishGermanTranslator = Translation.getClient(options)

            var conditions = DownloadConditions.Builder()
                .requireWifi()
                .build()
            englishGermanTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener {
                    englishGermanTranslator.translate(getsentence)
                        .addOnSuccessListener { translatedText ->
                            binding.translation.text = translatedText
                        }
                        .addOnFailureListener { exception ->
                            Log.e("Error", "smth smth", exception)
                        }
                }
                .addOnFailureListener { exception ->
                    // Model couldn’t be downloaded or other internal error.
                    Log.e("Error", "dwnld error ", exception)
                    // ...
                }
        }
    }

    private fun detect_Language(getsentence: String):String {
        val languageIdentifier = LanguageIdentification.getClient()
        languageIdentifier.identifyLanguage(getsentence)
            .addOnSuccessListener { languageCode ->
                if (languageCode == "und") {
                    Log.i("Error", "Can't identify language.")
                } else {
                    Log.i("Detected", "Language: $languageCode")
                    val languageIdentifier = LanguageIdentification
                        .getClient(
                            LanguageIdentificationOptions.Builder()
                                .setConfidenceThreshold(0.34f)
                                .build()
                        )
                }
                return_langauge = languageCode
            }
            .addOnFailureListener {
                // Model couldn’t be loaded or other internal error.
                // ...
            }
        return return_langauge
    }
}