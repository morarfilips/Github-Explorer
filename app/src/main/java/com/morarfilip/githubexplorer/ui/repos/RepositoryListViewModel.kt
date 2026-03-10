package com.morarfilip.githubexplorer.ui.repos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.morarfilip.githubexplorer.core.domain.usecase.GetRepositoriesUseCase
import com.morarfilip.githubexplorer.core.model.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

sealed interface RepoUiState {
    object Loading : RepoUiState
    data class Success(val repos: List<Repository>) : RepoUiState
    data class Error(val message: String) : RepoUiState
}

@HiltViewModel
class RepoListViewModel @Inject constructor(
    private val getRepositoriesUseCase: GetRepositoriesUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val refreshTrigger = MutableStateFlow(0)

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<RepoUiState> = combine(
        _searchQuery.debounce(1000L),
        refreshTrigger
    ) { query, _ -> query }
        .flatMapLatest { query ->
            flow {
                emit(RepoUiState.Loading)
                delay(500)

                val result = getRepositoriesUseCase(query)
                result.onSuccess {
                    emit(RepoUiState.Success(it))
                }.onFailure {
                    emit(RepoUiState.Error(it.message ?: "Unknown Error"))
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RepoUiState.Loading
        )

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun refresh() {
        refreshTrigger.value ++
    }
}