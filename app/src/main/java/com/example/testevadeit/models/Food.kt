package com.example.testevadeit.models

import android.graphics.Rect
import com.example.testevadeit.extensions.collidedWith
import com.example.testevadeit.traits.Collidable
import com.example.testevadeit.traits.FallableTrait

class Food(
    position: Coordinates,
    gameFieldSize: ObjectSize
) : GameObject {

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

    override fun checkCollideWith(subject: Collidable, onCollideCallback: (() -> Unit)?) {
        if (subject is Player) {
            if (subject.getRectangle().collidedWith(getRectangle())) {
                trait.recycleToTop()
                onCollideCallback?.invoke()
            }
        }
    }

    override fun getRects(): List<Rect> = listOf(getRectangle())

}