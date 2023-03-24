package yunho.compose.data.repository

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import yunho.compose.domain.model.LeagueEntryDTO
import yunho.compose.domain.model.SummonerDTO
import yunho.compose.yhgg_remaster.data.api.SummonerDataSource


internal class SummonerRepositoryImplTest {


    @MockK
    private lateinit var summonerDataSource: SummonerDataSource
    private lateinit var summonerRepository: SummonerRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        summonerRepository = SummonerRepositoryImpl(summonerDataSource)
    }

    @Test
    fun `getSummonerInfo should emit summonerDTO`() = runBlocking {
        // Given
        val summonerName = "TestSummoner"
        val summonerDTO = SummonerDTO("TestSummoner", 123, 1, "", "", "", 12)
        coEvery { summonerDataSource.getSummonerInfoByName(summonerName) } returns Response.success(
            summonerDTO
        )

        // When
        val result = summonerRepository.getSummonerInfo(summonerName).toList().first()
        // Then
        assertEquals(summonerDTO, result)
    }

    @Test
    fun getSummonerLeagueEntry() = runBlocking {
        val id = "summonerId"
        val given =
            LeagueEntryDTO(
                leagueId = "leagueId",
                summonerId = "summonerId",
                summonerName = "summonerName",
                queueType = "queueType",
                tier = "tier",
                rank = "rank",
                leaguePoints = 100,
                wins = 10,
                losses = 5,
                hotStreak = true,
                veteran = false,
                freshBlood = false,
                inactive = false,
                miniSeries = null
            )
        coEvery { summonerDataSource.getSummonerEntriesByEncryptedSummonerID(id) } returns Response.success(
            listOf(given)
        )
        val result = summonerRepository.getSummonerLeagueEntry(id).toList().first()
        //then
        assertEquals(listOf(given),result)
    }
}