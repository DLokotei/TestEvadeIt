package com.example.testevadeit.extensions

import android.graphics.Rect
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.example.testevadeit.MainActivity

fun Fragment.getNavigation(): NavController = (activity as MainActivity).navigation

fun Rect.collidedWith(target: Rect): Boolean {
    if (target.right < this.left || target.left > this.right) {
        return false
    }
    if (target.top > this.bottom || target.bottom < this.top) {
        return false
    }
    return true
}