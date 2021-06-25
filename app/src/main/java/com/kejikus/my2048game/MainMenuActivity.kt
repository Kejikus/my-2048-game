package com.kejikus.my2048game

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.kejikus.my2048game.utils.MAIN_MENU_BINDING
import com.kejikus.my2048game.utils.makeImmersiveFullscreen

class MainMenuActivity : AppCompatActivity() {
    private lateinit var binding: MAIN_MENU_BINDING

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = MAIN_MENU_BINDING.inflate(layoutInflater)

        setContentView(binding.root)

        makeImmersiveFullscreen(binding)

        binding.playButton.setOnClickListener {
            intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

        binding.aboutButton.setOnClickListener {
            Snackbar.make(this, binding.root,
                "Sorry, not implemented... yet?\nBtw, I was made by @kejikus.",
                Snackbar.LENGTH_SHORT).show()
        }

        binding.settingsButton.setOnClickListener {
            Snackbar.make(this, binding.root,
                "Sorry, not implemented... yet?",
                Snackbar.LENGTH_SHORT).show()
        }
    }
}