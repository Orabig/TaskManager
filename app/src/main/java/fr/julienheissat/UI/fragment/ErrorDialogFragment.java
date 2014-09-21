package fr.julienheissat.ui.fragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;

/**
 * Created by juju on 17/09/2014.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ErrorDialogFragment extends DialogFragment
{
    // Global field to contain the error dialog
    private Dialog mDialog;
    // Default constructor. Sets the dialog field to null
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ErrorDialogFragment() {
        super();
        mDialog = null;
    }
    // Set the dialog to display
    public void setDialog(Dialog dialog) {
        mDialog = dialog;
    }
    // Return a Dialog to the DialogFragment.
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return mDialog;
    }
}