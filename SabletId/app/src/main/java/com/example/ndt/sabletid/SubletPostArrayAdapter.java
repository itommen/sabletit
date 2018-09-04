package com.example.ndt.sabletid;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ndt.sabletid.Models.Image.ImageModel;
import com.example.ndt.sabletid.Models.SubletPost.SubletPost;

import java.util.ArrayList;

public class SubletPostArrayAdapter extends ArrayAdapter<SubletPost> {
    private ArrayList<SubletPost> sublets;

    public SubletPostArrayAdapter(@NonNull Context context, @NonNull ArrayList<SubletPost> sublets) {
        super(context, 0, sublets);

        this.sublets = sublets;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final View listItem = convertView == null
                ? LayoutInflater.from(getContext()).inflate(R.layout.sublet_post_list_item,parent,false)
                : convertView;

        if(sublets == null || sublets.size() == 0) {
            return listItem;
        }

        SubletPost sublet = sublets.get(position);

        final ImageView image = listItem.findViewById(R.id.imageView_poster);

        if (sublet.getPhoto() != null) {
            ImageModel.instance.getImage(sublet.getPhoto(), new ImageModel.GetImageListener() {
                @Override
                public void onDone(Bitmap bitmap) {
                    if (bitmap != null) {

                        image.setImageBitmap(bitmap);
                    }
                }
            });
        }
        else {
            image.setImageResource(android.R.color.transparent);
        }

        TextView city = listItem.findViewById(R.id.spl_location);
        city.setText(sublet.getCity());

        TextView fromDate = listItem.findViewById(R.id.splv_from);
        fromDate.setText(sublet.getStartDate());

        TextView toDate = listItem.findViewById(R.id.splv_to);
        toDate.setText(sublet.getStartDate());

        TextView price = listItem.findViewById(R.id.spl_price);
        price.setText(Integer.toString(sublet.getPrice()));

        return listItem;
    }
}
