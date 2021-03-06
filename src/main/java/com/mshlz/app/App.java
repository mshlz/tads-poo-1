package com.mshlz.app;

import javax.swing.JOptionPane;

import com.mshlz.dao.DBHelpers;
import com.mshlz.dao.UserDAO;
import com.mshlz.models.User;

/**
 * 
 * @author mshlz
 */
public class App {
    static User user;

    public static void main(String[] args) {
        // to reset database, uncomment next line, run, then comment again
        // DBHelpers.dropDB();
        DBHelpers.migrateDB();

        user = setupUser();

        Game game = new Game(user);
        game.start();
    }

    private static User setupUser() {
        String name, nickname;
        User user = null;
        UserDAO userDao = new UserDAO();

        do {
            nickname = JOptionPane.showInputDialog(null, "Qual seu nickname?", Game.TITLE,
                    JOptionPane.QUESTION_MESSAGE);
            if (nickname == null || nickname.trim().length() == 0)
                continue;
            nickname = nickname.trim().replaceAll(" ", "_");

            user = userDao.findOneByNickname(nickname);

            if (user == null) {
                JOptionPane.showMessageDialog(null, "Usuário não encontrado!\n\nVamos criar um novo usuário.",
                        Game.TITLE,
                        JOptionPane.INFORMATION_MESSAGE);

                do {
                    name = JOptionPane.showInputDialog(null, "Qual seu nome?", Game.TITLE,
                            JOptionPane.QUESTION_MESSAGE);
                } while (name == null || name.trim().length() == 0);

                // try create in DB
                user = new User(name, nickname);
                if (userDao.save(user)) {
                    JOptionPane.showMessageDialog(null,
                            "Usuário criado com sucesso!\n\nNome: " + user.getName() + "\nNickname: "
                                    + user.getNickname(),
                            Game.TITLE, JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Ocorreu um erro ao criar seu usuário. Tente novamente.",
                            Game.TITLE, JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }

            } else {
                JOptionPane.showMessageDialog(null, "Usuário encontrado!\n\nNome: " + user.getName(), Game.TITLE,
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } while (user == null);

        // update
        String[] actions = { "Iniciar jogo", "Atualizar usuário" };
        int response = -1;
        do {
            response = JOptionPane.showOptionDialog(null,
                    "O que deseja fazer " + user.getName() + "?",
                    Game.TITLE, JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                    actions, null);

            switch (response) {
                case JOptionPane.YES_OPTION:
                    return user;
                case JOptionPane.NO_OPTION:
                    // edit user
                    String newName = JOptionPane.showInputDialog(null,
                            user.getName() + ", para qual nome você deseja alterar?", Game.TITLE,
                            JOptionPane.QUESTION_MESSAGE);
                    user.setName(newName);
                    userDao.update(user);
                    break;
                default:
                    response = -1;
            }
        } while (response != -1);

        return user;
    }
}
