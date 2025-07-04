package com.betterdevs.betterme.ui.shared

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.betterdevs.betterme.R
import com.betterdevs.betterme.data.shared.value
import com.betterdevs.betterme.domain_model.Post
import com.betterdevs.betterme.domain_model.PostCategory
import com.betterdevs.betterme.domain_model.PostStatus
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun PostItem(
    post: Post,
    onReportedClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val openReportDialog = remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image (
                    painter = painterResource(R.drawable.profile_image_placeholder),
                    contentDescription = "Profile image",
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(50.dp)
                        .border(
                            1.dp,
                            Color.Black,
                            RoundedCornerShape(5.dp)
                        )
                )
                Column(modifier = Modifier.weight(1f)) {
                    Row () {
                        Text(
                            text = post.authorUsername,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                        )
                        if (post.authorIsVerified) {
                            Image (
                                painter = painterResource(R.drawable.verified_icon),
                                contentDescription = "Profile image",
                                modifier = Modifier
                                    .padding(start = 16.dp)
                                    .size(25.dp)
                            )
                        }
                    }

                    Text(
                        text = post.category.value(),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = formatDate(post.timeStamp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Column() {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More options")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Reportar") },
                            onClick = { openReportDialog.value = true }
                        )
                    }
                }
            }

            Text(
                post.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            post.multimedia?.let {
                val bitmap = BitmapFactory.decodeByteArray(post.multimedia, 0, post.multimedia?.size ?: 0)
                val imageBitmap = bitmap.asImageBitmap()
                Image(
                    bitmap = imageBitmap,
                    contentDescription = "Imagen del post",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
            }
            Text(
                post.description,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        if (openReportDialog.value) {
            ReportPostDialog(
                onDismissRequest = { openReportDialog.value = false },
                onConfirmation = { reason ->
                    onReportedClick(reason)
                    openReportDialog.value = false
                }
            )
        }
    }
}

@Composable
fun ReportPostDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit,
) {
    var reasonState by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Column (modifier = Modifier.background(Color.White).padding(16.dp)) {
            Text(
                text = "Reportar publicación",
                fontWeight = FontWeight.Bold
            )
            Text(text = "Introduce la razón de tu reporte")
            OutlinedTextField(
                value = reasonState,
                onValueChange = { reasonState = it },
                label = { Text("Razón") },
                isError = isError,
                supportingText = {
                    if (isError) {
                        Text("Debes introducir una razón")
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 8.dp)
            )
            Row (horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                TextButton(onClick = onDismissRequest) {
                    Text("Cancelar")
                }
                Button(
                    onClick = {
                        if (reasonState.isBlank()) {
                            isError = true
                        } else {
                            isError = false
                            onConfirmation(reasonState)
                            onDismissRequest()
                        }
                    }
                ) {
                    Text("Enviar")
                }

            }
        }
    }
}

fun formatDate(instant: Instant): String {
    val localDate = instant.atZone(ZoneId.of("UTC")).toLocalDate()
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
    return localDate.format(formatter)
}

@Preview(showBackground = true, showSystemUi = false)
@Composable
fun PostItemPreview() {
    PostItem(
        post = Post(
            id = "",
            title = "Post de ejemplo",
            description = "Este es un post de ejemplo con una descripción.",
            category = PostCategory.HEALTH,
            userId = "123",
            timeStamp = Instant.now(),
            status = PostStatus.PUBLISHED,
            multimediaUri = null,
            authorUsername = "Snoopy",
            authorIsVerified = true
        ),
        onReportedClick = {}
    )
}