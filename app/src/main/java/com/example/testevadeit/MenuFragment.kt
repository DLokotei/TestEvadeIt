package com.example.testevadeit


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavController
import com.example.testevadeit.extensions.getNavigation


class MenuFragment : Fragment() {

    private var isFromPause = false
    private var lastScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isFromPause = it.getBoolean(ARG_IS_FROM_PAUSE)
            lastScore = it.getInt(ARG_LAST_SCORE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu, container, false)

        view.findViewById<Button>(R.id.btn_continue).apply {
            if (isFromPause) this.visibility = View.VISIBLE
            setOnClickListener {
                GameFragment.open(getNavigation(), isNewGame = false)
            }
        }

        view.findViewById<Button>(R.id.btn_new_game).apply {
            setOnClickListener {
                GameFragment.open(getNavigation(), isNewGame = true)
            }
        }

        view.findViewById<Button>(R.id.btn_exit_game).apply {
            setOnClickListener {
                activity?.finish()
            }
        }

        view.findViewById<TextView>(R.id.text_last_score).apply {
            text = resources.getString(R.string.scores, lastScore.toString())
        }

        return view
    }

    companion object {
        private const val ARG_IS_FROM_PAUSE = "argFromPause"
        private const val ARG_LAST_SCORE = "argLastScore"

        @JvmStatic
        fun open(navigator: NavController, isFromPause: Boolean, lastScore: Int = 0) {
            val arguments = Bundle().apply {
                putBoolean(ARG_IS_FROM_PAUSE, isFromPause)
                putInt(ARG_LAST_SCORE, lastScore)
            }
            navigator.navigate(R.id.menuFragment, arguments)
        }
    }


}
