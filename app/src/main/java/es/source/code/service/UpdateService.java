package es.source.code.service;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.Map;

import es.source.code.activity.Food;
import es.source.code.activity.FoodDetailed;
import es.source.code.activity.HttpUtilsHttpURLConnection;
import es.source.code.activity.LoginOrRegister;
import es.source.code.activity.MainScreen;
import es.source.code.activity.R;
import es.source.code.activity.SCOSEntry;
import es.source.code.br.DeviceStartedListener;
import es.source.code.model.User;

import static android.app.Notification.*;
import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

public class UpdateService extends IntentService {
    private Food food;
    private String Foodname;
    private int Foodnum;
    private int totalnum;
    private NotificationManager manager;
    private int Notification_ID;
    public static int Fnum=20;

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
            food = (Food)getApplication();
            totalnum = intent.getIntExtra("total",20);
            if(intent.getBooleanExtra("flag",false)) {
                showNotification();
            }else {
                manager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                showNotification2();
            }
    }

    private void showNotification(){

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent pIntent = new Intent(UpdateService.this,MainScreen.class);
        final User LoginUser = new User();
        LoginUser.setUsername("xutao");
        LoginUser.setPassword("123456");
        LoginUser.setOlduser(true);

        Bundle bundle = new Bundle();
        bundle.putSerializable("Object",LoginUser);
        bundle.putString("Extra_data","LoginSuccess");
        pIntent.putExtras(bundle);
        PendingIntent nIntent = PendingIntent.getActivity(UpdateService.this,R.string.app_name,pIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews notify_food = new RemoteViews(UpdateService.this.getPackageName(),R.layout.custom_notification);

        //设置通知栏组件内容和监听
        notify_food.setTextViewText(R.id.title,"新品上架！");
        notify_food.setImageViewResource(R.id.image,R.drawable.cold_doufu);
        notify_food.setTextViewText(R.id.text,"菜品数量为"+totalnum+"件");
        Intent intent = new Intent(this,DeviceStartedListener.class);
        intent.setAction("android.intent.action.CANCEL_NOTIFICATION_ACTION");
        PendingIntent intentLast = PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        notify_food.setOnClickPendingIntent(R.id.updatefood,intentLast);
        Notification.Builder builder;
        //安卓8.0后的通知需要设置渠道否则不显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("SCOS","SCOS",NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
            builder = new Notification.Builder(this,notificationChannel.getId());
        }else{
            builder = new Notification.Builder(UpdateService.this);
        }
        builder.setContentIntent(nIntent);
        builder.setContent(notify_food);
        builder.setTicker("新品上架！");
        builder.setSmallIcon(R.mipmap.ic_launcher);

        Notification notify = builder.build();

        notificationManager.notify(R.string.app_name,notify);

//        //播放声音
//        MediaPlayer mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_NOTIFICATION_URI);
//        mediaPlayer.start();
//        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                mp.release();
//            }
//        });

    }


    private void showNotification2() {
        Intent clickIntent = new Intent(UpdateService.this, SCOSEntry.class);
        PendingIntent contentIntent = PendingIntent.getActivity(UpdateService.this, R.string.app_name,clickIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        //初始化通知栏信息
        Notification.Builder mBuilder = new Notification.Builder(UpdateService.this);
        mBuilder.setAutoCancel(true);
        mBuilder.setSmallIcon(R.drawable.order);
        mBuilder.setTicker("欢迎点菜！");
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.order));
        mBuilder.setContentTitle("欢迎点菜！");
        mBuilder.setContentIntent(contentIntent);


        mBuilder.setContentText("欢迎打开SCOS进行点菜");

        //安卓8.0后的通知需要设置渠道否则不显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder.setChannelId("SCOS");
        }

        Notification notification = mBuilder.build();

        //初始化通知管理并且发送通知
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(R.string.app_name,notification);
        startForeground(1, notification);
    }




    void checkupdate()
    {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String url= HttpUtilsHttpURLConnection.BASE_URL+"/FoodUpdateService";
//                Map<String, String> params = new HashMap<String, String>();
//
//                params.put("update","true");
//
//                String result = HttpUtilsHttpURLConnection.getContextByHttp(url,params);
//
//                Message msg = new Message();
//                msg.what=0x14;
//                Bundle data=new Bundle();
//                data.putString("result",result);
//                msg.setData(data);
//                hander.sendMessage(msg);
//            }
//
//            @SuppressLint("HandlerLeak")
//            Handler hander = new Handler(getMainLooper()){
//                @Override
//                public void handleMessage(Message msg) {
//                    if (msg.what==0x14){
//                        Bundle data = msg.getData();
//                        String key = data.getString("result");//得到json返回的json
//                        try {
//                            JSONArray jsonArray= new JSONArray(key);
//                            totalnum = 0;
//                            for (int i=0;i<jsonArray.length();i++)
//                            {
//                                JSONObject subObject= jsonArray.getJSONObject(i);
//                                Foodname = subObject.getString("title");
//                                Foodnum = subObject.getInt("foodnum");
//                                Map<String, Object> map=new HashMap<String, Object>();
//                                map.put("Foodnum", Foodnum);
//                                map.put("Foodname",Foodname);
//                                food.updatefoodlist.add(map);
//                                Log.d("tools2", "获得菜品名称:"+Foodname);
//                                Log.d("tools2", "获得菜品数量:"+Foodnum);
//                                totalnum  = totalnum + Foodnum;
//
//                                Bundle bundle = new Bundle();
//                                bundle.putString("Foodname",Foodname);
//                                bundle.putInt("Foodnum",Foodnum);
//                                Intent intent = new Intent(UpdateService.this,ServerObserverService.class);
//                                intent.putExtra("message", bundle);
//                                startService(intent);
//                            }
//                            manager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                            Log.d("tools2", "show");
//                            showNotification();
//                            Fnum = (int)food.updatefoodlist.get(0).get("Foodnum");
//                            Log.d("tools2", "获得菜品0数量:"+food.updatefoodlist.get(0).get("Foodnum"));
//                            Log.d("tools2", "获得菜品总数量:"+totalnum);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            };
//        }).start();
    }

    public UpdateService()
    {
        super("UpdateService");
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.e("TAG","onCreate");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.e("TAG","onDestroy");
    }
}
