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

    var onGameOverCallback: ((scores: Int) -> Unit)? = null
    var onPointsChangeCallback: ((scores: Int) -> Unit)? = null
    var trapsPerScreen = 10

    private lateinit var traps: Traps
    private lateinit var foods: Foods
    private lateinit var player: Player
    private lateinit var gameFieldSize: ObjectSize
    private val backgroundColor = ResourcesCompat.getColor(resources, R.color.black, null)
    private val playerColor = ResourcesCompat.getColor(resources, R.color.green, null)
    private val trapColor = ResourcesCompat.getColor(resources, R.color.red, null)
    private val eatableColor = ResourcesCompat.getColor(resources, R.color.yellow, null)
    private val touchEventHelper = TouchEventHelper()
    private var points = 0
    private var trapsGap = 0f // vertical space between traps
    private var foodsGap = 0f // vertical space between foods
    private val foodPerScreen: Int
        get() = trapsPerScreen + 5

    // on start game
    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        gameFieldSize = ObjectSize(width.toFloat(), height.toFloat())
        trapsGap = gameFieldSize.height / trapsPerScreen
        foodsGap = gameFieldSize.height / foodPerScreen
        val playerX = gameFieldSize.width / 2f
        val playerY = gameFieldSize.height * 0.9f

        player = Player(Coordinates(playerX, playerY), gameFieldSize, playerColor)
        traps = Traps(trapColor, gameFieldSize, trapsPerScreen)
        foods = Foods(eatableColor, gameFieldSize, foodPerScreen)
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