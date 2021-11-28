package com.mshlz.models.blackjack;

import java.util.Date;

public class DealerHand extends PlayerHand {
    public DealerHand() {
        super(null);
    }

    public DealerHand(Long id, Boolean busted, Boolean blackjack, Integer value, String description, Date date) {
        super(null);
        this.id = id;
        this.busted = busted;
        this.blackjack = blackjack;
        this.value = value;
        this.description = description;
        this.date = date;
    }

    public String getPreviewString(Boolean showAll) {
        return showAll ? "Dealer " + this.cards + " (" + this.getHandValue() + " pontos)" : this.getPreviewString();
    }

    public String getPreviewString() {
        return "Dealer [[?]," + this.cards.get(1) + "]";
    }
}
