package db;

import commands.Command;
import commands.dbcommands.*;
import commands.dbcommands.usercommands.LoginCommand;
import commands.dbcommands.usercommands.LogoutCommand;
import commands.dbcommands.usercommands.RegisterCommand;
import exceptions.NoLoginException;
import exceptions.WrongCommandException;
import organization.OrganizationCollection;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class DBUser {
    private static boolean isLogged = false;

    public static void setLogged(boolean logged) {
        isLogged = logged;
    }

    public static boolean isLogged() {
        return isLogged;
    }

    public static void toStart() {
        OrganizationCollection organizationCollection = new OrganizationCollection(DBParser.getOrganizationsFromDB());
        DBReceiver dbReceiver = new DBReceiver(organizationCollection);
        DBInvoker dbInvoker = new DBInvoker(
                new AddCommand(dbReceiver),
                new ShowCollectionCommand(dbReceiver),
                new RemoveByIdCommand(dbReceiver),
                new HelpCommand(dbReceiver),
                new ClearCollectionCommand(dbReceiver),
                new FilterGreaterThanAnnualTurnoverCommand(dbReceiver),
                new UpdateCommand(dbReceiver),
                new ExitCommand(dbReceiver),
                new InsertAnIndexCommand(dbReceiver),
                new SortCommand(dbReceiver),
                new InfoCommand(dbReceiver),
                new RegisterCommand(dbReceiver),
                new PrintAscendingCommand(dbReceiver),
                new RemoveAnyByTypeCommand(dbReceiver),
                new RemoveLowerCommand(dbReceiver),
                new ExecuteScriptCommand(dbReceiver),
                new LoginCommand(dbReceiver),
                new LogoutCommand(dbReceiver)
        );
        dbReceiver.setCommands(dbInvoker.getCommandMap());
        while (true) {
            try {
                //авторизация
                while (!isLogged){
                    System.out.println("Выберите действие: ");
                    System.out.println("1. Регистрация - register");
                    System.out.println("2. Вход - login");
                    System.out.println("3. Выход из программы - exit");
                    Scanner scanner = new Scanner(System.in);
                    String request = scanner.nextLine();
                    String[] tokens = request.split(" ");
                    dbReceiver.setTokens(tokens);
                    Command command = dbInvoker.commandMap.get(tokens[0]);
                    try {
                        if(!(tokens[0].equals("register") || tokens[0].equals("login") || tokens[0].equals("exit"))){
                            throw new WrongCommandException();
                        }
                        command.execute();
                        if(!isLogged){
                            throw new NoLoginException();
                        }
                    } catch (WrongCommandException e){
                    }
                    catch (NoLoginException e) {
                        System.out.println("Вы не авторизованы, введите свой логин и пароль с помощью команды login");
                        System.out.println("Или зарегистрируйтесь с помощью команды register");
                    }
                    catch (Exception e) {
                        System.out.println("Неизвестная команда...");
                    }
                }
                System.out.println("Введите команду: ");
                Scanner scanner = new Scanner(System.in);
                String request = scanner.nextLine();
                String[] tokens = request.split(" ");
                dbReceiver.setTokens(tokens);
                Command command = dbInvoker.commandMap.get(tokens[0]);
                try {
                    command.execute();
                } catch (Exception e) {
                    System.out.println("Неизвестная команда...");
                }
            }
            catch (NoSuchElementException e){
                System.out.println("ЗАЧЕМ ТЫ НАЖАЛ CTRL+D");
                System.exit(1);
            }
        }
    }
}
