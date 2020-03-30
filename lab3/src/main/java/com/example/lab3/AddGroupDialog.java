package com.example.lab3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class AddGroupDialog extends AlertDialog {
    private final GroupCache groupsCache = GroupCache.getInstance();
    Builder builder;

    protected AddGroupDialog(Context context) {
        super(context);
        builder = new AddGroupDialog.Builder(this.getContext());
        EditText groupName = new EditText(this.getContext());
        builder.setTitle(R.string.lab3_dialog_title)
                .setView(groupName)
                .setPositiveButton(R.string.lab3_DialogOk, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Group group = new Group(
                                groupName.getText().toString()
                        );
                        groupsCache.addGroup(group);

                    }
                })
                .create();
    }

    @Override
    public void show() {
        builder.show();
    }

}
