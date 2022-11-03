package com.example.ugd89_c_10665_project1

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private var mCamera: Camera? = null
    private var mCameraView: CameraView? = null
    lateinit var sensorProximity: Sensor
    lateinit var sensorManager: SensorManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorProximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        if (sensorProximity == null) {
            Toast.makeText(this, "Sensor proximity tidak ditemukan", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            sensorManager.registerListener(
                proximitySensorEventListener,
                sensorProximity,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }

        try{
            mCamera = Camera.open()
        }catch (e: Exception){
            Log.d("Error", "Failed to get Camera" + e.message)
        }

        if(mCamera !=null){
            mCameraView = CameraView(this, mCamera!!)
            val  camera_view = findViewById<View>(R.id.FLCamera) as FrameLayout
            camera_view.addView(mCameraView)
        }

        @SuppressLint("MissingInflatedId", "LocalSuppress") val imageClose =
            findViewById<View>(R.id.imgClose) as ImageButton
        imageClose.setOnClickListener{view: View? -> System.exit(0)}
    }

    var proximitySensorEventListener: SensorEventListener? = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_PROXIMITY) {
                if (event.values[0] == 0f) {
                    try {
                        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT)
                    } catch (e: Exception) {
                        Log.d("Error", "Failed to get Camera" + e.message)
                    }

                    if (mCamera != null) {
                        mCameraView = CameraView(this@MainActivity, mCamera!!)
                        val camera_view = findViewById<View>(R.id.FLCamera) as FrameLayout
                        camera_view.addView(mCameraView)
                    }

                    @SuppressLint("MissingInflatedId", "LocalSuppress") val imageClose =
                        findViewById<View>(R.id.imgClose) as ImageButton
                    imageClose.setOnClickListener { view: View? -> System.exit(0) }
                } else {
                    if (event.values[0] == 0f) {
                        try {
                            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK)
                        } catch (e: Exception) {
                            Log.d("Error", "Failed to get Camera" + e.message)
                        }

                        if (mCamera != null) {
                            mCameraView = CameraView(this@MainActivity, mCamera!!)
                            val camera_view = findViewById<View>(R.id.FLCamera) as FrameLayout
                            camera_view.addView(mCameraView)
                        }

                        @SuppressLint("MissingInflatedId", "LocalSuppress") val imageClose =
                            findViewById<View>(R.id.imgClose) as ImageButton
                        imageClose.setOnClickListener { view: View? -> System.exit(0) }

                    }
                }
            }
        }
    }
}