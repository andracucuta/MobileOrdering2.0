package com.example.cucutaae.mobileordering10.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.example.cucutaae.mobileordering10.R;
import com.example.cucutaae.mobileordering10.objects.Review;
import java.util.List;

/**
 * Created by cucut on 4/30/2017.
 */

public class ReviewAdapter extends BaseAdapter {

    private Context mContext;
    private List<Review> mReviewList;

    public ReviewAdapter(Context mContext, List<Review> mReviewList) {
        this.mContext = mContext;
        this.mReviewList = mReviewList;
    }

    @Override
    public int getCount() {
        return mReviewList.size();
    }

    @Override
    public Object getItem(int position) {
        return mReviewList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(mContext, R.layout.review_item_list,null);

        ImageView ivUserPicture = (ImageView) view.findViewById(R.id.ivUserPicture);
        TextView tvUserName = (TextView)view.findViewById(R.id.tvUserName);
        TextView tvPostDate = (TextView)view.findViewById(R.id.tvPostDate);
        TextView tvReview = (TextView) view.findViewById(R.id.tvReview);
        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);

        //set Content

        //Picasso.with(getBaseContext()).load(mReviewList.get(position).getProfilePictureUrl()).into(ivUserPicture);

        tvUserName.setText(mReviewList.get(position).getUserName());
        tvPostDate.setText(mReviewList.get(position).getPostDate().toString());
        tvReview.setText((mReviewList.get(position).getReview()));
        ratingBar.setRating(mReviewList.get(position).getScore());

        ratingBar.setEnabled(false);

        return view;
    }
}
