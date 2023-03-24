package yunho.compose.domain.usecase

import yunho.compose.domain.repository.MatchRepository
import javax.inject.Inject

class GetOneMatchInfoUseCase @Inject constructor(private val matchRepository: MatchRepository) {
    suspend fun getInfo(matchId: String) = matchRepository.getMatchInfo(matchId)
}