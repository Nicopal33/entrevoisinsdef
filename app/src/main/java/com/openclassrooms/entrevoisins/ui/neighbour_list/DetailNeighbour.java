package com.openclassrooms.entrevoisins.ui.neighbour_list;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;

import butterknife.BindView;

import static java.lang.System.load;

public class DetailNeighbour extends AppCompatActivity {

    public TextView mText;
    public TextView mTextPostalAddress;
    public TextView mTextPhone;
    public TextView mFacebook;
    public TextView mTextView2;
    public ImageView mImageAvatar;
    public ImageButton mReturn_button;
    private Neighbour neighbour;
    private NeighbourApiService mApiService;
    public FloatingActionButton mNeighbourFavoriteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_neighbour);

        mText = findViewById(R.id.prenom);
        mTextPostalAddress = findViewById(R.id.adresse);
        mTextPhone = findViewById(R.id.telephone);
        mFacebook = findViewById(R.id.adressemail);
        mTextView2 = findViewById(R.id.apropos);
        mImageAvatar = findViewById(R.id.imageView);
        mReturn_button = findViewById(R.id.return_button);
        mNeighbourFavoriteButton = findViewById(R.id.floatingActionButton);
        mApiService = DI.getNeighbourApiService();


        Intent intent = getIntent();
        neighbour = (Neighbour)intent.getSerializableExtra("neighbour");

        mText.setText(neighbour.getName());
        mTextPostalAddress.setText(neighbour.getAddress());
        mTextPhone.setText(neighbour.getPhoneNumber());
        mTextView2.setText(neighbour.getAboutMe());
        mFacebook.setText("www.Facebook.fr/"+neighbour.getName());

        Glide.with (this).load(neighbour.getAvatarUrl()).into(mImageAvatar);

        mReturn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailNeighbour.this.finish();
            }
        });
        if (!neighbour.getFavorite()) {
            mNeighbourFavoriteButton.setImageResource(R.drawable.ic_star_border_white_24dp);
        }else { mNeighbourFavoriteButton.setImageResource(R.drawable.ic_star_white_24dp);

        }
        mNeighbourFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mApiService.changeNeighbourFavoriteStatus(neighbour);
               if (neighbour.getFavorite()) {
                   mNeighbourFavoriteButton.setImageResource(R.drawable.ic_star_border_white_24dp);
                   neighbour.setFavorite(false);
               }else { mNeighbourFavoriteButton.setImageResource(R.drawable.ic_star_white_24dp);
               neighbour.setFavorite(true);
               }


        }});


    }



}