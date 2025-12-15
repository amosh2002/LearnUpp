package com.learnupp.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class MessageThreadType {
    @SerialName("direct")
    DIRECT,
    @SerialName("group")
    GROUP
}

@Serializable
data class MessageThread(
    val id: String,
    val title: String,
    val subtitle: String,
    @SerialName("last_message")
    val lastMessageSnippet: String,
    val timestamp: String? = null,
    @SerialName("is_unread")
    val isUnread: Boolean = false,
    @SerialName("unread_count")
    val unreadCount: Int = 0,
    @SerialName("avatar_url")
    val avatarUrl: String? = null,
    @SerialName("member_count")
    val memberCount: Int? = null,
    val type: MessageThreadType
)

@Serializable
data class MessageCategory(
    val id: String,
    val title: String,
    val description: String,
    @SerialName("icon_text")
    val iconText: String,
    @SerialName("icon_color")
    val iconColorHex: String,
    val threads: List<MessageThread>
)


