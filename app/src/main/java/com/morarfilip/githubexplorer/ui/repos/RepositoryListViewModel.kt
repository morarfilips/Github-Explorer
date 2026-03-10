package com.morarfilip.githubexplorer.ui.repos

import androidx.annotation.VisibleForTesting
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

sealed interface RepositoryUiState {
    object Loading : RepositoryUiState
    data class Success(val repos: List<Repository>) : RepositoryUiState
    data class Error(val message: String) : RepositoryUiState
}

sealed interface RepositoryListIntent {
    data class SearchQueryChanged(val query: String) : RepositoryListIntent
    data class RepositoryClicked(val repo: Repository) : RepositoryListIntent
    object RefreshTriggered : RepositoryListIntent
}

@HiltViewModel
class RepositoryListViewModel @Inject constructor(
    private val getRepositoriesUseCase: GetRepositoriesUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val refreshTrigger = MutableStateFlow(0)

    fun onIntent(intent: RepositoryListIntent) {
        when (intent) {
            is RepositoryListIntent.SearchQueryChanged -> onSearchQueryChanged(intent.query)
            is RepositoryListIntent.RefreshTriggered -> onRefresh()
            is RepositoryListIntent.RepositoryClicked -> {
                // navigation done in UI
            }
        }
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<RepositoryUiState> = combine(
        _searchQuery.debounce(1000L),
        refreshTrigger
    ) { query, _ -> query }
        .flatMapLatest { query ->
            flow {
                emit(RepositoryUiState.Loading)
                delay(500)

                val result = getRepositoriesUseCase(query)
                result.onSuccess {
                    emit(RepositoryUiState.Success(it))
                }.onFailure {
                    emit(RepositoryUiState.Error(it.message ?: "Unknown Error"))
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RepositoryUiState.Loading
        )

    @VisibleForTesting
    internal fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    @VisibleForTesting
    internal fun onRefresh() {
        refreshTrigger.value ++
    }
}