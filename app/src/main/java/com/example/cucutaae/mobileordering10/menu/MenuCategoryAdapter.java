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
import com.firebase.client.annotations.NotNull;
import java.util.List;


/**
 * Created by cucut on 4/18/2017.
 */

public class MenuCategoryAdapter extends ArrayAdapter<MenuCategory> {
    private Activity context;
    private int resources;
    private List<MenuCategory> listImage;

    public MenuCategoryAdapter(@NotNull Activity context, @LayoutRes int resources, @NotNull List<MenuCategory> objects){
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
        TextView tvName = (TextView)view.findViewById(R.id.tvCategoryName);
        ImageView img = (ImageView)view.findViewById(R.id.ivCategoryPicture);

        tvName.setText(listImage.get(position).getName());
        Glide.with(context).load(listImage.get(position).getUrl()).into(img);

        return view;
    }
}
