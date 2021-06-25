package com.kejikus.my2048game.game_logic

class Tile(val grid: Grid<Tile>, var value: Int) {
    fun interface OnChangeCallback {
        fun invoke(tile: Tile)
    }

    var merged: GridBound<Tile>? = null
    var callback: OnChangeCallback? = null
    var mergedInto: Tile? = null

    fun mergeWith(tile: GridBound<Tile>) {
        if (value == tile.data.value) {
            value += 1
            merged = tile
            tile.data.mergedInto = this
            grid[tile.x, tile.y] = null
            callback?.invoke(this)
        }
    }

    fun clearMergeCache() {
        if (merged != null) {
            merged?.data?.mergedInto = null
            merged = null
        }
        if (mergedInto != null) {
            mergedInto?.merged = null
            mergedInto = null
        }
    }
}