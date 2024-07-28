package com.example.translation_app

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.translation_app.databinding.ActivityMainBinding
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var Languages: MutableList<String>
    private lateinit var language_codes:List<String>
    lateinit var return_langauge:String
    private lateinit var detected_language:String
    private  lateinit var sentence:String
    private lateinit var convert_to:String

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initialization()
        set_language_list()
        set_spinners()
        translate("auto_detect")

    }

    private fun set_spinners() {

        val to_languages_list = Languages.subList(1 , Languages.size)
        binding.fromSpinner.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item , Languages)
        binding.toSpinner.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item ,to_languages_list)

        //spinner that selects target language
        binding.toSpinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val pos:Int = p2+1
                convert_to = get_language_code(pos.toString())
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        //spinner that selects detected language
        binding.fromSpinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Log.d("Language from spinner" , Languages.get(p2))
                    if(p2 == 0){
                        translate("auto_detect")
                    }else{
                        translate(p2.toString())
                    }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
    }

    private fun translate(mode:String) {

        binding.translateBtn.setOnClickListener {

            sentence = findViewById<EditText>(R.id.sentence).text.toString()

            if(mode == "auto_detect"){
                detected_language = detect_Language(sentence)
            }else{
                get_language_code(mode)
            }

            Log.d("check lan" , detected_language)
            val target_code = convert_to

            val options = TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.fromLanguageTag(detected_language).toString())
                .setTargetLanguage(TranslateLanguage.fromLanguageTag(target_code).toString())
                .build()
            val englishGermanTranslator = Translation.getClient(options)

            var conditions = DownloadConditions.Builder()
                .requireWifi()
                .build()


            englishGermanTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener {
                    englishGermanTranslator.translate(sentence)
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

    private fun get_language_code(mode: String):String {
        val position:Int = mode.toInt() - 1
        val code = language_codes.get(position)
        Log.d("language codes" , code.toString())
        return code
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

        language_codes = TranslateLanguage.getAllLanguages()

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
        Languages.add(0,"Auto Detect")
    }

    private fun initialization() {
        return_langauge = "en"
        convert_to = "en"

        Languages = ArrayList<String>()
        language_codes = ArrayList<String>()


    }
}