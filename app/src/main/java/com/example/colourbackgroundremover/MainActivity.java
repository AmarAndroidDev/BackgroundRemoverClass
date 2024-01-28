package com.example.colourbackgroundremover;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.backgroundremoverclass.BackgroundRemover;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.button.MaterialButton;
import com.google.mlkit.vision.segmentation.Segmenter;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

public class MainActivity extends AppCompatActivity {
private MaterialButton image,camera;
private ImageView imageView,removeImage,download;
//private ImageButton colorPicker;
private ByteBuffer bufferedt;
private int heightedt;
    Segmenter segmenter;
private int widthedt;
    long downloadId;
private Bitmap resultBitmap;
private SpinKitView pgBar;
private static final int CAMERA_CODE=100;
private static final int IMAGE_CODE=200;
    BackgroundRemover remover;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image=  findViewById(R.id.uploadImage);
        camera=  findViewById(R.id.camera);
        imageView=  findViewById(R.id.bgImage);
        removeImage=  findViewById(R.id.removeImage);
        download=  findViewById(R.id.download);
        //colorPicker=  findViewById(R.id.colorPicker);
        pgBar=  findViewById(R.id.spin_kit);
        getSupportActionBar().hide();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Dexter.withContext(MainActivity.this).withPermissions(Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.CAMERA).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {

                        }
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                           startActivity(intent);
                            Toast.makeText(MainActivity.this, "Please allow all permission", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

            } else {


                Dexter.withContext(MainActivity.this).withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {

                        }
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                        startActivity(intent);
                            Toast.makeText(MainActivity.this, "Please allow all permission", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

        }
  remover  =new BackgroundRemover(MainActivity.this, new BackgroundRemover.BackgroundListner() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        removeImage.setImageBitmap(bitmap);
                        resultBitmap=bitmap;
                        pgBar.setVisibility(View.GONE);

                    }
                });

            }

            @Override
            public void onFailure(Exception error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pgBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, ""+error, Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
      image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, IMAGE_CODE);
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, CAMERA_CODE);
                }
            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resultBitmap!=null){
                    pgBar.setVisibility(View.VISIBLE);
                    saveImageToGallery(MainActivity.this,resultBitmap);
                    pgBar.setVisibility(View.GONE);
                }else {
                    Toast.makeText(MainActivity.this, "Please select a Image", Toast.LENGTH_SHORT).show();
                }

            }
        });
    /*    colorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (resultBitmap!=null){
                    new AmbilWarnaDialog(MainActivity.this, Color.WHITE, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                        @Override
                        public void onCancel(AmbilWarnaDialog dialog) {

                        }

                        @Override
                        public void onOk(AmbilWarnaDialog dialog, int color) {


                   Bitmap bitmap=  changeBackgroundOfImage(resultBitmap,bufferedt,heightedt,widthedt,color);
                    removeImage.setImageBitmap(bitmap);
                        }
                    }).show();
                }else {
                    Toast.makeText(MainActivity.this, "Please select a Image", Toast.LENGTH_SHORT).show();
                }

            }
        });*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_CODE) {
                Uri selectedImageUri = data.getData();
                imageView.setImageURI(selectedImageUri);
Bitmap bitmap=getBitmapFromUri(MainActivity.this,selectedImageUri);
pgBar.setVisibility(View.VISIBLE);
remover.process(bitmap,Color.WHITE);


            } else if (requestCode == CAMERA_CODE) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(imageBitmap);

                pgBar.setVisibility(View.VISIBLE);

                remover.process(imageBitmap,Color.WHITE);
            }
        }
    }


    public static Bitmap getBitmapFromUri(Context context, Uri uri) {
        try {
            ContentResolver contentResolver = context.getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(uri);
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }
    public static void saveImageToGallery(Context context, Bitmap bitmap) {
        File imageFile = saveBitmapToFile(context, bitmap);

        // Insert the image into the media store
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Background");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Remover");
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.DATA, imageFile.getAbsolutePath());

        Uri imageUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        // Notify the media scanner to scan the new image
        MediaScannerConnection.scanFile(context, new String[]{imageFile.getAbsolutePath()}, null, null);

        if (imageUri != null) {
            Toast.makeText(context, "Image saved to gallery", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Failed to save image to gallery", Toast.LENGTH_SHORT).show();
        }
    }

    private static File saveBitmapToFile(Context context, Bitmap bitmap) {
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "BackgroundRemover");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File imageFile = new File(directory, "background_removed_image.jpg");

        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageFile;
    }
    private Bitmap changeBackgroundOfImage(Bitmap image, ByteBuffer buffer, int height, int width, int color) {

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                float bgConfidence = (float) ((1.0 - buffer.getFloat()) * 255);
                if (bgConfidence >= 100) {
                    image.setPixel(x, y, color);
                }


            }
        }
        buffer.rewind();
      return image;
    }

}