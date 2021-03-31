package gb.myhomework.mypreference.ui

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import gb.myhomework.mypreference.Constants
import gb.myhomework.mypreference.databinding.ActivityHistoryBinding
import gb.myhomework.mypreference.model.Game
import gb.myhomework.mypreference.viewmodel.HistoryViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class HistoryActivity : BaseActivity<List<Game>?>() {

    val TAG = "HW " + HistoryActivity::class.java.simpleName

    override val viewModel: HistoryViewModel by viewModel()
    private lateinit var adapter: HistoryAdapter
    override val ui: ActivityHistoryBinding by lazy {
        ActivityHistoryBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(ui.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = HistoryAdapter(object : OnItemClickListener {
            override fun onItemClick(game: Game) {
                openGameScreen(game)
            }
        })

        ui.historyRecyclerView.adapter = adapter
        ui.historyRecyclerView.layoutManager = LinearLayoutManager(this)

        ui.fab.setOnClickListener { openGameScreen(null) }

        if (Constants.DEBUG) {
            Log.v(TAG, "HistoryActivity onCreate")
        }
    }

    override fun renderData(data: List<Game>?) {
        if (data == null) return
        adapter.games = data
    }

    private fun openGameScreen(game: Game?) {
        val intent = GameHistoryActivity.getStartIntent(this, game?.id)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
