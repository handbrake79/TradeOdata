package ru.sk42.tradeodata.Activities.Document.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import ru.sk42.tradeodata.Activities.Document.ProductsFragment;
import ru.sk42.tradeodata.Activities.Document.RequisitesFragment;
import ru.sk42.tradeodata.Activities.Document.ServicesFragment;
import ru.sk42.tradeodata.Activities.Document.ShippingFragment;
import ru.sk42.tradeodata.Model.Document.SaleRecord;

/**
 * Created by PostRaw on 18.04.2016.
 */
public class DocumentFragmentPageAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 4;
    private String tabTitles[] = new String[]{"Реквизиты", "Товары", "Услуги", "Доставка"};

    public RequisitesFragment requisitesFragment;
    public ProductsFragment productsFragment;
    public ServicesFragment servicesFragment;
    public ShippingFragment shippingFragment;

    public DocumentFragmentPageAdapter(FragmentManager fm) {
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
                if (requisitesFragment == null) {
                    requisitesFragment = new RequisitesFragment();
                }
                return requisitesFragment;
            case 1:
                if (productsFragment == null) {
                    productsFragment = new ProductsFragment();
                }
                return productsFragment;
            case 2:
                if (servicesFragment == null) {
                    servicesFragment = new ServicesFragment();
                }
                return servicesFragment;
            case 3:
                if (shippingFragment == null) {
                    shippingFragment = new ShippingFragment();
                }
                return shippingFragment;
        }
        if (requisitesFragment == null) {
            requisitesFragment = new RequisitesFragment();
        }
        return requisitesFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }


}