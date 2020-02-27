package com.example.testevadeit.models

import android.graphics.Rect
import com.example.testevadeit.traits.AsRectangle
import com.example.testevadeit.traits.Movable
import com.example.testevadeit.traits.ReflectableTrait

class Trap(
    position: Coordinates,
    gameFieldSize: ObjectSize
) : Movable, AsRectangle {

    companion object{
        val SIZE = ObjectSize(10f, 2f)
    }

    private val trait = ReflectableTrait(position, SIZE, gameFieldSize)

    override fun move(): Coordinates = trait.move()

    override fun getRectangle(): Rect = Rect().apply {
        left = (trait.position.x - SIZE.width / 2).toInt()
        right = (trait.position.x + SIZE.width / 2).toInt()
        top = (trait.position.y - SIZE.height / 2).toInt()
        bottom = (trait.position.y + SIZE.height / 2).toInt()
    }

}