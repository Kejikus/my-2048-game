package com.kejikus.my2048game.utils

import com.kejikus.my2048game.databinding.ActivityMainMenuBinding

enum class SwipeDirection {
    Up, Down, Left, Right
}

fun interface PointCallback {
    operator fun invoke(point: Pair<Int, Int>)
}

fun interface SwipeCallback {
    operator fun invoke(direction: SwipeDirection)
}

fun interface DoubleClickCallback {
    operator fun invoke()
}

typealias MAIN_MENU_BINDING = ActivityMainMenuBinding

data class Point(val x: Int, val y: Int)
data class TransitionVector(val from: Point, val to: Point)