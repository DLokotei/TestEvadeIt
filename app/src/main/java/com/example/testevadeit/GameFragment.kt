package com.example.testevadeit

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.example.testevadeit.extensions.getNavigation
import com.example.testevadeit.logic.PeriodicalTask
import com.example.testevadeit.models.Coordinates
import com.example.testevadeit.models.Foods
import com.example.testevadeit.models.Player
import com.example.testevadeit.models.Traps
import com.example.testevadeit.views.ViewGame


class GameFragment : Fragment() {

    private var traps: Traps? = null
    private var foods: Foods? = null
    private var player: Player? = null

    private lateinit var gameView: ViewGame
    private lateinit var btnPause: ImageView
    private lateinit var pointsText: TextView
    private var imgPause: Drawable? = null
    private var imgContinue: Drawable? = null
    private var trapsPerScreen = 15
    private var foodPerScreen = 20
    private var isGameRunning: Boolean = false
    private var playerColor: Int = 0
    private var trapColor: Int = 0
    private var eatableColor: Int = 0
    private val gameTime = object : PeriodicalTask(10L) {
        override fun doTask() {
            gameView.nextFrame()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game, container, false)

        playerColor = ResourcesCompat.getColor(resources, R.color.green, null)
        trapColor = ResourcesCompat.getColor(resources, R.color.red, null)
        eatableColor = ResourcesCompat.getColor(resources, R.color.yellow, null)
        imgPause = ResourcesCompat.getDrawable(
            resources,
            R.drawable.ic_pause_circle_filled_black_24dp,
            null
        )
        imgContinue = ResourcesCompat.getDrawable(
            resources,
            R.drawable.ic_play_circle_outline_black_24dp,
            null
        )

        gameView = view.findViewById(R.id.view_game)
        btnPause = view.findViewById(R.id.btn_pause)
        pointsText = view.findViewById(R.id.text_points)
        btnPause.setOnClickListener {
            if (isGameRunning) {
                gameTime.pause()
                btnPause.setImageDrawable(imgContinue)
            } else {
                gameTime.resume()
                btnPause.setImageDrawable(imgPause)
            }
            isGameRunning = !isGameRunning
        }

        setupGame()
        gameTime.resume()
        isGameRunning = true
        return view
    }

    private fun setupGame() {
        gameView.onGameStartCallback = { gameFieldSize ->
            val playerX = gameFieldSize.width / 2f
            val playerY = gameFieldSize.height * 0.9f
            player = Player(Coordinates(playerX, playerY), gameFieldSize, playerColor)
            traps = Traps(trapColor, gameFieldSize, trapsPerScreen)
            foods = Foods(eatableColor, gameFieldSize, foodPerScreen)
            gameView.player = player!!
            gameView.traps = traps!!
            gameView.foods = foods!!
        }
        gameView.onPointsChangeCallback = { points ->
            pointsText.text = resources.getString(R.string.last_scores, points.toString())
        }
        gameView.onGameOverCallback = { score ->
            getNavigation()?.let {
                MenuFragment.open(it, isFromPause = false, lastScore = score)
            }
        }
    }


    companion object {
        @JvmStatic
        fun open(navigator: NavController) {
            navigator.navigate(R.id.gameFragment, null)
        }
    }
}
