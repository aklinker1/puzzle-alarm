package io.aklinker1.alarm.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.aklinker1.alarm.R
import io.aklinker1.alarm.utils.turnScreenOffAndKeyguardOn
import io.aklinker1.alarm.utils.turnScreenOnAndKeyguardOff

class AlarmActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)
        turnScreenOnAndKeyguardOff()
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O_MR1) {
//            setShowWhenLocked(true)
//            setTurnScreenOn(true)
//            window.addFlags(
//                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
//                    or WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON)
//        } else {
//            window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
//                    or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
//                    or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
//                    or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
//                    or WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON)
//        }
//        // Hide navigation bar to minimize accidental tap on Home key
//        window.decorView.apply {
//            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
//        }
//        // Close dialogs and window shade, so this is fully visible
//        sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
    }

    override fun onDestroy() {
        super.onDestroy()
        turnScreenOffAndKeyguardOn()
    }
}