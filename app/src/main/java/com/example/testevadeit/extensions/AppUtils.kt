package com.example.testevadeit.extensions

import android.graphics.Rect
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.example.testevadeit.MainActivity
import java.util.*

fun Fragment.getNavigation(): NavController = (activity as MainActivity).navigation

fun Rect.collided(target: Rect): Boolean {
    if (target.right < this.left || target.left > this.right) {
        return false
    }
    return target.top >= this.bottom && target.bottom <= this.bottom
}

fun <T> Array<T>.replace(list: Array<T>) {
    this.forEachIndexed { i, _ ->
        this[i] = list[i]
    }
}



