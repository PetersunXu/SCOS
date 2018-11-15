package es.source.code.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import es.source.code.*;
import es.source.code.model.User;
import es.source.code.service.UpdateService;

public class LoginOrRegister extends AppCompatActivity{

    private TimeHandler timehandler =new TimeHandler();
    private Button btn_pd;
    private Button btn_rt;
    private Button btn_register;
    private int progress = 0;
    private String Username;
    private String Password;
    private CustomProgressDialog progressDialog;
    private String[] MESSAGES = {"载入中","载入中.","载入中..","载入中..."};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_or_register);

        setTitle("注册/登录");
        progressDialog = new CustomProgressDialog(this);
        progressDialog.setMessage("载入中");
        timehandler.sendEmptyMessage(1);
        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
        if(sp.getString("username",null)==null)
        {
            btn_pd=findViewById(R.id.login);
            btn_pd.setVisibility(View.GONE);
        }else
        {
            btn_register=(Button)findViewById(R.id.register);
            btn_register.setVisibility(View.GONE);
        }

/*        登录按钮的点击事件*/
        btn_pd=(Button)findViewById(R.id.login);//获取按钮资源
        btn_pd.setOnClickListener(new OnClickListener()
        {

            public void onClick(View v)
            {

    //这里写的是加载进度条，并验证是否符合规则，不符合规则便在当前输入框提示错误信息，符合规则，跳转并传递“login success”
                progressDialog.show();
                progress=0;
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        EditText Eduser = (EditText) findViewById(R.id.username);
                        EditText Edpswd = (EditText) findViewById(R.id.password);
                        Username = Eduser.getText().toString();
                        Password = Edpswd.getText().toString();


    //使用正则表达式验证
                        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
                        sp.edit()
                                .putInt("loginState",1)
                                .putString("username", Username)
                                .apply();

                        String reg = "^[A-Za-z1-9_-]+$";
                        Pattern pattern = Pattern.compile(reg);
                        Matcher matcher1 = pattern.matcher(Username);
                        boolean b1 = matcher1.matches();
                        Matcher matcher2 = pattern.matcher(Password);
                        boolean b2 = matcher2.matches();
                        if (b1 && b2) {
                            final User LoginUser = new User();
                            LoginUser.setUsername(Username);
                            LoginUser.setPassword(Password);
                            LoginUser.setOlduser(true);

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    String url=HttpUtilsHttpURLConnection.BASE_URL+"/LoginValidator";
                                    Map<String, String> params = new HashMap<String, String>();

                                    params.put("name",Username);
                                    params.put("password",Password);

                                    String result = HttpUtilsHttpURLConnection.getContextByHttp(url,params);

                                    Message msg = new Message();
                                    msg.what=0x12;
                                    Bundle data=new Bundle();
                                    data.putString("result",result);
                                    msg.setData(data);
                                    handler.sendMessage(msg);
                                }

                                @SuppressLint("HandlerLeak")
                                Handler handler = new Handler(){
                                    @Override
                                    public void handleMessage(Message msg) {
                                        if (msg.what==0x12){
                                            Bundle data = msg.getData();
                                            String key = data.getString("result");//得到json返回的json

                                            try {
                                                JSONObject json= new JSONObject(key);
                                                String result = (String) json.get("RESULTCODE");
                                                if ("1".equals(result)){
                                                    Toast.makeText(LoginOrRegister.this,"登录成功",Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(LoginOrRegister.this, MainScreen.class);
                                                    Bundle bundle = new Bundle();
                                                    bundle.putSerializable("Object",LoginUser);
                                                    bundle.putString("Extra_data","LoginSuccess");
                                                    intent.putExtras(bundle);
                                                    startActivity(intent);

                                                }else if("0".equals(result)){
                                                    Toast.makeText(LoginOrRegister.this,"登录失败",Toast.LENGTH_LONG).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                };
                            }).start();


                        } else {
                            Eduser.setError("输入内容不符合规则！");
                        }

                    }
                });
            }
        });

/*        返回按钮的点击事件*/
        btn_rt=(Button)findViewById(R.id.return_to_mainscreen);
        btn_rt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginOrRegister.this,MainScreen.class);
                Bundle bundle = new Bundle();
                bundle.putString("Extra_data","return");
                intent.putExtras(bundle);
                startActivity(intent);

                SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
                sp.edit()
                        .putInt("loginState",0)
                        .apply();
            }
        });


/*        注册按钮的点击事件*/
        btn_register=(Button)findViewById(R.id.register);
        btn_register.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                progress=0;
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        EditText Eduser = (EditText) findViewById(R.id.username);
                        EditText Edpswd = (EditText) findViewById(R.id.password);
                        Username = Eduser.getText().toString();
                        Password = Edpswd.getText().toString();

                        SharedPreferences sp = getSharedPreferences("login", Context.MODE_PRIVATE);
                        sp.edit()
                                .putInt("loginState",1)
                                .putString("username", Username)
                                .apply();
//使用正则表达式验证
                        String reg = "^[A-Za-z1-9_-]+$";
                        Pattern pattern = Pattern.compile(reg);
                        Matcher matcher1 = pattern.matcher(Username);
                        boolean b1 = matcher1.matches();
                        Matcher matcher2 = pattern.matcher(Password);
                        boolean b2 = matcher2.matches();
                        if (b1 && b2) {
                            final User LoginUser = new User();
                            LoginUser.setUsername(Username);
                            LoginUser.setPassword(Password);
                            LoginUser.setOlduser(false);

                            Intent intent = new Intent(LoginOrRegister.this, MainScreen.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("Object",LoginUser);
                            bundle.putString("Extra_data","RegisterSuccess");
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            Eduser.setError("输入内容不符合规则！");
                        }

                    }
                });
            }
        });

    }


    class TimeHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:

                    if (progress < 100){
                        progress += 25;
                        progressDialog.setProgress(progress);
                        progressDialog.setMessage(MESSAGES[progress / 25 % 4]);
                    }else {
                        progress=0;
                        progressDialog.cancel();
                    }

                    sendEmptyMessageDelayed(1,500);
                    break;
            }
        }
    }


}

