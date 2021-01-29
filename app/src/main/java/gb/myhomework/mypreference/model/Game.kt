package gb.myhomework.mypreference.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Game(
    val id: String = "",
    val description: String = "",
    val playerOne: String = "",
    val playerTwo: String = "",
    val playerThree: String = "",
    val playerFour: String = "",
    val pointsOne: String = "",
    val pointsTwo: String = "",
    val pointsThree: String = "",
    val pointsFour: String = "",
    val color: Color = Color.WHITE,
    val lastChanged: Date = Date()
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Game

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    enum class Color {
        WHITE,
        YELLOW,
        GREEN,
        BLUE,
        RED,
        VIOLET,
        PINK
    }

}