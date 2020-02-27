package com.example.testevadeit.models

import androidx.annotation.ColorInt
import com.example.testevadeit.traits.Collidable
import java.util.*

class Traps(
    @ColorInt private val itemColor: Int,
    gameFieldSize: ObjectSize,
    trapsPerScreen: Int
) : GameObjects(itemColor, gameFieldSize, trapsPerScreen) {

    override val objects: Array<GameObject> = Array(trapsPerScreen) {
        Trap(
            Coordinates(
                Trap.SIZE.width / 2 + Random().nextInt((gameFieldSize.width - Trap.SIZE.width).toInt()).toFloat(),
                -objectsGap * it
            ),
            gameFieldSize
        )
    }

    override fun checkCollideWith(subject: Collidable, onCollideCallback: (() -> Unit)?) {
        //pass
    }
}