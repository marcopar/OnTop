package eu.flatworld.android.ontop;

import android.app.Activity;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.List;

import eu.flatworld.android.ontop.db.Card;
import eu.flatworld.android.ontop.db.CardType;
import eu.flatworld.android.ontop.db.DatabaseHelper;
import eu.flatworld.android.ontop.db.Util;

public class CardListFragment extends ListFragment implements DialogInteractionInterface {
    private DatabaseHelper dbh = null;

    public interface CardListFragmentListener {
        public void onCardSelected(Card m);
    }

    CardListFragmentListener listener = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof CardListFragmentListener)) {
            throw new IllegalStateException("Activity must implement listener.");
        }

        listener = (CardListFragmentListener) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(Main.LOGTAG, "create cardlistfragment");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ActionBar bar = ((OnTopActivity) getActivity()).getSupportActionBar();
        bar.setSubtitle(R.string.available_cards);
        registerForContextMenu(this.getListView());
        setHasOptionsMenu(true);
        loadCardList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (dbh != null) {
            OpenHelperManager.releaseHelper();
            dbh = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    Card createCard(String name, CardType ct) {
        try {
            return Util.createCard(getDbHelper(), name, ct);
        } catch (java.sql.SQLException ex) {
            String s = getString(R.string.error_creating_the_list);
            Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
            Log.e(Main.LOGTAG, s, ex);
        }
        return null;
    }

    void deleteCard(Card m) {
        try {
            Util.deleteChecklistItems(getDbHelper(), m);
            Util.deleteCard(getDbHelper(), m);
        } catch (java.sql.SQLException ex) {
            String s = getString(R.string.error_creating_the_list);
            Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
            Log.e(Main.LOGTAG, s, ex);
        }
    }

    void loadCardList() {
        try {
            List<Card> lm = Util.loadCardList(getDbHelper());
            ArrayAdapter<Card> adapter = new ArrayAdapter<Card>(getActivity(),
                    android.R.layout.simple_list_item_1, lm);
            setListAdapter(adapter);
        } catch (java.sql.SQLException ex) {
            String s = getString(R.string.error_loading_the_cards);
            Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
            Log.e(Main.LOGTAG, s, ex);
        }
    }

    private DatabaseHelper getDbHelper() {
        if (dbh == null) {
            dbh = OpenHelperManager.getHelper(getActivity(),
                    DatabaseHelper.class);
        }
        return dbh;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // This uses the imported MenuItem from ActionBarSherlock
        if (item.getItemId() == OnTopActivity.MENU_ADD_ID) {
            CreateCardDialogFragment df = new CreateCardDialogFragment();
            df.setTargetFragment(this, 0);
            df.show(getFragmentManager(), "CreateCardDialogFragment");
            return true;
        }

        // Toast.makeText(this, "Got click: " +
        // item.toString(),Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Card m = (Card) getListAdapter().getItem(position);
        if (listener != null) {
            listener.onCardSelected(m);
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
        Card m = (Card) adapter.getItem(position);
        menu.setHeaderTitle(m.getName());
        menu.add(R.string.delete);
    }

    @Override
    public boolean onContextItemSelected(android.view.MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        Card m = (Card) getListAdapter().getItem(info.position);
        deleteCard(m);
        loadCardList();
        return true;
    }

    @Override
    public void onDismiss(Object source, Bundle b) {
        if (source instanceof CreateCardDialogFragment) {
            if (b != null && b.containsKey("name")) {
                String name = b.getString("name");
                Card m = createCard(name, CardType.CHECKLIST);
                loadCardList();
            }
        }
    }
}