package com.betterdevs.betterme.ui.posts.post_creation

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.betterdevs.betterme.data.repository.PostRepository
import com.betterdevs.betterme.domain_model.Post
import com.betterdevs.betterme.domain_model.PostCategory
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

data class PostCreationState (
    val title: String = "",
    val description: String = "",
    val isEatingCategorySelected: Boolean = false,
    val isExerciseCategorySelected: Boolean = false,
    val isMedicineCategorySelected: Boolean = false,
    val isHealthCategorySelected: Boolean = false,
    val multimediaUri: Uri? = null,

    val isTitleWrong: Boolean = false,
    val isCategorySelectionWrong: Boolean = false,
    val isDescriptionWrong: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
)

class PostCreationViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext

    private val postRepository = PostRepository(context)

    private val _state = mutableStateOf(PostCreationState())
    val state: State<PostCreationState> = _state

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage: SharedFlow<String> = _snackbarMessage

    fun onTitleChanged(title: String) {
        _state.value = _state.value.copy(
            title = title,
            isTitleWrong = false
        )
    }

    fun onDescriptionChanged(description: String) {
        if (description.length < 300) {
            _state.value = _state.value.copy(
                description = description,
                isDescriptionWrong = false
            )
        }
    }

    fun onCategorySelected(category: PostCategory) {
        _state.value = _state.value.copy(
            isEatingCategorySelected = category == PostCategory.EATING,
            isExerciseCategorySelected = category == PostCategory.EXERCISE,
            isMedicineCategorySelected = category == PostCategory.MEDICINE,
            isHealthCategorySelected = category == PostCategory.HEALTH,
            isCategorySelectionWrong = false
        )
    }

    fun onImageSelected(uri: Uri?) {
        _state.value = _state.value.copy(
            multimediaUri = uri
        )
    }

    private fun validateFields(): Boolean {
        val isTitleValid = _state.value.title.isNotBlank()
        val isDescriptionValid = _state.value.description.isNotBlank()
        val isCategoryValid = _state.value.isEatingCategorySelected
                || _state.value.isExerciseCategorySelected
                || _state.value.isMedicineCategorySelected
                || _state.value.isHealthCategorySelected

        _state.value = _state.value.copy(
            isTitleWrong = !isTitleValid,
            isDescriptionWrong = !isDescriptionValid,
            isCategorySelectionWrong = !isCategoryValid
        )

        return isTitleValid && isDescriptionValid && isCategoryValid
    }

    fun createPost() {
        if (!validateFields()) return

        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true
            )

            val category = when {
                _state.value.isEatingCategorySelected -> PostCategory.EATING
                _state.value.isExerciseCategorySelected -> PostCategory.EXERCISE
                _state.value.isMedicineCategorySelected -> PostCategory.MEDICINE
                else -> PostCategory.HEALTH
            }
            val post = Post(
                title = _state.value.title,
                description = _state.value.description,
                category = category,
                multimediaUri = _state.value.multimediaUri
            )
            val response = postRepository.createPost(post)

            if (response.success) {
               _state.value = _state.value.copy(
                   title = "",
                   description = "",
                   isEatingCategorySelected = false,
                   isExerciseCategorySelected = false,
                   isMedicineCategorySelected = false,
                   isHealthCategorySelected = false,
                   multimediaUri = null
               )
               _snackbarMessage.emit(response.message)
            } else {
                _snackbarMessage.emit(response.message)
            }

            _state.value = _state.value.copy(
                isLoading = false
            )
        }
    }
}