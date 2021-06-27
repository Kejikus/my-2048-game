package com.kejikus.my2048game.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import com.kejikus.my2048game.GameViewModel
import com.kejikus.my2048game.utils.Point
import com.kejikus.my2048game.utils.TransitionVector
import java.util.*

const val ATTR_TILE_PADDING = "tile_padding"

class GameGridView : ViewGroup {

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0, defStyleRes: Int = 0)
            : super(context, attrs, defStyle, defStyleRes) {
        tilePadding = attrs?.getAttributeIntValue("app", ATTR_TILE_PADDING, 0) ?: 0
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
    }

    private val tilePadding: Int
    private val removeCache: Vector<View> = Vector(16)
    private fun removeViews() {
        for (view in removeCache)
            removeView(view)
        removeCache.clear()
    }

    var model: GameViewModel? = null
        set(value) {
            field = value
            field?.gridState?.observeForever {
                processModelData()
                requestLayout()
            }
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        layoutChildren(left, top, right, bottom)
    }

    fun layoutChildren() {
        layoutChildren(left, top, right, bottom)
    }

    private fun processModelData() {
        val gridState = model?.gridState?.value ?: return

        val tilesList = gridState.iterator().asSequence().toMutableList()
        val tileViewsList: List<TileView> = children.toList().filterIsInstance<TileView>()

        outer@for (tileView in tileViewsList) {
            val bind = tileView.dataBinding ?: break

            tileView.transitionStart = bind.point()

            val mergedInto = bind.data.mergedInto
            if (mergedInto != null) {
                tileView.fadeOut = true
                tileView.transitionEnd = mergedInto.getCurrentPosition()?.point()
                tileView.dataBinding = null
                continue
            }

            for (tile in tilesList) {
                if (bind.data == tile.data) {
                    tileView.dataBinding = tile
                    tilesList.remove(tile)
                    continue@outer
                }
            }

            tileView.fadeOut = true
            tileView.dataBinding = null
        }

        // Add new tiles
        for (tile in tilesList) {
            val tileView = TileView(context)
            tileView.dataBinding = tile
            tileView.layoutParams = LayoutParams(width / gridState.size, height / gridState.size)
            addView(tileView)
        }

        // TODO: Layout animation implementation

        removeAllViews()

        for (tile in gridState) {
            val tileView = TileView(context)
            tileView.dataBinding = tile
            tileView.layoutParams = LayoutParams(width / gridState.size, height / gridState.size)
            addView(tileView)
        }

        requestLayout()
    }

    fun layoutChildren(left: Int, top: Int, right: Int, bottom: Int) {
        val gridState = model?.gridState?.value
        if (gridState == null) {
            if (childCount > 0)
                removeAllViews()
            return
        }

        val width = right - left - paddingLeft - paddingRight
        val height = bottom - top - paddingTop - paddingBottom

        val colWidth = (width - tilePadding * (gridState.size - 1)) / gridState.size
        val rowHeight = (height - tilePadding * (gridState.size - 1)) / gridState.size

        val count = childCount
        for (i in 0 until count) {
            val view = getChildAt(i)
            if (view !is TileView) {
                removeCache.add(view)
                continue
            }

            val tileData = view.dataBinding
            if (tileData == null) {
                removeCache.add(view)
                continue
            }

            if (tileData.data.mergedInto != null) {
                tileData.data.clearMergeCache()
                removeCache.add(view)
                continue
            }

            val x = tileData.x * (colWidth + tilePadding) + paddingLeft
            val y = tileData.y * (rowHeight + tilePadding) + paddingTop
            val widthMeasureSpec = MeasureSpec.makeMeasureSpec(colWidth, MeasureSpec.EXACTLY)
            val heightMeasureSpec = MeasureSpec.makeMeasureSpec(rowHeight, MeasureSpec.EXACTLY)
            view.measure(widthMeasureSpec, heightMeasureSpec)
            view.layout(x, y, x + view.measuredWidth, y + view.measuredHeight)
        }
        removeViews()
    }
}