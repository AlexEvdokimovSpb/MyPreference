package gb.myhomework.mypreference.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import gb.myhomework.mypreference.Constants
import gb.myhomework.mypreference.databinding.ActivityHistoryBinding
import gb.myhomework.mypreference.model.Game
import gb.myhomework.mypreference.viewmodel.HistoryViewModel

class HistoryActivity : AppCompatActivity() {

    val TAG = "HW " + HistoryActivity::class.java.simpleName
    lateinit var ui: ActivityHistoryBinding
    lateinit var historyViewModel: HistoryViewModel
    lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(ui.root)

        historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        adapter = HistoryAdapter(object : OnItemClickListener {
            override fun onItemClick(game: Game) {
                openGameScreen(game)
            }
        })

        ui.historyRecyclerView.adapter = adapter
        ui.historyRecyclerView.layoutManager = LinearLayoutManager(this)

        historyViewModel.viewState().observe(this, { state ->
            state?.let { adapter.games = state.games }
        })

        ui.fab.setOnClickListener { openGameScreen() }

        if (Constants.DEBUG) {
            Log.v(TAG, "HistoryActivity onCreate")
        }
    }

    private fun openGameScreen(game: Game? = null) {
        val intent = GameHistoryActivity.getStartIntent(this, game)
        startActivity(intent)
    }

}
