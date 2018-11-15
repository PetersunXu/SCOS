package es.source.code.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import es.source.code.model.*;

public class MainScreen extends AppCompatActivity  {
    private Button btn;
    private User user=new User();
    private GridView mgridview;
    private List<Map<String,Object>> data_list;
    private SimpleAdapter sim_adpter;
    private int[] icon={R.drawable.order_dish,R.drawable.form,R.drawable.login_register,R.drawable.help};
    private String[] icon_name={"点菜","查看订单","登录/注册","系统帮助"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        setTitle("主页");
        mgridview = (GridView)findViewById(R.id.gv_menu);
        data_list= new ArrayList<Map<String,Object>>();
        getData();
        String [] from ={"image","text"};
        int [] to = {R.id.image,R.id.text};

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String data= null;
        if (bundle != null) {
            data = bundle.getString("Extra_data");
        }
        User LoginUser = (User)bundle.getSerializable("Object");

        if (data.equals("LoginSuccess"))
        {
            user.setUsername(LoginUser.getUsername());
            user.setPassword(LoginUser.getPassword());
            user.setOlduser(LoginUser.getOldUser());

            sim_adpter = new SimpleAdapter(this,data_list,R.layout.main_screen_items,from,to);
            mgridview.setAdapter(sim_adpter);
            mgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch(position)
                    {
                        case 0:
                            Intent intent1 = new Intent(MainScreen.this,FoodView.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("Object",user);
                            intent1.putExtras(bundle);
                            startActivity(intent1);
                            break;
                        case 1:
                            Intent intent2 = new Intent(MainScreen.this,FoodOrderView.class);
                            Bundle bundle2 = new Bundle();
                            bundle2.putString("order_or_not","not_order");
                            bundle2.putSerializable("Object",user);
                            intent2.putExtras(bundle2);
                            startActivity(intent2);
                            break;
                        case 2:
                            Intent intent = new Intent(MainScreen.this,LoginOrRegister.class);
                            startActivity(intent);
                            break;
                        case 3:
                            Intent intent3 = new Intent(MainScreen.this,SCOSHelper.class);
                            startActivity(intent3);
                            break;
                    }

                }
            });

        }else if(data.equals("RegisterSuccess"))
        {

            user.setUsername(LoginUser.getUsername());
            user.setPassword(LoginUser.getPassword());
            user.setOlduser(LoginUser.getOldUser());
            Context context = getApplicationContext();
            CharSequence text = "欢迎您成为SCOS新用户!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            sim_adpter = new SimpleAdapter(this,data_list,R.layout.main_screen_items,from,to);
            mgridview.setAdapter(sim_adpter);
            mgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch(position)
                    {
                        case 0:
                            Intent intent1 = new Intent(MainScreen.this,FoodView.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("Object",user);
                            intent1.putExtras(bundle);
                            startActivity(intent1);
                            break;
                        case 1:
                            Intent intent2 = new Intent(MainScreen.this,FoodOrderView.class);
                            Bundle bundle2 = new Bundle();
                            bundle2.putString("order_or_not","not_order");
                            bundle2.putSerializable("Object",user);
                            intent2.putExtras(bundle2);
                            startActivity(intent2);
                            break;
                        case 2:
                            Intent intent = new Intent(MainScreen.this,LoginOrRegister.class);
                            startActivity(intent);
                            break;
                        case 3:
                            Intent intent3 = new Intent(MainScreen.this,SCOSHelper.class);
                            startActivity(intent3);
                            break;
                    }

                }
            });
        }else{
            user = null;
            sim_adpter = new SimpleAdapter(this,data_list.subList(2,4),R.layout.main_screen_items,from,to);
            mgridview.setAdapter(sim_adpter);
            mgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch(position)
                    {
                        case 0:
                            Intent intent = new Intent(MainScreen.this,LoginOrRegister.class);
                            startActivity(intent);
                            break;
                        case 1:
                            Intent intent3 = new Intent(MainScreen.this,SCOSHelper.class);
                            startActivity(intent3);
                            break;
                    }

                }
            });
        }
    }

    public List<Map<String,Object>> getData()
    {
        for(int i=0;i<icon.length;i++)
        {
            Map<String,Object>  map=new HashMap<String,Object>();
            map.put("image",icon[i]);
            map.put("text",icon_name[i]);
            data_list.add(map);
        }

        return  data_list;
    }


}
