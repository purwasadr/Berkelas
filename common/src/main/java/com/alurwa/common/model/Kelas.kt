package com.alurwa.common.model

data class Kelas(
    val id: String = "",
    val name: String = "",
) {
    companion object {
        val EMPTY = Kelas(
            id = "",
            name = ""
        )
    }
}
