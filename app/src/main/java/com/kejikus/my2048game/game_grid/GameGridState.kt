package com.kejikus.my2048game.game_grid

data class GameGridState(val size: Int) {
    private var gridState: Array<Array<Tile?>> = Array(size) { Array(size) { null } }
}

data class Tile(var value: Int, var stack: Int = 0) {
    fun addTile(tile: Tile, maxStack: Int = 0) {
        when {
            stack >= maxStack -> {
                value += 1
                stack = 0
            }
            else -> stack += 1
        }
    }
}
