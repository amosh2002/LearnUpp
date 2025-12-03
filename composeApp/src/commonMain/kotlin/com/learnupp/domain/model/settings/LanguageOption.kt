package com.learnupp.domain.model.settings

import com.learnupp.domain.model.base.CommonSerializable
import kotlinx.serialization.Serializable

@Serializable
data class LanguageOption(
    val code: String,
    val name: String,
    val flagEmoji: String,
    val selected: Boolean = false
) : CommonSerializable

