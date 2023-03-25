package yunho.compose.presentation.viewmodel

import android.util.Log
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Rule
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

    @MockK
    private lateinit var viewModel: SummonerViewModel

    private val state = MutableStateFlow<SummonerState>(SummonerState.UnInitialized)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(dispatcher)
        every { viewModel.summonerState } returns state
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        dispatcher.cancel()
    }

    @Test
    fun getSummonerState() = runBlocking {
        val leagueEntryDTO = listOf(mockk<LeagueEntryDTO>())
        val summonerDTO = mockk<SummonerDTO>()
        viewModel.summonerState.value = SummonerState.Loading
        viewModel.summonerState.value = SummonerState.Success(summonerDTO)
        viewModel.summonerState.value = SummonerState.LoadLeagueEntry(leagueEntryDTO)
        val job = launch {
            viewModel.summonerState.collect {
                when (it) {
                    is SummonerState.UnInitialized -> assertEquals(SummonerState.UnInitialized, it)
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
    fun getSummonerInfo() = runBlocking {
        // Arrange
        val summonerDTO = mockk<SummonerDTO>()
        every { summonerDTO.name } returns "test"
        val name = summonerDTO.name
        coEvery { getOneSummonerInfoUseCase.getInfo(name) } returns flow { emit(summonerDTO) }
        coEvery { viewModel.getSummonerInfo(name) } returns launch {
            getOneSummonerInfoUseCase.getInfo(name).collect {
                viewModel.summonerState.value = SummonerState.Success(it)
            }
        }
        assertEquals(SummonerState.UnInitialized, viewModel.summonerState.value)
        viewModel.summonerState.value = SummonerState.Loading
        assertEquals(SummonerState.Loading, viewModel.summonerState.value)
        viewModel.getSummonerInfo(name).join()
        assertEquals(SummonerState.Success(summonerDTO).infoData, summonerDTO)
    }

    @Test
    fun getSummonerLeague() = runBlocking {
        val leagueEntryDTO = mockk<LeagueEntryDTO>()
        val response = listOf(leagueEntryDTO)
        every { leagueEntryDTO.summonerId } returns "test"
        val id = leagueEntryDTO.summonerId
        coEvery { getOneSummonerLeagueEntryUseCase.getLeagueEntry(id) } returns flow { emit(response) }
        coEvery { viewModel.getSummonerLeague(id) } returns launch {
            getOneSummonerLeagueEntryUseCase.getLeagueEntry(id).collect {
                viewModel.summonerState.value = SummonerState.LoadLeagueEntry(it)
            }
        }
        assertEquals(SummonerState.UnInitialized, viewModel.summonerState.value)
        viewModel.summonerState.value = SummonerState.Loading
        assertEquals(SummonerState.Loading, viewModel.summonerState.value)
        viewModel.getSummonerLeague(id).join()
        assertEquals(
            SummonerState.LoadLeagueEntry(listOf(leagueEntryDTO)),
            viewModel.summonerState.value
        )

    }

}