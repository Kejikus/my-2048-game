package com.kejikus.my2048game.game_logic

import android.content.SharedPreferences

class SettingsManager(preferences: SharedPreferences) {
    private var sharedPreferences: SharedPreferences = preferences

    var settings: GameSettings = GameSettings(4)

    fun save() {
        sharedPreferences.edit().apply {
            putInt("grid_size", settings.gridSize)
            apply()
        }
    }

    fun load() {
        sharedPreferences.apply {
            settings.gridSize = getInt("grid_size", 4)
        }
    }
}