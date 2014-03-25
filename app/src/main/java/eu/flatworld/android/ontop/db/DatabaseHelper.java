package eu.flatworld.android.ontop.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import eu.flatworld.android.ontop.Main;


/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "/sdcard/" + "ontop.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;

    // the DAO object we use to access the SimpleData table
    private Dao<Card, Integer> cardDao = null;
    private Dao<ChecklistItem, Integer> checklistItemDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void deleteDatabase(Context context) {
        Log.d(Main.LOGTAG, "Delete database");
        context.deleteDatabase(DATABASE_NAME);
    }

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.d(Main.LOGTAG, "Create database");
            TableUtils.createTable(connectionSource, Card.class);
            TableUtils.createTable(connectionSource, ChecklistItem.class);
        } catch (SQLException e) {
            Log.e(Main.LOGTAG, "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        Log.d(Main.LOGTAG, "Upgrade database " + oldVersion + "," + newVersion);
        /*try {
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}*/
    }

    public synchronized Dao<Card, Integer> getCardDao() throws SQLException {
        if (cardDao == null) {
            cardDao = getDao(Card.class);
        }
        return cardDao;
    }

    public synchronized Dao<ChecklistItem, Integer> getChecklistItemDao() throws SQLException {
        if (checklistItemDao == null) {
            checklistItemDao = getDao(ChecklistItem.class);
        }
        return checklistItemDao;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        cardDao = null;
        checklistItemDao = null;
    }
}
