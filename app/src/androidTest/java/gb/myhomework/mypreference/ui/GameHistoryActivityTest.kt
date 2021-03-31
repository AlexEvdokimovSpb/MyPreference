package gb.myhomework.mypreference.ui

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import gb.myhomework.mypreference.R
import gb.myhomework.mypreference.model.Game
import gb.myhomework.mypreference.model.Repository
import gb.myhomework.mypreference.viewmodel.GameHistoryViewModel
import io.mockk.*
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.core.IsNot.not

import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

class GameHistoryActivityTest {

    @get:Rule
    val activityTestRule = ActivityTestRule(GameHistoryActivity::class.java, true, false)

    private val viewModel: GameHistoryViewModel = spyk(GameHistoryViewModel(mockk<Repository>()))
    private val viewStateLiveData = MutableLiveData<GameHistoryViewState>()
    private val testGame =
        Game("777", "description", "playerOne", "playerTwo", "playerThree", "playerFour")

    @Before
    fun setUp() {
        startKoin {
            modules()
        }
        loadKoinModules(
            module {
                viewModel { viewModel }
            })

        every { viewModel.getViewState() } returns viewStateLiveData
        every { viewModel.loadGame(any()) } just runs
        every { viewModel.saveChanges(any()) } just runs
        every { viewModel.deleteGame() } just runs

        activityTestRule.launchActivity(Intent().apply {
            putExtra("GameHistoryActivity.extra.GAME", testGame.id)
        })

    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun should_show_color_picker() {
        onView(withId(R.id.palette)).perform(click())
        onView(withId(R.id.colorPicker)).check(matches(isCompletelyDisplayed()))
    }

    @Test
    fun should_hide_color_picker() {
        onView(withId(R.id.palette)).perform(click()).perform(click())

        onView(withId(R.id.colorPicker)).check(matches(not(isDisplayed())))
    }

    @Test
    fun should_set_toolbar_color() {
        onView(withId(R.id.palette)).perform(click())
        onView(withTagValue(`is`(Game.Color.BLUE))).perform(click())

        val colorInt = Game.Color.BLUE.getColorInt(activityTestRule.activity)

        onView(withId(R.id.toolbar)).check { view, _ ->
            assertTrue(
                "toolbar background color does not match",
                (view.background as? ColorDrawable)?.color == colorInt
            )
        }
    }

    @Test
    fun should_call_viewModel_loadGame() {
        verify(exactly = 1) { viewModel.loadGame(testGame.id) }
    }

    @Test
    fun should_show_game() {
        activityTestRule.launchActivity(null)
        viewStateLiveData.postValue(GameHistoryViewState(GameHistoryViewState.Data(game = testGame)))

        onView(withId(R.id.text_description)).check(matches(withText(testGame.description)))
        onView(withId(R.id.text_player_one)).check(matches(withText(testGame.playerOne)))
    }
}