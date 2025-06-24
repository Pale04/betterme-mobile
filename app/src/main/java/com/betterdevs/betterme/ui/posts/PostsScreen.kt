package com.betterdevs.betterme.ui.posts

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun PostsScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    Column (modifier = modifier) {
        Text("Hola")
    }
}