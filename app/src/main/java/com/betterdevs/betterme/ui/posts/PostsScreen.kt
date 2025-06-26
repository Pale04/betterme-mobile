package com.betterdevs.betterme.ui.posts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.betterdevs.betterme.R

@Composable
fun PostsScreen(
    onCreatePostClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Box (
        modifier = modifier.fillMaxSize()
    ) {
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PostsScreenPreview() {
    PostsScreen(onCreatePostClick = {})
}