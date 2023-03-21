package yunho.compose.yhgg_remaster.presentaion.state

import yunho.compose.yhgg_remaster.data.entity.SummonerDTO

sealed class SummonerState {
    object Loading : SummonerState()

    data class Success(val infoData: SummonerDTO) : SummonerState()

    data class Error(val e: Throwable) : SummonerState()
}
