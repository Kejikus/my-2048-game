package com.kejikus.my2048game.utils

import com.kejikus.my2048game.databinding.ActivityMainMenuBinding

enum class Direction {
    Up, Down, Left, Right
}

fun interface PointCallback {
    operator fun invoke(point: Pair<Int, Int>)
}

fun interface SwipeCallback {
    operator fun invoke(direction: Direction)
}

fun interface DoubleClickCallback {
    operator fun invoke()
}

typealias MAIN_MENU_BINDING = ActivityMainMenuBinding