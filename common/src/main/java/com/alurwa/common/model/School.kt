package com.alurwa.common.model

data class School(
    val id: String = "",
    val name: String = "",
    val kelasList: List<Kelas> = emptyList(),
    val province: String = "",
    val districts: String = ""
) {
    companion object {
        val EMPTY = School(
            id = "", name = "", kelasList = emptyList(), province = "", districts = ""
        )
    }
}
