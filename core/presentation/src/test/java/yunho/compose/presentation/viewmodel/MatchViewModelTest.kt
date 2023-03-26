package yunho.compose.presentation.viewmodel

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.jupiter.api.Assertions.*
import org.junit.Test
import yunho.compose.domain.model.MatchDTO
import yunho.compose.domain.usecase.GetMatchIdsUseCase
import yunho.compose.domain.usecase.GetOneMatchInfoUseCase
import yunho.compose.yhgg_remaster.presentaion.state.MatchState
import yunho.compose.yhgg_remaster.presentaion.viewmodel.MatchViewModel

@OptIn(ExperimentalCoroutinesApi::class)
internal class MatchViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()

    @MockK
    private lateinit var getMatchIdsUseCase: GetMatchIdsUseCase

    @MockK
    private lateinit var getOneMatchInfoUseCase: GetOneMatchInfoUseCase

    private lateinit var viewmodel: MatchViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(dispatcher)
        viewmodel = MatchViewModel(getMatchIdsUseCase, getOneMatchInfoUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getMatchIds should collect LoadIds of MatchState`() = runBlocking {
        val matchIds = listOf("1", "2", "3")
        val puuid = "test"
        coEvery { getMatchIdsUseCase.getMatchIds(puuid) } returns flow { emit(matchIds) }

        assertEquals(MatchState.Loading, viewmodel.matchState.value)
        val job = viewmodel.getMatchIds(puuid)
        delay(100)
        job.cancel()
        assertEquals(MatchState.LoadIds(matchIds), viewmodel.matchState.value)
    }

    @Test
    fun `getMatchInfo should collect Success of MatchState`() = runBlocking {
        val matchDTO = mockk<MatchDTO>()
        val matchId = "1"
        every { matchDTO.info } returns mockk()
        every { matchDTO.metadata } returns mockk()
        coEvery { getOneMatchInfoUseCase.getInfo(matchId) } returns flow { emit(matchDTO) }

        assertEquals(MatchState.Loading, viewmodel.matchState.value)
        val job = viewmodel.getMatchInfo(matchId)
        delay(100)
        job.cancel()
        assertEquals(MatchState.Success(matchDTO), viewmodel.matchState.value)
    }
}