package edu.uga.cs.shoppinglist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PurchaseRecyclerAdapter extends RecyclerView.Adapter<PurchaseRecyclerAdapter.PurchaseHolder>{
    public static final String DEBUG_TAG = "PurchaseRecyclerAdapter: ";

    private List<Purchase> purchaseList;

    private Context context;

    public PurchaseRecyclerAdapter(List<Purchase> purchaseList, Context context){
        this.purchaseList = purchaseList;
        this.context = context;
    }

    class PurchaseHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView user;
        TextView items;
        TextView price;

        public PurchaseHolder(View purchaseView){
            super(purchaseView);
            date = purchaseView.findViewById(R.id.textView5);
            user = purchaseView.findViewById(R.id.textView13);
            items = purchaseView.findViewById(R.id.textView8);
            price = purchaseView.findViewById(R.id.textView10);
        }
    }

    @NonNull
    @Override
    public PurchaseHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.purchase_card,parent,false);
        return new PurchaseHolder(view);
    }

    @Override
    public void onBindViewHolder(PurchaseHolder holder, int position){
        Purchase purchase = purchaseList.get(position);
        String key = purchase.getKey();
       Log.d(DEBUG_TAG, "onBindViewHolder: " + purchase);

       String foods = "";

       for(int i = 0; i < purchase.getItems().size(); i ++){
           foods += purchase.getItems().get(i).toString() +"\n";
       }

       holder.date.setText("Date: " + purchase.getBuyDate());
       holder.user.setText("User: " + purchase.getPurchaser());
       holder.items.setText("Items: \n" + foods);
       holder.price.setText("Price: $"+ purchase.getPrice());
    }

    @Override
    public int getItemCount(){return purchaseList.size();}
}
