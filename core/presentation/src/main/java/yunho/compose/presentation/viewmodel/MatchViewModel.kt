package yunho.compose.yhgg_remaster.presentaion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import yunho.compose.domain.usecase.GetMatchIdsUseCase
import yunho.compose.domain.usecase.GetOneMatchInfoUseCase
import yunho.compose.yhgg_remaster.presentaion.state.MatchState
import javax.inject.Inject

@HiltViewModel
class MatchViewModel @Inject constructor(
    private val getMatchIdsUseCase: GetMatchIdsUseCase,
    private val getOneMatchInfoUseCase: GetOneMatchInfoUseCase
) : ViewModel() {
    private val _matchState = MutableStateFlow<MatchState>(MatchState.Loading)
    val matchState = _matchState

    fun getMatchIds(puuid: String) = viewModelScope.launch {
        getMatchIdsUseCase.getMatchIds(puuid).catch {
            _matchState.value = MatchState.Error(it)
        }.collect {
            _matchState.value = MatchState.LoadIds(it)
        }
    }
    fun getMatchInfo(matchId: List<String>) = viewModelScope.launch {
        matchId.forEach { id ->
            getOneMatchInfoUseCase.getInfo(id).catch {
                _matchState.value = MatchState.Error(it)
            }.collect {
                _matchState.value = MatchState.Success(it)
            }
        }
    }
}