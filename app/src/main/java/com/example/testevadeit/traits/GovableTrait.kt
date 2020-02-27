package com.example.testevadeit.traits

import com.example.testevadeit.models.Coordinates
import com.example.testevadeit.models.MovingVector
import com.example.testevadeit.models.ObjectSize
import java.util.*

abstract class GovableTrait(
    var position: Coordinates,
    val objectSize: ObjectSize,
    val gameFieldSize: ObjectSize
): Movable {
    val movingVector = MovingVector(0f, 0f)

    // Return coordinates After moving
    override fun move(): Coordinates {
        position.x = position.x + movingVector.x
        position.y = position.y + movingVector.y
        return Coordinates(Math.round(position.x).toFloat(), Math.round(position.y).toFloat())
    }

    // move to top of field and get new random horizontal position
    fun recycleToTop() {
        position.y = 0f
        position.x =
            objectSize.width / 2 + Random().nextInt((gameFieldSize.width - objectSize.width).toInt()).toFloat()
    }
}