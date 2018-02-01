package com.simx.imagepicker;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.erikagtierrez.multiple_media_picker.Gallery;
import com.mlsdev.rximagepicker.RxImageConverters;
import com.mlsdev.rximagepicker.RxImagePicker;
import com.mlsdev.rximagepicker.Sources;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;

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
    String[] permissions = new String[]{
            Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    RxPermissions rxPermissions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        rxPermissions = new RxPermissions(this);
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermission23();
        }
    }

    private void requestPermission23() {
        rxPermissions.requestEachCombined(permissions).subscribe(
                permission -> {

                    if (permission.granted) {
                        showToas("ALL GRANTED");
                    } else if (permission.shouldShowRequestPermissionRationale) {

                    } else {

                    }
                }
        );
    }

    private void showToas(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnCamera)
    public void onBtnCameraClicked() {
        RxImagePicker.with(this).requestImage(Sources.CAMERA).flatMap(new Function<Uri, ObservableSource<Bitmap>>() {
            @Override
            public ObservableSource<Bitmap> apply(Uri uri) throws Exception {
                return RxImageConverters.uriToBitmap(getApplicationContext(), uri);
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
        RxImagePicker.with(Apps.getContext())
                .requestImage(Sources.GALLERY)
                .flatMap(uri -> RxImageConverters.uriToBitmap(Apps.getContext(), uri))
                .subscribe(
                        bitmap -> initDataBitmap(bitmap));

    }

    private void showDialogSelected() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.gallery_dialog_selected, null);
        Button btnSingle = view.findViewById(R.id.btnSingle);
        Button btnMulti = view.findViewById(R.id.btnMulti);
        btnSingle.setOnClickListener(btnSingleClickListener);
        builder.setView(view);
        builder.setTitle("Choose");
        AlertDialog dialog = builder.create();
        btnMulti.setOnClickListener(v -> {

        });
        dialog.show();
    }

    private static View.OnClickListener btnSingleClickListener = v -> {
    };


    private static void initDataBitmap(Bitmap bitmap) {

    }

    private static View.OnClickListener btnMultiClickListener = v -> {


    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == RESULT_OK) {
            ArrayList<String> selectionResult = data.getStringArrayListExtra("result");
            Log.e("MainActivity", "onActivityResult" + selectionResult.toString());
        }
    }

    @OnClick(R.id.btnMulti)
    public void onBtnMultiClicked() {
        Intent intent = new Intent(this, Gallery.class);
        // Set the title
        intent.putExtra("title", "Select media");
        // Mode 1 for both images and videos selection, 2 for images only and 3 for videos!
        intent.putExtra("mode", 1);
        intent.putExtra("maxSelection", 3); // Optional
        startActivityForResult(intent, 111);
    }
}
