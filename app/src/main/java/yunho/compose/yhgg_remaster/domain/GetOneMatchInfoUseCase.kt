package yunho.compose.yhgg_remaster.domain

import yunho.compose.yhgg_remaster.data.repository.MatchRepository
import javax.inject.Inject

class GetOneMatchInfoUseCase @Inject constructor(private val matchRepository: MatchRepository) {
    suspend fun getInfo(matchId: String) = matchRepository.getMatchInfo(matchId)
}