package edu.uga.cs.shoppinglist;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartItemRecyclerAdapter extends RecyclerView.Adapter<CartItemRecyclerAdapter.ItemHolder> {
    public static final String DEBUG_TAG = "ItemRecyclerAdapter";

    private static final int ADD_TO_CART = 3;

    private List<Item> itemList;

    private Context context;

    public CartItemRecyclerAdapter(List<Item> itemList, Context context){
        this.itemList = itemList;
        this.context = context;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        TextView itemName;

        TextView itemQuant;

        ImageButton removeFromCart;


        public ItemHolder(View itemView){
            super(itemView);
            itemName = itemView.findViewById(R.id.textView4);
            itemQuant = itemView.findViewById(R.id.textView6);
            removeFromCart = itemView.findViewById(R.id.AddToCart);

        }
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position){
        Item item = itemList.get(position);
        int pos = position;
        String key = item.getKey();

        Log.d(DEBUG_TAG, "onBindViewHolder: " + item);

        int value = Integer.parseInt(item.getQuantity().replaceAll("[^0-9]", ""));

        holder.itemName.setText( item.getName());
        holder.itemQuant.setText("Qty: " + value);
        holder.removeFromCart.setImageResource(R.drawable.remove_icon);

        holder.removeFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String Name = holder.itemName.getText().toString();
                String Quantity = holder.itemQuant.getText().toString();
                Item item = new Item(Name,Quantity);
                item.setKey(key);



                AppCompatActivity temp =  (AppCompatActivity) context;

                AddCartListener listener = (AddCartListener) temp.getSupportFragmentManager().findFragmentById(R.id.fragFrame);
                Log.d(DEBUG_TAG, "Returning: " + item.getName() + " to list");
                if(listener != null){
                    listener.addItem(item);
                    listener.removeFromCart(pos, item);
                }else {
                    Log.d(DEBUG_TAG,"Listener is Null :(");
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               EditItemDialogFragment editItemDialogFragment = EditItemDialogFragment.newInstance(holder.getAdapterPosition(),item.getKey(),item.getName(),item.getQuantity());
//               editItemDialogFragment.show(((AppCompatActivity)context).getSupportFragmentManager(),null);
            }
        });
    }

    public interface AddCartListener {
        void addItem(Item item);
        void removeFromCart(int postition, Item item);
    }
    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
