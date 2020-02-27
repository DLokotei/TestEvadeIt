package com.example.testevadeit.models

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.annotation.ColorInt
import com.example.testevadeit.traits.Collidable
import com.example.testevadeit.traits.Movable

abstract class GameObjects(
    @ColorInt private val objectColor: Int,
    protected val gameFieldSize: ObjectSize,
    objectsPerScreen: Int
) : Collidable, Movable {

    protected abstract val objects: Array<GameObject>
    protected val objectPaint: Paint
    protected val objectsGap = gameFieldSize.height / objectsPerScreen

    private val STROKE_WIDTH = 6f

    init {
        objectPaint = Paint().apply {
            color = objectColor
            isAntiAlias = true
            isDither = true
            style = Paint.Style.STROKE
            strokeWidth = STROKE_WIDTH
        }
    }

    fun draw(canvas: Canvas) {
        objects.forEach {
            canvas.drawRect(it.getRectangle(), objectPaint)
        }
    }

    override fun move(): Coordinates {
        objects.forEach {
            it.move()
        }
        return Coordinates(0f, 0f)
    }

    override fun getRects(): List<Rect> = objects.map { it.getRectangle() }

}