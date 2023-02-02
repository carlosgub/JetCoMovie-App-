@file:OptIn(ExperimentalComposeUiApi::class)

package com.example.moviejetpackcompose.features.search.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.example.moviejetpackcompose.core.sealed.GenericState
import com.example.moviejetpackcompose.features.movie.ui.model.MovieModel
import com.example.moviejetpackcompose.helpers.getDataFromUiState
import com.example.moviejetpackcompose.helpers.showLoading
import com.example.moviejetpackcompose.ui.theme.TextFieldBackgroundColor
import com.example.moviejetpackcompose.ui.theme.TextFieldTextColor
import com.example.moviejetpackcompose.ui.views.LazyVerticalGridMovies
import com.example.moviejetpackcompose.ui.views.Loading
import kotlinx.coroutines.Dispatchers

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    mainNavController: NavController
) {

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val queryValue: String by viewModel.query.asLiveData(Dispatchers.Main)
        .observeAsState(initial = "")
    val uiState by produceState<GenericState<List<MovieModel>>>(
        initialValue = GenericState.Loading,
        key1 = lifecycle,
        key2 = viewModel,
        producer = {
            lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    value = it
                }
            }
        }
    )

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (query, loading, content) = createRefs()
        val focusRequester = remember { FocusRequester() }
        val keyboardController = LocalSoftwareKeyboardController.current
        TextField(
            value = queryValue,
            onValueChange = {
                viewModel.queryFieldChange(it)
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = TextFieldTextColor
                )
            },
            singleLine = true,
            maxLines = 1,
            textStyle = TextStyle(
                fontSize = 14.sp
            ),
            placeholder = {
                Text(
                    text = "Search Movie ...",
                    fontSize = 14.sp,
                    color = TextFieldTextColor,
                    modifier = Modifier.focusRequester(focusRequester)
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = TextFieldBackgroundColor,
                textColor = TextFieldTextColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                }
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
                keyboardType = KeyboardType.Text
            ),
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .clip(RoundedCornerShape(25.dp))
                .constrainAs(query) {
                    linkTo(
                        start = parent.start,
                        startMargin = 24.dp,
                        end = parent.end,
                        endMargin = 24.dp
                    )
                    top.linkTo(parent.top, 24.dp)
                    width = Dimension.fillToConstraints
                    height = Dimension.value(56.dp)
                }
        )
        if (showLoading(uiState) && queryValue.length > 3) {
            Loading(
                modifier = Modifier.constrainAs(loading) {
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                    linkTo(
                        top = query.bottom,
                        bottom = parent.bottom
                    )
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
            )
        } else {
            val list = getDataFromUiState(uiState) ?: listOf()
            LazyVerticalGridMovies(
                list = list,
                modifier = Modifier.constrainAs(content) {
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                    linkTo(
                        top = query.bottom,
                        bottom = parent.bottom
                    )
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
            ) {
                mainNavController.navigate("detail/$it")
            }
        }
    }
}