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


public class AddItemDialogFragment extends DialogFragment {

    EditText itemName;
    EditText itemQuant;
    public interface AddItemDialogListener {
        void addItem(Item item);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        // Create the AlertDialog view
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.fragment_add_item_dialog,
                getActivity().findViewById(R.id.root));

        //get view objects
        itemName = layout.findViewById(R.id.itemName);
        itemQuant = layout.findViewById(R.id.itemQuant);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);
        builder.setView(layout);

        builder.setTitle("New Item");
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton(android.R.string.ok, new AddItemListener());

        return builder.create();

        }

        private class AddItemListener implements DialogInterface.OnClickListener {
            @Override
            public void onClick(DialogInterface dialog, int whichButton){
                String iName = itemName.getText().toString();
                String iQuant = itemQuant.getText().toString();

                Item item = new Item(iName,iQuant);

                AddItemDialogListener listener = (AddItemDialogListener) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragFrame);

                listener.addItem(item);

                dismiss();
        }

    }
}