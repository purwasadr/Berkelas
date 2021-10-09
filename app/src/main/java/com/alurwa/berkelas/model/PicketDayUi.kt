package com.alurwa.berkelas.model

import com.alurwa.common.model.PicketDay

data class PicketDayUi(
    val day: Int,
    val picketsUi: List<PicketUi>
)

internal fun PicketDay.toPicketDayUi(action: (userId: String) -> String) = PicketDayUi(
    day = day,
    picketsUi = pickets.toPicketUiList(action)
)
