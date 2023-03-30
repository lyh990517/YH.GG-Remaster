package yunho.compose.presentation.viewmodel

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import yunho.compose.domain.model.LeagueEntryDTO
import yunho.compose.domain.model.SummonerDTO
import yunho.compose.domain.usecase.GetOneSummonerInfoUseCase
import yunho.compose.domain.usecase.GetOneSummonerLeagueEntryUseCase
import yunho.compose.yhgg_remaster.presentaion.state.SummonerState
import yunho.compose.yhgg_remaster.presentaion.viewmodel.SummonerViewModel

@OptIn(ExperimentalCoroutinesApi::class)
internal class SummonerViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()

    @MockK
    private lateinit var getOneSummonerInfoUseCase: GetOneSummonerInfoUseCase

    @MockK
    private lateinit var getOneSummonerLeagueEntryUseCase: GetOneSummonerLeagueEntryUseCase

    private lateinit var viewModel: SummonerViewModel


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(dispatcher)
        viewModel = SummonerViewModel(getOneSummonerInfoUseCase, getOneSummonerLeagueEntryUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        dispatcher.cancel()
    }

    @Test
    fun `getSummonerState should collect SummonerState`() = runBlocking {
        val leagueEntryDTO = listOf(mockk<LeagueEntryDTO>())
        val summonerDTO = mockk<SummonerDTO>()
        viewModel.summonerState.value = SummonerState.Loading
        viewModel.summonerState.value = SummonerState.Success(summonerDTO)
        viewModel.summonerState.value = SummonerState.LoadLeagueEntry(leagueEntryDTO)
        val job = launch {
            viewModel.summonerState.collect {
                when (it) {
                    is SummonerState.Loading -> assertEquals(SummonerState.Loading, it)
                    is SummonerState.Success -> assertEquals(SummonerState.Success(summonerDTO), it)
                    is SummonerState.LoadLeagueEntry -> assertEquals(
                        SummonerState.LoadLeagueEntry(
                            leagueEntryDTO
                        ), it
                    )
                    else -> {}
                }
            }
        }
        delay(10)
        job.cancel()
    }

    @Test
    fun `getSummonerInfo should collect Success of SummonerState`() = runBlocking {
        // Arrange
        val summonerDTO = mockk<SummonerDTO>()
        every { summonerDTO.name } returns "lyh123"
        val name = summonerDTO.name
        coEvery { getOneSummonerInfoUseCase.getInfo(name) } returns flow { emit(summonerDTO) }

        assertEquals(SummonerState.Loading, viewModel.summonerState.value)
        val job = viewModel.getSummonerInfo(name)
        delay(10)
        job.cancel()
        assertEquals(SummonerState.Success(summonerDTO), viewModel.summonerState.value)
    }

    @Test
    fun `getSummonerLeague should collect Success of SummonerState`() = runBlocking {
        val leagueEntryDTO = mockk<LeagueEntryDTO>()
        val response = listOf(leagueEntryDTO)
        every { leagueEntryDTO.summonerId } returns "test"
        val id = leagueEntryDTO.summonerId
        coEvery { getOneSummonerLeagueEntryUseCase.getLeagueEntry(id) } returns flow { emit(response) }

        assertEquals(SummonerState.Loading, viewModel.summonerState.value)
        val job = viewModel.getSummonerLeague(leagueEntryDTO.summonerId)
        delay(100)
        job.cancel()
        assertEquals(SummonerState.LoadLeagueEntry(response), viewModel.summonerState.value)

    }

}