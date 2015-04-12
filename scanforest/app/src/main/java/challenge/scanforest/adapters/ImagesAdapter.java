package challenge.scanforest.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import challenge.scanforest.R;
import challenge.scanforest.models.Image;
import challenge.scanforest.vh.ImageHolder;

/**
 * Created by gerardo on 4/12/15.
 */
public class ImagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Image> mImages;

    public ImagesAdapter(Context context,ArrayList<Image> images){
        mImages=images;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.picture, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ImageHolder vh = new ImageHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        ImageHolder vh=(ImageHolder)viewHolder;
        //vh.mImage
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }
}
