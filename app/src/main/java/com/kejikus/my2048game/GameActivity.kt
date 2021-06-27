package com.kejikus.my2048game

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.kejikus.my2048game.databinding.ActivityGameBinding
import com.kejikus.my2048game.game_logic.GameController
import com.kejikus.my2048game.utils.makeImmersiveFullscreen
import com.kejikus.my2048game.views.GameGridView


class GameActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProvider(this).get(GameViewModel::class.java) }
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityGameBinding

    private lateinit var gameController: GameController

    private lateinit var gameGrid: GameGridView

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("GameSettings", Context.MODE_PRIVATE)
        gameController = GameController(this, viewModel, sharedPreferences)

        binding = ActivityGameBinding.inflate(layoutInflater)
        gameGrid = binding.gameGrid

        setContentView(binding.root)

        makeImmersiveFullscreen(binding)

        binding.menuButton.setOnClickListener {
            this.finish()
        }
    }

    override fun onResume() {
        super.onResume()
        renderTiles()
    }

    private fun renderTiles() {
        if (gameGrid.model != viewModel)
            gameGrid.model = viewModel
        else
            gameGrid.requestLayout()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gameController.gestureDetector.onTouchEvent(event)
    }
}