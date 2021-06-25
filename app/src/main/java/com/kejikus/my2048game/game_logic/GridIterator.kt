package com.kejikus.my2048game.game_logic

import android.util.Log

class GridIterator<T>(private val grid: Grid<T>): Iterator<GridBound<T>> {
    private var x = 0
    private var y = 0

    private var nextX: Int = 0
    private var nextY: Int = 0

    private var noMore = false

    override fun hasNext(): Boolean {
        if (noMore) return false

        // Item was already found and cached
        val nextVal = nextY * grid.size + nextX
        val curVal = y * grid.size + x
        if ((nextVal >= curVal) && grid[nextX, nextY] != null)
            return true

        // Find tile iterating over slots (X) in rows (Y)
        for (Y in y until grid.size) {
            val xStart = if (Y == y) x else 0
            for (X in xStart until grid.size)
                if (grid[X, Y] != null) {
                    nextX = X
                    nextY = Y
                    return true
                }
        }

        noMore = true
        return false
    }

    override fun next(): GridBound<T> {
        if (!hasNext())
            throw NoSuchElementException("No more tiles to iterate through.")

        // Set x and y to next slot after found tile
        when (nextX) {
            grid.size - 1 -> {
                x = 0
                y = nextY + 1
            }
            else -> {
                x = nextX + 1
                y = nextY
            }
        }

        return GridBound(grid[nextX, nextY]!!, nextX, nextY)
    }

}