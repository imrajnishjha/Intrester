package com.wormos.intrester;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class CreatePlaceAdapter extends FirebaseRecyclerAdapter<CreatePlaceModel,CreatePlaceAdapter.placeViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CreatePlaceAdapter(@NonNull FirebaseRecyclerOptions<CreatePlaceModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull placeViewHolder holder, int position, @NonNull CreatePlaceModel model) {
        Log.d("ucl", "onBindViewHolder: ");
        holder.placeName.setText(model.getPlace());
        Glide.with(holder.placeImage.getContext())
                .load(model.getPurl())
                .error(R.drawable.wormos_logo)
                .into(holder.placeImage);
        holder.view.setOnClickListener(v->{
            v.getContext().startActivity(new Intent(v.getContext(),UserData.class).putExtra("id",getRef(position).getKey()));
        });

    }

    @NonNull
    @Override
    public placeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.create_place_icon,parent,false);
        return new placeViewHolder(view);
    }

    static class placeViewHolder extends RecyclerView.ViewHolder{
         TextView placeName;
         ImageView placeImage;
         View view;


        public placeViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            placeName= itemView.findViewById(R.id.place_name);
            placeImage= itemView.findViewById(R.id.place_image);
        }
    }
}
