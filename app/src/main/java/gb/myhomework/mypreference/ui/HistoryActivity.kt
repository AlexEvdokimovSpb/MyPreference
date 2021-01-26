package gb.myhomework.mypreference.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import gb.myhomework.mypreference.Constants
import gb.myhomework.mypreference.databinding.ActivityHistoryBinding
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
        adapter = HistoryAdapter()
        ui.historyRecyclerView.adapter = adapter
        ui.historyRecyclerView.layoutManager = LinearLayoutManager(this)

        historyViewModel.viewState().observe(this, { state ->
            state?.let { adapter.games = it.games }
        })

        if (Constants.DEBUG) {
            Log.v(TAG, "HistoryActivity onCreate")
        }
    }
}
