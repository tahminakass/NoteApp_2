package com.example.noteapp.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.noteapp.R
import com.example.noteapp.databinding.ActivityMainBinding
import com.example.noteapp.utils.PreferenceHelper

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var sharedPreferenceHelper: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container)as NavHostFragment
        navController= navHostFragment.navController

        sharedPreferenceHelper = PreferenceHelper(this@MainActivity)

        if (!sharedPreferenceHelper.isBoardingComplete()) {
            sharedPreferenceHelper.setOnBoardingComplete(true)
        } else {
            navController.navigate(R.id.noteFragment)
        }
    }
}