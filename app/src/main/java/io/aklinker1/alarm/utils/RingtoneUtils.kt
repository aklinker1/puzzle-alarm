package io.aklinker1.alarm.utils

import android.media.RingtoneManager
import android.net.Uri


object RingtoneUtils {

    // https://stackoverflow.com/questions/2618182/how-to-play-ringtone-alarm-sound-in-android
    fun getDefaultRingtone(): Uri {
        var uri: Uri? = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        if (uri != null) return uri

        uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        if (uri != null) return uri

        return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)!!
    }
}