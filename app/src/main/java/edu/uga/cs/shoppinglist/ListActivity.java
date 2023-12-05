package edu.uga.cs.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ListActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    public FirebaseUser user;

    private static final String DEBUG_TAG = "ListActivity: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Fragment fragment = null;
        if (item.getItemId() == R.id.menu_list) {
            fragment = new ShopListFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragFrame,fragment).commit();
            Log.d(DEBUG_TAG, "Adding Fragment");
            return true;
        } else if (item.getItemId() == R.id.menu_cart) {
            fragment = new CartFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragFrame,fragment).commit();
            Log.d(DEBUG_TAG, "Adding Fragment");
            return true;
        } else if (item.getItemId() == R.id.menu_history) {
            fragment = new PurchaseHistory();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragFrame,fragment).commit();
            return true;
        } else if (item.getItemId() == R.id.menu_settle) {

            return true;
        } else if (item.getItemId() == R.id.menu_logout) {
            mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            Toast.makeText(ListActivity.this, "Logged Out",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            getApplication().startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
