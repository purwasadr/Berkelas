package com.alurwa.berkelas.model

import com.alurwa.common.model.SubjectItem

sealed class SubjectUiModel {
    data class Header(val title: String) : SubjectUiModel()
    data class Subject(
        val subject: SubjectItem
    ) : SubjectUiModel()
}
