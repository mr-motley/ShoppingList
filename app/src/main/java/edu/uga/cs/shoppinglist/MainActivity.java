package edu.uga.cs.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView img = findViewById(R.id.imageView);
        Button proceed = findViewById(R.id.button2);
        proceed.setOnClickListener(new proOnClickListener());

    }

    private class proOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            //create intent, and start results Activity
            Intent intent = new Intent(view.getContext(), LoginPage.class);
            view.getContext().startActivity(intent);
        }
    }
}

