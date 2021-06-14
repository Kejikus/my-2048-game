package com.kejikus.my2048game

import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.kejikus.my2048game.databinding.ActivityGameBinding
import com.kejikus.my2048game.utils.makeImmersiveFullscreen


class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)

        setContentView(binding.root)

        makeImmersiveFullscreen(binding)
    }


}