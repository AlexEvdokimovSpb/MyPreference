package gb.myhomework.mypreference.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import gb.myhomework.mypreference.Constants
import gb.myhomework.mypreference.Constants.DATE_TIME_FORMAT
import gb.myhomework.mypreference.R
import gb.myhomework.mypreference.databinding.ActivityGameHistoryBinding
import gb.myhomework.mypreference.model.Game
import gb.myhomework.mypreference.viewmodel.GameHistoryViewModel
import java.text.SimpleDateFormat
import java.util.*

private const val SAVE_DELAY = 2000L

class GameHistoryActivity : AppCompatActivity() {

    val TAG = "HW " + GameHistoryActivity::class.java.simpleName

    companion object {
        private val EXTRA_GAME = "GameHistoryActivity.extra.GAME"

        fun getStartIntent(context: Context, game: Game?): Intent {
            val intent = Intent(context, GameHistoryActivity::class.java)
            intent.putExtra(EXTRA_GAME, game)
            return intent
        }
    }

    private var game: Game? = null
    lateinit var ui: ActivityGameHistoryBinding
    private lateinit var viewModel: GameHistoryViewModel

    private val textChangeListener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            triggerSaveGame()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // not used
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // not used
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityGameHistoryBinding.inflate(layoutInflater)
        setContentView(ui.root)

        viewModel = ViewModelProvider(this).get(GameHistoryViewModel::class.java)

        game = intent.getParcelableExtra(EXTRA_GAME)
        setSupportActionBar(ui.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.title = if (game != null) {
            SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(game!!.lastChanged)
        } else {
            getString(R.string.new_game)
        }

        if (Constants.DEBUG) {
            Log.v(TAG, "onCreate")
        }

        initView()
    }

    private fun initView() {
        if (game != null) {
            ui.textDescription.setText(game?.description ?: "")
            ui.textPlayerOne.setText(game?.playerOne ?: "")
            ui.textPlayerTwo.setText(game?.playerTwo ?: "")
            ui.textPlayerThree.setText(game?.playerThree ?: "")
            ui.textPlayerFour.setText(game?.playerFour ?: "")
            ui.textPointsPlayerOne.setText(game?.pointsOne ?: "")
            ui.textPointsPlayerTwo.setText(game?.pointsTwo ?: "")
            ui.textPointsPlayerThree.setText(game?.pointsThree ?: "")
            ui.textPointsPlayerFour.setText(game?.pointsFour ?: "")

            val color = when (game?.color) {
                Game.Color.WHITE -> R.color.color_white
                Game.Color.VIOLET -> R.color.color_violet
                Game.Color.YELLOW -> R.color.color_yello
                Game.Color.RED -> R.color.color_red
                Game.Color.PINK -> R.color.color_pink
                Game.Color.GREEN -> R.color.color_green
                Game.Color.BLUE -> R.color.color_blue
                else -> R.color.color_white
            }

            ui.toolbar.setBackgroundColor(resources.getColor(color))

            ui.textDescription.addTextChangedListener(textChangeListener)
            ui.textPlayerOne.addTextChangedListener(textChangeListener)
            ui.textPlayerTwo.addTextChangedListener(textChangeListener)
            ui.textPlayerThree.addTextChangedListener(textChangeListener)
            ui.textPlayerFour.addTextChangedListener(textChangeListener)
            ui.textPointsPlayerOne.addTextChangedListener(textChangeListener)
            ui.textPointsPlayerTwo.addTextChangedListener(textChangeListener)
            ui.textPointsPlayerThree.addTextChangedListener(textChangeListener)
            ui.textPointsPlayerFour.addTextChangedListener(textChangeListener)

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun createNewGame(): Game = Game(
        UUID.randomUUID().toString(),
        ui.textDescription.getText().toString(),
        ui.textPlayerOne.getText().toString(),
        ui.textPlayerTwo.getText().toString(),
        ui.textPlayerThree.getText().toString(),
        ui.textPlayerFour.getText().toString(),
        ui.textPointsPlayerOne.getText().toString(),
        ui.textPointsPlayerTwo.getText().toString(),
        ui.textPointsPlayerThree.getText().toString(),
        ui.textPointsPlayerFour.getText().toString()
    )

    private fun triggerSaveGame() {
        if (ui.textDescription == null || ui.textDescription!!.length() < 3) return

        Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
            override fun run() {
                game = game?.copy(
                    description = ui.textDescription.getText().toString(),
                    playerOne = ui.textPlayerOne.getText().toString(),
                    playerTwo = ui.textPlayerTwo.getText().toString(),
                    playerThree = ui.textPlayerThree.getText().toString(),
                    playerFour = ui.textPlayerFour.getText().toString(),
                    pointsOne = ui.textPointsPlayerOne.getText().toString(),
                    pointsTwo = ui.textPointsPlayerTwo.getText().toString(),
                    pointsThree = ui.textPointsPlayerThree.getText().toString(),
                    pointsFour = ui.textPointsPlayerFour.getText().toString(),
                    lastChanged = Date()
                ) ?: createNewGame()

                if (game != null) {
                    viewModel.saveChanges(game!!)
                    if (Constants.DEBUG) {
                        Log.v(TAG, "viewModel.saveChanges")
                    }
                }
            }
        }, SAVE_DELAY)
    }
}