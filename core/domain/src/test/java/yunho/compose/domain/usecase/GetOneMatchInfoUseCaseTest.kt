package yunho.compose.domain.usecase

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import yunho.compose.domain.model.InfoDTO
import yunho.compose.domain.model.MatchDTO
import yunho.compose.domain.model.MetadataDTO
import yunho.compose.domain.repository.MatchRepository


internal class GetOneMatchInfoUseCaseTest {

    @MockK
    private lateinit var matchRepository: MatchRepository
    private lateinit var getOneMatchInfoUseCase: GetOneMatchInfoUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getOneMatchInfoUseCase = GetOneMatchInfoUseCase(matchRepository)
    }

    @Test
    fun `getInfo should emit MatchDTO`() = runBlocking {
        val matchId = "1"
        val matchDTO = mockk<MatchDTO>()
        val infoDTO = mockk<InfoDTO>()
        val metadataDTO = mockk<MetadataDTO>()
        every { matchDTO.metadata } returns metadataDTO
        every { matchDTO.info } returns infoDTO

        coEvery { matchRepository.getMatchInfo(matchId) } returns flow { emit(matchDTO) }

        val result = getOneMatchInfoUseCase.getInfo(matchId).toList().first()

        assertEquals(matchDTO, result)
    }
}