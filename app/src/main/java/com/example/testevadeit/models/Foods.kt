package com.example.testevadeit.models

import androidx.annotation.ColorInt
import com.example.testevadeit.traits.Collidable
import java.util.*

class Foods(
    @ColorInt private val itemColor: Int,
    gameFieldSize: ObjectSize,
    foodPerScreen: Int
) : GameObjects(itemColor, gameFieldSize, foodPerScreen) {

    override val objects: Array<GameObject> = Array(foodPerScreen) {
        Food(
            Coordinates(
                Food.SIZE.width / 2 + Random().nextInt((gameFieldSize.width - Food.SIZE.width).toInt()).toFloat(),
                -objectsGap * it
            ),
            gameFieldSize
        )
    }

    override fun checkCollideWith(subject: Collidable, onCollideCallback: (() -> Unit)?) {
        objects.forEach {
            (it as Food).checkCollideWith(subject, onCollideCallback)
        }
    }

}