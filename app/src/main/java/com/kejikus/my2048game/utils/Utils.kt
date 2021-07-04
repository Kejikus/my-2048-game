package com.kejikus.my2048game.utils

import android.view.View
import android.view.animation.Animation
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

fun interface AnimationActionListener {
    operator fun invoke(animation: Animation?)
}

typealias MAIN_MENU_BINDING = ActivityMainMenuBinding

data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point): Point
        = Point(x + other.x, y + other.y)

    operator fun minus(other: Point): Point
        = Point(x - other.x, y - other.y)
}

class OnEndAnimationListener(val listener: AnimationActionListener): Animation.AnimationListener {
    override fun onAnimationStart(animation: Animation?) {}

    override fun onAnimationEnd(animation: Animation?) = listener(animation)

    override fun onAnimationRepeat(animation: Animation?) {}

}