package com.sarfa.mathengineservice.core.extensions

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager

fun View.hideKeyboard() {
    try {
        val imm: InputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    } catch (e: Exception) {
        Log.e("hideKeyboard", e.message.toString())
    }
}