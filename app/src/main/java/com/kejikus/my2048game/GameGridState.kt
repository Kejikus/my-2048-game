package com.kejikus.my2048game

data class GameGridState(val size: Int, val maxTileStack: Int): Iterable<BoundTile> {
    private var grid: Array<Array<Tile?>> = Array(size) { Array(size) { null } }

    operator fun get(x: Int, y: Int) = grid[y][x]
    operator fun set(x: Int, y: Int, tile: Tile) { grid[y][x] = tile }
    override operator fun iterator(): GameGridStateIterator = GameGridStateIterator(this)
}

data class Tile(val grid: GameGridState, val value: Int, val stack: Int = 0) {
    fun addTile(tile: Tile): Tile = when {
        value != tile.value -> this
        (stack + tile.stack) >= grid.maxTileStack ->
            Tile(grid, value + 1, (stack + tile.stack) - grid.maxTileStack)
        else -> Tile(grid, value, stack + tile.stack)
    }
}

data class BoundTile(val tile: Tile, val x: Int, val y: Int)

class GameGridStateIterator(private val grid: GameGridState): Iterator<BoundTile> {
    private var x = 0
    private var y = 0

    private var nextX: Int = 0
    private var nextY: Int = 0

    override fun hasNext(): Boolean {
        // Tile was already found and cached
        if (nextY > y || (nextY == y && nextX >= x) && grid[nextX, nextY] !== null)
            return true

        // Find tile iterating over slots (X) in rows (Y)
        for (Y in y until grid.size)
            for (X in x until grid.size)
                if (grid[X, Y] !== null) {
                    nextX = X; nextY = Y
                    return true
                }

        return false
    }

    override fun next(): BoundTile {
        if (!hasNext())
            throw NoSuchElementException("No more tiles to iterate through.")

        // Set x and y to next slot after found tile
        when (nextX) {
            grid.size - 1 -> {
                y = nextY + 1; x = 0
            }
            else -> {
                y = nextY; x = nextX + 1
            }
        }

        return BoundTile(grid[nextX, nextY]!!, nextX, nextY)
    }

}
