package challenge.scanforest.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import challenge.scanforest.R;
import challenge.scanforest.models.AlertImage;
import challenge.scanforest.vh.ImageHolder;

/**
 * Created by gerardo on 4/12/15.
 */
public class ImagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<AlertImage> mAlertImages;

    public ImagesAdapter(Context context,ArrayList<AlertImage> alertImages){
        mAlertImages = alertImages;
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
        AlertImage m= mAlertImages.get(i);
        //vh.mImage.setImageURI(Uri.fromFile());
    }

    @Override
    public int getItemCount() {
        return mAlertImages.size();
    }
}
