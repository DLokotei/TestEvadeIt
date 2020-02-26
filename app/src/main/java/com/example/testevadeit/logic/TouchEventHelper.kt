package com.example.testevadeit.logic

import android.view.MotionEvent
import kotlin.math.abs

class TouchEventHelper() {

    companion object {
        private const val BARIER = 5
    }

    var lastCashedX: Float? = null

    fun getSlideEvent(event: MotionEvent, callback: (distance: Float) -> Unit) {
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                if (lastCashedX == null) lastCashedX = event.x
                val distance = event.x - lastCashedX!!
                if ( abs(distance) > BARIER) {
                    callback(distance*2)
                    lastCashedX = null
                }
            }
            MotionEvent.ACTION_UP -> lastCashedX = null
        }
    }
}