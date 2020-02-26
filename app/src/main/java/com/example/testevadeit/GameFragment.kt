package com.example.testevadeit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.example.testevadeit.extensions.getNavigation
import com.example.testevadeit.logic.PeriodicalTask
import com.example.testevadeit.views.ViewGame


class GameFragment : Fragment() {
    private var isNewGame: Boolean = true
    private lateinit var gameView: ViewGame
    private lateinit var btnPause: ImageView
    private lateinit var pointsText: TextView

    val gameTime = object : PeriodicalTask(20L) {
        override fun doTask() {
            gameView.nextFrame()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isNewGame = it.getBoolean(ARG_IS_NEW_GAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game, container, false)
        gameView = view.findViewById(R.id.view_game)
        btnPause = view.findViewById(R.id.btn_pause)
        pointsText = view.findViewById(R.id.text_points)
        btnPause.setOnClickListener {
            gameTime.pause()
            MenuFragment.open(getNavigation(), isFromPause = true)
        }
        gameView.onGameOverCallback = { score ->
            MenuFragment.open(getNavigation(), isFromPause = false, lastScore = score)
        }
        gameView.onPointsChangeCallback = { points ->
            pointsText.text = resources.getString(R.string.scores, points.toString())
        }
        if (isNewGame) {
            gameView.trapsPerScreen = 20
        }

        gameTime.resume()
        return view
    }


    companion object {
        private const val ARG_IS_NEW_GAME = "argNewGame"

        @JvmStatic
        fun open(navigator: NavController, isNewGame: Boolean) {
            val arguments = Bundle().apply {
                putBoolean(ARG_IS_NEW_GAME, isNewGame)
            }
            navigator.navigate(R.id.gameFragment, arguments)
        }
    }
}
