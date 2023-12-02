package edu.uga.cs.shoppinglist;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.ArrayList;

public class CartFragment extends Fragment
        implements CartItemRecyclerAdapter.AddCartListener{
    private static final String DEBUG_TAG = "CartFragment: ";

    private FirebaseDatabase firebaseDatabase;

    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;

    private String userId;

    private RecyclerView recyclerView;

    private CartItemRecyclerAdapter recyclerAdapter;

    private List<Item> itemList;

    public CartFragment() {
        //required empty public constructor
    }

    public static CartFragment newInstance() {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_cart,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        mAuth = FirebaseAuth.getInstance();

        userId = mAuth.getCurrentUser().getUid();

        itemList = new ArrayList<Item>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = firebaseDatabase.getReference("shopItems/users/"+ userId +"/cart");
        itemList = new ArrayList<Item>();
        recyclerAdapter = new CartItemRecyclerAdapter(itemList,getContext());
        recyclerView.setAdapter(recyclerAdapter);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemList.clear();
                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    Item item = postSnapshot.getValue(Item.class);
                    item.setKey(postSnapshot.getKey());
                    if(item.getName() != null && item.getQuantity() != null){
                        itemList.add(item);
                        Log.d(DEBUG_TAG, "ValueEventListener: added: " + item);
                        Log.d(DEBUG_TAG, "ValueEventListener: key: " + postSnapshot.getKey());
                    }
                }
                Log.d(DEBUG_TAG, "ValueEventListener: notifying recycler");
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("ValueEventListner: reading failed: " + error.getMessage());
            }
        });
    }
    public void addItem(Item item) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("shopItems");

        myRef.push().setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(DEBUG_TAG, "Item returned to list: " + item);
                Toast.makeText(getContext(), "Item returned to list" + item.getName(),
                        Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText( getContext(), "Failed to create a item for " + item.getName(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void removeFromCart(int position, Item item) {
        Log.d(DEBUG_TAG, "Deleting item at: " + position + "(" + item.getName() + ")");

        itemList.remove(position);

        recyclerAdapter.notifyItemRemoved(position);

        DatabaseReference ref = firebaseDatabase.getReference().child("shopItems/users/" + userId + "/cart").child(item.getKey());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d( DEBUG_TAG, "deleted item at: " + position + "(" + item.getName() + ")" );
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(DEBUG_TAG,"failed to delete item at: " + position + "(" + item.getName() + ")");
                Toast.makeText(getContext(), "Failed to delete " + item.getName(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
