package eu.flatworld.android.ontop.db;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;


public class Util {


    public static Card createCard(DatabaseHelper dh, String name, CardType ct) throws SQLException {

        Card c = new Card();
        c.setName(name);
        c.setCardType(ct);
        dh.getCardDao().create(c);
        return c;
    }

    public static void deleteCard(DatabaseHelper dh, Card c) throws SQLException {
        dh.getCardDao().delete(c);
    }

    public static List<Card> loadCardList(DatabaseHelper dh) throws SQLException {
        Dao<Card, Integer> md = dh.getCardDao();
        List<Card> lc = md.queryForAll();
        return lc;
    }

    public static ChecklistItem createChecklistItem(DatabaseHelper dh, Card c, String name) throws SQLException {
        if (c.getCardType() != CardType.CHECKLIST) {
            throw new IllegalArgumentException("Wrong card type");
        }
        ChecklistItem ci = new ChecklistItem();
        ci.setName(name);
        ci.setCard(c);
        ci.setChecked(false);
        dh.getChecklistItemDao().create(ci);
        return ci;
    }

    public static void deleteChecklistItem(DatabaseHelper dh, ChecklistItem ci) throws SQLException {
        dh.getChecklistItemDao().delete(ci);
    }

    public static void deleteChecklistItems(DatabaseHelper dh, Card m) throws SQLException {
        List<ChecklistItem> l = loadChecklistItemList(dh, m);
        for (ChecklistItem ci : l) {
            Util.deleteChecklistItem(dh, ci);
        }
    }

    public static List<ChecklistItem> loadChecklistItemList(DatabaseHelper dh,
                                                            Card m) throws SQLException {
        Dao<ChecklistItem, Integer> cid = dh.getChecklistItemDao();
        List<ChecklistItem> lm = cid.queryBuilder().where().eq("card_id", m.getId()).query();
        return lm;
    }

    public static ChecklistItem update(DatabaseHelper dh, ChecklistItem ci) throws SQLException {
        dh.getChecklistItemDao().update(ci);
        return ci;
    }
}
