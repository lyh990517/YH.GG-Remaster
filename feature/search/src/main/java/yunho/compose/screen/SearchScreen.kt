package yunho.compose.screen

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import yunho.compose.yhgg_remaster.presentaion.viewmodel.SummonerViewModel

@Composable
fun SearchScreen(viewModel: SummonerViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val state = viewModel.summonerState.collectAsState()
    Text(text = "${state.value}")
}

@Preview
@Composable
fun SearchScreenDetail() {
    SearchScreen()
}