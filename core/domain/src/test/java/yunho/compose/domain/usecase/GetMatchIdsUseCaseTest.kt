package yunho.compose.domain.usecase

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import yunho.compose.domain.repository.MatchRepository

internal class GetMatchIdsUseCaseTest {

    @MockK
    private lateinit var matchRepository: MatchRepository


    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun getMatchIds() = runBlocking {
        val puuid = "1"
        val matchIds = listOf("1", "2", "3")

        coEvery { matchRepository.getMatchId(puuid) } returns flow { emit(matchIds) }

        val result = GetMatchIdsUseCase(matchRepository).getMatchIds(puuid).toList().first()

        assertEquals(matchIds,result)
    }
}