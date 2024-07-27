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
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var Languages: MutableList<String>
    private lateinit var language_codes:List<String>;
    lateinit var return_langauge:String

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        return_langauge = "en"

        set_language_list()

        binding.button.setOnClickListener {
            val getsentence = findViewById<EditText>(R.id.sentence).text.toString()
            val detected_language = detect_Language(getsentence)
            Log.d("check lan" , detected_language)
            val options = TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.THAI)
                .setTargetLanguage(TranslateLanguage.ENGLISH)
                .build()
            language_codes = TranslateLanguage.getAllLanguages()

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

    private fun set_language_list() {
        Languages.add("Afrikaans")
        Languages.add("Arabic")
        Languages.add("Belarusian")
        Languages.add("Bulgarian")
        Languages.add("Bengali")
        Languages.add("Catalan")
        Languages.add("Czech")
        Languages.add("Welsh")
        Languages.add("Danish")
        Languages.add("German")
        Languages.add("Greek")
        Languages.add("English")
        Languages.add("Esperanto")
        Languages.add("Spanish")
        Languages.add("Estonian")
        Languages.add("Persian")
        Languages.add("Finnish")
        Languages.add("French")
        Languages.add("Irish")
        Languages.add("Galician")
        Languages.add("Gujarati")
        Languages.add("Hebrew")
        Languages.add("Hindi")
        Languages.add("Croatian")
        Languages.add("Haitian")
        Languages.add("Hungarian")
        Languages.add("Indonesian")
        Languages.add("Icelandic")
        Languages.add("Italian")
        Languages.add("Japanese")
        Languages.add("Georgian")
        Languages.add("Kannada")
        Languages.add("Korean")
        Languages.add("Lithuanian")
        Languages.add("Latvian")
        Languages.add("Macedonian")
        Languages.add("Marathi")
        Languages.add("Malay")
        Languages.add("Maltese")
        Languages.add("Dutch")
        Languages.add("Norwegian")
        Languages.add("Polish")
        Languages.add("Portuguese")
        Languages.add("Romanian")
        Languages.add("Russian")
        Languages.add("Slovak")
        Languages.add("Slovenian")
        Languages.add("Albanian")
        Languages.add("Swedish")
        Languages.add("Swahili")
        Languages.add("Tamil")
        Languages.add("Telugu")
        Languages.add("Thai")
        Languages.add("Tagalog")
        Languages.add("Turkish")
        Languages.add("Ukrainian")
        Languages.add("Urdu")
        Languages.add("Vietnamese")
        Languages.add("Chinese")
        Languages.sort()
    }
}