package com.kejikus.my2048game.utils

import android.view.GestureDetector
import android.view.MotionEvent

const val MIN_SWIPE_DISTANCE = 100

class GameGestureListener(
val swipeCallback: SwipeCallback,
val doubleClickCallback: DoubleClickCallback? = null)
: GestureDetector.SimpleOnGestureListener() {
    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        if (e1 !== null && e2 !== null) {
            val deltaX = e2.x - e1.x
            val deltaY = e2.y - e1.y
            val deltaXAbs = Math.abs(deltaX)
            val deltaYAbs = Math.abs(deltaY)

            val res: SwipeDirection? = when {
                deltaXAbs >= MIN_SWIPE_DISTANCE && deltaXAbs > deltaYAbs -> when {
                    deltaX > 0 -> SwipeDirection.Right
                    else -> SwipeDirection.Left
                }
                deltaYAbs >= MIN_SWIPE_DISTANCE -> when {
                    deltaY > 0 -> SwipeDirection.Down
                    else -> SwipeDirection.Up
                }
                else -> null
            }
            if (res !== null)
                swipeCallback(res)

            return true
        }
        return super.onFling(e1, e2, velocityX, velocityY)
    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        doubleClickCallback?.invoke()
        return true
    }
}