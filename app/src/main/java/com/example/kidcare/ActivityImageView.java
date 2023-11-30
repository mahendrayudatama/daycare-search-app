package com.example.kidcare;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ortiz.touchview.TouchImageView;
import com.squareup.picasso.Picasso;

public class ActivityImageView extends AppCompatActivity {
    TouchImageView ivActivity;
    ImageView ivBackButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
//        Toast.makeText(this, getIntent().getStringExtra("url"), Toast.LENGTH_SHORT).show();
        ivBackButton = findViewById(R.id.iv_backButtonImageView);
        ivActivity = (TouchImageView) findViewById(R.id.iv_activityTouchImageview);
        Picasso.get().load(getIntent().getStringExtra("url")).into(ivActivity);
        ivBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
