package com.learnupp.domain.model.base

import kotlinx.serialization.Serializable

@Serializable
open class BaseObjectWithId(
    var objectId: Int = 0
): CommonSerializable