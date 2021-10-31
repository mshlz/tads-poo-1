package com.mshlz.models;

public class Card {
    private String name;
    private Suit suit;

    public Card(String name, Suit suit) {
        this.name = name;
        this.suit = suit;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Suit getSuit() {
        return this.suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    @Override
    public String toString() {
        return "[" + this.name + " " + this.suit.getSymbol() + "]";
    }
}
