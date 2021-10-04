package com.alurwa.berkelas.ui.profilecrop

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileCropViewModel @Inject constructor(
    private val stateHandle: SavedStateHandle
) : ViewModel() {
    val uri = stateHandle.get<Uri>(ProfileCropActivity.EXTRA_URI)!!
}