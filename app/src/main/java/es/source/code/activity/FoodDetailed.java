package es.source.code.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static es.source.code.activity.Tab1Fragment.listViewAdapter;

public class FoodDetailed extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private ViewPager v_pager;
    public static ArrayList<PullFoodlistBean> mData;
    public static ViewPagerAdapter mAdapter;
    Food food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_detailed);
        setTitle("菜品详情");
        food =(Food)getApplication();
        initView();
    }

    private void initView() {
        // TODO Auto-generated method stub
        v_pager = (ViewPager) findViewById(R.id.viewPager);
        mData = new ArrayList<PullFoodlistBean> ();


        for(int i=0;i<food.icon.length;i++) {
            PullFoodlistBean foodlist = new PullFoodlistBean(food.icon_name[i],food.price[i],food.isadd[i],food.icon[i]);
            mData.add(foodlist);
        }

        ArrayList<View> mList = new ArrayList<View>();
        for (int i = 0; i < mData.size(); i++) {
            View v = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(
                    R.layout.food_detailed_page, null);
            mList.add(v);
        }

        Intent intent=getIntent();
        int postion=intent.getIntExtra("position",2);

        mAdapter = new ViewPagerAdapter(getApplicationContext(), mList,mData);
        v_pager.setAdapter(mAdapter);
        v_pager.setCurrentItem(postion);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int arg0) {
        // TODO Auto-generated method stub
    }

    class ViewPagerAdapter extends PagerAdapter {
        ArrayList<PullFoodlistBean> mData;
        TextView title,price;
        ImageView image;
        Context mContext;
        ArrayList<View> mList;
        Button btn_choose;

        public ViewPagerAdapter(Context mContext,
                                ArrayList<View> mList, ArrayList<PullFoodlistBean> mData) {
            super();
            this.mContext = mContext;
            this.mData = mData;
            this.mList = mList;
        }

        @Override
        public int getCount() {
            // TODO
            // 获取要滑动的控件的数量，在这里我们以滑动的广告栏为例，那么这里就应该是展示的广告图片的ImageView数量
            return this.mList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO
            // 来判断显示的是否是同一张图片，这里我们将两个参数相比较返回即可
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO
            // PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
            container.removeView(this.mList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            // TODO
            View v = mList.get(position);
            title = (TextView)v.findViewById(R.id.title);
            price = (TextView)v.findViewById(R.id.price);
            image = (ImageView)v.findViewById(R.id.fooddetailedimage);
            btn_choose = (Button) v.findViewById(R.id.choose);
            setData(position);

            if(food.isadd[position]){
                btn_choose.setText("退点");
                btn_choose.setTextColor(android.graphics.Color.RED);
            }else
            {
                btn_choose.setText("点菜");
            }
            final Button btn = btn_choose;
            //为什么这里必须要用final？？
            btn_choose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(btn.getText().toString().equals("点菜")) {
                        btn.setText("退点");
                        btn.setTextColor(android.graphics.Color.RED);

                        CharSequence text = "点菜成功!";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(mContext, text, duration);
                        toast.show();

                        final int p=position;
                        food.foodnum[p]--;
                        food.isadd[p]=true;

                    }
                    else{
                        btn.setText("点菜");
                        btn.setTextColor(Color.BLACK);

                        CharSequence text = "退点成功!";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(mContext, text, duration);
                        toast.show();
                        final int p=position;
                        food.isadd[p]=false;
                        food.foodnum[p]++;
                    }
                    food.getcoldfoodlist();
                    listViewAdapter.notifyDataSetChanged();
                }
            });

            container.addView(v);
            return this.mList.get(position);
        }

        private void setData(final int position) {
            // TODO Auto-generated method stub
            title.setText(mData.get(position).getIcon_name());
            price.setText(mData.get(position).getPrice()+"rmb");
            image.setImageResource(mData.get(position).getIcon());
        }
    }

}




