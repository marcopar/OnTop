package eu.flatworld.android.ontop.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.UUID;

@DatabaseTable(tableName = "textitem")
public class TextItem implements Serializable {
    private static final long serialVersionUID = 1L;

    @DatabaseField(id = true)
    UUID id;

    @DatabaseField(canBeNull = false)
    String name;

    @DatabaseField(canBeNull = true, width = 4096)
    String text;

    @DatabaseField(canBeNull = false, foreign = true, index = true)
    Card card;

    public TextItem() {
        id = UUID.randomUUID();
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

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
