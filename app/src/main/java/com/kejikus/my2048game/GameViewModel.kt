package com.kejikus.my2048game

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    val gridState: MutableLiveData<GameGridState> by lazy { MutableLiveData<GameGridState>()}
}