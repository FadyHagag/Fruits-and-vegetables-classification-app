package com.example.fruitsandvegetables;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.CatHoldView>{
    ArrayList<Fruits> fruits;

    public RecycleViewAdapter(ArrayList<Fruits> fruits) {
        this.fruits = fruits;
    }

    @NonNull
    @Override
    public CatHoldView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.post_coustme,null,false);
        CatHoldView viewHold=new CatHoldView(v);

        return viewHold;
    }

    @Override
    public void onBindViewHolder(@NonNull CatHoldView holder, int position) {
        Fruits f = fruits.get(position);
        holder.iv_image.setImageResource(f.getImg());
        holder.tv_name.setText(f.getName());


    }

    @Override
    public int getItemCount() {
        return fruits.size();
    }







    // holder class for recycle view
    class CatHoldView extends RecyclerView.ViewHolder{
        ImageView iv_image;
        TextView tv_name;
        public CatHoldView(@NonNull View itemView) {
            super(itemView);
            iv_image=itemView.findViewById(R.id.post_custom_iv);
            tv_name=itemView.findViewById(R.id.post_custom_iv);

        }
    }
}
