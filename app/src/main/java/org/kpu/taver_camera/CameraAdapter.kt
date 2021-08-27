package org.kpu.taver_camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.hardware.Camera
import android.media.ExifInterface
import android.net.Uri
import android.os.Environment
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.*
import java.lang.Thread.sleep
import java.net.URI

class CameraAdapter(val cafeNum : Int, val context: Context) : SurfaceHolder.Callback, Camera.PictureCallback {
    private var surfaceHolder : SurfaceHolder? = null
    private var camera : Camera? = null
    private var surfaceView : SurfaceView? = null
    private val storage : FirebaseStorage = FirebaseStorage.getInstance()
    private val storageRef : StorageReference = storage.getReference()

    internal fun setupSurfaceHolder(view: SurfaceView) {
        surfaceView = view
        surfaceHolder = surfaceView!!.holder
        surfaceHolder!!.addCallback(this)
        storageRef.child(cafeNum.toString()).delete()   //자동종료로 남게되는 사진을 모두 지우고 새로 시작
    }



    internal fun captureImage() {
        if (camera != null){
            camera!!.takePicture(null, null, this)
        }
    }

    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        startCamera()
    }

    private fun startCamera() {
        camera = Camera.open()
        camera!!.setDisplayOrientation(90)
        try {
            camera!!.setPreviewDisplay(surfaceHolder)
            camera!!.startPreview()

        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {
        resetCamera()
    }

    private fun resetCamera() {
        if (surfaceHolder!!.surface == null) {
            // Return if preview surface does not exist
            return
        }

        // Stop if preview surface is already running.
        camera!!.stopPreview()
        try {
            // Set preview display
            camera!!.setPreviewDisplay(surfaceHolder)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // Start the camera preview...
        camera!!.startPreview()
    }

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
        releaseCamera()
    }

    private fun releaseCamera() {
        camera!!.stopPreview()
        camera!!.release()
        camera = null
    }

    override fun onPictureTaken(bytes: ByteArray, camera: Camera) {
        rotateImg(bytes)
        resetCamera()
        captureImage()
    }

    private fun register_firebaseImg(bytes: ByteArray){
        val imageName = "Taver_" + System.currentTimeMillis() + ".jpg"
        val pathRef : StorageReference = storageRef.child(cafeNum.toString()).child(imageName)

        pathRef.putBytes(bytes).addOnSuccessListener {
            Toast.makeText(context, "업로드 완료", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(context, "파이어베이스 업로드 실패", Toast.LENGTH_SHORT).show()
        }

        sleep(30000L)

        pathRef.delete()
    }

    private fun rotateImg(bytes: ByteArray){
        val bitmapImg : Bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, null)
        val matrix : Matrix = Matrix()
        matrix.postRotate(90F)
        val rotatedBitmap = Bitmap.createBitmap(bitmapImg, 0, 0, bitmapImg.width, bitmapImg.height, matrix, true)

        val stream = ByteArrayOutputStream()
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
        register_firebaseImg(stream.toByteArray())
    }
}