package com.betterdevs.betterme.ui.posts

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.betterdevs.betterme.R
import com.betterdevs.betterme.domain_model.PostCategory
import com.betterdevs.betterme.ui.shared.PostItem
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PostsScreen(
    onCreatePostClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PostsViewModel = viewModel()
){
    val state by viewModel.state
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect (key1 = Unit) {
        viewModel.getPosts()
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.snackbarMessage.collectLatest { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message = message)
            }
        }
    }

    Scaffold (
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier.fillMaxSize()
    ) {
        Box (
            modifier = Modifier.fillMaxSize()
        ) {
            Column(Modifier.fillMaxSize()) {
                Row (Modifier.fillMaxWidth()) {
                    Text( text = "Cateagoría: " + when (state.category) {
                        PostCategory.HEALTH -> "Salud"
                        PostCategory.MEDICINE -> "Medicina"
                        PostCategory.EXERCISE -> "Ejercicio"
                        else -> "Alimentación"
                    },
                        modifier = Modifier.padding(8.dp).weight(1f)
                    )
                    Column() {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "More options")
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Salud") },
                                onClick = { viewModel.onCategoryChange(PostCategory.HEALTH) }
                            )
                            DropdownMenuItem(
                                text = { Text("Ejercicio") },
                                onClick = { viewModel.onCategoryChange(PostCategory.EXERCISE) }
                            )
                            DropdownMenuItem(
                                text = { Text("Alimentación") },
                                onClick = { viewModel.onCategoryChange(PostCategory.EATING) }
                            )
                            DropdownMenuItem(
                                text = { Text("Medicina") },
                                onClick = { viewModel.onCategoryChange(PostCategory.MEDICINE) }
                            )
                        }
                    }
                }
                PostList(
                    state = state,
                    onReportedClick = { postId, reason ->
                        viewModel.reportPost(postId, reason)
                    }
                )
            }
            ExtendedFloatingActionButton(
                onClick = { onCreatePostClick() },
                icon = { Icon(
                    imageVector = Icons.Outlined.Create,
                    contentDescription = "Create post"
                )},
                text = { Text(text = stringResource(R.string.posts_create_post_button)) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun PostList(
    state: PostsState,
    onReportedClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.isLoading) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
    else if (state.posts.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No hay publicaciones aún.")
        }
    } else {
        val listState = rememberLazyListState()

        LazyColumn(
            state = listState,
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = state.posts,
                key = { post -> post.id }
            ) { post ->
                PostItem(
                    post = post,
                    onReportedClick = { reason ->
                        onReportedClick(post.id, reason)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PostsScreenPreview() {
    PostsScreen(onCreatePostClick = {})
}