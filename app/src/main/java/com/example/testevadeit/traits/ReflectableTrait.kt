package com.example.testevadeit.traits

import com.example.testevadeit.models.Coordinates
import com.example.testevadeit.models.ObjectSize

class ReflectableTrait(
    position: Coordinates,
    objectSize: ObjectSize,
    gameFieldSize: ObjectSize
) : GovableTrait(position, objectSize, gameFieldSize) {

    init {
        // set moving directions
        movingVector.x = 1f
        movingVector.y = 3f
    }


    override fun move(): Coordinates {
        super.move()
        if (position.y >= gameFieldSize.height){
            recycleToTop()
        }
        if (position.x >= gameFieldSize.width){
            movingVector.x = -1f
        }
        if (position.x <= 0){
            movingVector.x = 1f
        }
        return position
    }


}