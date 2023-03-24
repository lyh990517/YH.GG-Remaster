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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import yunho.compose.yhgg_remaster.presentaion.viewmodel.SummonerViewModel
import yunho.compose.yhgg_remaster.ui.theme.YHGGRemasterTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val viewmodel: SummonerViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            viewmodel.getSummonerInfo("hide on bush")
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
}

@Composable
fun Greeting(name: String, viewModel: SummonerViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val state = viewModel.summonerState.collectAsState()
    Text(text = "Hello ${state.value}!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    YHGGRemasterTheme {
        Greeting("Android")
    }
}