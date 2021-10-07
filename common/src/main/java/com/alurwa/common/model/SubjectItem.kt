package com.alurwa.common.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SubjectItem(
    val id: String = "",
    val subject: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val teacher: String = ""
) : Parcelable
