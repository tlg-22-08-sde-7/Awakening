package com.awakening.app;

import com.apps.util.Prompter;
import com.awakening.app.game.Item;
import com.awakening.app.game.Player;
import com.awakening.app.game.Room;
import com.awakening.app.game.RoomMap;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//Class that will control gameplay
public class Game {

    public static RoomMap world;
    public static List<Item.ItemsSetup> roomItems;
    public static Player player = new Player();
    private static final Prompter prompter = new Prompter(new Scanner(System.in));
    private UI ui = new UI();
    private TextParser textParser = new TextParser();
    private List<Room> rooms = new ArrayList<>();

    public Game() {
    }

    public void initGame() {
        boolean gameOver = false;
        boolean gameStart = false;
        String confirmation;

        ui.splashScreen();


        while (!gameStart) {
            String playGame = prompter.prompt("Do you want to play Awakening? [Y/N]").toLowerCase().trim();


            switch (playGame) {
                case ("y"):
                case ("yes"):
                    System.out.println();
                    ui.displayGamePlayOptions();
                    gameStart = true;
                    break;
                case ("n"):
                case ("no"):
                    confirmation = prompter.prompt("Are you sure? [Y/N]").toLowerCase().trim();
                    if (!"y".equals(confirmation)) {
                        break;
                    }
                    gameOver = true;
                    gameStart = true;
                    break;
                default:
                    System.out.println("\033[31mInvalid input, please provide [Y] for Yes, [N] for No.\033[0m");
                    System.out.println();
            }
            //This is to add a line, with the intention of spacing out the text fields of U/I and game text
            System.out.println();
        }

        generateWorld();

        while (!gameOver) {
            ui.displayGameInfo(player);
//            ui.displayGamePlayOptions();
            String response = prompter.prompt("What do you want to do?\n");
            List<String> move = textParser.parseInput(response);
            while ("\033[31minvalid\033[0m".equals(move.get(0))) {
                response = prompter.prompt("What do you want to do?\n");
                move = textParser.parseInput(response);
            }

            if ("quit".equals(move.get(0))) {
                confirmation = prompter.prompt("Are you sure? [Y/N]").toLowerCase().trim();
                switch (confirmation) {
                    case ("y"):
                    case ("yes"):
                        gameOver = true;
                        break;

                    case ("n"):
                    case ("no"):
                        break;
                }
            }
            else if ("help".equals(move.get(0))) {
                ui.displayGamePlayOptions();
            }
            else {
                executeCommand(move);
            }
        }
    }

    private void executeCommand(List<String> move) {
        // execute command based on verb
        String verb = move.get(0);
        String noun = move.get(1);
        switch (verb) {
            case "go":
                move(noun);
                break;
            case "quit":
                System.out.println("Thanks for playing!");
                break;
            case "get":
                pickUp(noun);
                break;
            default:
                System.out.println("\033[31mInvalid command\033[0m");
        }
    }

    private void move(String direction) {
        RoomMap.RoomLayout currentRoom = player.getCurrentRoom();
        RoomMap.RoomLayout nextRoom = world.getRoom(currentRoom.getDirections().get(direction));
        if (nextRoom == null) {
            System.out.println("You can't go that way");
        } else {
            player.setCurrentRoom(nextRoom);
        }
    }


    private void pickUp(String noun) {
        RoomMap.RoomLayout currentRoom = player.getCurrentRoom();
        List itemList = player.getCurrentRoom().getItems();
        System.out.println(itemList);
        int index;
        Item item;

        for (int i = 0; i < itemList.size() ; i++) {
            if(noun.equals(itemList.get(i))){
                index = i;
                //Remove item form room
                player.getCurrentRoom().getItems().remove(index);
            }
        }

        for (int i = 0; i < roomItems.size(); i++) {
            if (roomItems.contains(noun)){
                int j = roomItems.indexOf(noun);
                player.addToInventory(roomItems.get(j));
            }

        }



//        Item searchItem = null;
//        player.addToInventory(roomItems.get(index));

    }

    private void generateWorld() {
        try (Reader reader = new FileReader("resources/JSON/roomsListNew.json")) {
            world = new Gson().fromJson(reader, RoomMap.class);
            player.setCurrentRoom(world.getBasement());
        } catch (IOException e) {
            e.printStackTrace();
        }
        generateItems();
    }

    private void generateItems(){
        Item item;
        try (Reader reader = new FileReader("resources/JSON/Items.json")) {
            item = new Gson().fromJson(reader, Item.class);
            roomItems = item.loadItems();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void startGame() {
    }
}

