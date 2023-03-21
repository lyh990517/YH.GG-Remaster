package yunho.compose.yhgg_remaster.presentaion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import yunho.compose.yhgg_remaster.domain.GetOneSummonerInfoUseCase
import yunho.compose.yhgg_remaster.presentaion.state.SummonerState
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getOneSummonerInfoUseCase: GetOneSummonerInfoUseCase
) : ViewModel() {
    private val _summonerState = MutableStateFlow<SummonerState>(SummonerState.Loading)
    val summonerState = _summonerState
    suspend fun getSummonerInfo(summonerName: String) = viewModelScope.launch {
        getOneSummonerInfoUseCase.getInfo(summonerName).catch {
            _summonerState.value = SummonerState.Error(it)
        }.collect {
            _summonerState.value = SummonerState.Success(it)
        }
    }
}