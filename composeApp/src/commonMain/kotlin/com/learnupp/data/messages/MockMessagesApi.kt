package com.learnupp.data.messages

import com.learnupp.domain.model.MessageCategory
import com.learnupp.domain.model.MessageThread
import com.learnupp.domain.model.MessageThreadType

class MockMessagesApi : MessagesApi {
    override suspend fun fetchCategories(): List<MessageCategory> {
        val avatars = listOf(
            "https://images.unsplash.com/photo-1544723795-3fb6469f5b39?q=80&w=600&auto=format&fit=crop",
            "https://images.unsplash.com/photo-1544005313-94ddf0286df2?q=80&w=600&auto=format&fit=crop",
            "https://images.unsplash.com/photo-1525134479668-1bee5c7c6845?q=80&w=600&auto=format&fit=crop",
            "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?q=80&w=600&auto=format&fit=crop",
        )

        val brandingCategory = MessageCategory(
            id = "course-branding",
            title = "Course Name",
            description = "2 chats",
            iconText = "</>",
            iconColorHex = "#B71C1C",
            threads = listOf(
                MessageThread(
                    id = "lecturer-1",
                    title = "Lecturer Name",
                    subtitle = "Lead Designer",
                    lastMessageSnippet = "Last message short",
                    timestamp = null,
                    isUnread = true,
                    avatarUrl = avatars[0],
                    type = MessageThreadType.DIRECT
                ),
                MessageThread(
                    id = "group-1",
                    title = "Course Group",
                    subtitle = "Name: Last message short...",
                    lastMessageSnippet = "Sprint planning call on Friday",
                    timestamp = "4h",
                    isUnread = false,
                    avatarUrl = avatars[1],
                    memberCount = 24,
                    type = MessageThreadType.GROUP
                )
            )
        )

        val mobileCategory = MessageCategory(
            id = "course-mobile",
            title = "Mobile UI Systems",
            description = "2 chats",
            iconText = "</>",
            iconColorHex = "#D84315",
            threads = listOf(
                MessageThread(
                    id = "lecturer-2",
                    title = "Lecturer Name",
                    subtitle = "Senior Android Engineer",
                    lastMessageSnippet = "Iteration build is ready",
                    timestamp = null,
                    isUnread = true,
                    avatarUrl = avatars[2],
                    type = MessageThreadType.DIRECT
                ),
                MessageThread(
                    id = "ta-1",
                    title = "Instructor Assistant",
                    subtitle = "Last message short",
                    lastMessageSnippet = "Uploading today's recording",
                    timestamp = null,
                    isUnread = false,
                    avatarUrl = avatars[3],
                    type = MessageThreadType.DIRECT
                )
            )
        )

        val analyticsDirect = MessageCategory(
            id = "course-analytics",
            title = "Course Name",
            description = "2 chats",
            iconText = "{ }",
            iconColorHex = "#6A1B9A",
            threads = listOf(
                MessageThread(
                    id = "lecturer-3",
                    title = "Lecturer Name",
                    subtitle = "Data Strategist",
                    lastMessageSnippet = "Last message short",
                    timestamp = null,
                    isUnread = true,
                    avatarUrl = avatars[0],
                    type = MessageThreadType.DIRECT
                ),
                MessageThread(
                    id = "mentor-1",
                    title = "Mentor Team",
                    subtitle = "Weekly sync summary",
                    lastMessageSnippet = "Slides are ready for review",
                    timestamp = null,
                    isUnread = false,
                    avatarUrl = avatars[1],
                    type = MessageThreadType.DIRECT
                )
            )
        )

        val advancedUx = MessageCategory(
            id = "ux-advanced",
            title = "Advanced UX Design",
            description = "1 group chat",
            iconText = "UX",
            iconColorHex = "#1E88E5",
            threads = listOf(
                MessageThread(
                    id = "group-ux",
                    title = "Course Group",
                    subtitle = "Last message short...",
                    lastMessageSnippet = "Prototype feedback is uploaded",
                    timestamp = "11:32 AM",
                    isUnread = false,
                    avatarUrl = avatars[2],
                    memberCount = 247,
                    type = MessageThreadType.GROUP
                )
            )
        )

        return listOf(brandingCategory, mobileCategory, analyticsDirect, advancedUx)
    }
}

