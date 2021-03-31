package gb.myhomework.mypreference.ui

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import gb.myhomework.mypreference.R
import gb.myhomework.mypreference.model.Game
import gb.myhomework.mypreference.model.Repository
import gb.myhomework.mypreference.viewmodel.GameHistoryViewModel
import gb.myhomework.mypreference.viewmodel.HistoryViewModel
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

class HistoryActivityTest {

    @get:Rule
    val activityTestRule = IntentsTestRule(HistoryActivity::class.java, true, false)

    private val EXTRA_GAME = HistoryActivity::class.java.name + "extra.GAME_ID"

    private val viewModel: HistoryViewModel = spyk(HistoryViewModel(mockk<Repository>()))

    private val viewStateLiveData = MutableLiveData<HistoryViewState>()
    private val testGames = listOf(
        Game("333", "title", "body"),
        Game("444", "title1", "body1"),
        Game("555", "title2", "body2"))

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

        activityTestRule.launchActivity(null)
        viewStateLiveData.postValue(HistoryViewState(games = testGames))
    }


    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun check_data_is_displayed() {
        onView(withId(R.id.history_recycler_view))
            .perform(scrollToPosition<HistoryAdapter.GameViewHolder>(1))
        onView(withText(testGames[1].playerOne)).check(matches(isDisplayed()))
    }


}