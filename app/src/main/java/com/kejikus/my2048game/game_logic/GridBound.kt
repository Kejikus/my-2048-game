package com.kejikus.my2048game.game_logic

import com.kejikus.my2048game.utils.Point

data class GridBound<T>(val data: T, val x: Int, val y: Int) {
    fun point(): Point = Point(x, y)
}