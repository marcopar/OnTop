package eu.flatworld.android.ontop;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.List;

import eu.flatworld.android.ontop.db.Card;
import eu.flatworld.android.ontop.db.ChecklistItem;
import eu.flatworld.android.ontop.db.DatabaseHelper;
import eu.flatworld.android.ontop.db.Util;

public class ChecklistFragment extends ListFragment implements DialogInteractionInterface {
    private DatabaseHelper dbh = null;

    Card currentCard = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(Main.LOGTAG, "create checklistfragment");
        super.onCreate(savedInstanceState);

        if (getArguments().getSerializable("card") != null) {
            currentCard = (Card) getArguments().getSerializable("card");
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerForContextMenu(this.getListView());
        setHasOptionsMenu(true);
        ActionBar bar = ((OnTopActivity) getActivity()).getSupportActionBar();
        if (currentCard != null) {
            Toast.makeText(getActivity(),
                    "Got card: " + currentCard,
                    Toast.LENGTH_SHORT).show();
            bar.setSubtitle(currentCard.getName());
        }
        loadChecklistItemList(currentCard);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("card", currentCard);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (dbh != null) {
            OpenHelperManager.releaseHelper();
            dbh = null;
        }
    }

    ChecklistItem createChecklistItem(Card card, String name) {
        try {
            return Util.createChecklistItem(getDbHelper(), card, name);
        } catch (java.sql.SQLException ex) {
            String s = getString(R.string.error_creating_the_item);
            Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
            Log.e(Main.LOGTAG, s, ex);
        }
        return null;
    }

    void deleteChecklistItem(ChecklistItem ci) {
        try {
            Util.deleteChecklistItem(getDbHelper(), ci);
        } catch (java.sql.SQLException ex) {
            String s = getString(R.string.error_deleting_the_item);
            Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
            Log.e(Main.LOGTAG, s, ex);
        }
    }

    void loadChecklistItemList(Card card) {
        try {
            List<ChecklistItem> lm = Util.loadChecklistItemList(getDbHelper(), card);
            ChecklistItemAdapter adapter = new ChecklistItemAdapter(getActivity(), lm);
            setListAdapter(adapter);
        } catch (java.sql.SQLException ex) {
            String s = getString(R.string.error_loading_the_items);
            Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
            Log.e(Main.LOGTAG, s, ex);
        }
    }

    private DatabaseHelper getDbHelper() {
        if (dbh == null) {
            dbh = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);
        }
        return dbh;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // This uses the imported MenuItem from ActionBarSherlock
        if (item.getItemId() == OnTopActivity.MENU_ADD_ID) {
            CreateChecklistItemDialogFragment df = new CreateChecklistItemDialogFragment();
            df.setTargetFragment(this, 0);
            df.show(getFragmentManager(), "CreateChecklistItemDialogFragment");
            return true;
        }

        // Toast.makeText(this, "Got click: " +
        // item.toString(),Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ChecklistItemAdapter cia = (ChecklistItemAdapter) getListAdapter();
        ChecklistItem ci = cia.getItem(position);
        ci.setChecked(!ci.isChecked());
        cia.notifyDataSetChanged();
        try {
            Util.update(getDbHelper(), ci);
        } catch (java.sql.SQLException ex) {
            String s = getString(R.string.error_updating_item);
            Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
            Log.e(Main.LOGTAG, s, ex);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.clear();
        // menu.setHeaderIcon(R.drawable.icon);
        // menu.setHeaderTitle(R.string.options);

        AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
        int position = info.position;
        Adapter adapter = ((ListView) v).getAdapter();
        ChecklistItem ci = (ChecklistItem) adapter.getItem(position);
        menu.setHeaderTitle(ci.getName());
        menu.add(R.string.delete);
    }

    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) {
        // Recupera l'oggetto associato alla lista
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                .getMenuInfo();
        ChecklistItem ci = (ChecklistItem) getListAdapter().getItem(info.position);
        deleteChecklistItem(ci);
        loadChecklistItemList(currentCard);
        return true;
    }

    @Override
    public void onDismiss(Object source, Bundle b) {
        if (source instanceof CreateChecklistItemDialogFragment) {
            if (b != null && b.containsKey("name")) {
                String name = b.getString("name");
                ChecklistItem ci = createChecklistItem(currentCard, name);
                loadChecklistItemList(currentCard);
            }
        }
    }
}