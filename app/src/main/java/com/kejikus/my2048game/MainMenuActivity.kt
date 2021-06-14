package com.kejikus.my2048game

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.kejikus.my2048game.databinding.ActivityMainMenuBinding
import com.kejikus.my2048game.utils.makeImmersiveFullscreen

class MainMenuActivity : AppCompatActivity() {
    private lateinit var binding: ViewBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainMenuBinding.inflate(layoutInflater)

        setContentView(binding.root)

        makeImmersiveFullscreen(binding)
    }
}