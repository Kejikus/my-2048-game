package com.kejikus.my2048game.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import androidx.core.view.children
import androidx.lifecycle.Observer
import com.kejikus.my2048game.GameViewModel
import com.kejikus.my2048game.game_logic.Grid
import com.kejikus.my2048game.game_logic.Tile
import com.kejikus.my2048game.utils.OnEndAnimationListener
import com.kejikus.my2048game.utils.Point
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

    private var modelObserver: Observer<Grid<Tile>>
        = Observer { processModelData() }
    var model: GameViewModel? = null
        set(value) {
            field?.gridState?.removeObserver(modelObserver)
            field = value
            field?.gridState?.observeForever(modelObserver)
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

    private class AnimationFactory(val view: GameGridView, val gridState: Grid<Tile>,
                                   val interpolator: Interpolator, val duration: Long)
    {
        //region Helpers
        private val paddedWidth
            get() = view.width - view.paddingLeft - view.paddingRight

        private val paddedHeight
            get() = view.height - view.paddingTop - view.paddingBottom

        private val Grid<Tile>.colWidth: Int
            get() = (paddedWidth - view.tilePadding * (this.size * 2)) / this.size

        private val Grid<Tile>.rowHeight: Int
            get() = (paddedHeight - view.tilePadding * (this.size * 2)) / this.size

        private fun getTileX(col: Int): Float
            = (col * (colWidth + view.tilePadding * 2) + view.paddingLeft).toFloat()

        private fun getTileY(row: Int): Float
            = (row * (rowHeight + view.tilePadding * 2) + view.paddingTop).toFloat()

        private fun getRelTileX(col: Int): Float
            = (getTileX(col)) / view.width

        private fun getRelTileY(row: Int): Float
            = (getTileY(row)) / view.height
        //endregion

        val animType = TranslateAnimation.RELATIVE_TO_PARENT
        val colWidth = gridState.colWidth
        val rowHeight = gridState.rowHeight

        fun translate(delta: Point): TranslateAnimation {
            val anim = TranslateAnimation(
                animType, 0f,
                animType, getRelTileX(delta.x),
                animType, 0f,
                animType, getRelTileY(delta.y)
            )
            anim.interpolator = interpolator
            anim.duration = duration
            return anim
        }

        fun fadeIn(): AlphaAnimation {
            val anim = AlphaAnimation(0f, 1f)
            anim.interpolator = interpolator
            anim.duration = duration
            return anim
        }

        fun fadeOut(listener: Animation.AnimationListener? = null): AlphaAnimation {
            val anim = AlphaAnimation(1f, 0f)
            anim.interpolator = interpolator
            anim.duration = duration
            if (listener != null)
                anim.setAnimationListener(listener)
            return anim
        }

        fun makeSet(vararg animations: Animation): AnimationSet {
            val animSet = AnimationSet(true)
            for (anim in animations)
                animSet.addAnimation(anim)
            animSet.interpolator = interpolator
            animSet.duration = duration
            return animSet
        }

    }

    private fun processModelData() {
        val gridState = model?.gridState?.value ?: return

        val tilesList = gridState.iterator().asSequence().toMutableList()
        val tileViewsList: List<TileView> = children.toList().filterIsInstance<TileView>()

        val interpolator = DecelerateInterpolator()
        val animFactory = AnimationFactory(this, gridState, interpolator, 300)

        outer@for (tileView in tileViewsList) {
            val bind = tileView.dataBinding ?: break

            val translateFrom = bind.point()

            // If tile was merged - translate it into this tile and fade out
            val mergedInto = bind.data.mergedInto
            if (mergedInto != null) {
                val translateTo = mergedInto.getCurrentPosition()!!.point()

                tileView.startAnimation(animFactory.run {
                    makeSet(
                        translate(translateTo - translateFrom),
                        fadeOut()
                    ).apply {
                        setAnimationListener(
                            OnEndAnimationListener {
                                this@GameGridView.removeCache.addElement(tileView)
                                this@GameGridView.requestLayout()
                            }
                        )
                    }
                })

                continue
            }

            // If tile was moved - move it too
            for (tile in tilesList) {
                if (bind.data == tile.data) {
                    val translateTo = tile.point()

                    tileView.dataBinding = tile
                    // If tile is not on the same spot, translate it
                    if (tile.point() != bind.point())
                        tileView.startAnimation(
                            animFactory.translate(translateTo - translateFrom).apply {
                                setAnimationListener(
                                    OnEndAnimationListener {
                                        tileView.forceLayout = true
                                        requestLayout()
                                        tileView.startAnimation(animFactory.translate(Point(0, 0)))
                                    }
                                )
                            }
                        )

                    tilesList.remove(tile)
                    continue@outer
                }
            }

            // Tile is a ghost? :)
            tileView.startAnimation(
                animFactory.fadeOut(OnEndAnimationListener {
                    removeCache.addElement(tileView)
                    requestLayout()
                }))
        }

        // Add new tiles
        for (tile in tilesList) {
            val tileView = TileView(context)
            tileView.dataBinding = tile
            tileView.layoutParams = LayoutParams(width / gridState.size, height / gridState.size)
            addView(tileView)

            tileView.startAnimation(animFactory.fadeIn())
        }

//        removeAllViews()
//
//        for (tile in gridState) {
//            val tileView = TileView(context)
//            tileView.dataBinding = tile
//            tileView.layoutParams = LayoutParams(width / gridState.size, height / gridState.size)
//            addView(tileView)
//        }

        requestLayout()
    }

    private fun layoutChildren(left: Int, top: Int, right: Int, bottom: Int) {
        val gridState = model?.gridState?.value
        if (gridState == null) {
            if (childCount > 0)
                removeAllViews()
            return
        }

        removeViews()

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
                val anim = view.animation
                if (anim == null || anim.hasEnded()) {
                    removeCache.add(view)
                }
                continue
            }

            val x = tileData.x * (colWidth + tilePadding) + paddingLeft
            val y = tileData.y * (rowHeight + tilePadding) + paddingTop
            val widthMeasureSpec = MeasureSpec.makeMeasureSpec(colWidth, MeasureSpec.EXACTLY)
            val heightMeasureSpec = MeasureSpec.makeMeasureSpec(rowHeight, MeasureSpec.EXACTLY)
            view.measure(widthMeasureSpec, heightMeasureSpec)
            val anim = view.animation
            if (view.forceLayout || anim == null || !anim.willChangeTransformationMatrix() || anim.hasEnded()) {
                view.layout(x, y, x + view.measuredWidth, y + view.measuredHeight)
                view.forceLayout = false
            }
        }
        removeViews()
    }
}