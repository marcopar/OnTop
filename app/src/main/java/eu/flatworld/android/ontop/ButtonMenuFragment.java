package eu.flatworld.android.ontop;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * Created by marcopar on 19/11/13.
 */
public class ButtonMenuFragment extends Fragment {
    enum BUTTON {MAP, BROWSER, CHECKLIST, TEXT}

    public interface ButtonMenuFragmentListener {
        public void onButtonMenuClick(BUTTON button);
    }

    ButtonMenuFragmentListener listener = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof ButtonMenuFragmentListener)) {
            throw new IllegalStateException("Activity must implement listener.");
        }

        listener = (ButtonMenuFragmentListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.buttonmenu, container, false);
        Button b = (Button) v.findViewById(R.id.bChecklist);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onButtonMenuClick(BUTTON.CHECKLIST);
            }
        });
        b = (Button) v.findViewById(R.id.bBrowser);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onButtonMenuClick(BUTTON.BROWSER);
            }
        });
        b = (Button) v.findViewById(R.id.bMap);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onButtonMenuClick(BUTTON.MAP);
            }
        });
        b = (Button) v.findViewById(R.id.bText);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onButtonMenuClick(BUTTON.TEXT);
            }
        });
        return v;
    }
}
