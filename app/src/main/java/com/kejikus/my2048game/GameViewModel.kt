package com.kejikus.my2048game

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kejikus.my2048game.game_logic.Grid
import com.kejikus.my2048game.game_logic.Tile

class GameViewModel : ViewModel() {
    val gridState: MutableLiveData<Grid<Tile>> = MutableLiveData<Grid<Tile>>()
}