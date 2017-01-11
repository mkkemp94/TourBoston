package com.example.android.tourboston;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class AttractionDetailsActivity extends AppCompatActivity {

    // Have current food attraction
    public static final String EXTRA_NAME = "activity_name";
    public static final String EXTRA_DESCRIPTION = "activity_description";
    public static final String EXTRA_IMAGE = "activity_image";

    String attractionName;
    String attractionDescription;
    int attractionImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Get attraction info from the caller of this activity
        Intent intent = getIntent();
        attractionName = intent.getStringExtra(EXTRA_NAME);
        attractionDescription = intent.getStringExtra(EXTRA_DESCRIPTION);
        attractionImage = intent.getIntExtra(EXTRA_IMAGE, -1);

        // Set toolbar as app bar and allow up to home
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_details);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set title to attraction title
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(attractionName);

        // Set description to attraction description
        TextView textView = (TextView) findViewById(R.id.tv_item_description);
        textView.setText(attractionDescription);

        // Set background
        ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        //imageView.setImageResource(attractionImage);
        Glide.with(this).load(attractionImage).centerCrop().into(imageView);

        // Set up floating action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareAttraction();
            }
        });
    }

    // Share attraction via any social media
    private void shareAttraction() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Check out TourBoston!");
        intent.putExtra(Intent.EXTRA_TEXT, "Here's what's in Boston: \n" +
                attractionName + "\n" +
                attractionDescription);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
