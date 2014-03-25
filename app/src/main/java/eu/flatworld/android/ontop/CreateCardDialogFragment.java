package eu.flatworld.android.ontop;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by marcopar on 12/11/13.
 */
public class CreateCardDialogFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.createcarddialog, container, false);
        getDialog().setTitle(R.string.enter_name_);
        final Button b = (Button) v.findViewById(R.id.bOk);
        final EditText et = (EditText) v.findViewById(R.id.etName);
        et.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String value = et.getText().toString();
                if (value.trim().length() == 0) {
                    value = getString(R.string.no_name);
                }
                DialogInteractionInterface listener = (DialogInteractionInterface) getTargetFragment();
                if (listener != null) {
                    Bundle b = new Bundle();
                    b.putString("name", value);
                    listener.onDismiss(CreateCardDialogFragment.this, b);
                }
                dismiss();
                return;
            }
        });
        final Button b2 = (Button) v.findViewById(R.id.bCancel);
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DialogInteractionInterface listener = (DialogInteractionInterface) getTargetFragment();
                listener.onDismiss(CreateCardDialogFragment.this, null);
                dismiss();
                return;
            }
        });
        return v;
    }
}

