package es.source.code.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private String[] mTitles = new String[]{"冷菜", "热菜", "海鲜","酒水"};

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return new Tab2Fragment();
        } else if (position == 2) {
            return new Tab3Fragment();
        }else if (position==3){
            return new Tab4Fragment();
        }
        return new Tab1Fragment();
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
