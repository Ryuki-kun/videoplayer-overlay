package com.arconsis.videoplayeroverlay

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.view.Gravity
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.WindowManager

/**
 * Created by benedictp on 17/03/16.
 */
class VideoPlayerOverviewService : Service(), MediaPlayer.OnPreparedListener, SurfaceHolder.Callback {

    private var windowManager: WindowManager? = null
    private var surfaceView: SurfaceView? = null
    private var surfaceHolder: SurfaceHolder? = null
    private var mediaPlayer: MediaPlayer? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        var params: WindowManager.LayoutParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT)
        params.gravity = Gravity.TOP or Gravity.LEFT
        params.x = 20
        params.y = 200

        if (surfaceView != null) {
            surfaceView?.keepScreenOn = true
            windowManager?.addView(surfaceView, params)
        } else {
            surfaceView = SurfaceView(applicationContext)
            surfaceView?.keepScreenOn = true
            surfaceHolder = surfaceView?.holder
            surfaceHolder?.addCallback(this)
            surfaceHolder?.setFixedSize(320, 240)
            windowManager?.addView(surfaceView, params)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mediaPlayer = MediaPlayer()
        val videoUri = Uri.parse("android.resource://$packageName/${R.raw.samplevideo}")
        try {
            mediaPlayer?.setDataSource(this, videoUri)
        } catch (e: Exception) {

        }
        mediaPlayer?.isLooping = true
        mediaPlayer?.setOnPreparedListener(this)
        mediaPlayer?.prepareAsync()
        return START_NOT_STICKY
    }

    override fun onPrepared(mp: MediaPlayer?) {
        mediaPlayer?.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        mediaPlayer?.setDisplay(holder)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        mediaPlayer?.isLooping = false
        mediaPlayer?.stop()
        mediaPlayer?.release()
    }


    override fun onDestroy() {
        super.onDestroy()
        stopSelf()
        windowManager?.removeView(surfaceView)
    }

}
