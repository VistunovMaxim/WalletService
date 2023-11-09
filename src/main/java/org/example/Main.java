package org.example;

import org.example.configuration.Config;
import org.example.controller.PlayerController;
import org.example.controller.impl.PlayerControllerImpl;
import org.example.entity.EntryActivity;
import org.example.entity.Player;
import org.example.entity.Transaction;
import org.example.util.dictionary.TransactionType;
import org.example.util.exception.AccessDeniedException;
import org.example.util.exception.UserNotFoundException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Player player = null;
        boolean isStop = true;
        int variable;
        double sum;
        String login;
        String password;
        String identify;

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        PlayerController playerController = applicationContext.getBean(PlayerControllerImpl.class);
        applicationContext.start();

        while (isStop) {
            if (player == null) {
                System.out.println("""
                        1) Регистрация нового игрока
                        2) Авторизация
                        3) Выход
                        """);
                variable = scanner.nextInt();
                switch (variable) {
                    case 1 -> {
                        System.out.println("Введите логин и пароль:");
                        login = scanner.next();
                        password = scanner.next();
                        try {
                            player = playerController.registration(login, password);
                        } catch (AccessDeniedException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    case 2 -> {
                        System.out.println("Введите логин и пароль:");
                        login = scanner.next();
                        password = scanner.next();
                        try {
                            player = playerController.authorization(login, password);
                        } catch (UserNotFoundException | AccessDeniedException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    case 3 -> isStop = false;
                }
            } else {
                System.out.println("""
                        1) Проверить баланс
                        2) Снять со счета
                        3) Пополнить счет
                        4) История операций
                        5) История действий
                        6) Выйти из аккаунта
                        """);
                variable = scanner.nextInt();
                switch (variable) {
                    case 1 -> System.out.println(playerController.getBalance(player));
                    case 2 -> {
                        System.out.println("Введите уникальный идентификатор операции и сумму операции:");
                        identify = scanner.next();
                        sum = scanner.nextDouble();
                        try {
                            playerController.doTransaction(player, TransactionType.DEBIT, identify, sum);
                        } catch (AccessDeniedException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    case 3 -> {
                        System.out.println("Введите уникальный идентификатор операции и сумму операции:");
                        identify = scanner.next();
                        sum = scanner.nextDouble();
                        try {
                            playerController.doTransaction(player, TransactionType.CREDIT, identify, sum);
                        } catch (AccessDeniedException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    case 4 -> {
                        List<Transaction> list = playerController.getTransactions(player);
                        for (Object operation : list) {
                            System.out.println(operation);
                        }
                    }
                    case 5 -> {
                        List<EntryActivity> list = playerController.getActions(player);
                        for (Object action : list) {
                            System.out.println(action);
                        }
                    }
                    case 6 -> player = (Player) playerController.exit(player);
                }

            }
        }
    }
}
