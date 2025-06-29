package com.betterdevs.betterme.ui.posts.post_creation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.betterdevs.betterme.R
import com.betterdevs.betterme.domain_model.PostCategory
import com.betterdevs.betterme.ui.shared.ImagePicker
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PostCreationScreen(
    modifier: Modifier = Modifier,
    viewModel: PostCreationViewModel = viewModel()
) {
    val state by viewModel.state
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        viewModel.snackbarMessage.collectLatest { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message = message)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text (
                text = stringResource(R.string.post_creation_title),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
            )

            OutlinedTextField(
                value = state.title,
                onValueChange = { viewModel.onTitleChanged(it) },
                label = { Text( stringResource(R.string.post_creation_title_input_label)) },
                isError = state.isTitleWrong,
                supportingText = {
                    if (state.isTitleWrong) {
                        Text( text = stringResource(R.string.post_creation_title_error) )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Column (modifier = Modifier.padding(top = 8.dp)) {
                Text(
                    text = stringResource(R.string.post_creation_category_label),
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row (modifier = Modifier.fillMaxWidth()) {
                    FilterChip(
                        onClick = { viewModel.onCategorySelected(PostCategory.EATING) },
                        label = { Text(
                            text = stringResource(R.string.post_eating_category),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )},
                        selected = state.isEatingCategorySelected,
                        leadingIcon = if (state.isEatingCategorySelected) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "Done icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        } else {
                            null
                        },
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    )
                    FilterChip(
                        onClick = { viewModel.onCategorySelected(PostCategory.EXERCISE) },
                        label = { Text(
                            text = stringResource(R.string.post_exercise_category),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )},
                        selected = state.isExerciseCategorySelected,
                        leadingIcon = if (state.isExerciseCategorySelected) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "Done icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        } else {
                            null
                        },
                        modifier = Modifier.weight(1f).padding(start = 8.dp)
                    )
                }
                Row (Modifier.fillMaxWidth()) {
                    FilterChip(
                        onClick = { viewModel.onCategorySelected(PostCategory.HEALTH) },
                        label = { Text(
                            text = stringResource(R.string.post_health_category),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )},
                        selected = state.isHealthCategorySelected,
                        leadingIcon = if (state.isHealthCategorySelected) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "Done icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        } else {
                            null
                        },
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    )
                    FilterChip(
                        onClick = { viewModel.onCategorySelected(PostCategory.MEDICINE) },
                        label = { Text(
                            text = stringResource(R.string.post_medicine_category),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )},
                        selected = state.isMedicineCategorySelected,
                        leadingIcon = if (state.isMedicineCategorySelected) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "Done icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        } else {
                            null
                        },
                        modifier = Modifier.weight(1f).padding(start = 8.dp)
                    )
                }
                if (state.isCategorySelectionWrong) {
                    Text(
                        text = stringResource(R.string.post_creation_category_error),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Red,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            OutlinedTextField(
                value = state.description,
                onValueChange = { viewModel.onDescriptionChanged(it) },
                label = { Text(stringResource(R.string.post_creation_description_input_label)) },
                maxLines = 5,
                isError = state.isDescriptionWrong,
                supportingText = {
                    if (state.isDescriptionWrong) {
                        Text( text = stringResource(R.string.post_creation_description_error), color = Color.Red )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp)
                    .height(180.dp)
            )

            if (state.multimediaUri == null) {
                ImagePicker(
                    onImageSelected = { uri -> viewModel.onImageSelected(uri) },
                    modifier = Modifier.padding(bottom = 16.dp)
                ) { onClick ->
                    FilledTonalButton (
                        onClick = onClick,
                        modifier = Modifier.fillMaxWidth()
                    ){
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = "Add multimedia icon",
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(text = stringResource(R.string.post_creation_select_multimedia))
                    }
                }
            } else {
                Box (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Image (
                        painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(state.multimediaUri)
                                .crossfade(true)
                                .build()
                        ),
                        contentDescription = "Multimedia selected",
                        modifier = Modifier
                            .height(500.dp)
                            .align(Alignment.Center),
                        contentScale = ContentScale.Fit
                    )
                    FilledTonalIconButton (
                        onClick = { viewModel.onImageSelected(null) },
                        modifier = Modifier.align(Alignment.TopEnd),

                    ){
                        Icon(
                            imageVector = Icons.Outlined.Clear,
                            contentDescription = "Remove multimedia icon",
                            modifier = Modifier
                                .padding(8.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                enabled = !state.isLoading,
                onClick = { viewModel.createPost() }
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text(text = stringResource(R.string.post_creation_save_button))
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PostCreationPreview() {
    PostCreationScreen()
}

