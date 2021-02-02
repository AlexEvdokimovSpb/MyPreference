package gb.myhomework.mypreference.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

object Repository {

    private val gamesLiveData = MutableLiveData<List<Game>>()

    private val games: MutableList<Game> = mutableListOf(
        Game(
            id = UUID.randomUUID().toString(),
            description = "Первая игра: победа Коли",
            playerOne = "Николай",
            playerTwo = "Константин",
            playerThree = "Екатерина",
            playerFour = "Надежда",
            pointsOne = "300",
            pointsTwo = "100",
            pointsThree = "-300",
            pointsFour = "-100",
            color = Game.Color.BLUE
        ),
        Game(
            id = UUID.randomUUID().toString(),
            description = "Вторая игра: Катя всех сделала",
            playerOne = "Николай",
            playerTwo = "Константин",
            playerThree = "Екатерина",
            playerFour = "Надежда",
            pointsOne = "-300",
            pointsTwo = "-300",
            pointsThree = "1000",
            pointsFour = "-400",
            color = Game.Color.GREEN
        ),
        Game(
            id = UUID.randomUUID().toString(),
            description = "Третья игра: Костя, как же так? Мизер с такими не играют!",
            playerOne = "Николай",
            playerTwo = "Константин",
            playerThree = "Екатерина",
            playerFour = "Надежда",
            pointsOne = "300",
            pointsTwo = "-1000",
            pointsThree = "300",
            pointsFour = "400" ,
            color = Game.Color.RED
        ),
        Game(
            id = UUID.randomUUID().toString(),
            description = "Четвертая игра: Катя всех сделала",
            playerOne = "Николай",
            playerTwo = "Константин",
            playerThree = "Екатерина",
            playerFour = "Надежда",
            pointsOne = "-300",
            pointsTwo = "-300",
            pointsThree = "1000",
            pointsFour = "-400",
            color = Game.Color.PINK
        )
    )

    init {
        gamesLiveData.value = games
    }

    fun getGames(): LiveData<List<Game>> = gamesLiveData

    fun saveGame(game: Game) {
        addOrReplace(game)
        gamesLiveData.value = games
    }

    private fun addOrReplace(game: Game) {

        for (i in 0 until games.size) {
            if (games[i] == game) {
                games[i] = game
                return
            }
        }
        games.add(game)
    }

}