package com.mshlz.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mshlz.exceptions.EmptyDeckException;

public abstract class Deck {
    protected List<Suit> suits = new ArrayList<>();
    protected List<Card> cards = new ArrayList<>();

    public void shuffle() {
        Collections.shuffle(this.cards);
    }

    public Boolean isEmpty() {
        return this.cards.isEmpty();
    }

    public Integer availableCards() {
        return this.cards.size();
    }

    public Card drawCard() throws EmptyDeckException {
        if (this.isEmpty())
            throw new EmptyDeckException();

        return this.cards.remove(this.cards.size() - 1);
    }

    public List<Suit> getSuits() {
        return this.suits;
    }

    public List<Card> getCards() {
        return this.cards;
    }

    @Override
    public String toString() {
        return "Deck " + this.cards;
    }
}
