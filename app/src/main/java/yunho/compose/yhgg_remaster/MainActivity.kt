package yunho.compose.yhgg_remaster

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import yunho.compose.yhgg_remaster.presentaion.state.MatchState
import yunho.compose.yhgg_remaster.presentaion.state.SummonerState
import yunho.compose.yhgg_remaster.presentaion.viewmodel.MatchViewModel
import yunho.compose.yhgg_remaster.presentaion.viewmodel.SummonerViewModel
import yunho.compose.yhgg_remaster.ui.theme.YHGGRemasterTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewmodel: SummonerViewModel by viewModels()
    val matchViewmodel: MatchViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            matchViewmodel.matchState.collect {
                when (it) {
                    is MatchState.Loading -> {
                        Log.e("state", "Loading")
                    }
                    is MatchState.LoadIds -> {
                        handleMatchIds(it)
                    }
                    is MatchState.Success -> {
                        handleMatch(it)
                    }
                    is MatchState.Error -> {
                        handleMatchError(it)
                    }
                    else -> {}
                }
            }
        }
        lifecycleScope.launch {
            viewmodel.getSummonerInfo("hide on bush")
            viewmodel.summonerState.collect {
                when (it) {
                    is SummonerState.Loading -> {
                        Log.e("state", "Loading")
                    }
                    is SummonerState.LoadLeagueEntry -> {
                        handleLeagueEntry(it)
                    }
                    is SummonerState.Success -> {
                        handleSuccess(it)
                    }
                    is SummonerState.Error -> {
                        handleError(it)
                    }
                    else -> {}
                }
            }
        }
        setContent {
            YHGGRemasterTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }

    private fun handleMatchError(it: MatchState.Error) {
        Log.e("match", "error: ${it.e.message}")
    }

    private fun handleMatch(it: MatchState.Success) {
        Log.e("match", "${it.matchData.info}")
    }

    private suspend fun handleMatchIds(it: MatchState.LoadIds) {
        Log.e("Match", "${it.ids}")
        for (id in it.ids) {
            matchViewmodel.getMatchInfo(id)
        }
    }

    private fun handleLeagueEntry(it: SummonerState.LoadLeagueEntry) {
        Log.e("state", "${it.data}")
    }

    private fun handleError(it: SummonerState.Error) {
        Log.e("state", "error: ${it.e.message}")
    }

    private suspend fun handleSuccess(it: SummonerState.Success) {
        Log.e("state", "success: ${it.infoData}")
        viewmodel.getSummonerLeague(it.infoData.id)
        matchViewmodel.getMatchIds(it.infoData.puuid)
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    YHGGRemasterTheme {
        Greeting("Android")
    }
}