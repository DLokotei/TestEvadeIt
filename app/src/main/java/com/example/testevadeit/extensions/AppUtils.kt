package com.example.testevadeit.extensions

import android.graphics.Rect
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.example.testevadeit.MainActivity
import com.example.testevadeit.traits.Collidable

fun Fragment.getNavigation(): NavController? = (activity as? MainActivity)?.navigation

fun Rect.collidedWith(target: Rect): Boolean {
    if (target.right < this.left || target.left > this.right) {
        return false
    }
    if (target.top > this.bottom || target.bottom < this.top) {
        return false
    }
    return true
}

fun Rect.collidedWith(targets: List<Rect>): Boolean {
    targets.forEach {
        if (this.collidedWith(it))
            return true
    }
    return false
}

fun Collidable.checkCollidingWith(subject: Collidable, onCollideCallback: (() -> Unit)?) {
    subject.getRects().forEach {
        if (it.collidedWith(this.getRects()))
            onCollideCallback?.invoke()
    }
}