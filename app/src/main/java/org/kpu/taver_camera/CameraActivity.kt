package org.kpu.taver_camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.hardware.Camera
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import java.io.*
import java.lang.Thread.sleep
import javax.security.auth.callback.Callback

class CameraActivity : AppCompatActivity() {
    val context : Context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        val surfaceView = findViewById<SurfaceView>(R.id.surfaceView)

        var intent = intent
        val cafeNum = intent.getIntExtra("cafeNum", 0)
        val adapter = CameraAdapter(cafeNum, context)
        adapter.setupSurfaceHolder(surfaceView)

        val btn = findViewById<Button>(R.id.btn)
        btn.setOnClickListener {
            adapter.captureImage()
        }

        val btn2 = findViewById<Button>(R.id.btn2)
        btn2.setOnClickListener {
            finish()
        }


    }

}