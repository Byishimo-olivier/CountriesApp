package com.example.countriesapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.countriesapp.ui.navigation.AppNavigation
import com.example.countriesapp.ui.theme.CountriesAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    companion object {
        private val _isDarkMode = MutableStateFlow(false)
        val isDarkMode = _isDarkMode

        fun toggleTheme(context: Context) {
            val sharedPrefs = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
            val newValue = !_isDarkMode.value
            _isDarkMode.value = newValue
            sharedPrefs.edit().putBoolean("is_dark_mode", newValue).apply()
        }
        
        fun loadTheme(context: Context) {
            val sharedPrefs = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
            _isDarkMode.value = sharedPrefs.getBoolean("is_dark_mode", false)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadTheme(this)
        enableEdgeToEdge()
        setContent {
            val darkMode by isDarkMode.collectAsState()
            CountriesAppTheme(darkTheme = darkMode) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AppNavigation()
                }
            }
        }
    }
}
