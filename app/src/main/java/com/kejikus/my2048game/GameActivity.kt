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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import com.kejikus.my2048game.databinding.ActivityGameBinding
import com.kejikus.my2048game.utils.makeImmersiveFullscreen


class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private val viewModel by lazy { ViewModelProvider(this).get(GameViewModel::class.java) }
    private lateinit var gridState: GameGridState

    private lateinit var gridBinding: ConstraintLayout

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)
        gridBinding = binding.gameGrid

        setContentView(binding.root)

        makeImmersiveFullscreen(binding)
    }

    override fun onResume() {
        super.onResume()
        gridState = viewModel.gridState.value ?: GameGridState(4, 1)
    }

    override fun onPause() {
        super.onPause()
        viewModel.gridState.value = gridState
    }

    private fun renderTiles() {
        for ((tile, x, y) in gridState) {

        }
    }
}