package edu.uga.cs.shoppinglist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class EditItemDialogFragment extends DialogFragment {

    public static final int SAVE = 1;

    public static final int DELETE = 2;

    public static final int ADD_TO_CART = 3;

    private EditText itemNameView;

    private EditText itemQuantView;

    int position;

    String key;

    String name;

    String quantity;


    public interface EditItemDialogListener {
        void updateItem(int position, Item item, int action);
    }

    public static EditItemDialogFragment newInstance(int postion, String key, String name, String quantity) {
        EditItemDialogFragment dialog = new EditItemDialogFragment();
        Bundle args = new Bundle();
        args.putString("key", key);
        args.putString("name", name);
        args.putString("quantity", quantity);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        key = getArguments().getString("key");
        name = getArguments().getString("name");
        quantity = getArguments().getString("quantity");
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.fragment_add_item_dialog,getActivity().findViewById(R.id.root));

        itemNameView = layout.findViewById(R.id.itemName);
        itemQuantView = layout.findViewById(R.id.itemQuant);

        itemNameView.setText(name);
        itemQuantView.setText(quantity);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), androidx.appcompat.R.style.AlertDialog_AppCompat);
        builder.setView(layout);

        builder.setTitle("Edit Item");

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("Save", new SaveButtonClickListener());

        builder.setNeutralButton("DELETE", new DeleteButtonClickListener());

        return builder.create();
    }

    private class SaveButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String Name = itemNameView.getText().toString();
            String Quantity = itemQuantView.getText().toString();
            Item item = new Item(Name,Quantity);
            item.setKey(key);

            EditItemDialogListener listener = (EditItemDialogListener) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragFrame);

            listener.updateItem(position, item, SAVE);

            dismiss();
        }

    }

    private class DeleteButtonClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Item item = new Item(name, quantity);
            item.setKey(key);

            EditItemDialogFragment.EditItemDialogListener listener = (EditItemDialogListener) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragFrame);
            listener.updateItem(position, item, DELETE);
            dismiss();
        }
    }

    private class CartAddButtonClickListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Item item = new Item(name, quantity);
            item.setKey(key);

            EditItemDialogFragment.EditItemDialogListener listener = (EditItemDialogListener) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragFrame);
            listener.updateItem(position, item, ADD_TO_CART);

            dismiss();
        }


    }

}