package com.example.ankita.tseccanteen.Menu;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.ankita.tseccanteen.BuildConfig;
import com.example.ankita.tseccanteen.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FoodsAnkita");
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    ProgressBar menuProgressBar;

    String name, price, availability, description;
    EditText foodName, foodPrice, foodDescription;
    RadioGroup availableRadioGroup;
    ImageView foodImage;
    Button addFoodItem, addFoodImage, changeAvailability;

    File file;
    Uri imageUri;
    String title;
    Task<Uri> downloadUrl;

    RecyclerView menuRecyclerView;
    RecyclerView.Adapter adapter;
    ArrayList<MenuModalClass> menuList = new ArrayList<>();
    MenuAdapter.OnMenuClickListener onMenuClickListener;
    FloatingActionButton addMenuItem;
    Context context;
    int totalFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        context = MenuActivity.this;
        menuRecyclerView = findViewById(R.id.rv_menu);
        addMenuItem = findViewById(R.id.fab_add_menu_item);
        menuProgressBar = findViewById(R.id.progress_bar_menu);

        addMenuItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                                                .customView(R.layout.dialog_add_food_item_and_available, false)
                                                .title("Add Food Item")
                                                .show();

                foodName = (EditText) materialDialog.findViewById(R.id.et_food_name);
                foodPrice = (EditText) materialDialog.findViewById(R.id.et_food_price);
                foodDescription = (EditText) materialDialog.findViewById(R.id.et_food_description);
                availableRadioGroup = (RadioGroup) materialDialog.findViewById(R.id.rg_availability);
                foodImage = (ImageView) materialDialog.findViewById(R.id.iv_add_food_image);
                addFoodImage = (Button) materialDialog.findViewById(R.id.btn_add_image);
                addFoodItem = (Button) materialDialog.findViewById(R.id.btn_add_menu_item);

                availableRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.rb_yes:
                                availability = "Available";
                                break;

                            case R.id.rb_no:
                                availability = "Not Available";
                                break;
                        }
                    }
                });

                addFoodImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new MaterialDialog.Builder(context)
                                .title("Add food image")
                                .positiveText("Camera")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        if(hasPermissionsCamera()) {
                                            takePhoto();
                                        } else {
                                            requestPermissionCamera();
                                        }
                                    }
                                })
                                .negativeText("Gallery")
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        if(hasPermissionsGallery()) {
                                            accessImage();
                                        } else {
                                            requestPermissionGallery();
                                        }
                                    }
                                })
                                .show();
                    }
                });

                addFoodItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        name = foodName.getText().toString();
                        price = foodPrice.getText().toString();
                        description = foodDescription.getText().toString();
                        String foodId = "F"+(totalFood + 1);

                        uploadFile(name, price, description, availability, foodId);

                        materialDialog.dismiss();
                    }
                });
            }

        });


        onMenuClickListener = new MenuAdapter.OnMenuClickListener() {
            @Override
            public void onMenuItemClick(final int position) {
                final MaterialDialog materialDialog = new MaterialDialog.Builder(context)
                        .customView(R.layout.dialog_add_food_item_and_available, false)
                        .title("Availability")
                        .show();

                foodName = (EditText) materialDialog.findViewById(R.id.et_food_name);
                foodPrice = (EditText) materialDialog.findViewById(R.id.et_food_price);
                foodDescription = (EditText) materialDialog.findViewById(R.id.et_food_description);
                availableRadioGroup = (RadioGroup) materialDialog.findViewById(R.id.rg_availability);
                addFoodItem = (Button) materialDialog.findViewById(R.id.btn_add_menu_item);
                addFoodImage = (Button) materialDialog.findViewById(R.id.btn_add_image);
                changeAvailability = (Button) materialDialog.findViewById(R.id.btn_change_available);

                foodName.setVisibility(View.GONE);
                foodPrice.setVisibility(View.GONE);
                foodDescription.setVisibility(View.GONE);
                addFoodItem.setVisibility(View.GONE);
                addFoodImage.setVisibility(View.GONE);
                changeAvailability.setVisibility(View.VISIBLE);

                availableRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId) {
                            case R.id.rb_yes:
                                availability = "Available";
                                break;

                            case R.id.rb_no:
                                availability = "Not Available";
                                break;
                        }
                    }
                });

                changeAvailability.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String foodId = "F"+(position+1);
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("FoodsAnkita")
                                                                .child(foodId).child("availability");
                        databaseReference.setValue(availability);
                        materialDialog.dismiss();
                        menuRecyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                menuRecyclerView.smoothScrollToPosition(position);
                            }
                        });
                    }
                });

            }
        };

    }


    @Override
    protected void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                menuList.clear();
                int count = 0;
                for (DataSnapshot menuSnapshot: dataSnapshot.getChildren()) {
                    MenuModalClass menuModalClass = menuSnapshot.getValue(MenuModalClass.class);
                    menuList.add(menuModalClass);
                    count++;
                    if(count >= dataSnapshot.getChildrenCount()) {
                        menuProgressBar.setVisibility(View.GONE);
                        menuRecyclerView.setVisibility(View.VISIBLE);
                    }
                }
                menuRecyclerView.setHasFixedSize(true);
                menuRecyclerView.setLayoutManager(new LinearLayoutManager(MenuActivity.this));
                adapter = new MenuAdapter(MenuActivity.this, menuList, onMenuClickListener);
                totalFood = menuList.size();
                menuRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if(requestCode == 0) {
                title = getTitleOfFile(file.toString());
                Log.d("urmi", "title: "+title);

            }
            if(requestCode == 1) {
                imageUri=data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                String picturePath = getPath(filePathColumn);
                title=getTitleOfFile(picturePath);
            }
            if(imageUri != null){
                foodImage.setVisibility(View.VISIBLE);
                foodImage.setImageURI(imageUri);
            }
        }
    }

    public boolean hasPermissionsCamera() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    public boolean hasPermissionsGallery() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    public void requestPermissionGallery() {
        String[] permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, 2);
        }
    }

    public void requestPermissionCamera() {

        String[] permissions = new String[]{Manifest.permission.CAMERA};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, 1);
        }
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(this.getExternalCacheDir(),
                String.valueOf(System.currentTimeMillis()) + ".jpg");
        imageUri = FileProvider.getUriForFile(context,
                BuildConfig.APPLICATION_ID + ".provider",
                file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        this.startActivityForResult(intent, 0);

    }

    private void accessImage() {
        Intent photoLibraryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(photoLibraryIntent, 1);
    }

    public String getTitleOfFile(String filePath){

        char[] title=filePath.toCharArray();
        String finalTitle="";
        for(int count=title.length-1;count>=0;count--){
            if(title[count]=='/')
                break;
            finalTitle=finalTitle+title[count];
        }
        return new StringBuilder(finalTitle).reverse().toString();
    }

    public String getPath(String[] filePathColumn){
        Cursor cursor = getContentResolver().query(imageUri,
                filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filepath = cursor.getString(columnIndex);
        cursor.close();
        return filepath;
    }

    public void uploadFile(final String name, final String price, final String description, final String availability, final String foodId){
        final StorageReference storageRef = firebaseStorage.getReference(""+title);
        UploadTask uploadTask = storageRef.putFile(imageUri);

        downloadUrl = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return storageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();

                    MenuModalClass menuModalClass = new MenuModalClass();
                    menuModalClass.setName(name);
                    menuModalClass.setPrice(price);
                    menuModalClass.setDescription(description);
                    menuModalClass.setAvailability(availability);
                    menuModalClass.setImage(downloadUri.toString());
                    databaseReference.child(foodId).setValue(menuModalClass);

                } else {

                }
            }
        });

    }

}
