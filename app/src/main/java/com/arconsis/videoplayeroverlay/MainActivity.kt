package com.arconsis.videoplayeroverlay

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity

/**
 * Created by benedictp on 17/03/16.
 */
class MainActivity : AppCompatActivity() {

    companion object {
        val OVERLAY_PERMISSION = 1234
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (!Settings.canDrawOverlays(this)) {
            var intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
            startActivityForResult(intent, OVERLAY_PERMISSION)
        } else {
            startService(Intent(this, VideoPlayerOverviewService::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == OVERLAY_PERMISSION) {
            if (!Settings.canDrawOverlays(this)) {
                //
            } else {
                startService(Intent(this, VideoPlayerOverviewService::class.java))
            }
        }
    }

}