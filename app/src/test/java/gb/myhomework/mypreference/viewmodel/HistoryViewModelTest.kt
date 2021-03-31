package gb.myhomework.mypreference.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import gb.myhomework.mypreference.model.Game
import gb.myhomework.mypreference.model.HistoryGameResult
import gb.myhomework.mypreference.model.Repository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule

class HistoryViewModelTest {

    private val mockRepository = mockk<Repository>()
    private val gamesLiveData = MutableLiveData<HistoryGameResult>()
    private lateinit var viewModel: HistoryViewModel

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        every { mockRepository.getGames() } returns gamesLiveData
        viewModel = HistoryViewModel(mockRepository)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `should call getGames once`() {
        verify(exactly = 1) { mockRepository.getGames() }
    }

    @Test
    fun `should return error`() {
        var result: Throwable? = null
        val testData = Throwable("error")

        viewModel.getViewState().observeForever { result = it?.error }
        gamesLiveData.value = HistoryGameResult.Error(testData)
        assertEquals(result, testData)
    }

    @Test
    fun `should return Notes`() {
        var result: List<Game>? = null
        val testData = listOf(Game(id = "1"), Game(id = "2"))

        viewModel.getViewState().observeForever { result = it?.data }
        gamesLiveData.value = HistoryGameResult.Success(testData)
        assertEquals(testData, result)
    }

    @Test
    fun `should remove observer`() {
        viewModel.onCleared()
        assertFalse(gamesLiveData.hasObservers())
    }
}