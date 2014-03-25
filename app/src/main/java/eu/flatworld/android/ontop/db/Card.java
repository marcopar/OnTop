package eu.flatworld.android.ontop.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.UUID;

@DatabaseTable(tableName = "card")
public class Card implements Serializable {

    private static final long serialVersionUID = 1L;

    @DatabaseField(id = true)
    UUID id;

    @DatabaseField(canBeNull = false)
    CardType cardType;

    @DatabaseField(canBeNull = false)
    String name;

    public Card() {
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

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
