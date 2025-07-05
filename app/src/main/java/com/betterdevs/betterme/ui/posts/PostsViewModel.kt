package com.betterdevs.betterme.ui.posts

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.betterdevs.betterme.data.repository.PostRepository
import com.betterdevs.betterme.data.repository.ReportsRepository
import com.betterdevs.betterme.domain_model.Post
import com.betterdevs.betterme.domain_model.PostCategory
import com.betterdevs.betterme.domain_model.Report
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

data class PostsState (
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean = false,
    val category: PostCategory = PostCategory.HEALTH
)

class PostsViewModel(application: Application) : AndroidViewModel(application)  {
    private val context = application.applicationContext

    private val repository: PostRepository = PostRepository(context)
    private val reportsRepository: ReportsRepository = ReportsRepository(context)

    private val _state = mutableStateOf(PostsState())
    val state: State<PostsState> = _state

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage: SharedFlow<String> = _snackbarMessage

    fun onCategoryChange(category: PostCategory) {
        _state.value = _state.value.copy(
            category = category
        )
        getPosts()
    }

    fun getPosts() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true
            )

            val postsResult = repository.getPosts(_state.value.category)

            if (postsResult.success) {
                val postsWithMultimedia = mutableListOf<Post>()
                for(post in postsResult.data!!) {
                    val multimediaResult = repository.getPostMultimedia(post.id)
                    if (multimediaResult.success) {
                        post.multimedia = multimediaResult.data
                        postsWithMultimedia.add(post)
                    }
                }
                _state.value = _state.value.copy(
                    posts = postsWithMultimedia
                )
            } else {
                _snackbarMessage.emit(postsResult.message)
            }

            _state.value = _state.value.copy(
                isLoading = false
            )
        }
    }

    fun reportPost(postId: String, reason: String) {
        viewModelScope.launch {
            val report = Report(
                postId = postId,
                reason = reason
            )

            val result = reportsRepository.reportPost(report)
            _snackbarMessage.emit(result.message)
        }
    }
}