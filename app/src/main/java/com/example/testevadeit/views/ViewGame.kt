package com.example.testevadeit.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.example.testevadeit.R
import com.example.testevadeit.extensions.collided
import com.example.testevadeit.logic.TouchEventHelper
import java.util.*


class ViewGame(context: Context, attrs: AttributeSet) : View(context, attrs) {

    var onPlayerMoveCallback: ((newPosition: Float) -> Unit)? = null
    var onGameOverCallback: ((scores: Int) -> Unit)? = null
    var onPointsChangeCallback: ((scores: Int) -> Unit)? = null
    var trapsPerScreen = 5
        set(value) {
            field = value
            trapsData = getInitialTrapData(value)
            eatablesData = getInitialEatablesData()
        }
    private val eatablesPerScreen: Int
        get() = trapsPerScreen + 5
    private var trapsGap = 100f // vertical space between traps
    private var eatablesGap = 30f // vertical space between traps
    private var objectWidth = 0f

    private lateinit var trapsData: Array<Fallable>
    private lateinit var eatablesData: Array<Fallable>

    private var points = 0
    private var localWidth = 0
    private var localHeight = 0f
    private var playerPosition = 0f
    private var playerYPosition = 0f
    private val backgroundColor = ResourcesCompat.getColor(resources, R.color.black, null)
    private val playerColor = ResourcesCompat.getColor(resources, R.color.green, null)
    private val trapColor = ResourcesCompat.getColor(resources, R.color.red, null)
    private val eatableColor = ResourcesCompat.getColor(resources, R.color.yellow, null)
    private val touchEventHelper = TouchEventHelper()
    private val STROKE_WIDTH = 6f // has to be float
    private val playerPaint = Paint().apply {
        color = playerColor
        isAntiAlias = true
        isDither = true
        style = Paint.Style.STROKE
        strokeWidth = STROKE_WIDTH
    }
    private val trapPaint = Paint().apply {
        color = trapColor
        isAntiAlias = true
        isDither = true
        style = Paint.Style.STROKE
        strokeWidth = STROKE_WIDTH
    }
    private val eatablePaint = Paint().apply {
        color = eatableColor
    }

    // on start game
    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        localWidth = width
        localHeight = height.toFloat()
        trapsGap = localHeight / trapsPerScreen
        eatablesGap = localHeight / eatablesPerScreen
        playerYPosition = localHeight * 0.9f // player always on bottom
        playerPosition = localWidth / 2f // player started on middle
        objectWidth = localWidth * 0.1f // player is 10% of screen

        trapsData = getInitialTrapData(trapsPerScreen)
        eatablesData = getInitialEatablesData()
    }

    private fun getInitialTrapData(trapsPerScreen: Int): Array<Fallable> = Array(trapsPerScreen) {
        Fallable(
            Coordinates(kotlin.random.Random.nextFloat(), -trapsGap * it),
            objectWidth.toInt(),
            2
        ) { onGameOverCallback?.invoke(points) }
    }

    private fun getInitialEatablesData(): Array<Fallable> = Array(eatablesPerScreen) { index ->
        Fallable(
            Coordinates(kotlin.random.Random.nextFloat(), -eatablesGap * index),
            objectWidth.toInt() / 2,
            objectWidth.toInt() / 2
        ) {
            increasePoints()
            eatablesData[index].recycleToTop()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return
        canvas.drawColor(backgroundColor)

        drawPlayer(canvas)
        drawTraps(canvas)
        drawEatables(canvas)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        touchEventHelper.getSlideEvent(event, ::onSlide)
        return true
    }

    private fun onSlide(distance: Float) {
        val newPlayerPosition = playerPosition + distance
        Log.d("tagtag", "newPlayerPosition = $newPlayerPosition")
        if (newPlayerPosition - objectWidth < 0 || newPlayerPosition + objectWidth > localWidth) return
        playerPosition = newPlayerPosition
        onPlayerMoveCallback?.invoke(playerPosition)
        invalidate()
    }

    private val playerRect: Rect
        get() = Rect().apply {
            left = (playerPosition - objectWidth).toInt()
            top = (playerYPosition + 1).toInt()
            right = (playerPosition + objectWidth).toInt()
            bottom = (playerYPosition - 1).toInt()
        }

    private fun drawPlayer(canvas: Canvas) {
        canvas.drawRect(playerRect, playerPaint)
    }

    private fun drawTraps(canvas: Canvas) {
        trapsData.forEach { obj ->
            canvas.drawRect(obj.getRect(), trapPaint)
        }
    }

    private fun drawEatables(canvas: Canvas) {
        eatablesData.forEach { obj ->
            canvas.drawRect(obj.getRect(), eatablePaint)
        }
    }

    private fun increasePoints() {
        points++
        onPointsChangeCallback?.invoke(points)
    }

    fun nextFrame() {
        invalidate()
    }

    data class Coordinates(var x: Float, var y: Float)
    inner class Fallable(
        var position: Coordinates,
        private val itemWidth: Int,
        private val itemHeight: Int,
        var onPlayerCollideCallback: (() -> Unit)? = null
    ) {
        fun getRect(): Rect {
            position.y = position.y + 1
            val rect = Rect().apply {
                left = (position.x * localWidth - itemWidth / 2).toInt()
                top = (position.y - itemHeight / 2).toInt()
                right = (position.x * localWidth + itemWidth / 2).toInt()
                bottom = (position.y + itemHeight / 2).toInt()
            }
            if (position.y < 0) return rect
            if (position.y >= localHeight - 10) {
                recycleToTop()
            }
            if (rect.collided(playerRect)) {
                // game over
                onPlayerCollideCallback?.invoke()
            }
            return rect
        }

        fun recycleToTop() {
            position.y = 0f
            // get new random horizontal position
            position.x = Random().nextFloat()
        }
    }

}