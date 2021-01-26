package gb.myhomework.mypreference.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gb.myhomework.mypreference.Constants
import gb.myhomework.mypreference.R
import gb.myhomework.mypreference.databinding.ItemHistoryBinding
import gb.myhomework.mypreference.model.Game

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.GameViewHolder>() {

    val TAG = "HW " + HistoryAdapter::class.java.simpleName
    var games: List<Game> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
            if (Constants.DEBUG) {
                Log.v(TAG, "HistoryAdapter set")
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_history, parent, false)
        if (Constants.DEBUG) {
            Log.v(TAG, "onCreateViewHolder")
        }
        return GameViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (Constants.DEBUG) {
            Log.v(TAG, "Колличество игр" + games.size)
        }
        return games.size
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bind(games[position])
        if (Constants.DEBUG) {
            Log.v(TAG, "onBindViewHolder")
        }
    }

    class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val TAG = "HW " + GameViewHolder::class.java.simpleName
        val ui: ItemHistoryBinding = ItemHistoryBinding.bind(itemView)
        fun bind(game: Game) {
            ui.textDescription.text = game.description
            ui.textPlayerOne.text = game.playerOne
            ui.textPlayerTwo.text = game.playerTwo
            ui.textPlayerThree.text = game.playerThree
            ui.textPlayerFour.text = game.playerFour
            ui.textPointsPlayerOne.text = game.pointsOne.toString()
            ui.textPointsPlayerTwo.text = game.pointsTwo.toString()
            ui.textPointsPlayerThree.text = game.pointsThree.toString()
            ui.textPointsPlayerFour.text = game.pointsFour.toString()
            if (Constants.DEBUG) {
                Log.v(TAG, "GameViewHolder")
            }
        }
    }
}