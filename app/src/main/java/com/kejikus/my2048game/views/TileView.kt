package com.kejikus.my2048game.views

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.kejikus.my2048game.R
import com.kejikus.my2048game.databinding.TileViewBinding
import com.kejikus.my2048game.game_logic.GridBound
import com.kejikus.my2048game.game_logic.Tile
import kotlin.math.pow
import kotlin.properties.Delegates


class TileView : FrameLayout {

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = TileViewBinding.bind(inflater.inflate(R.layout.tile_view, this, true))
        setBackgroundResource(R.drawable.tile_outline)

        this.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
        Gravity.CENTER)

        valueDisplay = binding.valueDisplay
        valueDisplay.text = "0"
    }

    private var binding: TileViewBinding
    private var valueDisplay: TextView

    var dataBinding: GridBound<Tile>? = null
        set(value) {
            field = value
            updateValue(value?.data)
        }

    private fun updateValue(value: Tile?) {
        if (value != null) {
            valueDisplay.text = 2.0.pow((value.value).toDouble()).toInt().toString()

            val colorArr = resources.obtainTypedArray(R.array.tile_colors)
            if (value.value < colorArr.length()) {
                valueDisplay.setBackgroundColor(colorArr.getColor(value.value, 0))
            } else {
                valueDisplay.setBackgroundColor(colorArr.getColor(colorArr.length() - 1, 0))
            }
            colorArr.recycle()
        } else
            valueDisplay.text = "0"
    }
}