package com.example.cucutaae.mobileordering10.menu;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.cucutaae.mobileordering10.R;
import com.example.cucutaae.mobileordering10.utils.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddProductActivity extends AppCompatActivity {

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private ImageView ivProductImage;
    private EditText txtProductName;
    private EditText txtProductDescription;
    private EditText txtPrice;
    private EditText txtQuantity;
    private EditText txtCategoryName;
    private EditText txtCal;
    private EditText txtProductIngredients;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    List<String> categoryList = new ArrayList<String>();

    private Uri imgUri;

    String uri = "@drawable/defaultimage";

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("Categories");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(Constants.FB_DATABASE_PRODUCTS_PATH);

        initComponents();

        prepareCategoryList();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                txtCategoryName.setText(
                        listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition));

                return false;
            }
        });

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                int height = 0;
                for (int i = 0; i < expListView.getChildCount(); i++) {
                    height += expListView.getChildAt(i).getMeasuredHeight();
                    height += expListView.getDividerHeight();
                }
                expListView.getLayoutParams().height = (height+6)*10;
            }
        });

        // Listview Group collapsed listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                expListView.getLayoutParams().height = 87;
            }
        });
    }

    private void initComponents() {

        int imageResource = getResources().getIdentifier(uri, null, getPackageName());

        Drawable res = getResources().getDrawable(imageResource);
        ivProductImage = (ImageView) findViewById(R.id.ivProductImage);
        ivProductImage.setImageDrawable(res);

        txtCategoryName = (EditText) findViewById(R.id.txtCategoryName);
        txtProductName = (EditText) findViewById(R.id.txtProductName);
        txtProductDescription = (EditText) findViewById(R.id.txtProductDescription);
        txtPrice = (EditText) findViewById(R.id.txtPrice);
        txtQuantity = (EditText) findViewById(R.id.txtQuantity);
        txtCal = (EditText) findViewById(R.id.txtCal);
        txtProductIngredients = (EditText) findViewById(R.id.txtProductIngredients);

        expListView = (ExpandableListView) findViewById(R.id.lvExp);
    }

    private void clearComponents() {

        int imageResource = getResources().getIdentifier(uri, null, getPackageName());

        Drawable res = getResources().getDrawable(imageResource);
        ivProductImage.setImageDrawable(res);

        txtCategoryName.setText("");
        txtProductName.setText("");
        txtProductIngredients.setText("");
        txtProductDescription.setText("");
        txtPrice.setText("");
        txtCal.setText("");
        txtQuantity.setText("");

        txtCategoryName.setHint("Category Name");
        txtProductName.setHint("Product Name");
        txtProductDescription.setHint("Description");
        txtPrice.setHint("Price");
        txtCal.setHint("Calories");
        txtProductIngredients.setHint("Ingredients");
        txtQuantity.setHint("Quantity");

    }

    private void prepareCategoryList() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Categories");

        // Adding child data
        setCategoryList();

        listDataChild.put(listDataHeader.get(0), categoryList); // Header, Child data
    }

   private void setCategoryList() {

        ref.orderByChild("name").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {

                MenuCategory menuCategory = dataSnapshot.getValue(MenuCategory.class);

                categoryList.add(menuCategory.getName());

                Log.v("Name_V", dataSnapshot.getKey() + " " + menuCategory.toString());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    public void btnBrowse_Click(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image"), Constants.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgUri = data.getData();

            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                ivProductImage.setImageBitmap(bm);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getImageExt(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @SuppressWarnings("VisibleForTests")
    public void btnUpload_Click(View v) {
        if (imgUri != null) {
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setTitle("Saving Product");
            dialog.show();

            //Get the storage reference
            StorageReference ref = mStorageRef.child(Constants.FB_STORAGE_PRODUCTS_PATH
                    + System.currentTimeMillis() + "." + getImageExt(imgUri));

            //Add file to reference

            ref.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //Dimiss dialog when success
                    dialog.dismiss();
                    //Display success toast msg
                    Toast.makeText(getApplicationContext(), "Product Saved", Toast.LENGTH_SHORT).show();

                    MenuProduct menuCategory = new MenuProduct(txtProductName.getText().toString(),
                            txtCategoryName.getText().toString(),txtProductIngredients.getText().toString(), txtProductDescription.getText().toString(),
                            Float.parseFloat(txtPrice.getText().toString()),0,"",
                                                taskSnapshot.getDownloadUrl().toString(),Integer.parseInt(txtCal.getText().toString()),
                            Integer.parseInt(txtQuantity.getText().toString()));

                    //Save image info in to firebase database
                    String uploadId = mDatabaseRef.push().getKey();
                    mDatabaseRef.child(uploadId).setValue(menuCategory);

                    clearComponents();

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            //Dimiss dialog when error
                            dialog.dismiss();
                            //Display err toast msg
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            //Show upload progress

                            double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            dialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Please select image", Toast.LENGTH_SHORT).show();
        }
    }

    public void btnShowProductList_Click(View v) {
        Intent i = new Intent(AddProductActivity.this, ProductListActivity.class);
        startActivity(i);
    }
}
