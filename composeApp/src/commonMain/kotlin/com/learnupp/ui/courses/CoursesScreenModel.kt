package com.learnupp.ui.courses

import cafe.adriel.voyager.core.model.screenModelScope
import com.learnupp.domain.usecase.courses.GetGlobalCoursesUseCase
import com.learnupp.domain.usecase.courses.LoadMoreGlobalCoursesUseCase
import com.learnupp.domain.usecase.courses.PreloadGlobalCoursesUseCase
import com.learnupp.domain.usecase.courses.ReloadCoursesUseCase
import com.learnupp.ui.base.BaseScreenModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CoursesScreenModel(
    private val preloadCourses: PreloadGlobalCoursesUseCase,
    private val reloadCourses: ReloadCoursesUseCase,
    private val getCourses: GetGlobalCoursesUseCase,
    private val loadMoreCourses: LoadMoreGlobalCoursesUseCase,
) : BaseScreenModel() {

    val courses = getCourses()
        .stateIn(screenModelScope, SharingStarted.Eagerly, emptyList())

    init {
        screenModelScope.launch(Dispatchers.IO) {
            preloadCourses()
            reloadCourses()
        }
    }

    suspend fun refresh() {
        reloadCourses()
    }

    fun loadMore() {
        screenModelScope.launch(Dispatchers.IO) {
            loadMoreCourses()
        }
    }
}

