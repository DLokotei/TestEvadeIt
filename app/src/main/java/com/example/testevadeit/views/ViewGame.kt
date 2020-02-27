package com.example.testevadeit.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.example.testevadeit.R
import com.example.testevadeit.extensions.checkCollidingWith
import com.example.testevadeit.extensions.collidedWith
import com.example.testevadeit.logic.TouchEventHelper
import com.example.testevadeit.models.*
import java.util.*


class ViewGame(context: Context, attrs: AttributeSet) : View(context, attrs) {

    lateinit var traps: Traps
    lateinit var foods: Foods
    lateinit var player: Player

    var onGameStartCallback: ((gameFieldSize: ObjectSize) -> Unit)? = null
    var onGameOverCallback: ((scores: Int) -> Unit)? = null
    var onPointsChangeCallback: ((scores: Int) -> Unit)? = null

    private val backgroundColor = ResourcesCompat.getColor(resources, R.color.black, null)

    private val touchEventHelper = TouchEventHelper()
    private var points = 0

    // on start game
    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        onGameStartCallback?.invoke(ObjectSize(width.toFloat(), height.toFloat()))
    }

    // on time tick
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return
        canvas.drawColor(backgroundColor)

        traps.move()
        foods.move()

        player.draw(canvas)
        foods.draw(canvas)
        traps.draw(canvas)

        player.checkCollideWith(traps) {
            onGameOverCallback?.invoke(points)
        }
        foods.checkCollideWith(player) {
            increasePoints()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        touchEventHelper.getSlideEvent(event, ::onSlide)
        return true
    }

    private fun onSlide(distance: Float) {
        player.onSlide(distance)
    }

    private fun increasePoints() {
        points++
        onPointsChangeCallback?.invoke(points)
    }

    fun nextFrame() {
        if (!::player.isInitialized) return
        invalidate()
    }

}