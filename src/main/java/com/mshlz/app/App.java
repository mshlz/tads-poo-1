package com.mshlz.app;

import javax.swing.JOptionPane;

import com.mshlz.exceptions.EmptyDeckException;
import com.mshlz.models.Deck;
import com.mshlz.models.FrenchDeck;
import com.mshlz.models.User;
import com.mshlz.models.blackjack.DealerHand;
import com.mshlz.models.blackjack.PlayerHand;

/**
 * 
 * @author mshlz
 */
public class App {
    static final String TITLE = "Simple Blackjack";
    static User user;

    public static void main(String[] args) {
        user = setupUser();
        mainMenu();
    }

    private static User setupUser() {
        String name, nickname;
        User user = null;

        do {
            nickname = JOptionPane.showInputDialog(null, "Qual seu nickname?", TITLE, JOptionPane.QUESTION_MESSAGE);
            if (nickname == null || nickname.trim().length() == 0)
                continue;
            nickname = nickname.trim().replaceAll(" ", "_");

            // find in DB...
            user = null;

            if (user == null) {
                JOptionPane.showMessageDialog(null, "Usuário não encontrado!\n\nVamos criar um novo usuário.", TITLE,
                        JOptionPane.INFORMATION_MESSAGE);

                do {
                    name = JOptionPane.showInputDialog(null, "Qual seu nome?", TITLE, JOptionPane.QUESTION_MESSAGE);
                } while (name == null || name.trim().length() == 0);

                // try create in DB
                user = new User(name, nickname);

                JOptionPane.showMessageDialog(null,
                        "Usuário criado com sucesso!\n\nNome: " + user.getName() + "\nNickname: " + user.getNickname(),
                        TITLE, JOptionPane.INFORMATION_MESSAGE);

            } else {
                JOptionPane.showMessageDialog(null, "Usuário encontrado!\n\nNome: " + user.getName(), TITLE,
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } while (user == null);

        return user;
    }

    private static void mainMenu() {
        String[] availableOptions = { "Nova partida", "Ver partidas passadas", "Sair" };

        while (true) {
            int result = JOptionPane.showOptionDialog(null, "O que você deseja fazer?", TITLE,
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, availableOptions, null);

            switch (result) {

            case JOptionPane.YES_OPTION:
                initGame();
                break;

            case JOptionPane.NO_OPTION:
                JOptionPane.showMessageDialog(null, "Suas partidas: xxx", TITLE, JOptionPane.INFORMATION_MESSAGE);
                break;

            case JOptionPane.CANCEL_OPTION:
            default:
                JOptionPane.showMessageDialog(null, "Até mais.", TITLE, JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        }
    }

    private static void initGame() {
        String[] actions = { "Hit (+1)", "Stand" };

        try {
            int rounds = 0;
            int action;

            Deck deck = new FrenchDeck();
            deck.shuffle();

            while (!deck.isEmpty()) {
                ++rounds;
                action = -1;

                DealerHand dealer = new DealerHand();
                PlayerHand player = new PlayerHand();

                for (int i = 0; i < 2; ++i) {
                    player.addCard(deck.drawCard());
                    dealer.addCard(deck.drawCard());
                }

                if (player.getHandValue() == 21) {
                    JOptionPane.showMessageDialog(null, "Você fez Blackjack!\n\n" + getRoundInfo(dealer, player, true),
                            "Jogada #" + rounds + " - Blackjack!", JOptionPane.INFORMATION_MESSAGE);
                    continue;
                }

                do {
                    action = JOptionPane.showOptionDialog(null,
                            getRoundInfo(dealer, player, false) + "\n" + "Qual sua próxima ação?",
                            "Jogada #" + (rounds), JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, actions,
                            null);

                    switch (action) {
                    case JOptionPane.YES_OPTION:
                        player.addCard(deck.drawCard());
                        System.out.println("player: Hit!");
                        break;
                    case JOptionPane.NO_OPTION:
                        System.out.println("player: Stand!");
                        break;
                    default:
                        action = Integer.MIN_VALUE;
                        continue;
                    }
                } while (!player.isBusted() && action == JOptionPane.YES_OPTION);

                if (player.isBusted()) {
                    JOptionPane.showMessageDialog(null, "Ops! Você estorou!\n" + getRoundInfo(dealer, player, true),
                            "Você perdeu!", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                if (action == Integer.MIN_VALUE) {
                    JOptionPane.showMessageDialog(null, "Você abandonou a partida!", TITLE,
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                while (dealer.getHandValue() < 16) {
                    dealer.addCard(deck.drawCard());
                }

                int playerSum = player.getHandValue();
                int dealerSum = dealer.getHandValue();

                if (dealer.isBusted() || playerSum > dealerSum) {
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

    private static String getRoundInfo(DealerHand dealer, PlayerHand player, Boolean revealDealerCards) {
        return dealer.getPreviewString(revealDealerCards) + "\n" + player.getPreviewString() + "\n";
    }
}
