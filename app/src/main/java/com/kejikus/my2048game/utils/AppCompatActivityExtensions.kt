package com.kejikus.my2048game.utils

import android.os.Build
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.viewbinding.ViewBinding

fun AppCompatActivity.makeImmersiveFullscreen(binding: ViewBinding) {
    this.supportActionBar?.hide()
    val insetsController = WindowInsetsControllerCompat(window, binding.root)

    // Hide status bars and make them appear only by swipe
    insetsController.hide(WindowInsetsCompat.Type.systemBars())
    insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
        window.attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
}