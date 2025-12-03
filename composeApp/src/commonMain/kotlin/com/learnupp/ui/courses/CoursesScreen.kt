package com.learnupp.ui.courses

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.koin.koinScreenModel
import com.learnupp.domain.model.Course
import com.learnupp.ui.base.BaseScreen
import com.learnupp.ui.base.ScreenNameStrings
import com.learnupp.ui.widgets.RefreshableBox
import com.learnupp.ui.widgets.SearchFilterChipsSection
import com.learnupp.util.LearnUppStrings
import com.learnupp.util.getValue
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.launch

class CoursesScreen : BaseScreen(ScreenNameStrings.COURSES, hideTopAppBar = true) {
    @Composable
    override fun Content() {
        val screenModel: CoursesScreenModel = koinScreenModel()
        val courses by screenModel.courses.collectAsState()
        val isRefreshing = remember { mutableStateOf(false) }

        RefreshableBox(
            onRefresh = {
                isRefreshing.value = true
                screenModel.screenModelScope.launch {
                    screenModel.refresh()
                }.invokeOnCompletion {
                    isRefreshing.value = false
                }
            },
            isRefreshing = isRefreshing.value
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                SearchAndChips()
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    itemsIndexed(courses, key = { _, course -> course.id }) { index, course ->
                        CourseCard(course = course)
                        if (index >= courses.lastIndex - 2) {
                            screenModel.loadMore()
                        }
                    }
                    item { Spacer(modifier = Modifier.height(32.dp)) }
                }
            }
        }
    }
}

@Composable
private fun SearchAndChips() {
    SearchFilterChipsSection(
        placeholder = LearnUppStrings.SEARCH_COURSES_PLACEHOLDER.getValue(),
        chipLabels = listOf(
            LearnUppStrings.FITNESS_LABEL.getValue(),
            LearnUppStrings.DESIGN_LABEL.getValue(),
            LearnUppStrings.MARKETING_LABEL.getValue()
        ),
        seeAllLabel = LearnUppStrings.SEE_ALL.getValue(),
        modifier = Modifier.padding(bottom = 20.dp)
    )
}

@Composable
private fun CourseCard(course: Course) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            KamelImage(
                resource = asyncPainterResource(course.previewImageUrl),
                contentDescription = course.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16 / 9f)
                    .clip(RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp)),
                contentScale = ContentScale.Crop,
                onFailure = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16 / 9f)
                            .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.05f))
                    )
                }
            )

            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = course.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = course.instructorName,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f),
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "★ ${course.rating} (${course.ratingCount})",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Dot()
                    Text(
                        text = "${course.lecturesCount} ${LearnUppStrings.LECTURES_LABEL.getValue()}",
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                    )
                    Dot()
                    Text(
                        text = course.durationText,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = formatPrice(course.currentPrice),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            course.originalPrice?.let {
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = formatPrice(it),
                                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f),
                                    textDecoration = TextDecoration.LineThrough
                                )
                            }
                        }
                        course.discountPercent?.let {
                            Text(
                                text = "$it% OFF",
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(22.dp)
                    ) {
                        Text(
                            text = LearnUppStrings.ENROLL_NOW.getValue(),
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .clickable { }
                                .padding(horizontal = 20.dp, vertical = 10.dp),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Dot() {
    Text(
        text = "•",
        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
        modifier = Modifier.padding(horizontal = 6.dp)
    )
}

private fun formatPrice(value: Double): String {
    return if (value % 1.0 == 0.0) {
        "$${value.toInt()}"
    } else {
        "$$value"
    }
}

