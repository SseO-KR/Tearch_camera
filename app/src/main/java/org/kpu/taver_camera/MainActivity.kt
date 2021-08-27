package org.kpu.taver_camera

import android.content.Intent
import android.graphics.Bitmap
import android.hardware.Camera
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.airbnb.lottie.LottieAnimationView
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        settingPermission()



        val btn_Number = findViewById<Button>(R.id.btn_number)
        btn_Number.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            val edit_num = findViewById<EditText>(R.id.edit_number)
            val cafeNum = edit_num.text.toString().toInt()
            intent.putExtra("cafeNum", cafeNum)
            startActivity(intent)
        }


    }






    fun settingPermission(){
        var permis = object : PermissionListener{
            override fun onPermissionGranted() {
                Toast.makeText(this@MainActivity, "권한 허가", Toast.LENGTH_SHORT).show()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(this@MainActivity, "권한 거부", Toast.LENGTH_SHORT).show()
                ActivityCompat.finishAffinity(this@MainActivity)    //권한 거부 시 앱 종료
            }
        }

        TedPermission.with(this).setPermissionListener(permis)
            .setRationaleMessage("카메라 사진 권한 필요")
            .setDeniedMessage("카메라 권한 요청 거부")
            .setPermissions(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA)
            .check()
    }
}