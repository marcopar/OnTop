package eu.flatworld.android.ontop;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import eu.flatworld.android.ontop.db.Card;

/**
 * Created by marcopar on 12/11/13.
 */
public class ImportFragment extends Fragment {
    public interface ImportFragmentListener {
        public void onImportCompleted(Card m);
    }

    ImportFragmentListener listener = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof ImportFragmentListener)) {
            throw new IllegalStateException("Activity must implement listener.");
        }

        listener = (ImportFragmentListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(Main.LOGTAG, "create importfragment");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.importfragment, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ActionBar bar = ((OnTopActivity) getActivity()).getSupportActionBar();
        bar.setSubtitle(R.string.import_text);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}