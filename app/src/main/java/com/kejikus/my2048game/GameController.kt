package com.kejikus.my2048game

import android.widget.TextView

class GameController {
    private var helloWorldText: String
    private var clickCount: Int = 0

    constructor(text: String) {
        helloWorldText = text
    }

    fun buttonClick(textView: TextView) {
        clickCount += 1
        val color = 0xFF00FFFF
        textView.setTextColor(color.toInt())
        textView.text = helloWorldText + "Color: ${Integer.toHexString(color.toInt())}"
    }
}