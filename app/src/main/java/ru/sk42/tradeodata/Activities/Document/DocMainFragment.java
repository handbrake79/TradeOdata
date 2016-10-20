package ru.sk42.tradeodata.Activities.Document;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.sk42.tradeodata.Activities.Document.Adapters.DocumentFragmentPageAdapter;
import ru.sk42.tradeodata.Activities.MyActivityFragmentInteractionInterface;
import ru.sk42.tradeodata.Model.Documents.DocSale;
import ru.sk42.tradeodata.R;

// In this case, the fragment displays simple text based on the page
public class DocMainFragment extends Fragment {
    private MyActivityFragmentInteractionInterface mListener;

    FragmentPagerAdapter fragmentPagerAdapter ;
    public ViewPager viewPager;
    PagerSlidingTabStrip pagerSlidingTabStrip;

    @Bind(R.id.doc_total_total_text)
    TextView mTotalText;

    @Bind(R.id.doc_total_products_count_text)
    TextView mProductsCount;

    @Bind(R.id.doc_total_need_shipping_text)
    TextView mNeedShippingText;

    @Bind(R.id.doc_total_shipping_cost_text)
    TextView mShippingCostText;

    @Bind(R.id.doc_total_need_unload_text)
    TextView mNeedUnloadText;

    @Bind(R.id.doc_total_unload_cost_text)
    TextView mUnloadCostText;

    @Bind(R.id.doc_total_workers_count_text)
    TextView mWorkersCountText;

    @Bind(R.id.doc_total_shipping_total_text)
    TextView mShippingTotalText;


    public void refreshTotal() {
        DocumentActivity activity = (DocumentActivity) getActivity();
        DocSale docSale = activity.getDocSale();

        mTotalText.setText(docSale.getTotal().toString());

        mProductsCount.setText(String.valueOf(docSale.getProducts().size()));

        mNeedShippingText.setText(docSale.getNeedShipping() ? "Нужна доставка" : "Самовывоз");

        mShippingCostText.setText(docSale.getShippingCost().toString());

        mNeedUnloadText.setText(docSale.getNeedUnload() ? "Наши грузчики" : "Без разгрузки");

        mUnloadCostText.setText(docSale.getUnloadCost().toString());

        mWorkersCountText.setText(docSale.getWorkersCount().toString());

        mShippingTotalText.setText(docSale.getShippingTotal().toString());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.doc_main_fragment, container, false);
        ButterKnife.bind(this, v);

        refreshTotal();

        return v;
    }

    public void setCurrentPage(int page){
        viewPager.setCurrentItem(page);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragmentPagerAdapter = new DocumentFragmentPageAdapter(getChildFragmentManager());

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setAdapter(fragmentPagerAdapter);

        // Give the PagerSlidingTabStrip the ViewPager
         pagerSlidingTabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        pagerSlidingTabStrip.setViewPager(viewPager);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = (MyActivityFragmentInteractionInterface) getActivity();
        mListener.onFragmentDetached(this);
    }

}
