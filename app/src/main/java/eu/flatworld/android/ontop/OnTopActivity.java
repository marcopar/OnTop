package eu.flatworld.android.ontop;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import eu.flatworld.android.ontop.db.Card;
import eu.flatworld.android.ontop.db.CardType;

public class OnTopActivity extends ActionBarActivity
        implements CardListFragment.CardListFragmentListener, ButtonMenuFragment.ButtonMenuFragmentListener {
    static int MENU_ADD_ID = 1;
    static int MENU_SWITCH_ID = 2;
    static int MENU_SETTINGS_ID = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(Main.LOGTAG, "create main activity");
        super.onCreate(savedInstanceState);

        ActionBar bar = getSupportActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        bar.setDisplayShowTitleEnabled(true);
        bar.setDisplayShowHomeEnabled(true);

        if (MainService.RUNNING) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        } else {
            getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        }

        if (getSupportFragmentManager().findFragmentById(android.R.id.content) == null) {
            getSupportFragmentManager().beginTransaction().add(android.R.id.content, new CardListFragment()).commit();
        }

        handleIntent(getIntent());
        handleClipboard();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    void handleClipboard() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard.hasText()) {
            String text = (String) clipboard.getText();
            handleInputText(text);
            clipboard.setText(null);
        }
    }

    void handleIntent(Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            String type = intent.getType();
            if (Intent.ACTION_SEND.equals(action) && type != null) {
                if ("text/plain".equals(type)) {
                    String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                    if (sharedText != null) {
                        handleInputText(sharedText);
                    }
                    intent.removeExtra(Intent.EXTRA_TEXT);
                }
            }
        }
    }

    void handleInputText(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MainService.RUNNING) {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        } else {
            getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        int icon = MainService.RUNNING ? R.drawable.ic_action_switchon
                : R.drawable.ic_action_switchoff;
        menu.findItem(MENU_SWITCH_ID).setIcon(icon);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem mi = menu.add(Menu.NONE, MENU_ADD_ID, Menu.FIRST + 1, R.string.add)
                .setIcon(android.R.drawable.ic_menu_add);
        MenuItemCompat.setShowAsAction(mi, MenuItem.SHOW_AS_ACTION_IF_ROOM);
        mi = menu.add(Menu.NONE, MENU_SETTINGS_ID, Menu.FIRST + 2, R.string.settings)
                .setIcon(android.R.drawable.ic_menu_preferences);
        MenuItemCompat.setShowAsAction(mi, MenuItem.SHOW_AS_ACTION_IF_ROOM);
        int icon = MainService.RUNNING ? R.drawable.ic_action_switchon
                : R.drawable.ic_action_switchoff;
        mi = menu.add(Menu.NONE, MENU_SWITCH_ID, Menu.FIRST, "Switch").setIcon(icon);
        MenuItemCompat.setShowAsAction(mi, MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // This uses the imported MenuItem from ActionBarSherlock
        if (item.getItemId() == MENU_SWITCH_ID) {
            if (!MainService.RUNNING) {
                Log.d(Main.LOGTAG, getString(R.string.ontop_activated));
                Intent i = new Intent();
                i.setClassName("eu.flatworld.android.ontop",
                        "eu.flatworld.android.ontop.MainService");
                startService(i);
                Toast.makeText(this, R.string.ontop_activated, Toast.LENGTH_SHORT)
                        .show();
                item.setIcon(R.drawable.ic_action_switchon);
                getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
            } else {
                Log.d(Main.LOGTAG, getString(R.string.ontop_deactivated));
                Intent i = new Intent();
                i.setClassName("eu.flatworld.android.ontop",
                        "eu.flatworld.android.ontop.MainService");
                stopService(i);
                Toast.makeText(this, R.string.ontop_deactivated, Toast.LENGTH_SHORT)
                        .show();
                item.setIcon(R.drawable.ic_action_switchoff);

                KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
                if (!km.inKeyguardRestrictedInputMode()) {
                    getWindow().clearFlags(
                            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
                }
            }
            return true;
        }
        //Toast.makeText(this, "Got click: " + item.toString(),Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onCardSelected(Card c) {
        if (c.getCardType() == CardType.CHECKLIST) {
            Bundle b = new Bundle();
            b.putSerializable("card", c);
            ChecklistFragment f = new ChecklistFragment();
            f.setArguments(b);
            getSupportFragmentManager().beginTransaction().replace(android.R.id.content, f).addToBackStack(null).commit();
        }
    }

    @Override
    public void onButtonMenuClick(ButtonMenuFragment.BUTTON button) {
        switch (button) {
            case CHECKLIST:
                CardListFragment mlf = new CardListFragment();
                getSupportFragmentManager().beginTransaction().replace(android.R.id.content, mlf).addToBackStack(null).commit();
                break;
            case BROWSER:
                WebViewFragment wvf = new WebViewFragment();
                getSupportFragmentManager().beginTransaction().replace(android.R.id.content, wvf).addToBackStack(null).commit();

        }
    }
}