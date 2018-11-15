package es.source.code.service;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.source.code.activity.Food;
import es.source.code.activity.HttpUtilsHttpURLConnection;

import static es.source.code.service.UpdateService.Fnum;


public class  ServerObserverService extends Service {

    public static final String TAG = "ServerObserverService";
    //用来接收客户端法发来的消息
    private final Messenger c_msger = new Messenger(new cMessageHandler());
    //用来发送消息到客户端
    private Messenger s_msger = null;
    private int i=0;
    private String Foodname;
    private int Foodnum;
    private int totalnum;


    class cMessageHandler extends Handler{


        Thread thread;
        @Override
        public void handleMessage( Message msg) {
            //之前因为这句话，信息发送失败

            s_msger = msg.replyTo;
            switch(msg.what)
            {
                case 0:
                    thread.interrupt();
                    Log.d(TAG, "线程关闭");
                    break;
                case 1:
                    final Food food;
                    food = (Food)getApplication();
                    thread=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                //发消息请求服务器传json数据
                                String url= HttpUtilsHttpURLConnection.BASE_URL+"/FoodUpdateService";
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("update","true");
                                String result = HttpUtilsHttpURLConnection.getContextByHttp(url,params);
                                Message msg = new Message();
                                msg.what=0x14;
                                Bundle data=new Bundle();
                                data.putString("result",result);
                                msg.setData(data);
                                handler.sendMessage(msg);

                                //发消息请求服务器传json数据
                                String url2= HttpUtilsHttpURLConnection.BASE_URL+"/FoodUpdateServiceXML";
                                Map<String, String> params2 = new HashMap<String, String>();
                                params.put("update","true");
                                String result2 = HttpUtilsHttpURLConnection.getContextByHttp(url2,params2);
                                Message msg2 = new Message();
                                msg2.what=0x18;
                                Bundle data2=new Bundle();
                                data2.putString("result",result2);
                                msg2.setData(data2);
                                handler.sendMessage(msg2);

                                Message s2cmsg = new Message();
                                Log.d(TAG, "线程开始");
                                while(true)
                                {
                                    if(i<=10) {
                                        thread.sleep(2000);

                                        s2cmsg.what = 10;
                                        Bundle bundle = new Bundle();
                                        bundle.putString("foodname", (String) food.updatefoodlist.get(i).get("Foodname"));
                                        bundle.putInt("position",i);
                                        bundle.putInt("foodnum", (int) food.updatefoodlist.get(i).get("Foodnum"));
                                        s2cmsg.setData(bundle);
                                        i++;
                                    }


                                    if (true) {
                                        try {
                                            s_msger.send(s2cmsg);
                                            Log.d(TAG, "发送成功");
                                        } catch (RemoteException e) {
                                            e.printStackTrace();
                                            Log.d(TAG, "信息发送失败");
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                Log.d(TAG, "信息发送失败");
                                e.printStackTrace();
                            }
                        }
                        @SuppressLint("HandlerLeak")
                        Handler handler = new Handler(getMainLooper()){
                            @Override
                            public void handleMessage(Message msg) {
                                if (msg.what==0x14){
                                    Bundle data = msg.getData();
                                    String key = data.getString("result");//得到json返回的json
                                    try {
                                        JSONArray jsonArray= new JSONArray(key);
                                        totalnum = 0;
                                        long start = System.currentTimeMillis();
                                        for (int i=0;i<jsonArray.length();i++)
                                        {
                                            JSONObject subObject= jsonArray.getJSONObject(i);
                                            Foodname = subObject.getString("title");
                                            Foodnum = subObject.getInt("foodnum");
                                            Map<String, Object> map=new HashMap<String, Object>();
                                            map.put("Foodnum", Foodnum);
                                            map.put("Foodname",Foodname);
                                            food.updatefoodlist.add(map);


                                            Log.d("json", "获得菜品名称:"+Foodname);
                                            Log.d("json", "获得菜品数量:"+Foodnum);
                                            totalnum  = totalnum + Foodnum;

                                            Bundle bundle = new Bundle();
                                            bundle.putString("Foodname",Foodname);
                                            bundle.putInt("Foodnum",Foodnum);
                                            Intent intent = new Intent(ServerObserverService.this,UpdateService.class);
                                            intent.putExtra("total", totalnum);
                                            intent.putExtra("flag",true);
                                            startService(intent);
                                        }
                                        long stop = System.currentTimeMillis();
                                        Fnum = (int)food.updatefoodlist.get(0).get("Foodnum");
                                        Log.d("tools2", "获得菜品总数量:"+totalnum);
                                        Log.d("paser time", "解析json总耗时为："+(stop-start)+"ms");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else if(msg.what==0x18)
                                {
                                    Bundle data = msg.getData();
                                    String key = data.getString("result");

                                    try {
                                        XmlPullParserFactory  factory = XmlPullParserFactory.newInstance();
                                        XmlPullParser xmlPullParser = factory.newPullParser();
                                        //设置输入的内容
                                        xmlPullParser.setInput(new StringReader(key));
                                        //获取当前解析事件，返回的是数字
                                        int eventType = xmlPullParser.getEventType();
                                        //保存内容
                                        String foodname = "";
                                        String foodnum = "";

                                        long start = System.currentTimeMillis();
                                        while (eventType != (XmlPullParser.END_DOCUMENT)){
                                            String nodeName = xmlPullParser.getName();
                                            switch (eventType){
                                                //开始解析XML
                                                case XmlPullParser.START_TAG:{
                                                    //nextText()用于获取结点内的具体内容
                                                    if("foodname".equals(nodeName))
                                                        foodname = xmlPullParser.nextText();
                                                    else if("foodnum".equals(nodeName))
                                                        foodnum = xmlPullParser.nextText();

                                                } break;
                                                //结束解析
                                                case XmlPullParser.END_TAG:{
                                                    if("food".equals(nodeName)){

                                                        Log.d("xml", "parseXMLWithPull: foodname is "+ foodname);
                                                        Log.d("xml", "parseXMLWithPull: foodnum is "+ foodnum);
                                                    }
                                                } break;
                                                default: break;
                                            }
                                            //下一个
                                            eventType = xmlPullParser.next();
                                        }
                                        long stop = System.currentTimeMillis();
                                        Log.d("paser time", "解析xml总耗时为："+(stop-start)+"ms");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        };
                    });

                    thread.start();
                    break;
            }
        }
    }

    public boolean getAppSatus(Context context, String pageName) {

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(20);

        //判断程序是否在栈顶
        if (list.get(0).topActivity.getPackageName().equals(pageName)) {
            return true;
        } else {
            //判断程序是否在栈里
            for (ActivityManager.RunningTaskInfo info : list) {
                if (info.topActivity.getPackageName().equals(pageName)) {
                    return true;
                }
            }
            return false;//栈里找不到，返回3
        }
    }

    public void getfoodupdate()
    {

    }

//    //创建服务时调用
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        Log.d(TAG, "onCreate");
//    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return c_msger.getBinder();
    }

}
