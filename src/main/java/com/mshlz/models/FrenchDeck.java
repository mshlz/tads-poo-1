package com.mshlz.models;

public class FrenchDeck extends Deck {

    public FrenchDeck() {
        super();

        /** Init Suits */
        Suit spades = new Suit("Spades", "♠");
        Suit hearts = new Suit("Hearts", "♥");
        Suit diamonds = new Suit("Diamonds", "♦");
        Suit clubs = new Suit("Clubs", "♣");

        this.suits.add(spades);
        this.suits.add(hearts);
        this.suits.add(diamonds);
        this.suits.add(clubs);

        /** Init Cards */
        String[] baseCards = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };
        for (Suit suit : this.suits) {
            for (String cardName : baseCards) {
                this.cards.add(new Card(cardName, suit));
            }
        }
    }

}
