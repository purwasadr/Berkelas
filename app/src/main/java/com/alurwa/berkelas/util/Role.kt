package com.alurwa.berkelas.util

import android.content.Context
import com.alurwa.berkelas.R

/**
 * Berisikan Role dari aplikasi ini
 */
enum class Role(val code: String) {
    LEADER("leader") {
        override fun toString(context: Context) =
            context.getString(R.string.role_leader)
    },
    CO_LEADER("co_leader") {
        override fun toString(context: Context): String =
            context.getString(R.string.role_co_leader)

    },
    SECRETARY("secretary") {
        override fun toString(context: Context): String =
            context.getString(R.string.role_secretary)
    },
    TREASURY("treasury") {
        override fun toString(context: Context): String =
            context.getString(R.string.role_treasury)
    },
    MEMBER("member") {
        override fun toString(context: Context): String =
            context.getString(R.string.role_member)
    };

    abstract fun toString(context: Context): String
}