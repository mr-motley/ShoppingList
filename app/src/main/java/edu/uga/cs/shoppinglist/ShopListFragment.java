package edu.uga.cs.shoppinglist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ShopListFragment
        extends Fragment
        implements AddItemDialogFragment.AddItemDialogListener,
                   EditItemDialogFragment.EditItemDialogListener,
                   ItemRecyclerAdapter.AddCartListener {

    private static final String DEBUG_TAG = "ShopListFragment: ";
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;

    private String userId;

    private RecyclerView recyclerView;

    private ItemRecyclerAdapter recyclerAdapter;

    private List<Item> itemList;

    public ShopListFragment() {
        // Required empty public constructor
    }


    public static ShopListFragment newInstance() {
        ShopListFragment fragment = new ShopListFragment();
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
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shop_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        mAuth = FirebaseAuth.getInstance();

        userId = mAuth.getCurrentUser().getUid();



        FloatingActionButton floatingButton = view.findViewById(R.id.floatingActionButton2);

        floatingButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new AddItemDialogFragment();
                newFragment.show( getParentFragmentManager(), null);
            }
        });



        itemList = new ArrayList<Item>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = firebaseDatabase.getReference("shopItems");
        itemList = new ArrayList<Item>();
        recyclerAdapter = new ItemRecyclerAdapter(itemList, getContext());
        recyclerView.setAdapter(recyclerAdapter);


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemList.clear();
                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    Item item = postSnapshot.getValue(Item.class);
                    item.setKey(postSnapshot.getKey());
                    if(item.getName() != null && item.getQuantity() != null) {
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
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.smoothScrollToPosition(itemList.size() -1);

                    }
                });
                Log.d(DEBUG_TAG, "Item saved: " + item);
                Toast.makeText(getContext(), "List Item created for " + item.getName(),
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

    public void updateItem(int position, Item item, int action) {
        if(action == EditItemDialogFragment.SAVE){
            Log.d(DEBUG_TAG, "Updating Item at: " + position + "(" + item.getName() + ")" );

            recyclerAdapter.notifyItemChanged(position);

            DatabaseReference ref = firebaseDatabase
                    .getReference()
                    .child("shopItems")
                    .child(item.getKey());

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    snapshot.getRef().setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d( DEBUG_TAG, "updated item at: " + position + "(" + item.getName() + ")" );
                            Toast.makeText(getContext(), "Item updated for " + item.getName(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d( DEBUG_TAG, "failed to update item at: " + position + "(" + item.getName() + ")" );
                    Toast.makeText(getContext(), "Failed to update " + item.getName(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        } else if (action == EditItemDialogFragment.DELETE){
            Log.d(DEBUG_TAG, "Deleting item at: " + position + "(" + item.getName() + ")");

            itemList.remove(position);

            recyclerAdapter.notifyItemRemoved(position);

            DatabaseReference ref = firebaseDatabase.getReference().child("shopItems").child(item.getKey());

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    snapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d( DEBUG_TAG, "deleted item at: " + position + "(" + item.getName() + ")" );
                            Toast.makeText(getContext(), "Item deleted for " + item.getName(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(DEBUG_TAG,"failed to delete item at: " + position + "(" + item.getName() + ")");
                    Toast.makeText(getContext(), "Failed to delete " + item.getName(),Toast.LENGTH_SHORT).show();
                }
            });
        } else if (action == EditItemDialogFragment.ADD_TO_CART){
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            DatabaseReference myRef = database.getReference("shopItems/users/" + userId + "/cart");
            myRef.push().setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d(DEBUG_TAG, "Item added to cart: " + item);
                    Toast.makeText(getContext(),  item.getName() + " added to cart",
                            Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText( getContext(), "Failed to add " + item.getName() + " to cart",
                            Toast.LENGTH_SHORT).show();
                }
            });
            updateItem(position,item,EditItemDialogFragment.DELETE);

        }
    }


}