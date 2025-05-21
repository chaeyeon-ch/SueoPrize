package com.example.sueoprize

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.widget.TextView
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarker
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarkerResult
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode

class HandAnalyzer(private val context: Context, private val textView: TextView, private val isFrontCamera: Boolean) : ImageAnalysis.Analyzer {
    private val handLandmarker: HandLandmarker
    private val classifier = TFLiteSignClassifier(context)
    private val inputSequence = ArrayList<FloatArray>()

    init {
        val baseOptions = BaseOptions.builder()
            .setModelAssetPath("hand_landmarker.task")
            .build()

        val options = HandLandmarker.HandLandmarkerOptions.builder()
            .setBaseOptions(baseOptions)
            .setRunningMode(RunningMode.LIVE_STREAM)
            .setNumHands(2)
            .setResultListener { result: HandLandmarkerResult?, _ ->
                val landmarksList = result?.landmarks()
                val handedness = result?.handedness()

                if (landmarksList.isNullOrEmpty() || handedness.isNullOrEmpty()) {
                    (context as? Activity)?.runOnUiThread {
                        textView.text = ""
                    }
                    return@setResultListener
                }

                var leftLandmarks: List<com.google.mediapipe.tasks.components.containers.NormalizedLandmark>? = null
                var rightLandmarks: List<com.google.mediapipe.tasks.components.containers.NormalizedLandmark>? = null
                landmarksList.forEachIndexed { index, landmarks ->
                    val handLabel = handedness[index][0].categoryName()
                    if (isFrontCamera) {
                        if (handLabel == "Left") rightLandmarks = landmarks
                        else if (handLabel == "Right") leftLandmarks = landmarks
                    } else {
                        if (handLabel == "Left") leftLandmarks = landmarks
                        else if (handLabel == "Right") rightLandmarks = landmarks
                    }
                }

                if (leftLandmarks == null || rightLandmarks == null) {
                    (context as? Activity)?.runOnUiThread {
                        textView.text = ""
                    }
                    return@setResultListener
                }

                fun normalizeLandmarks(landmarks: List<com.google.mediapipe.tasks.components.containers.NormalizedLandmark>): FloatArray {
                    val keypoints = FloatArray(63)
                    for ((i, lm) in landmarks.withIndex()) {
                        keypoints[i * 3] = if (isFrontCamera) 1.0f - lm.x() else lm.x()
                        keypoints[i * 3 + 1] = lm.y()
                        keypoints[i * 3 + 2] = lm.z()
                    }
                    val wrist = floatArrayOf(keypoints[0], keypoints[1])
                    for (i in 0 until 21) {
                        keypoints[i * 3] -= wrist[0]
                        keypoints[i * 3 + 1] -= wrist[1]
                    }
                    val maxDist = keypoints.indices.step(3).map { i ->
                        Math.hypot(keypoints[i].toDouble(), keypoints[i + 1].toDouble())
                    }.maxOrNull() ?: 1e-5
                    for (i in 0 until 21) {
                        keypoints[i * 3] /= maxDist.toFloat()
                        keypoints[i * 3 + 1] /= maxDist.toFloat()
                    }
                    return keypoints
                }

                val leftKp = normalizeLandmarks(leftLandmarks!!)
                val rightKp = normalizeLandmarks(rightLandmarks!!)
                val input = leftKp + rightKp

                inputSequence.add(input)

                if (inputSequence.size >= 30) {
                    val sequence = inputSequence.takeLast(30).toTypedArray()
                    val prediction = classifier.predict(sequence)

                    (context as? Activity)?.runOnUiThread {
                        textView.text = classifier.labels.getOrNull(prediction) ?: "?"
                        println("Prediction: ${classifier.labels.getOrNull(prediction)}, isFrontCamera=$isFrontCamera")
                    }
                    inputSequence.removeAt(0)
                }
            }
            .build()

        handLandmarker = HandLandmarker.createFromOptions(context, options)
    }

    override fun analyze(image: ImageProxy) {
        val bitmap = image.toBitmap() ?: run {
            image.close()
            return
        }

        val mpImage = BitmapImageBuilder(bitmap).build()
        handLandmarker.detectAsync(mpImage, image.imageInfo.timestamp)
        image.close()
    }

    fun close() {
        handLandmarker.close()
        classifier.close()
    }
}