package com.mshlz.app;

import javax.swing.JOptionPane;

import com.mshlz.exceptions.EmptyDeckException;
import com.mshlz.models.Deck;
import com.mshlz.models.FrenchDeck;
import com.mshlz.models.blackjack.DealerHand;
import com.mshlz.models.blackjack.PlayerHand;

/**
 * 
 * @author mshlz
 */
public class App {
    public static void main(String[] args) {
        try {
            int rounds = 0;
            String action;

            Deck deck = new FrenchDeck();
            deck.shuffle();

            while (!deck.isEmpty()) {
                ++rounds;
                action = "";

                DealerHand dealer = new DealerHand();
                PlayerHand player = new PlayerHand();

                for (int i = 0; i < 2; ++i) {
                    player.addCard(deck.drawCard());
                    dealer.addCard(deck.drawCard());
                }

                if (player.getHandValue() == 21) {
                    JOptionPane.showMessageDialog(null, "Blackjack!\n\n" + getRoundInfo(dealer, player, true),
                            "Jogada #" + rounds + " - Blackjack!", JOptionPane.INFORMATION_MESSAGE);
                    continue;
                }

                while (!(player.hasBusted() || action.equals("stand"))) {
                    action = JOptionPane.showInputDialog(null,
                            getRoundInfo(dealer, player) + "\n" + "Qual sua próxima ação? (hit, stand)",
                            "Jogada #" + (rounds), JOptionPane.QUESTION_MESSAGE);

                    switch (action) {
                    case "hit":
                        player.addCard(deck.drawCard());
                        System.out.println("player: Hit!");
                        break;
                    case "stand":
                        System.out.println("player: Stand!");
                        break;
                    // case "double-down":
                    // System.out.println("player: Double Down!");
                    // break;
                    // case "surrender":
                    // System.out.println("player: Surrender!");
                    // break;
                    default:
                        continue;
                    }
                }

                if (player.hasBusted()) {
                    JOptionPane.showMessageDialog(null, "Ops! Você estorou!\n" + getRoundInfo(dealer, player, true),
                            "Você perdeu!", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                while (dealer.hasBusted() || dealer.getHandValue() < 16) {
                    dealer.addCard(deck.drawCard());
                }

                int playerSum = player.getHandValue();
                int dealerSum = dealer.getHandValue();

                if (dealer.hasBusted() || playerSum > dealerSum) {
                    JOptionPane.showMessageDialog(null, "Você ganhou!\n" + getRoundInfo(dealer, player, true),
                            "Você ganhou!", JOptionPane.INFORMATION_MESSAGE);
                } else if (playerSum < dealerSum) {
                    JOptionPane.showMessageDialog(null, "Você perdeu!\n" + getRoundInfo(dealer, player, true),
                            "Você perdeu!", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Empate!\n" + getRoundInfo(dealer, player, true), "Empate!",
                            JOptionPane.INFORMATION_MESSAGE);
                }

            }
        } catch (EmptyDeckException exception) {
            JOptionPane.showMessageDialog(null, "Ops! Acabaram as cartas do baralho!", "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private static String getRoundInfo(DealerHand dealer, PlayerHand player) {
        return getRoundInfo(dealer, player, false);
    }

    private static String getRoundInfo(DealerHand dealer, PlayerHand player, Boolean revealDealerCards) {
        String dealerInfo = revealDealerCards ? dealer.getPreviewString(true) : dealer.getPreviewString();
        return dealerInfo + "\n" + player.getPreviewString() + "\n";
    }
}
