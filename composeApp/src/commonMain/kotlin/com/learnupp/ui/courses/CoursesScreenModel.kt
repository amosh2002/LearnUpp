package com.learnupp.ui.courses

import cafe.adriel.voyager.core.model.screenModelScope
import com.learnupp.domain.model.Course
import com.learnupp.domain.usecase.courses.GetCoursesUseCase
import com.learnupp.domain.usecase.courses.LoadMoreCoursesUseCase
import com.learnupp.domain.usecase.courses.PreloadCoursesUseCase
import com.learnupp.domain.usecase.courses.ReloadCoursesUseCase
import com.learnupp.ui.base.BaseScreenModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CoursesScreenModel(
    private val preloadCourses: PreloadCoursesUseCase,
    private val reloadCourses: ReloadCoursesUseCase,
    private val getCourses: GetCoursesUseCase,
    private val loadMoreCourses: LoadMoreCoursesUseCase,
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

