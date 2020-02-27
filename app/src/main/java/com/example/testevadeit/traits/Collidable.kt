package com.example.testevadeit.traits

import android.graphics.Rect

interface Collidable {
    fun checkCollideWith(subject: Collidable, onCollideCallback: (() -> Unit)? = null)
    fun getRects(): List<Rect>
}