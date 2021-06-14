package com.kejikus.my2048game.game_grid

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameGridViewModel : ViewModel() {
    private var grid: MutableLiveData<GameGridState> = MutableLiveData()
}