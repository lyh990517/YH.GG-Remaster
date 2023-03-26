package yunho.compose.data.repository

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import yunho.compose.domain.model.InfoDTO
import yunho.compose.domain.model.MatchDTO
import yunho.compose.domain.model.MetadataDTO
import yunho.compose.yhgg_remaster.data.api.MatchDataSource

internal class MatchRepositoryImplTest {

    @MockK
    private lateinit var matchDataSource: MatchDataSource

    private lateinit var matchRepositoryImpl: MatchRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        matchRepositoryImpl = MatchRepositoryImpl(matchDataSource)
    }

    @Test
    fun `getMatchId should emit listOf string`() = runBlocking {
        val puuid = "123"
        val idListOfMatch = listOf("matchId1", "matchId1", "matchId3")

        coEvery { matchDataSource.getMatchIdBypuuid(puuid, 1, 10) } returns Response.success(
            idListOfMatch
        )
        val result = matchRepositoryImpl.getMatchId(puuid).toList().first()
        assertEquals(idListOfMatch, result)
    }

    @Test
    fun `getMatchInfo should emit matchDTO`() = runBlocking {
        val matchId = "matchId"
        val metadata = mockk<MetadataDTO>()
        val info = mockk<InfoDTO>()
        val matchDTO = mockk<MatchDTO>()
        every { matchDTO.metadata } returns metadata
        every { matchDTO.info } returns info

        coEvery { matchDataSource.getMatchInfoByMatchID(matchId) } returns Response.success(matchDTO)

        val result = matchRepositoryImpl.getMatchInfo(matchId).toList().first()

        assertEquals(matchDTO, result)
    }
}