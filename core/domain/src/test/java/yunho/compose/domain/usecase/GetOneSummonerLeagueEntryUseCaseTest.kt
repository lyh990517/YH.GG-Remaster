package yunho.compose.domain.usecase

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import yunho.compose.domain.model.LeagueEntryDTO
import yunho.compose.domain.repository.SummonerRepository

internal class GetOneSummonerLeagueEntryUseCaseTest {

    @MockK
    private lateinit var summonerRepository: SummonerRepository
    private lateinit var getOneSummonerLeagueEntryUseCase: GetOneSummonerLeagueEntryUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getOneSummonerLeagueEntryUseCase = GetOneSummonerLeagueEntryUseCase(summonerRepository)
    }

    @Test
    fun `getLeagueEntry should emit LeagueEntryDTO`() = runBlocking {
        val id = "1"
        val leagueEntryDTO = listOf(mockk<LeagueEntryDTO>())

        coEvery { summonerRepository.getSummonerLeagueEntry(id) } returns flow {
            emit(
                leagueEntryDTO
            )
        }
        val result = getOneSummonerLeagueEntryUseCase.getLeagueEntry(id).toList().first()
        assertEquals(leagueEntryDTO, result)
    }
}