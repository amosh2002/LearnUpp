package com.learnupp.ui.messages

import cafe.adriel.voyager.core.model.screenModelScope
import com.learnupp.domain.model.MessageCategory
import com.learnupp.domain.model.MessageThreadType
import com.learnupp.domain.usecase.messages.GetMessagesUseCase
import com.learnupp.domain.usecase.messages.PreloadMessagesUseCase
import com.learnupp.domain.usecase.messages.ReloadMessagesUseCase
import com.learnupp.ui.base.BaseScreenModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class MessageFilter {
    ALL, DIRECT, GROUPS
}

data class MessagesUiState(
    val filter: MessageFilter = MessageFilter.ALL,
    val searchQuery: String = "",
    val categories: List<MessageCategory> = emptyList(),
    val expandedCategories: Set<String> = emptySet()
)

class MessagesScreenModel(
    private val preloadMessages: PreloadMessagesUseCase,
    private val reloadMessages: ReloadMessagesUseCase,
    private val getMessages: GetMessagesUseCase,
) : BaseScreenModel() {

    private val _filter = MutableStateFlow(MessageFilter.ALL)
    private val _searchQuery = MutableStateFlow("")
    private val _expandedCategories = MutableStateFlow<Set<String>>(emptySet())
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val categories = getMessages()
        .stateIn(screenModelScope, SharingStarted.Eagerly, emptyList())

    private var expandedTouched = false

    val uiState = combine(
        categories,
        _filter,
        _searchQuery,
        _expandedCategories
    ) { categories, filter, search, expanded ->
        val filteredCategories = filterCategories(categories, filter, search)

        val effectiveExpanded = if (!expandedTouched) {
            filteredCategories.firstOrNull()?.let { setOf(it.id) } ?: emptySet()
        } else {
            expanded
        }

        MessagesUiState(
            filter = filter,
            searchQuery = search,
            categories = filteredCategories,
            expandedCategories = effectiveExpanded.filter { id ->
                filteredCategories.any { it.id == id }
            }.toSet()
        )
    }.stateIn(
        screenModelScope,
        SharingStarted.Eagerly,
        MessagesUiState()
    )

    init {
        screenModelScope.launch {
            categories.collect { list ->
                if (!expandedTouched && list.isNotEmpty()) {
                    val filtered =
                        filterCategories(list, _filter.value, _searchQuery.value)
                    _expandedCategories.value =
                        filtered.firstOrNull()?.let { setOf(it.id) } ?: emptySet()
                } else {
                    val ids = list.map { it.id }.toSet()
                    _expandedCategories.value =
                        _expandedCategories.value.filter { it in ids }.toSet()
                }
            }
        }
        screenModelScope.launch(Dispatchers.IO) {
            preloadMessages()
            reloadMessages()
        }
    }

    fun refresh() {
        if (_isRefreshing.value) return
        screenModelScope.launch(Dispatchers.IO) {
            _isRefreshing.value = true
            reloadMessages()
            _isRefreshing.value = false
        }
    }

    fun selectFilter(filter: MessageFilter) {
        _filter.value = filter
        expandedTouched = false
        setAutoExpanded(filter, _searchQuery.value)
    }

    fun updateSearch(query: String) {
        _searchQuery.value = query
        expandedTouched = false
        setAutoExpanded(_filter.value, query)
    }

    fun toggleCategory(id: String) {
        val current = _expandedCategories.value
        _expandedCategories.value = if (current.contains(id)) {
            emptySet()
        } else {
            setOf(id)
        }
        expandedTouched = true
    }

    private fun setAutoExpanded(filter: MessageFilter, query: String) {
        val filtered = filterCategories(categories.value, filter, query)
        _expandedCategories.value =
            filtered.firstOrNull()?.let { setOf(it.id) } ?: emptySet()
    }
}

private fun filterCategories(
    categories: List<MessageCategory>,
    filter: MessageFilter,
    search: String
): List<MessageCategory> {
    val normalizedQuery = search.trim().lowercase()
    return categories.map { category ->
        val filteredThreads = category.threads.filter { thread ->
            val matchesFilter = when (filter) {
                MessageFilter.ALL -> true
                MessageFilter.DIRECT -> thread.type == MessageThreadType.DIRECT
                MessageFilter.GROUPS -> thread.type == MessageThreadType.GROUP
            }
            val matchesQuery = normalizedQuery.isEmpty() ||
                thread.title.lowercase().contains(normalizedQuery) ||
                thread.subtitle.lowercase().contains(normalizedQuery) ||
                thread.lastMessageSnippet.lowercase().contains(normalizedQuery)
            matchesFilter && matchesQuery
        }
        category.copy(threads = filteredThreads)
    }.filter { it.threads.isNotEmpty() }
}

