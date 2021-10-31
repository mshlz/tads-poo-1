package com.mshlz.models.blackjack;

public class DealerHand extends PlayerHand {

    public String getPreviewString(Boolean showAll) {
        return showAll ? "Dealer " + this.cards + " (" + this.getHandValue() + " pontos)" : this.getPreviewString();
    }

    public String getPreviewString() {
        return "Dealer [[?]," + this.cards.get(1) + "]";
    }
}
