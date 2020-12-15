package com.handcontrol.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.handcontrol.R
import com.handcontrol.api.Api
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class Navigation : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val recognizer = CommandRecognizer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_telemetry,
                R.id.navigation_gestures,
                R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        recognizer.setUp(findViewById(R.id.speech_recognition))
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.default_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager
                    .PERMISSION_GRANTED
            ) {
                recognizer.onPermissionsGranted()
            } else {
                Toast.makeText(
                    this@Navigation, getString(R.string.permission_denied),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    inner class CommandRecognizer {
        internal lateinit var speech: SpeechRecognizer
        internal var logTag = "VoiceRecognitionActivity"

        @SuppressLint("ClickableViewAccessibility")
        fun setUp(button: View) {
            speech = SpeechRecognizer.createSpeechRecognizer(this@Navigation)

            button.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        ActivityCompat.requestPermissions(
                            this@Navigation,
                            arrayOf(Manifest.permission.RECORD_AUDIO),
                            PERMISSION
                        )
                    }
                    MotionEvent.ACTION_UP -> {
                        speech.stopListening()
                    }
                }
                true
            }
        }

        private fun getErrorText(error: Int): String {
            return when (error) {
                SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
                SpeechRecognizer.ERROR_CLIENT -> "Client side error"
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
                SpeechRecognizer.ERROR_NETWORK -> "Network error"
                SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
                SpeechRecognizer.ERROR_NO_MATCH -> "No match"
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RecognitionService busy"
                SpeechRecognizer.ERROR_SERVER -> "error from server"
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
                else -> "Didn't understand, please try again."
            }
        }

        internal fun onPermissionsGranted() {
            val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            recognizerIntent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
            recognizerIntent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                recognitionLocale
            )
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, recognitionLocale)
            speech.setRecognitionListener(getListener())
            speech.startListening(recognizerIntent)
        }

        private fun getListener() = object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
            }

            override fun onBeginningOfSpeech() {
            }

            override fun onRmsChanged(rmsdB: Float) {
                // if rmsDb is within -10 .. 10, then move it to positive numbers and stretch to 100
//            progressBar.progress = ((rmsdB + 10) / 20 * 100).toInt()
            }

            override fun onBufferReceived(buffer: ByteArray?) {
            }

            override fun onEndOfSpeech() {
            }

            override fun onError(error: Int) {
                if (error == SpeechRecognizer.ERROR_NO_MATCH || error == SpeechRecognizer.ERROR_CLIENT)
                    return // just ignore
                val errorMessage: String = getErrorText(error)
                Log.d(logTag, "FAILED $errorMessage")
                Toast.makeText(
                    this@Navigation,
                    "${getString(R.string.error)}: $errorMessage",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResults(results: Bundle?) {
                val commandText =
                    results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()
                val apiHandler = Api.getApiHandler()

                val gesture = commandText?.let {
                    runBlocking {
                        val gestures = apiHandler.getGestures()
                        when (recognitionLocale) { // find command according to language
                            RUSSIAN_LOCALE_TEXT -> gestures.find { it.name == commandText }
                            else -> gestures.find { it.englishName == commandText }
                        }
                    }
                }

                if (gesture == null) {
                    Toast.makeText(
                        this@Navigation,
                        getString(R.string.unknown_command, commandText),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@Navigation,
                        getString(R.string.command_recognised, commandText),
                        Toast.LENGTH_SHORT
                    ).show()
                    GlobalScope.launch {
                        apiHandler.performGesture(gesture)
                    }
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {
            }

            override fun onEvent(eventType: Int, params: Bundle?) {
            }

        }
    }

    companion object {
        internal const val RUSSIAN_LOCALE_TEXT = "RU-ru"
        internal const val ENGLISH_LOCALE_TEXT = "US-en"
        private const val PERMISSION = 100
        internal var recognitionLocale = RUSSIAN_LOCALE_TEXT
    }
}
