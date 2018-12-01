package com.example.adammoyer.assignment_maps_adammoyer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private ArrayList<MapLocation> locationDataList = null;
    private static final String TAG = "RecyclerViewAdapter";

    public RecyclerViewAdapter(Context context, ArrayList<MapLocation> locationDataList) {
        this.context = context;
        this.locationDataList = locationDataList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CardView parentLayout;
        TextView textViewLatitude;
        TextView textViewLongitude;
        TextView textViewLocation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parentLayout = itemView.findViewById(R.id.cardView);
            textViewLatitude = itemView.findViewById(R.id.txt_view_latitute);
            textViewLongitude = itemView.findViewById(R.id.txt_view_longitute);
            textViewLocation =  itemView.findViewById(R.id.text_view_location);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        MapLocation locationData = locationDataList.get(position);
        viewHolder.textViewLatitude.setText("Lat: "+locationData.getLatitude().toString());
        viewHolder.textViewLongitude.setText("Long: "+locationData.getLongitude().toString());
        viewHolder.textViewLocation.setText("Location: "+String.valueOf(locationData.getTitle()));
        Toast.makeText(context, locationData.getTitle(), Toast.LENGTH_SHORT );
    }

    @Override
    public int getItemCount() {
        return locationDataList.size();
    }
}