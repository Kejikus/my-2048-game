package com.kejikus.my2048game

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.kejikus.my2048game.databinding.ActivityMainMenuBinding
import com.kejikus.my2048game.utils.makeImmersiveFullscreen

typealias BINDING = ActivityMainMenuBinding

class MainMenuActivity : AppCompatActivity() {
    private lateinit var binding: BINDING

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = BINDING.inflate(layoutInflater)

        setContentView(binding.root)

        makeImmersiveFullscreen(binding)

        binding.playButton.setOnClickListener {
            intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }
    }
}