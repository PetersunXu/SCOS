package es.source.code.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;



public class FoodOrderView extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private MyFragmentPagerAdapter2 myFragmentPagerAdapter2;
    private TabLayout.Tab one;
    private TabLayout.Tab two;

    private Food food;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        food=(Food)getApplication();
        food.setIsadd(false,3);
        setContentView(R.layout.food_order_view);
        setTitle("点餐详情");

        //初始化视图
        initViews();
    }

    private void initViews() {

        //使用适配器将ViewPager与Fragment绑定在一起
        mViewPager= (ViewPager) findViewById(R.id.viewPager);
        myFragmentPagerAdapter2 = new MyFragmentPagerAdapter2(getSupportFragmentManager());
        mViewPager.setAdapter(myFragmentPagerAdapter2);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String data=bundle.getString("order_or_not");
        if(data.equals("not_order")) {
            mViewPager.setCurrentItem(0);
        }else
        {
            mViewPager.setCurrentItem(1);
        }

        //将TabLayout与ViewPager绑定在一起
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);

        //指定Tab的位置
        one = mTabLayout.getTabAt(0);
        two = mTabLayout.getTabAt(1);

    }

    class MyFragmentPagerAdapter2 extends FragmentPagerAdapter {

        private String[] mTitles = new String[]{"未下单菜","已下单菜"};

        public MyFragmentPagerAdapter2(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 1) {
                return new Tab6Fragment();
            }
            return new Tab5Fragment();
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

        //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }
}

