package ru.sk42.tradeodata.Activities.Product;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import ru.sk42.tradeodata.Helpers.MyHelper;
import ru.sk42.tradeodata.Helpers.Uttils;
import ru.sk42.tradeodata.Model.Catalogs.Product;
import ru.sk42.tradeodata.Model.Constants;
import ru.sk42.tradeodata.Model.ProductInfo;
import ru.sk42.tradeodata.R;

public class DescriptionFragment extends Fragment implements ProductPresenterContract.ProductDescriptionContract {

    Product product;
    ProductInfo productInfo;

    @Bind(R.id.imageView)
    ImageView mImageView;
    @Bind(R.id.tvProduct_Name)
    TextView tvProductName;
    @Bind(R.id.tvProduct_Description)
    TextView tvProductDescription;
    @Bind(R.id.tvProduct_Show_Hide_Description)
    TextView tvShowHide;
    @Bind(R.id.tvProduct_Price)
    TextView tvProductPrice;

    @Bind(R.id.tvProduct_Code)
    TextView tvProductCode;
    @Bind(R.id.tvProduct_SKU)
    TextView tvProductSKU;


    public DescriptionFragment() {
        // Required empty public constructor
    }

    public static DescriptionFragment newInstance(String refkey) {
        DescriptionFragment fragment = new DescriptionFragment();
        Bundle args = new Bundle();
        args.putString(Constants.REF_KEY_LABEL, refkey);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the shake_anim for this fragment
        View view = inflater.inflate(R.layout.product__description, container, false);
        ButterKnife.bind(this, view);



        if (getArguments() != null) {
            String ref_Key = getArguments().getString(Constants.REF_KEY_LABEL);
            try {
                product = MyHelper.getInstance().getDao(Product.class).queryForEq(Constants.REF_KEY_LABEL, ref_Key).get(0);
                productInfo = MyHelper.getProductInfoDao().queryForEq(Constants.REF_KEY_LABEL, ref_Key).get(0);
            } catch (Exception e) {
                Toast.makeText(this.getContext(), "не найден товар в базе по ссылке " + ref_Key, Toast.LENGTH_SHORT).show();
            }
        }else {
            throw new RuntimeException("Жопа какая-то!");
        }

        tvProductName.setText(product.getDescription());
        tvProductPrice.setText("В розницу по " + Uttils.formatDoubleToMoney(productInfo.getPrice()) + "р");
        tvProductCode.setText(product.getCode());
        tvProductSKU.setText(product.getSKU());

        tvProductDescription.setText(product.getAdditional_Description());
        tvShowHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvProductDescription.setVisibility(View.VISIBLE);
                tvShowHide.setVisibility(View.GONE);

                tvProductDescription.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tvProductDescription.setVisibility(View.GONE);
                        tvShowHide.setVisibility(View.VISIBLE);
                    }
                });
            }
        });


        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void showImage(@Nullable Bitmap bitmap) {
        if (bitmap != null && mImageView != null) {
            mImageView.setImageBitmap(bitmap);
        }
    }
}
