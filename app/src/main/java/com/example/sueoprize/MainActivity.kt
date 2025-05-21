package com.example.sueoprize

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var previewView: androidx.camera.view.PreviewView
    private lateinit var textViewTranslation: TextView
    private lateinit var flipButton: Button
    private lateinit var cameraExecutor: ExecutorService

    private var isFrontCamera = false // 전면 카메라 여부

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        previewView = findViewById(R.id.previewView)
        textViewTranslation = findViewById(R.id.textViewTranslation)
        flipButton = findViewById(R.id.btnFlipCamera)
        cameraExecutor = Executors.newSingleThreadExecutor()

        flipButton.setOnClickListener {
            flipCamera()
        }

        startCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // 카메라 선택: isFrontCamera가 true면 전면, false면 후면
            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(if (isFrontCamera) CameraSelector.LENS_FACING_FRONT else CameraSelector.LENS_FACING_BACK)
                .build()

            // Preview 설정
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            // ImageAnalysis 설정
            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, HandAnalyzer(this, textViewTranslation, isFrontCamera))
                }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun flipCamera() {
        isFrontCamera = !isFrontCamera
        startCamera()
        println("Camera flipped: isFrontCamera=$isFrontCamera")
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}