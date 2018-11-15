package es.source.code.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import es.source.code.model.User;
import es.source.code.service.ServerObserverService;

import static es.source.code.activity.Tab1Fragment.listViewAdapter;


public class FoodView extends AppCompatActivity {

    private Food food;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private User user;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    private MenuItem update_or_not;
    private TabLayout.Tab one;
    private TabLayout.Tab two;
    private TabLayout.Tab three;
    private TabLayout.Tab four;
    private boolean isBound = false;
    private Messenger s_msger = new Messenger(new sMessageHandler());//用来接收消息
    private Messenger c_msger;//用来向服务器发消息


    class sMessageHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle_data;
            switch (msg.what)
            {
                case 10:
                    //解析该Message携带的菜品库存信息，更新FoodView
                    bundle_data = msg.getData();
                    String foodname = bundle_data.getString("foodname");
                    int position = bundle_data.getInt("position");
                    int foodnum = bundle_data.getInt("foodnum");
                    food.foodnum[position] = foodnum;
                    food.getcoldfoodlist();
                    listViewAdapter.notifyDataSetChanged();
                    Log.d("tool", "进行到这");
                    break;
            }

        }
    }

    private ServiceConnection sc=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            c_msger = new Messenger(service);
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            c_msger = null;
            isBound = false;
        }
    };



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.not_order:
                Intent intent1 = new Intent(FoodView.this,FoodOrderView.class);
                Bundle bundle = new Bundle();
                bundle.putString("order_or_not","not_order");
                bundle.putSerializable("Object",user);
                intent1.putExtras(bundle);
                startActivity(intent1);
                break;
            case R.id.see_ordered:
                Intent intent2 = new Intent(FoodView.this,FoodOrderView.class);
                Bundle bundle2 = new Bundle();
                bundle2.putString("order_or_not","order");
                bundle2.putSerializable("Object",user);
                intent2.putExtras(bundle2);
                startActivity(intent2);
                break;
//                sos.sayHello();
            case R.id.update:
                if(item.getTitle().toString().equals("启动实时更新")) {
                    Message c2smsg = new Message();
                    c2smsg.what = 1;
                    c2smsg.replyTo = s_msger;
                    try {
                        c_msger.send(c2smsg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    item.setTitle("停止更新");
                }else if(item.getTitle().toString().equals("停止更新"))
                {
                    Message c2smsg = new Message();
                    c2smsg.what = 0;
                    c2smsg.replyTo = s_msger;
                    try {
                        c_msger.send(c2smsg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    item.setTitle("启动实时更新");
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_view);
        setTitle("点餐");

        food = (Food)getApplication();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        user = (User)bundle.getSerializable("Object");
        //初始化视图
        initViews();
    }

    private void initViews() {
//使用适配器将ViewPager与Fragment绑定在一起
        mViewPager= (ViewPager) findViewById(R.id.viewPager);
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(myFragmentPagerAdapter);

//将TabLayout与ViewPager绑定在一起
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);

//指定Tab的位置
        one = mTabLayout.getTabAt(0);
        two = mTabLayout.getTabAt(1);
        three = mTabLayout.getTabAt(2);
        four = mTabLayout.getTabAt(3);

//设置Tab的图标
        one.setIcon(R.drawable.cold_dishes);
        two.setIcon(R.drawable.hot_dishes);
        three.setIcon(R.drawable.seafood);
        four.setIcon(R.drawable.drinkings);
    }

    @Override
    protected void onStart() {

        super.onStart();
        Intent intent = new Intent(this, ServerObserverService.class);

        bindService(intent, sc, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isBound){
            unbindService(sc);
            isBound = false;
        }
    }
}
