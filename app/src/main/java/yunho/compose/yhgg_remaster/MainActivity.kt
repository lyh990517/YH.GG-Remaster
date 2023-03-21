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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import yunho.compose.yhgg_remaster.presentaion.state.SummonerState
import yunho.compose.yhgg_remaster.presentaion.viewmodel.MainViewModel
import yunho.compose.yhgg_remaster.ui.theme.YHGGRemasterTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewmodel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            viewmodel.getSummonerInfo("lyh123")
            viewmodel.summonerState.collect {
                when (it) {
                    is SummonerState.Loading -> {
                        Log.e("state", "Loading")
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

    private fun handleError(it: SummonerState.Error) {
        Log.e("state", "error: ${it.e.message}")
    }

    private fun handleSuccess(it: SummonerState.Success) {
        Log.e("state", "success: ${it.infoData}")
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