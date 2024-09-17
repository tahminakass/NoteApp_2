package com.example.noteapp.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceHelper(context: Context) {

    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("shared", Context.MODE_PRIVATE)

    fun unit(context: Context) {
        sharedPreferences = context.getSharedPreferences("shared", Context.MODE_PRIVATE)
    }

    fun setOnBoardingComplete(isComplete: Boolean) {
        sharedPreferences.edit().putBoolean(SHOWED, isComplete).apply()
    }

    fun isBoardingComplete(): Boolean {
        return sharedPreferences.getBoolean(SHOWED, false)
    }

    companion object {
        const val SHOWED = "SHOWED"
    }
}
