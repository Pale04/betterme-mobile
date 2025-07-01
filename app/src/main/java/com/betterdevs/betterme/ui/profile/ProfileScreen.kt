package com.betterdevs.betterme.ui.profile

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.betterdevs.betterme.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    onEditProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel()
){
    val state by viewModel.state
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect (key1 = Unit) {
        viewModel.getUserDetails()
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
        modifier = modifier
    ) {
        Column (
            modifier = Modifier.padding(16.dp).fillMaxSize()
        ) {
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image (
                    painter = painterResource(R.drawable.betterme_logo),
                    contentDescription = "Profile image",
                    modifier = Modifier.padding(end = 8.dp).size(50.dp).border(1.dp, Color.Black, androidx.compose.foundation.shape.RoundedCornerShape(5.dp))
                )
                Text(
                    text = state.username,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                )
                if (state.isVerified) {
                    Image (
                        painter = painterResource(R.drawable.verified_icon),
                        contentDescription = "Profile image",
                        modifier = Modifier.padding(start = 16.dp).size(25.dp)
                    )
                }
            }

            if (state.description.isNotEmpty()) {
                ProfileSection(
                    title = R.string.profile_description_label,
                    content = state.description,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            ProfileSection(
                title = R.string.profile_birthday_label,
                content = state.birthday,
                modifier = Modifier.padding(top = 16.dp)
            )
            if (state.webSite.isNotEmpty()) {
                ProfileSection(
                    title = R.string.profile_web_site_label,
                    content = state.webSite,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            Button(
                onClick = onEditProfileClick,
                modifier = Modifier
                    .padding(top = 16.dp,bottom = 8.dp)
                    .fillMaxWidth()
            ) {
                Text( text = stringResource(R.string.profile_edit_button) )
            }

        }
    }
}

@Composable
fun ProfileSection(@StringRes title: Int, content: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(title),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = content,
            fontSize = 16.sp,
            modifier = Modifier
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview(){
    ProfileScreen({})
}