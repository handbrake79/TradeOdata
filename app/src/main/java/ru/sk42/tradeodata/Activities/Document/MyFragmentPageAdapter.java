package ru.sk42.tradeodata.Activities.Document;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by test on 18.04.2016.
 */
public class MyFragmentPageAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 4;
    private String tabTitles[] = new String[]{"Реквизиты", "Товары", "Услуги", "Доставка"};

    public MyFragmentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new RequisitesFragment();
            case 1:
                return new ProductsFragment();
            case 2:
                return new ServicesFragment();
            case 3:
                return new ShippingFragment();


        }
        return new RequisitesFragment();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}