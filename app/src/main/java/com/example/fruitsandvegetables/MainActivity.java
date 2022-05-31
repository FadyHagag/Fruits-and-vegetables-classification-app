package com.example.fruitsandvegetables;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fruitsandvegetables.ml.FinalModel;


import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MainActivity extends AppCompatActivity {
    ImageView iv_image;
    TextView tv_result,tv_confidence;
    Button bt_take_image,bt_menu;
    final static int imageSize=224;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_image=findViewById(R.id.iv_image);
        tv_result=findViewById(R.id.tv_result);
        tv_confidence=findViewById(R.id.tv_confidence);
        bt_take_image=findViewById(R.id.bt_take_image);
       // bt_menu=findViewById(R.id.bt_to_menu);

        bt_take_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // lunch camera if we have permition
                if(checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED){
                    Intent intentCamera=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intentCamera,1);
                }
                else{
                    requestPermissions(new String[]{Manifest.permission.CAMERA},100);
                }
            }
        });

//        bt_menu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intentMenu=new Intent(MainActivity.this,MainActivityRecycleView.class);
//                intentMenu.putExtra("my_main_image",R.id.iv_image);
//                intentMenu.putExtra("my_main_Text",R.id.tv_result);
//                startActivity(intentMenu);
//            }
//        });





    }




    public void ClassifyImage(Bitmap bitmapimage)
    {
        try {
            FinalModel model = FinalModel.newInstance(this);
            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer=ByteBuffer.allocateDirect(4*imageSize*imageSize*3);
            byteBuffer.order(ByteOrder.nativeOrder());
            int []intValues=new int[imageSize*imageSize];
            bitmapimage.getPixels(intValues,0,bitmapimage.getWidth(),0,0,bitmapimage.getWidth(),bitmapimage.getHeight());
            int pixel=0;
            for (int i=0 ;i<imageSize;i++){
                for(int j=0;j<imageSize;j++)
                {
                    int val = intValues[pixel++];
                    byteBuffer.putFloat(((val>>16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val>>8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));

                }

            }
            inputFeature0.loadBuffer(byteBuffer);
            // Runs model inference and gets result.
            FinalModel.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            float[] confidence=outputFeature0.getFloatArray();
            int maxPos=0;
            float maxconfidece=0;
            for(int i=0; i<confidence.length;i++)
            {
                if(confidence[i]>maxconfidece)
                {
                    maxconfidece=confidence[i];
                    maxPos=i;
                }
            }

            String[]classes={"apple","carrot","grape","lemon","okra","onions","orange","peach","pepper","pineapple",
                    "potato","tomato","watermelon"
                    ,"pomegranate","banana","Cucumber","guava","mango","strawberries","Zucchini"};
            tv_result.setText(classes[maxPos]);

//            String s="";
//            for(int i=0;i< classes.length;i++)
//            {
//                s += String.format("%s: %.1f%%\n",classes[i],confidence[i]*100);
//            }
           tv_confidence.setText(maxPos+" - "+classes[maxPos]+" "+(confidence[maxPos]*100)+" ");
            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }

    }









    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==RESULT_OK)
        {
            Bitmap bitmabImage=(Bitmap) data.getExtras().get("data");
            int dimantion = Math.min(bitmabImage.getWidth(),bitmabImage.getHeight());
            bitmabImage = ThumbnailUtils.extractThumbnail(bitmabImage,dimantion,dimantion);
            iv_image.setImageBitmap(bitmabImage);
            bitmabImage=Bitmap.createScaledBitmap(bitmabImage,imageSize,imageSize,false);
            ClassifyImage(bitmabImage);

        }
    }
}