package com.example.moviejetpackcompose.ui.features.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.example.moviejetpackcompose.R
import com.example.moviejetpackcompose.core.sealed.GenericState
import com.example.moviejetpackcompose.helpers.getDataFromUiState
import com.example.moviejetpackcompose.ui.features.model.MovieModel
import com.example.moviejetpackcompose.ui.theme.*
import com.example.moviejetpackcompose.ui.views.LazyVerticalGridMovies
import com.example.moviejetpackcompose.ui.views.Loading

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    mainNavController: NavController,
    modifier: Modifier = Modifier
) {

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val keyboardController = LocalSoftwareKeyboardController.current
    val queryValue: String by viewModel.query.observeAsState(initial = "")
    val showLoading: Boolean by viewModel.loading.observeAsState(initial = false)
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
        modifier = modifier.fillMaxSize()
    ) {
        val (query, content) = createRefs()
        SearchTextField(
            queryValue = queryValue,
            keyboardController = keyboardController,
            modifier = Modifier.constrainAs(query) {
                linkTo(
                    start = parent.start,
                    startMargin = spacing_6,
                    end = parent.end,
                    endMargin = spacing_6
                )
                top.linkTo(parent.top, spacing_6)
                width = Dimension.fillToConstraints
            }
        ) {
            viewModel.queryFieldChange(it)
        }
        SearchContent(
            uiState = uiState,
            queryValue = queryValue,
            keyboardController = keyboardController,
            showLoading = showLoading,
            modifier = Modifier.constrainAs(content) {
                linkTo(
                    start = parent.start,
                    end = parent.end
                )
                linkTo(
                    top = query.bottom,
                    topMargin = spacing_3,
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

@Composable
fun SearchTextField(
    queryValue: String,
    keyboardController: SoftwareKeyboardController?,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    TextField(
        value = queryValue,
        onValueChange = {
            onValueChange(it)
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
        textStyle = TextFieldStyle,
        placeholder = {
            Text(
                text = "Search Movie ...",
                style = TextFieldStyle
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
                focusManager.clearFocus()
                keyboardController?.hide()
            }
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
            keyboardType = KeyboardType.Text
        ),
        shape = MaterialTheme.shapes.small,
        modifier = modifier
            .clip(RoundedShape)
    )
}

@Composable
fun SearchContent(
    uiState: GenericState<List<MovieModel>>,
    queryValue: String,
    keyboardController: SoftwareKeyboardController?,
    showLoading: Boolean,
    modifier: Modifier = Modifier,
    onItemListClicked: (Int) -> Unit
) {
    ConstraintLayout(modifier = modifier) {
        val (hint, loading, listRef, noMovies) = createRefs()
        if (queryValue.length < 3) {
            Text(
                text = "Enter at least 3 characters to search something",
                style = Typography.body2,
                color = myColors.surface,
                textAlign = TextAlign.Center,
                modifier = Modifier.constrainAs(hint) {
                    linkTo(
                        start = parent.start,
                        startMargin = spacing_6,
                        end = parent.end,
                        endMargin = spacing_6
                    )
                    top.linkTo(parent.top, spacing_4)
                }
            )
        } else if (showLoading && queryValue.isNotEmpty()) {
            Loading(
                modifier = Modifier.constrainAs(loading) {
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                    linkTo(
                        top = parent.top,
                        bottom = parent.bottom
                    )
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
            )
        } else {
            val list = getDataFromUiState(uiState) ?: listOf()
            val state = rememberLazyGridState()
            val scroll = remember { derivedStateOf { state.firstVisibleItemScrollOffset } }
            if (scroll.value > 0) {
                keyboardController?.hide()
            }
            if (list.isEmpty()) {
                ShowNoMoviesFound(
                    modifier = Modifier
                        .constrainAs(noMovies) {
                            linkTo(
                                start = parent.start,
                                end = parent.end
                            )
                            linkTo(
                                top = parent.top,
                                bottom = parent.bottom
                            )
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        }
                )
            } else {
                LazyVerticalGridMovies(
                    list = list,
                    state = state,
                    contentPaddingValues = PaddingValues(bottom = spacing_3),
                    modifier = Modifier.constrainAs(listRef) {
                        linkTo(
                            start = parent.start,
                            end = parent.end
                        )
                        linkTo(
                            top = parent.top,
                            bottom = parent.bottom
                        )
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                ) {
                    onItemListClicked(it)
                }
            }

        }
    }
}

@Composable
fun ShowNoMoviesFound(
    modifier: Modifier = Modifier
) {
    ConstraintLayout(modifier = modifier) {
        val (lottie, spacer, text) = createRefs()
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.no_found))
        val progressLottie by animateLottieCompositionAsState(
            composition,
            iterations = LottieConstants.IterateForever
        )
        LottieAnimation(
            composition = composition,
            progress = { progressLottie },
            modifier = Modifier
                .height(view_50)
                .constrainAs(lottie) {
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                    linkTo(
                        top = parent.top,
                        bottom = spacer.top
                    )
                }
        )
        Spacer(
            modifier = Modifier
                .size(view_6)
                .constrainAs(spacer) {
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                    linkTo(
                        top = lottie.bottom,
                        bottom = text.top
                    )
                }
        )
        Text(
            text = "No Movies Found",
            color = Color.White,
            modifier = Modifier
                .constrainAs(text) {
                    linkTo(
                        start = parent.start,
                        end = parent.end
                    )
                    linkTo(
                        top = spacer.bottom,
                        bottom = parent.bottom
                    )
                }
        )
        createVerticalChain(lottie, spacer, text, chainStyle = ChainStyle.Packed)
    }
}
