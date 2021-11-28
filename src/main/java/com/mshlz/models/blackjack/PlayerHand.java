package com.mshlz.models.blackjack;

import java.util.ArrayList;
import java.util.List;

import com.mshlz.interfaces.IPlayableHand;
import com.mshlz.models.Card;
import com.mshlz.models.User;

public class PlayerHand extends BaseHand implements IPlayableHand {
    public PlayerHand(User user) {
        super(user);
    }

    protected List<Card> cards = new ArrayList<>();

    public void addCard(Card card) {
        this.cards.add(card);
    }

    public List<Card> getCards() {
        return this.cards;
    }

    public Integer getHandValue() {
        int sum = 0;
        int aces = 0;
        for (Card card : this.cards) {
            switch (card.getName()) {
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "8":
            case "9":
            case "10":
                sum += Integer.parseInt(card.getName());
                break;
            case "J":
            case "Q":
            case "K":
                sum += 10;
                break;
            case "A":
                sum += 11;
                aces++;
            }
        }

        while (sum > 21 && aces > 0) {
            --aces;
            sum -= 10;
        }

        return sum;
    }

    public Boolean isBusted() {
        return this.getHandValue() > 21;
    }

    public String getPreviewString() {
        return "Sua mão: " + this.toString() + " (" + this.getHandValue() + " pontos)";
    }

    @Override
    public String toString() {
        return this.cards.toString();
    }
}
