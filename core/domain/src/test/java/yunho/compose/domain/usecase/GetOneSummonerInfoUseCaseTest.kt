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
import yunho.compose.domain.model.SummonerDTO
import yunho.compose.domain.repository.SummonerRepository

class GetOneSummonerInfoUseCaseTest {

    @MockK
    private lateinit var summonerRepository: SummonerRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun getInfo() = runBlocking {
        val summonerName = "lyh123"
        val summonerDTO = mockk<SummonerDTO>()

        coEvery { summonerRepository.getSummonerInfo(summonerName) } returns flow { emit(summonerDTO) }
        val result = GetOneSummonerInfoUseCase(summonerRepository).getInfo(summonerName).toList().first()

        assertEquals(summonerDTO, result)
    }
}