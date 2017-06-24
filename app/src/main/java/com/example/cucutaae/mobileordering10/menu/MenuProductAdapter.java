package com.example.cucutaae.mobileordering10.menu;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.cucutaae.mobileordering10.R;
import com.example.cucutaae.mobileordering10.menu.MenuProduct;
import com.firebase.client.annotations.NotNull;
import java.util.List;

/**
 * Created by cucut on 4/22/2017.
 */

public class MenuProductAdapter extends ArrayAdapter<MenuProduct> {
    private Activity context;
    private int resources;
    private List<MenuProduct> listImage;

    public MenuProductAdapter(@NotNull Activity context, @LayoutRes int resources, @NotNull List<MenuProduct> objects){
        super(context,resources,objects);
        this.context = context;
        this.resources =  resources;
        this.listImage = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View view  = inflater.inflate(resources,null);
        TextView tvProductName = (TextView)view.findViewById(R.id.tvProductItemName);
        ImageView ivProductPicture = (ImageView)view.findViewById(R.id.ivProductPicture);

        tvProductName.setText(listImage.get(position).getName());
        Glide.with(context).load(listImage.get(position).getImageURL()).into(ivProductPicture);

        return view;
    }
}
