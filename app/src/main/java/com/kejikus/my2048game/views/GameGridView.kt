package com.kejikus.my2048game.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import androidx.core.view.children
import com.kejikus.my2048game.GameViewModel
import com.kejikus.my2048game.game_logic.Grid
import com.kejikus.my2048game.game_logic.Tile
import com.kejikus.my2048game.utils.Point
import com.kejikus.my2048game.utils.TransitionVector
import com.kejikus.my2048game.utils.ViewAnimation
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

    private val transitionAnimations: Vector<ViewAnimation>
        = Vector(16)

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

    private val paddedWidth
        get() = width - paddingLeft - paddingRight

    private val paddedHeight
        get() = height - paddingTop - paddingBottom


    private fun getColWidth(gridState: Grid<Tile>): Int
        = (paddedWidth - tilePadding * (gridState.size - 1)) / gridState.size

    private fun getRowHeight(gridState: Grid<Tile>): Int
        = (paddedHeight - tilePadding * (gridState.size - 1)) / gridState.size


    private fun getTileX(colWidth: Int, col: Int): Int
        = col * (colWidth + tilePadding) + paddingLeft

    private fun getTileY(rowHeight: Int, row: Int): Int
        = row * (rowHeight + tilePadding) + paddingTop


    private fun makeTileTranslateAnimation(
            colWidth: Int, rowHeight: Int,
            tileStart: Point, tileEnd: Point)
        = TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_PARENT, getTileX(colWidth, tileStart.x).toFloat(),
            TranslateAnimation.RELATIVE_TO_PARENT, getTileY(rowHeight, tileStart.y).toFloat(),
            TranslateAnimation.RELATIVE_TO_PARENT, getTileX(colWidth, tileEnd.x).toFloat(),
            TranslateAnimation.RELATIVE_TO_PARENT, getTileY(rowHeight, tileEnd.y).toFloat()
        )

    private val fadeInAnimation
        get() = AlphaAnimation(0f, 1f)

    private val fadeOutAnimation
        get() = AlphaAnimation(1f, 0f)

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

        val colWidth = getColWidth(gridState)
        val rowHeight = getRowHeight(gridState)
        val aType: Int = TranslateAnimation.RELATIVE_TO_PARENT

        outer@for (tileView in tileViewsList) {
            val bind = tileView.dataBinding ?: break

            tileView.transitionStart = bind.point()
            val transitionStart = bind.point()

            val mergedInto = bind.data.mergedInto
            if (mergedInto != null) {
                val transitionEnd = mergedInto.getCurrentPosition()!!.point()
                tileView.dataBinding = null

                val aSet = AnimationSet(true)
                aSet.addAnimation(makeTileTranslateAnimation(
                    colWidth, rowHeight, transitionStart, transitionEnd
                ))
                aSet.addAnimation(fadeOutAnimation)
                aSet.duration = 300
                aSet.interpolator = DecelerateInterpolator()

                tileView.animation = aSet

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
            tileView.fadeIn = true
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

    private fun layoutChildren(left: Int, top: Int, right: Int, bottom: Int) {
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