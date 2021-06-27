package com.kejikus.my2048game.game_logic

import android.content.Context
import android.content.SharedPreferences
import android.view.GestureDetector
import com.kejikus.my2048game.GameViewModel
import com.kejikus.my2048game.utils.SwipeDirection
import com.kejikus.my2048game.utils.GameGestureListener
import com.kejikus.my2048game.utils.PointCallback
import kotlin.random.Random

class GameController(val context: Context, val viewModel: GameViewModel, sharedPrefs: SharedPreferences) {
    var gridState: Grid<Tile>
    var settingsManager: SettingsManager = SettingsManager(sharedPrefs)
    var gestureDetector: GestureDetector

    init {
        val saved = viewModel.gridState.value
        if (saved == null) {
            gridState = Grid(settingsManager.settings.gridSize)
            viewModel.gridState.value = gridState
            addTileRandom()
        } else {
            gridState = saved
        }

        gestureDetector = GestureDetector(context, GameGestureListener({ transition(it) }))
    }

    private fun checkItems(item: GridBound<Tile>, direction: SwipeDirection,
                           merge: PointCallback, place: PointCallback) {
        val x = item.x
        val y = item.y
        val size = gridState.size

        fun loop(xLoop: Int, yLoop: Int): Boolean {
            val cell = gridState[xLoop, yLoop]
            if (cell != null) {
                if (cell.value == item.data.value && cell.merged == null)
                    merge.invoke(Pair(xLoop, yLoop))
                else when(direction) {
                    SwipeDirection.Left -> place.invoke(Pair(xLoop + 1, yLoop))
                    SwipeDirection.Up -> place.invoke(Pair(xLoop, yLoop + 1))
                    SwipeDirection.Right -> place.invoke(Pair(xLoop - 1, yLoop))
                    SwipeDirection.Down -> place.invoke(Pair(xLoop, yLoop - 1))
                }
                return true
            }
            return false
        }

        when(direction) {
            SwipeDirection.Left ->
                for (xIter in (x - 1) downTo 0)
                    if (loop(xIter, y))
                        break
                    else if (xIter == 0 && gridState[xIter, y] == null)
                        place.invoke(Pair(xIter, y))
            SwipeDirection.Up ->
                for (yIter in (y - 1) downTo 0)
                    if (loop(x, yIter))
                        break
                    else if (yIter == 0 && gridState[x, yIter] == null)
                        place.invoke(Pair(x, yIter))
            SwipeDirection.Right ->
                for (xIter in (x + 1) until size)
                    if (loop(xIter, y))
                        break
                    else if (xIter == (size - 1) && gridState[xIter, y] == null)
                        place.invoke(Pair(xIter, y))
            SwipeDirection.Down ->
                for (yIter in (y + 1) until size)
                    if (loop(x, yIter))
                        break
                    else if (yIter == (size - 1) && gridState[x, yIter] == null)
                        place.invoke(Pair(x, yIter))
        }
    }

    private fun transition(direction: SwipeDirection) {
        for (tile in gridState)
            tile.data.clearMergeCache()

        val tiles = gridState.iterator().asSequence().toList().sortedBy {
            when(direction) {
                SwipeDirection.Left -> it.x
                SwipeDirection.Up -> it.y
                SwipeDirection.Right -> -it.x
                SwipeDirection.Down -> -it.y
            }
        }
        var stateChanged = false

        for (tile in tiles) {
            val merge = PointCallback { point ->
                val x = point.first
                val y = point.second
                gridState[x, y]?.mergeWith(tile)
                stateChanged = true
            }
            val place = PointCallback { point ->
                val x = point.first
                val y = point.second
                gridState.move(tile, x, y)
                if (tile.x != x || tile.y != y)
                    stateChanged = true
            }
            checkItems(tile, direction, merge, place)
        }

        if (stateChanged) {
            addTileRandom()
            viewModel.gridState.value = gridState
        }
    }

    private fun addTileRandom() {
        val freeCells = gridState.cellCount - gridState.count()
        val rndCell = Random.nextInt(freeCells)
        val rndCellVal = Random.nextInt(10)
        var cellCounter = 0
        outer@for (x in 0 until gridState.size)
            for (y in 0 until gridState.size) {
                if (gridState[x, y] == null) {
                    if (cellCounter < rndCell)
                        cellCounter++
                    else {
                        gridState[x, y] = Tile(gridState, if (rndCellVal == 9) 2 else 1)
                        break@outer
                    }
                }
            }
    }
}