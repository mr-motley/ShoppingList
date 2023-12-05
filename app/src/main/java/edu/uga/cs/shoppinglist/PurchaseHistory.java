package edu.uga.cs.shoppinglist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class PurchaseHistory extends Fragment {

    private static final String DEBUG_TAG = "ShopListFragment: ";
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabase;

    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;

    private PurchaseRecyclerAdapter recyclerAdapter;

    private List<Purchase> purchaseHist;

    public PurchaseHistory() {
        // Required empty public constructor
    }

    public static PurchaseHistory newInstance(String param1, String param2) {
        PurchaseHistory fragment = new PurchaseHistory();
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
        return inflater.inflate(R.layout.fragment_purchase_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        mAuth = FirebaseAuth.getInstance();

        purchaseHist = new ArrayList<Purchase>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = firebaseDatabase.getReference("shopItems/purchases");
        recyclerAdapter = new PurchaseRecyclerAdapter(purchaseHist,getContext());
        recyclerView.setAdapter(recyclerAdapter);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                purchaseHist.clear();
                for(DataSnapshot postSnapshot: snapshot.getChildren()){
                    Purchase purchase = postSnapshot.getValue(Purchase.class);
                    purchase.setKey(postSnapshot.getKey());
                    if(!purchase.getItems().isEmpty()){
                        purchaseHist.add(purchase);
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
}