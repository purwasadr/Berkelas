package com.alurwa.berkelas.ui.profilecrop

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.alurwa.berkelas.databinding.ActivityProfileCropBinding
import com.canhub.cropper.CropImageView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileCropActivity : AppCompatActivity(), CropImageView.OnCropImageCompleteListener {
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityProfileCropBinding.inflate(layoutInflater)
    }

    private val viewModel: ProfileCropViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupCropImageView()
        setupBtnView()
    }

    private fun setupCropImageView() {
        val intentUri = intent.data!!

        with(binding.cropImg) {
            setImageUriAsync(intentUri)
            setAspectRatio(1, 1)
            setOnCropImageCompleteListener(this@ProfileCropActivity)
        }

    }

    private fun setupBtnView() {
        binding.btnDone.setOnClickListener {
            binding.cropImg.croppedImageAsync(
                saveCompressFormat = Bitmap.CompressFormat.JPEG,
                saveCompressQuality = 90,
                reqWidth = 500,
                reqHeight = 500,
            )
        }

        binding.imgbtnRotate.setOnClickListener {
            binding.cropImg.rotateImage(-90)
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }
    }

    override fun onCropImageComplete(view: CropImageView, result: CropImageView.CropResult) {
        val uri = result.uriContent
        val intentResult = Intent().also {
            it.data = uri
        }

        setResult(RESULT_CROP_CODE, intentResult)
        finish()
    }


    companion object {
        const val EXTRA_URI = "extra_uri"
        const val RESULT_CROP_URI = "RESULT_CROP_URI"
        const val RESULT_CROP_CODE = 100
    }
}