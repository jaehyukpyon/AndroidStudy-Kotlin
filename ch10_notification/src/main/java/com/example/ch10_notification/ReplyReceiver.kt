package com.example.ch10_notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.RemoteInput

class ReplyReceiver : BroadcastReceiver() {
    // 알림에서 글을 입력하면 실행 될 브로드캐스트 리시버 생성

    override fun onReceive(context: Context, intent: Intent) {
        // 이 BroadcastReceiver가 실행 되면, onReceive 함수가 자동 call 됨

        // 제일 먼저 알림에서 유저가 입력한 글을 추출
        val replyTxt = RemoteInput.getResultsFromIntent(intent)?.getCharSequence("key_text_reply")
        Log.d("eggjam82", "replyTxt: $replyTxt")

        // 알림 취소 구문 작성 - getSystemService를 실행하여 NotificationManager를 먼저 얻고, 이 Manager의 cancel 함수를 통해서 알림 취소 가능
        var manager = context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(11)
    }

}