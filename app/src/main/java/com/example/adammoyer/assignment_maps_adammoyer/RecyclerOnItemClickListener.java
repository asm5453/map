package com.example.adammoyer.assignment_maps_adammoyer;

import android.view.View;

public interface RecyclerOnItemClickListener {

    void onItemClick(View view, int position);

    void onLongItemClick(View view, int position);
}
