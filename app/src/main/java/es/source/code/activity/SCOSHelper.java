package es.source.code.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.os.Handler;
import android.os.Message;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;


public class SCOSHelper extends AppCompatActivity {
    private GridView mgridview;
    private List<Map<String, Object>> data_list;
    private SimpleAdapter sim_adpter;
    private int[] icon = {R.drawable.protocal, R.drawable.system, R.drawable.telephone, R.drawable.text, R.drawable.mail};
    private String[] icon_name = {"用户使用协议", "关于系统", "电话人工帮助", "短信帮助", "邮件帮助"};
    private Handler mHandler ;

    @SuppressLint("HandlerLeak")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        EventBus.getDefault().register(this);
        setTitle("主页");
        mgridview = (GridView) findViewById(R.id.gv_menu);
        data_list = new ArrayList<Map<String, Object>>();
        getData();
        String[] from = {"image", "text"};
        int[] to = {R.id.image, R.id.text};

//        mHandler = new Handler(){
//            public void handleMessage(Message msg) {
//                switch (msg.what) {
//                    case 1:
//                        CharSequence text = "发送邮件成功!";
//                        int duration = Toast.LENGTH_SHORT;
//                        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
//                        toast.show();
//                        break;
//                    default:
//                        break;
//                }
//            }
//        };

        sim_adpter = new SimpleAdapter(this, data_list, R.layout.main_screen_items, from, to);
        mgridview.setAdapter(sim_adpter);
        mgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:

                        break;
                    case 1:

                        break;

                    case 2:
                        Intent intent2 = new Intent();
                        intent2.setAction(Intent.ACTION_CALL);
                        intent2.setData(Uri.parse("tel:5554"));
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + "5554"));
                        intent3.putExtra("sms_body", "test scos helper");
                        startActivity(intent3);
                        break;
                    case 4:
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    EmailSender sender = new EmailSender();
                                    //设置服务器地址和端口，网上搜的到
                                    sender.setProperties("smtp.163.com", "25");
                                    //分别设置发件人，邮件标题和文本内容
                                    sender.setMessage("xutaoup@163.com", "SCOS help!", "我需要帮助！");
                                    //设置收件人
                                    sender.setReceiver(new String[]{"xutaoup@163.com"});
                                    //发送邮件

                                    sender.sendEmail("smtp.163.com", "xutaoup@163.com", "wstgabcd134517");//<span style="font-family: Arial, Helvetica, sans-serif;">sender.setMessage("你的163邮箱账号", "EmailS//ender", "Java Mail ！");这里面两个邮箱账号要一致</span>

//                                    mHandler.sendEmptyMessage(1);
                                    EventBus.getDefault().post(new FirstEvent("1"));
                                } catch (AddressException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } catch (MessagingException e) {

                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }).start();


                }

            }
        });
    }

    public List<Map<String, Object>> getData() {
        for (int i = 0; i < icon.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", icon[i]);
            map.put("text", icon_name[i]);
            data_list.add(map);
        }

        return data_list;
    }

    class FirstEvent {

        private String msg;
        public FirstEvent(String str){
            msg = str;
        }

        public String getMsg(){
            return msg;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FirstEvent event){
        String str = event.getMsg();

        if(str.equals("1")) {
            Toast.makeText(this, "发送成功!", Toast.LENGTH_LONG).show();
        }
    }

        protected void onDestroy() {
                 super.onDestroy();
                 EventBus.getDefault().unregister(this);
             }

}

