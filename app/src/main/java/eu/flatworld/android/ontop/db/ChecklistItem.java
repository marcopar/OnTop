package eu.flatworld.android.ontop.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.UUID;

@DatabaseTable(tableName = "checklistitem")
public class ChecklistItem implements Serializable {
    private static final long serialVersionUID = 1L;

    @DatabaseField(id = true)
    UUID id;

    @DatabaseField(canBeNull = false)
    String name;

    @DatabaseField(canBeNull = false)
    boolean checked;

    @DatabaseField(canBeNull = false, foreign = true, index = true)
    Card card;

    public ChecklistItem() {
        id = UUID.randomUUID();
        checked = false;
    }

    public String toString() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }


}
