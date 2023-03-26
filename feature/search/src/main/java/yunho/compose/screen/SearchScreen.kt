package yunho.compose.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Scaffold
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchScreen(navController: NavController) {
    SearchContent(navController = navController)
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchContent(modifier: Modifier = Modifier,navController: NavController) {
    Scaffold(modifier = modifier) {
        Column {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .weight(1f)
                    .background(Color.Cyan)
            )
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f), horizontalArrangement = Arrangement.Center
            ) {
                SearchBar(navController)
            }
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .weight(1f)
                    .background(Color.Blue),
            )
        }
    }
}

@Composable
private fun SearchBar(navController: NavController) {
    val state = rememberSaveable {
        mutableStateOf(String())
    }
    TextField(
        value = state.value,
        onValueChange = { state.value = it },
        modifier = Modifier.background(Color.Gray),
        keyboardActions = KeyboardActions(onSearch = {
            navController.navigate("info/${state.value}")
        }),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search)
    )
}

@Preview
@Composable
fun SearchScreenPreview() {
    SearchContent(navController = rememberNavController())
}