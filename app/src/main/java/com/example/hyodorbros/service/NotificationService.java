package com.example.hyodorbros.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.hyodorbros.R;
import com.example.hyodorbros.constant.Constant;
import com.example.hyodorbros.receiver.NotificationReceiver;
import com.example.hyodorbros.room.entity.DDayEntity;
import com.example.hyodorbros.ui.home.HomeActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class NotificationService extends Service {


    NotificationManager mNotificationManager;
    NotificationChannel mNotificationChannel;
    NotificationCompat.Builder mNotificationBuilder;

    Calendar mTargetCalendar = new GregorianCalendar();
    Calendar mBaseCalendar = new GregorianCalendar();

    Resources mResources;

    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mResources = getResources();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final InnerNotificationServiceHandler handler = new InnerNotificationServiceHandler();

        Thread mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    try {
                        if (intent.getExtras() != null && intent.getExtras().containsKey(Constant.INTENT.EXTRA.KEY.SQLITE_TABLE_CLT_DDAY_ROWID)) {
                            int mId = intent.getIntExtra(Constant.INTENT.EXTRA.KEY.SQLITE_TABLE_CLT_DDAY_ROWID, 0);
                            if (mId > 0) {
                                DDayEntity ddayItem = intent.getParcelableExtra(Constant.INTENT.EXTRA.KEY.SQLITE_TABLE_CLT_DDAY_ITEM);
                                Message message = handler.obtainMessage();
                                message.obj = ddayItem;
                                handler.sendMessage(message);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mThread.start();

        return START_REDELIVER_INTENT;

    }

    /**
     * 서비스가 종료될 때 할 작업
     */
    public void onDestroy() {

    }

    /**
     * 두 날짜 간 차이 구하기
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    private String getDiffDays(int year, int month, int day) {
        mTargetCalendar.set(Calendar.YEAR, year);
        mTargetCalendar.set(Calendar.MONTH, month);
        mTargetCalendar.set(Calendar.DAY_OF_MONTH, day);

        // 밀리초(1000분의 1초) 단위로 두 날짜 간 차이를 변환 후 초 단위로 다시 변환
        long diffSec = (mTargetCalendar.getTimeInMillis() - mBaseCalendar.getTimeInMillis()) / 1000;
        // 1분(60초), 1시간(60분), 1일(24시간) 이므로 다음과 같이 나누어 1일 단위로 다시 변환
        long diffDays = diffSec / (60 * 60 * 24);

        int flag = diffDays > 0 ? 1 : diffDays < 0 ? -1 : 0;

        final String msg;

        switch (flag) {
            case 1:
                msg = getString(R.string.dday_valid_prefix) + Math.abs(diffDays);
                break;
            case 0:
                msg = getString(R.string.dday_today);
                break;
            case -1:
                msg = getString(R.string.dday_invalid_prefix) + Math.abs(diffDays);
                break;
            default:
                msg = "";
        }

        return msg;
    }

    class InnerNotificationServiceHandler extends Handler {

        // 핸들러 메시지큐에 있는 작업을 처리 ( 실제 처리 메소드)
        @Override
        public void handleMessage(Message msg) {
            final DDayEntity ddayItem = (DDayEntity) msg.obj;
            final int requestCode = ddayItem.getUid();
            final int notificationId = requestCode;

            // 알림 클릭시 MergeActivity 화면에 띄운다.
            Intent intent = new Intent(NotificationService.this, HomeActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(NotificationService.this, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationChannel = new NotificationChannel(Constant.NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);

                // Configure the notification channel.
                mNotificationChannel.setDescription("Channel description");
                mNotificationChannel.enableLights(true);
                mNotificationChannel.setLightColor(Color.RED);
                mNotificationChannel.enableVibration(true);
                mNotificationChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
                mNotificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
                mNotificationManager.createNotificationChannel(mNotificationChannel);
            }

            mNotificationBuilder = new NotificationCompat.Builder(getApplicationContext(), Constant.NOTIFICATION_CHANNEL_ID);

            // Notification 객체는 다음을 반드시 포함해야 합니다.

            // setSmallIcon()이 설정한 작은 아이콘
            // setContentTitle()이 설정한 제목
            // setContentText()이 설정한 세부 텍스트

            // reference: https://developer.android.com/guide/topics/ui/notifiers/notifications?hl=ko

            mNotificationBuilder
                    .setContentTitle(ddayItem.getTitle())
                    .setContentText(ddayItem.getDate())
                    .setTicker(ddayItem.getTitle())
                    .setLargeIcon(BitmapFactory.decodeResource(mResources, R.mipmap.ic_launcher_round))
                    .setSmallIcon(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP ?
                            R.drawable.ic_notification_star_white : R.mipmap.ic_launcher)
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
//                    .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent))
                    .setOngoing(true)
                    .setShowWhen(true)
                    .setAutoCancel(false)
                    .setContentIntent(pendingIntent)
                    .setWhen(System.currentTimeMillis())
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setDefaults(Notification.DEFAULT_ALL);

            // FIXME 알림 메시지 수량이 늘어나 그룹으로 묶이는 경우 그룹을 스와이프 하면 노티 삭제됨

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                mNotificationBuilder
                        .setCategory(Notification.CATEGORY_MESSAGE)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            }

            mNotificationManager.notify(notificationId, mNotificationBuilder.build());

            // Set Date
            String selectedDate = ddayItem.getDate();
            String[] arrDate = selectedDate.split(Constant.REGEX.SLASH);

            String strYear = arrDate[0];
            String strMonth = arrDate[1];
            String strDay = arrDate[2];

            final int year = Integer.parseInt(strYear);
            final int month = Integer.parseInt(strMonth);
            final int day = Integer.parseInt(strDay);

            final String diffDays = getDiffDays(year, month - 1, day);

            // FIXME 재귀호출, 브로드캐스트 수신자 사용으로 코드 리팩토링 해보기
            // reference: http://la-stranger.blogspot.com/2013/09/blog-post_26.html

            // 인텐트 필터 설정
            IntentFilter intentFilter = new IntentFilter();

            // reference: http://la-stranger.blogspot.com/2013/09/blog-post_26.html

            // TEST 를 위해 임시로 추가한 인텐트
            // 매 분마다 이벤트가 발생한다.
            // 이 이벤트는 AndroidManifest에 Intent filter를 적용하는 것으로 캐치할 수 없고 코드내에서 동적으로 등록을 해야 한다.
            // 아마도 실수로 이 이벤트에 대한 로직을 추가하여 디바이스 배터리 광탈을 막기위한 목적이 아닌가 한다.
//            intentFilter.addAction(Intent.ACTION_TIME_TICK);

            // 날짜가 변경 될 때 발생한다.
            // 다시 설명하면 어느 날의 11:59  PM에서 자정으로 넘어가 날짜가 변경되는 경우 브로드캐스트 되는 인텐트

            // FIXME it doesn't work.
            intentFilter.addAction(Intent.ACTION_DATE_CHANGED);

            // 동적리시버 생성
            NotificationReceiver mNotificationReceiver = new NotificationReceiver();

            // 위에서 설정한 인텐트필터+리시버정보로 리시버 등록
            registerReceiver(mNotificationReceiver, intentFilter);

            mNotificationReceiver.callback(action -> {
                mTargetCalendar.set(Calendar.YEAR, year);
                mTargetCalendar.set(Calendar.MONTH, month - 1);
                mTargetCalendar.set(Calendar.DAY_OF_MONTH, day);

                Date today = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                sdf.format(today);
                mBaseCalendar = sdf.getCalendar();

                mNotificationBuilder.setContentText(diffDays);
                mNotificationManager.notify(notificationId, mNotificationBuilder.build());
            });

            Intent fromNotificationToDetailActivityIntent = new Intent(Constant.ACTION_INTENT_FILTER_NOTIFICATION_ON_START_COMMAND);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(fromNotificationToDetailActivityIntent);
        }
    }

}
