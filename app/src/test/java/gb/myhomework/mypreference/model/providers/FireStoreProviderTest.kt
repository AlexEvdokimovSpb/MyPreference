package gb.myhomework.mypreference.model.providers

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import gb.myhomework.mypreference.model.Game
import gb.myhomework.mypreference.model.HistoryGameResult
import gb.myhomework.mypreference.model.NoAuthException
import io.mockk.*
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class FireStoreProviderTest {

    private val mockDb = mockk<FirebaseFirestore>()
    private val mockAuth = mockk<FirebaseAuth>()
    private val mockCollection = mockk<CollectionReference>()
    private val mockUser = mockk<FirebaseUser>()
    private val mockDocument1 = mockk<DocumentSnapshot>()
    private val mockDocument2 = mockk<DocumentSnapshot>()
    private val mockDocument3 = mockk<DocumentSnapshot>()
    private val testGames = listOf(Game(id = "1"), Game(id = "2"), Game(id = "3"))

    private val provider: FireStoreProvider = FireStoreProvider(mockAuth, mockDb)

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setUp() {
        clearMocks(mockCollection, mockDocument1, mockDocument2, mockDocument3)
        every { mockAuth.currentUser } returns mockUser
        every { mockUser.uid } returns ""
        every {
            mockDb.collection(any()).document(any()).collection(any())
        } returns mockCollection
        every { mockDocument1.toObject(Game::class.java) } returns testGames[0]
        every { mockDocument2.toObject(Game::class.java) } returns testGames[1]
        every { mockDocument3.toObject(Game::class.java) } returns testGames[2]
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `should throw if no auth`() {
        var result: Any? = null

        every { mockAuth.currentUser } returns null
        provider.subscribeToAllGames().observeForever {
            result = (it as? HistoryGameResult.Error)?.error
        }
        assertTrue(result is NoAuthException)
    }

    @Test
    fun `subscribeAllGames return games`() {
        var result: List<Game>? = null
        val slot = slot<EventListener<QuerySnapshot>>()
        val mockSnapshot = mockk<QuerySnapshot>()

        every { mockSnapshot.documents } returns
                listOf(mockDocument1, mockDocument2, mockDocument3)
        every { mockCollection.addSnapshotListener(capture(slot)) } returns mockk()

        provider.subscribeToAllGames().observeForever {
            result = (it as? HistoryGameResult.Success<List<Game>>)?.data
        }

        slot.captured.onEvent(mockSnapshot, null)

        assertEquals(testGames, result)
    }

    @Test
    fun `subscribeAllGames return error`() {
        var result: Throwable? = null
        val slot = slot<EventListener<QuerySnapshot>>()
        val testError = mockk<FirebaseFirestoreException>()

        every { mockCollection.addSnapshotListener(capture(slot)) } returns mockk()

        provider.subscribeToAllGames()
            .observeForever { result = (it as? HistoryGameResult.Error)?.error }

        slot.captured.onEvent(null, testError)

        assertNotNull(result)
        assertEquals(testError, result)
    }

    @Test
    fun `saveGame calls document set`() {
        val mockDocumentReference: DocumentReference = mockk()

        every { mockCollection.document(testGames[0].id) } returns mockDocumentReference
        provider.saveGame(testGames[0])

        verify(exactly = 1) { mockDocumentReference.set(testGames[0]) }
    }

    @Test
    fun `saveGame return Game`() {
        val mockDocumentReference: DocumentReference = mockk()
        val slot = slot<OnSuccessListener<in Void>>()
        var result: Game? = null

        every { mockCollection.document(testGames[0].id) } returns mockDocumentReference
        every {
            mockDocumentReference.set(testGames[0]).addOnSuccessListener(capture(slot))
        } returns mockk()

        provider.saveGame(testGames[0]).observeForever {
            result = (it as? HistoryGameResult.Success<Game>)?.data
        }
        slot.captured.onSuccess(null)

        assertNotNull(result)
        assertEquals(testGames[0], result)
    }
}