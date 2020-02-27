package com.example.testevadeit.models

import android.graphics.Rect
import com.example.testevadeit.traits.Collidable
import com.example.testevadeit.traits.ReflectableTrait

class Trap(
    position: Coordinates,
    gameFieldSize: ObjectSize
) : GameObject {

    companion object {
        val SIZE = ObjectSize(20f, 4f)
    }

    private val trait = ReflectableTrait(position, SIZE, gameFieldSize)

    override fun move(): Coordinates = trait.move()

    override fun getRectangle(): Rect = Rect().apply {
        left = (trait.position.x - SIZE.width / 2).toInt()
        right = (trait.position.x + SIZE.width / 2).toInt()
        top = (trait.position.y - SIZE.height / 2).toInt()
        bottom = (trait.position.y + SIZE.height / 2).toInt()
    }

    override fun checkCollideWith(subject: Collidable, onCollideCallback: (() -> Unit)?) {
        //pass
    }

    override fun getRects(): List<Rect> = listOf(getRectangle())

}