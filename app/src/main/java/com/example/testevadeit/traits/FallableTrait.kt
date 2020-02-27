package com.example.testevadeit.traits

import com.example.testevadeit.models.Coordinates
import com.example.testevadeit.models.ObjectSize

class FallableTrait(position: Coordinates, objectSize: ObjectSize, gameFieldSize: ObjectSize) :
    GovableTrait(position, objectSize, gameFieldSize) {

    init {
        // set moving directions
        movingVector.x = 0f
        movingVector.y = 1f
    }


    override fun move(): Coordinates {
        super.move()
        if (position.y >= gameFieldSize.height)
            recycleToTop()
        return position
    }
}