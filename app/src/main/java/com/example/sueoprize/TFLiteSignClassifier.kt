package com.example.sueoprize

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class TFLiteSignClassifier(context: Context) {

    private val interpreter: Interpreter
    val labels = listOf("남아", "눈")

    init {
        val model = loadModelFile(context, "sign_model.tflite")
        val options = Interpreter.Options()
        interpreter = Interpreter(model, options)
    }

    private fun loadModelFile(context: Context, filename: String): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(filename)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    fun predict(sequence: Array<FloatArray>): Int {
        val inputBuffer = ByteBuffer.allocateDirect(1 * 30 * 126 * 4).order(ByteOrder.nativeOrder())
        val floatBuffer = inputBuffer.asFloatBuffer()
        sequence.forEach { floatBuffer.put(it) }

        val outputBuffer = ByteBuffer.allocateDirect(1 * 2 * 4).order(ByteOrder.nativeOrder())
        interpreter.run(inputBuffer, outputBuffer)

        val output = FloatArray(2)
        outputBuffer.rewind()
        outputBuffer.asFloatBuffer().get(output)
        return output.indices.maxByOrNull { output[it] } ?: -1
    }

    fun close() {
        interpreter.close()
    }
}