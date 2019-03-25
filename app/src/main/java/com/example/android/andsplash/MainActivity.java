package com.example.android.andsplash;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_IMAGE_CAPTURE = 949;
    private DrawerLayout drawerLayout;
    private Uri mCurrentPhotoUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);

        // set the toolbar as action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // show the burger button
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        }

        // responding to navigation from the drawer
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        showCollection(getString(R.string.collection_default));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.drawer_item_nature:
                showCollection(getString(R.string.collection_nature));
                break;
            case R.id.drawer_item_people:
                showCollection(getString(R.string.collection_people));
                break;
            case R.id.drawer_item_animals:
                showCollection(getString(R.string.collection_animals));
                break;
            case R.id.drawer_item_textures:
                showCollection(getString(R.string.collection_textures));
                break;
            default:
                Toast.makeText(this, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                break;
        }

        menuItem.setChecked(true);
        drawerLayout.closeDrawers();

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode != RESULT_OK || data == null) return;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(mCurrentPhotoUri, "image/*");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
                break;
        }
    }

    private void showCollection(String endPoint) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = CollectionFragment.newInstance(endPoint);

        transaction.replace(R.id.collection_fragment, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        transaction.commit();
    }

    public void takePicture(View v) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                File photoFile = createPictureFile();
                // when providing URI as extra in the Intent, no extras are received, that's
                // why the uri is saved in a private field, so we can do something with it on
                // activity result
                mCurrentPhotoUri = FileProvider.getUriForFile(
                        this,
                        getApplicationContext().getPackageName(),
                        photoFile
                );

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentPhotoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } catch (IOException e) {
                Log.e("FILE_ERROR", e.getMessage());
            }
        }
    }

    private File createPictureFile() throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(getString(R.string.uid_file_name), Locale.CANADA);
        String fileName = "JPEG_" + dateFormat.format(new Date()) + "_";

        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        return File.createTempFile(fileName, ".jpg", storageDir);
    }
}
