package com.kejikus.my2048game.game_logic

data class Grid<T>(val size: Int): Iterable<GridBound<T>> {
    private var grid: Array<Array<Any?>> = Array(size) { Array(size) { null } }

    val cellCount = size * size

    operator fun get(x: Int, y: Int): T? = grid[y][x] as T?
    operator fun set(x: Int, y: Int, item: T?) { grid[y][x] = item }
    override operator fun iterator(): GridIterator<T> = GridIterator(this)

    fun move(item: GridBound<T>, x: Int, y: Int) {
        if (this[x, y] === item.data)
            return
        if (item.data !== this[item.x, item.y])
            throw RuntimeException("Moving tile not existing in grid.")
        if (this[x, y] != null)
            throw RuntimeException("Moving to an occupied position in grid.")

        this[x, y] = this[item.x, item.y]
        this[item.x, item.y] = null
    }

    fun moveRelative(item: GridBound<T>, xRel: Int = 0, yRel: Int = 0) {
        if (xRel == 0 && yRel == 0) return

        move(item, item.x + xRel, item.y + yRel)
    }
}