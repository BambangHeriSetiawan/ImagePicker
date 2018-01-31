package com.simx.imagepicker;

import android.Manifest;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.mlsdev.rximagepicker.RxImageConverters;
import com.mlsdev.rximagepicker.RxImagePicker;
import com.mlsdev.rximagepicker.Sources;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.img)
    ImageView img;

    // TODO: 1/31/2018 List permission app
    String [] permissions = new String[] {
            Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    RxPermissions rxPermissions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        rxPermissions  = new RxPermissions(this);
        if (Build.VERSION.SDK_INT >=23){
            requestPermission23();
        }
    }

    private void requestPermission23() {
        rxPermissions.requestEachCombined(permissions).subscribe(
                permission -> {

                    if (permission.granted){
                        showToas("ALL GRANTED");
                    }else if (permission.shouldShowRequestPermissionRationale){

                    }else {

                    }
                }
        );
    }

    private void showToas(String s) {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnCamera)
    public void onBtnCameraClicked() {
        RxImagePicker.with(this).requestImage(Sources.CAMERA).flatMap(new Function<Uri, ObservableSource<Bitmap>>() {
            @Override
            public ObservableSource<Bitmap> apply(Uri uri) throws Exception {
                return RxImageConverters.uriToBitmap(getApplicationContext(),uri);
            }
        }).subscribe(
                new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        img.setImageBitmap(bitmap);
                    }
                }
        );

    }

    @OnClick(R.id.btnGallery)
    public void onBtnGalleryClicked() {
        RxImagePicker.with(this).requestImage(Sources.GALLERY).flatMap(new Function<Uri, ObservableSource<Bitmap>>() {
            @Override
            public ObservableSource<Bitmap> apply(Uri uri) throws Exception {
                return RxImageConverters.uriToBitmap(getApplicationContext(),uri);
            }
        }).subscribe(
                new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        img.setImageBitmap(bitmap);
                    }
                }
        );
    }
}
