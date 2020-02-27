package com.example.testevadeit.models

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.annotation.ColorInt
import com.example.testevadeit.extensions.checkCollidingWith
import com.example.testevadeit.traits.AsRectangle
import com.example.testevadeit.traits.Collidable
import com.example.testevadeit.traits.Controlable

class Player(
    val position: Coordinates,
    private val gameFieldSize: ObjectSize,
    @ColorInt private val playerColor: Int
) : Collidable, AsRectangle, Controlable {

    companion object {
        val SIZE = ObjectSize(90f, 10f)
    }

    private val playerPaint = Paint().apply {
        color = playerColor
    }

    override fun getRectangle(): Rect = Rect().apply {
        left = (position.x - SIZE.width / 2).toInt()
        top = (position.y - SIZE.height / 2).toInt()
        right = (position.x + SIZE.width / 2).toInt()
        bottom = (position.y + SIZE.height / 2).toInt()
    }

    override fun onSlide(distance: Float) {
        val newPlayerPosition = position.x + distance
        if (newPlayerPosition - SIZE.width / 2 < 0 || newPlayerPosition + SIZE.width / 2 > gameFieldSize.width) return
        position.x = newPlayerPosition
    }

    override fun checkCollideWith(subject: Collidable, onCollideCallback: (() -> Unit)?) {
        this.checkCollidingWith(subject, onCollideCallback)
    }

    override fun getRects(): List<Rect> = listOf(getRectangle())

    fun draw(canvas: Canvas) {
        canvas.drawRect(getRectangle(), playerPaint)
    }

}