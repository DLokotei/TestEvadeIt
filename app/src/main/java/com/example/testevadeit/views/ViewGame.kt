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
import com.example.testevadeit.extensions.collidedWith
import com.example.testevadeit.logic.TouchEventHelper
import com.example.testevadeit.models.Coordinates
import com.example.testevadeit.models.Food
import com.example.testevadeit.models.ObjectSize
import com.example.testevadeit.models.Trap
import java.util.*


class ViewGame(context: Context, attrs: AttributeSet) : View(context, attrs) {

    var onPlayerMoveCallback: ((newPosition: Float) -> Unit)? = null
    var onGameOverCallback: ((scores: Int) -> Unit)? = null
    var onPointsChangeCallback: ((scores: Int) -> Unit)? = null
    var trapsPerScreen = 10
    private val foodPerScreen: Int
        get() = trapsPerScreen + 5
    private var trapsGap = 100f // vertical space between traps
    private var foodsGap = 30f // vertical space between foods
    private var objectWidth = 0f

    private var traps: Array<Trap>? = null
    private var foods: Array<Food>? = null

    private var points = 0
    var gameFieldSize: ObjectSize = ObjectSize(0f, 0f)
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
    private val foodPaint = Paint().apply {
        color = eatableColor
    }

    // on start game
    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        gameFieldSize = ObjectSize(width.toFloat(), height.toFloat())
        trapsGap = gameFieldSize.height / trapsPerScreen
        foodsGap = gameFieldSize.height / foodPerScreen
        playerYPosition = gameFieldSize.height * 0.9f // player always on bottom
        playerPosition = gameFieldSize.width / 2f // player started on middle
        objectWidth = gameFieldSize.width * 0.1f // player is 10% of screen

        traps = getInitialTrapData(trapsPerScreen)
        foods = getInitialFoodsData(foodPerScreen)
    }

    private fun getInitialTrapData(trapsPerScreen: Int): Array<Trap> = Array(trapsPerScreen) {
        Trap(
            Coordinates(
                Trap.SIZE.width / 2 + Random().nextInt((gameFieldSize.width - Trap.SIZE.width).toInt()).toFloat(),
                -trapsGap * it
            ),
            gameFieldSize
        )
    }

    private fun getInitialFoodsData(itemsPrerScreen: Int): Array<Food> = Array(itemsPrerScreen) {
        Food(
            Coordinates(
                Food.SIZE.width / 2 + Random().nextInt((gameFieldSize.width - Food.SIZE.width).toInt()).toFloat(),
                -foodsGap * it
            ),
            gameFieldSize
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return
        canvas.drawColor(backgroundColor)

        drawPlayer(canvas)
        drawFoods(canvas)
        drawTraps(canvas)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        touchEventHelper.getSlideEvent(event, ::onSlide)
        return true
    }

    private fun onSlide(distance: Float) {
        val newPlayerPosition = playerPosition + distance
        Log.d("tagtag", "newPlayerPosition = $newPlayerPosition")
        if (newPlayerPosition - objectWidth < 0 || newPlayerPosition + objectWidth > gameFieldSize.width) return
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
        traps?.forEach { trap ->
            val rect = trap.getRectangle()
            if (rect.collidedWith(playerRect)) {
                onGameOverCallback?.invoke(points)
            }
            canvas.drawRect(rect, trapPaint)
        }
    }

    private fun drawFoods(canvas: Canvas) {
        foods?.forEach { food ->
            val rect = food.getRectangle()
            if (rect.collidedWith(playerRect)) {
                increasePoints()
                food.onCollideWithPlayer()
            }
            canvas.drawRect(rect, foodPaint)
        }
    }

    private fun increasePoints() {
        points++
        onPointsChangeCallback?.invoke(points)
    }

    fun nextFrame() {
        traps?.forEach {
            it.move()
        }
        foods?.forEach {
            it.move()
        }
        invalidate()
    }

}