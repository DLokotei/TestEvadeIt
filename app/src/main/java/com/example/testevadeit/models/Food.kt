package com.example.testevadeit.models

import android.graphics.Rect
import com.example.testevadeit.traits.AsRectangle
import com.example.testevadeit.traits.CollidableWithPlayer
import com.example.testevadeit.traits.FallableTrait
import com.example.testevadeit.traits.Movable

class Food(
    position: Coordinates,
    gameFieldSize: ObjectSize
) : Movable, AsRectangle, CollidableWithPlayer {

    companion object {
        val SIZE = ObjectSize(5f, 5f)
    }

    private val trait = FallableTrait(position, SIZE, gameFieldSize)

    override fun move(): Coordinates = trait.move()

    override fun getRectangle(): Rect = Rect().apply {
        left = (trait.position.x - SIZE.width / 2).toInt()
        right = (trait.position.x + SIZE.width / 2).toInt()
        top = (trait.position.y - SIZE.height / 2).toInt()
        bottom = (trait.position.y + SIZE.height / 2).toInt()
    }

    override fun onCollideWithPlayer() {
        trait.recycleToTop()
    }

}