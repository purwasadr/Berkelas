package com.alurwa.berkelas.model

import com.alurwa.common.model.Picket

data class PicketUi(
    val id: String,
    val userId: String,
    val name: String,
    val note: String
) : ListItem()

fun Picket.toPicketUi(name: String) = PicketUi(
    id = id, userId = userId, name = name, note = note
)

fun PicketUi.toPicket() = Picket(
    id = id, userId = userId, note = note
)

fun List<Picket>.toPicketUiList(action: (userId: String) -> String) = this.map {
    it.run {
        PicketUi(
            id = id, userId = userId, name = action(userId), note = note
        )
    }
}