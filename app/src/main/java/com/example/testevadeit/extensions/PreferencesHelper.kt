package com.example.testevadeit.extensions

import android.content.Context.MODE_PRIVATE
import com.example.testevadeit.MainActivity


class PreferencesHelper(activity: MainActivity) {

    val name = activity.application.applicationInfo.name
    val preferences = activity.getSharedPreferences(name, MODE_PRIVATE)

    val topScoreFieldName = "${name}.topscore"

    fun saveTopScore(score: Int){
        preferences.edit().putInt(topScoreFieldName, score).apply()
    }

    fun getTopScore(): Int{
        return preferences.getInt(topScoreFieldName, 0)
    }

}