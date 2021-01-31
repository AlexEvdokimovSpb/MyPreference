package gb.myhomework.mypreference.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import gb.myhomework.mypreference.Constants
import gb.myhomework.mypreference.R
import gb.myhomework.mypreference.databinding.ActivityGameHistoryBinding
import gb.myhomework.mypreference.databinding.ActivityHistoryBinding
import gb.myhomework.mypreference.model.Game
import gb.myhomework.mypreference.viewmodel.GameHistoryViewModel
import kotlinx.android.synthetic.main.item_history.*
import java.util.*

private const val SAVE_DELAY = 2000L

class GameHistoryActivity : BaseActivity<Game?, GameHistoryViewState>() {

    val TAG = "HW " + GameHistoryActivity::class.java.simpleName

    companion object {
        private val EXTRA_GAME = "GameHistoryActivity.extra.GAME"

        fun getStartIntent(context: Context, gameId: String?): Intent {
            val intent = Intent(context, GameHistoryActivity::class.java)
            intent.putExtra(EXTRA_GAME, gameId)
            return intent
        }
    }

    override val viewModel: GameHistoryViewModel by lazy {
        ViewModelProvider(this).get(
            GameHistoryViewModel::class.java
        )
    }
    private var game: Game? = null
    override val ui: ActivityGameHistoryBinding  by lazy {
        ActivityGameHistoryBinding.inflate(layoutInflater)
    }

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

        val gameId = intent.getStringExtra(EXTRA_GAME)

        setSupportActionBar(ui.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        gameId?.let {
            viewModel.loadGame(it)
        }

        if (gameId == null) supportActionBar?.title = getString(R.string.new_game)

        if (Constants.DEBUG) {
            Log.v(TAG, "onCreate")
        }

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

    private fun initView() {
        game?.run {
            ui.toolbar.setBackgroundColor(color.getColorInt(this@GameHistoryActivity))

            ui.textDescription.setText(description)
            ui.textPlayerOne.setText(playerOne)
            ui.textPlayerTwo.setText(playerTwo)
            ui.textPlayerThree.setText(playerThree)
            ui.textPlayerFour.setText(playerFour)
            ui.textPointsPlayerOne.setText(pointsOne)
            ui.textPointsPlayerTwo.setText(pointsTwo)
            ui.textPointsPlayerThree.setText(pointsThree)
            ui.textPointsPlayerFour.setText(pointsFour)

            supportActionBar?.title = lastChanged.format()
        }
        if (Constants.DEBUG) {
            Log.v(TAG, "initView $game ")
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
        if (ui.textDescription.text == null || ui.textDescription.text!!.length < 3) return

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
                    color = Game.Color.BLUE,
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

    override fun renderData(data: Game?) {
        this.game = data
        initView()

    }
}