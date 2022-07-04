package com.example.ch10_notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import com.example.ch10_notification.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // View Binding 기법 이용
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // activity_main.xml의 Button에 click event 설정
        binding.notificationButton.setOnClickListener {
            // 이 Button 클릭 시, 가장 먼저 notification을 띄워야 함 -> NotificationManager를 System Service로 얻는 코드가 가장 먼저 필요
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager // Manager를 얻음

            // Notification 객체는 Notification Builder에 의해서 만들어 진다
            // Builder 객체 먼저 선언
            val builder: NotificationCompat.Builder
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelId = "one-channel"
                val channelName = "My Channel One"
                val channel = NotificationChannel (
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "My Channel One Description" // 환경 설정에 표시될 이 채널의 설명 문자열
                    setShowBadge(true)
                    val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                    val audioAttributes = AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build()

                    setSound(uri, audioAttributes)
                    enableVibration(true)
                }

                // channel 매니저에 등록
                manager.createNotificationChannel(channel)
                builder = NotificationCompat.Builder(this, channelId)
            } else { // Oreo 하위 version은 아예 channel 개념이 없음
                builder = NotificationCompat.Builder(this)
            }

            builder.run {
                setSmallIcon(R.drawable.small)
                setWhen(System.currentTimeMillis())
                setContentTitle("Jaehyuk Pyon")
                setContentText("Hello World Guys!!")
                setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.big))
            }

            // remote input 준비
            val KEY_TEXT_REPLY = "key_text_reply" // remote input으로 보내고자 하는 메시지의 key 값
            // 내가 작성한 BroadcastReceiver에서 그 메시지를 받을 텐데 그 메시지 받을 때 사용한 key 값과 맞춘 것

            var replyLabel = "답잡을 입력하세요."
            var remoteInput: RemoteInput = RemoteInput.Builder(KEY_TEXT_REPLY).run {
                setLabel(replyLabel)
                build()
            }

            // BroadcastReceiver를 실행 시키기 위한 준비 과정
            val replyIntent = Intent(this, ReplyReceiver::class.java)
            val replyPendingIntent = PendingIntent.getBroadcast(
                this, 30, replyIntent, PendingIntent.FLAG_MUTABLE
            )

            builder.addAction(
                NotificationCompat.Action.Builder(
                    R.drawable.send,
                    "답장하기", // 액션 영역에서 이 액션이 어떤 액션인지 사용자에게 보이는 "이름"
                    replyPendingIntent
                ).addRemoteInput(remoteInput).build()
            )

            manager.notify(11, builder.build())
        } // button의 setOnClickListener
    } // onCreate ends

}