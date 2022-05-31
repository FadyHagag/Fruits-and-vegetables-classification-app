package com.example.fruitsandvegetables;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivityRecycleView extends AppCompatActivity {
    RecyclerView rv;
    int res_img;
    String res_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_recycle_view);
        rv=findViewById(R.id.rv_manu);

        Bundle bundle= getIntent().getExtras();
        if (bundle !=null)
        {
            res_img=bundle.getInt("my_main_image");
            res_type=bundle.getString("my_main_Text");

        }
        ArrayList<Fruits> myfruits =new ArrayList<>();
          myfruits.add(new Fruits(res_img,res_type));
//        myfruits.add(new Fruits(R.drawable.like,"cat2"));
//        myfruits.add(new Fruits(R.drawable.logo1,"cat3"));
//        myfruits.add(new Fruits(R.drawable.post,"cat1"));
//        myfruits.add(new Fruits(R.drawable.like,"cat2"));
//        myfruits.add(new Fruits(R.drawable.logo1,"cat3"));

        RecycleViewAdapter adapter =new RecycleViewAdapter(myfruits);
        RecyclerView.LayoutManager lm=new LinearLayoutManager(this);


        rv.setHasFixedSize(true);
        rv.setLayoutManager(lm);
        rv.setAdapter(adapter);
    }
}