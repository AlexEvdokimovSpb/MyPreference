package gb.myhomework.mypreference.ui

import android.content.Context
import androidx.core.content.ContextCompat
import gb.myhomework.mypreference.R
import gb.myhomework.mypreference.model.Game
import java.text.SimpleDateFormat
import java.util.*

const val DATE_TIME_FORMAT = "dd.MMM.yy HH:mm"

fun Date.format(): String =
    SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
        .format(this)

fun Game.Color.getColorInt(context: Context): Int =
    ContextCompat.getColor(
        context, when (this) {
            Game.Color.WHITE -> R.color.color_white
            Game.Color.VIOLET -> R.color.color_violet
            Game.Color.YELLOW -> R.color.color_yello
            Game.Color.RED -> R.color.color_red
            Game.Color.PINK -> R.color.color_pink
            Game.Color.GREEN -> R.color.color_green
            Game.Color.BLUE -> R.color.color_blue
        }
    )
