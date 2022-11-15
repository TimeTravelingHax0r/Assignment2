import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

import javax.script.ScriptEngineManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class GameManager {
    private Board board;
    private LinkedList<Player> players;
    private Player activePlayer;
    private int numPlayers;
    private int activeIndex;
    private int maxDays;
    private int scenesUsed;
    private int startCredits;
    private int startRank;
    private int day;
    private boolean moveUsed;
    private boolean worked;
    private WinSequence activateWin;
    private Scanner scan;

    public GameManager() {
        this.scan = new Scanner(System.in);
        this.players = new LinkedList<>();
    }

    public void startGame() {
        System.out.println("Welcome to Deadwood!");
        // reads in XML data
        LinkedList<Object> boardData = this.readXMLBoard("board.xml");
        LinkedList<SceneCard> cardData = this.readXMLCards("cards.xml");

        LinkedList<Location> locations = (LinkedList<Location>) boardData.get(0);
        HashMap<String, LinkedList<String>> connections = (HashMap<String, LinkedList<String>>) boardData.get(1);
        Upgrades upgrades = (Upgrades) boardData.get(2);
        Location trailer = new Location("trailer");
        Location castingOffice = new Location("office");
        locations.add(trailer);
        locations.add(castingOffice);

        // Initialize board containing locations and location data
        this.board = new Board(locations, connections, cardData, upgrades, trailer, castingOffice);
        board.setCards();

        this.setupGame();

        // Get random player for initial round
        Random rand = new Random();
        this.activeIndex = rand.nextInt(this.players.size());
        this.activePlayer = players.get(this.activeIndex);

        // Setup game state for initial round
        this.moveUsed = false;
        this.worked = false;

        // begin game loop
        this.gameLoop();

        // FOR DEBUGGING

        // LinkedList<Player> playersList = new LinkedList<>();
        // playersList.add(new Player("cool", 1, 3));
        // playersList.add(new Player("dude", 2, 1));
        // playersList.add(new Player("bro", 1, 6));
        // playersList.add(new Player("bro2", 1, 6));
        // WinSequence ws = new WinSequence(playersList);
        // ws.activate();

    }

    private void setupGame() {
        System.out.println("Please enter the number of players: ");
        this.numPlayers = Integer.parseInt(scan.nextLine());

        // setup game settings for number of players
        if (2 <= this.numPlayers && this.numPlayers <= 8) {

            switch (this.numPlayers) {
                case 2:
                    this.maxDays = 3;
                    this.startCredits = 0;
                    this.startRank = 1;
                    break;
                case 3:
                    this.maxDays = 3;
                    this.startCredits = 0;
                    this.startRank = 1;
                    break;
                case 4:
                    this.maxDays = 4;
                    this.startCredits = 0;
                    this.startRank = 1;
                    break;
                case 5:
                    this.maxDays = 4;
                    this.startCredits = 2;
                    this.startRank = 1;
                    break;
                case 6:
                    this.maxDays = 4;
                    this.startCredits = 4;
                    this.startRank = 1;
                    break;
                case 7:
                    this.maxDays = 4;
                    this.startCredits = 0;
                    this.startRank = 2;
                case 8:
                    this.maxDays = 4;
                    this.startCredits = 0;
                    this.startRank = 2;
                default:
                    System.out.println("Unrecognized player amount!");
            }

            // ask user to initialize player names and gender (for printing data)
            for (int i = 0; i < this.numPlayers; ++i) {
                System.out.print("Please enter the name of player " + (i + 1) + ": ");
                String currPlayer = this.scan.next();
                System.out.println("Please enter the gender of [" + currPlayer + "] [M/F/GN]: ");
                String genderChoice = this.scan.next();

                // ask until user input is valid
                while ((!genderChoice.equals("M") && !genderChoice.equals("F")) && !genderChoice.equals("GN")) {
                    System.out.println("Invalid selection, please try again.");
                    System.out.println("Please enter the gender of [" + currPlayer + "] [M/F/GN]: ");
                    genderChoice = this.scan.next();
                }

                String playerGender;
                switch (genderChoice) {
                    case "M":
                        playerGender = "He";
                        break;
                    case "F":
                        playerGender = "She";
                        break;
                    case "GN":
                    default:
                        playerGender = "They";
                        break;
                }

                Player tempPlayer = new Player(currPlayer, this.startRank, this.startCredits, playerGender);
                tempPlayer.movePlayer(board.getTrailer());

                // Add new player to player list
                this.players.add(tempPlayer);
            }

        } else {
            // exit program if player number is invalid
            this.scan.close();
            System.exit(0);
        }
    }

    private LinkedList<Object> readXMLBoard(String infile) {

        LinkedList<Location> locations = new LinkedList<>();
        HashMap<Integer, Integer> dollarCosts = new HashMap<>();
        HashMap<Integer, Integer> creditCosts = new HashMap<>();
        HashMap<String, LinkedList<String>> connections = new HashMap<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {

            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(new File(infile));

            document.getDocumentElement().normalize();

            // for each set
            NodeList setList = document.getElementsByTagName("set");
            for (int i = 0; i < setList.getLength(); ++i) {

                Node set = setList.item(i);

                if (set.getNodeType() == Node.ELEMENT_NODE) {

                    Element setElement = (Element) set;

                    String currName = setElement.getAttribute("name");

                    // get each neighbor of each location and make a hashmap (key (String): current
                    // location, value(LinkedList<String>): list of adjacent locations)
                    Node neighbors = setElement.getElementsByTagName("neighbors").item(0);
                    Element neighsElement = (Element) neighbors;
                    NodeList neighList = neighsElement.getElementsByTagName("neighbor");
                    LinkedList<String> neighborsList = new LinkedList<>();
                    for (int j = 0; j < neighList.getLength(); ++j) {
                        Node neighbor = neighList.item(j);
                        if (neighbor.getNodeType() == Node.ELEMENT_NODE) {
                            Element neighElement = (Element) neighbor;
                            String currNeighName = neighElement.getAttribute("name");
                            neighborsList.add(currNeighName);
                        }
                    }

                    // get number of takes at each location
                    Node takes = setElement.getElementsByTagName("takes").item(0);
                    Element takesElement = (Element) takes;
                    NodeList takeList = takesElement.getElementsByTagName("take");
                    int highestTake = 0;
                    for (int j = 0; j < takeList.getLength(); ++j) {
                        Node take = takeList.item(j);
                        if (take.getNodeType() == Node.ELEMENT_NODE) {
                            Element takeElement = (Element) take;
                            String currTake = takeElement.getAttribute("number");
                            int currTakeInt = Integer.parseInt(currTake);
                            if (currTakeInt > highestTake) {
                                highestTake = currTakeInt;
                            }
                        }
                    }

                    // get all roles in each location
                    Node parts = setElement.getElementsByTagName("parts").item(0);
                    Element partsElement = (Element) parts;
                    NodeList partList = partsElement.getElementsByTagName("part");
                    LinkedList<Role> roles = new LinkedList<>();
                    for (int j = 0; j < partList.getLength(); ++j) {
                        Node part = partList.item(j);
                        if (part.getNodeType() == Node.ELEMENT_NODE) {
                            Element partElement = (Element) part;
                            String partName = partElement.getAttribute("name");
                            int level = Integer.parseInt(partElement.getAttribute("level"));
                            String currLine = partElement.getElementsByTagName("line").item(0).getTextContent();
                            roles.add(new Role(partName, level, currLine, false));
                        }
                    }

                    // generate location
                    Location currLocation = new Location(currName, roles, highestTake);
                    locations.add(currLocation);
                    connections.put(currName, neighborsList);

                    // FOR DEBUGGING

                    // for (String s : neighborsList) {
                    // System.out.println(s);
                    // }

                    // System.out.println(highestTake);

                    // for (Role r : roles) {
                    // System.out.println("Role name: " + r.getRoleName());
                    // System.out.println("Role num: " + r.getDiceNum());
                    // System.out.println("Role line: " + r.getCatch());
                    // }
                }
            }

            Node trailer = document.getElementsByTagName("trailer").item(0);
            Node office = document.getElementsByTagName("office").item(0);
            LinkedList<String> trailerNeighbors = new LinkedList<>();
            LinkedList<String> officeNeighbors = new LinkedList<>();

            if (trailer.getNodeType() == Node.ELEMENT_NODE) {

                Element trailerElement = (Element) trailer;

                // get neighbors of trailer
                Node neighbors = trailerElement.getElementsByTagName("neighbors").item(0);
                Element neighsElement = (Element) neighbors;
                NodeList neighList = neighsElement.getElementsByTagName("neighbor");
                for (int j = 0; j < neighList.getLength(); ++j) {
                    Node neighbor = neighList.item(j);
                    if (neighbor.getNodeType() == Node.ELEMENT_NODE) {
                        Element neighElement = (Element) neighbor;
                        String currNeighName = neighElement.getAttribute("name");
                        trailerNeighbors.add(currNeighName);
                    }
                }
            }

            if (office.getNodeType() == Node.ELEMENT_NODE) {

                Element officeElement = (Element) office;

                // get neighbors of office
                Node neighbors = officeElement.getElementsByTagName("neighbors").item(0);
                Element neighsElement = (Element) neighbors;
                NodeList neighList = neighsElement.getElementsByTagName("neighbor");
                for (int j = 0; j < neighList.getLength(); ++j) {
                    Node neighbor = neighList.item(j);
                    if (neighbor.getNodeType() == Node.ELEMENT_NODE) {
                        Element neighElement = (Element) neighbor;
                        String currNeighName = neighElement.getAttribute("name");
                        officeNeighbors.add(currNeighName);
                    }
                }

                // get rank upgrade information
                Node upgrades = officeElement.getElementsByTagName("upgrades").item(0);
                Element upgradesElement = (Element) upgrades;
                NodeList upgradeList = upgradesElement.getElementsByTagName("upgrade");
                for (int j = 0; j < upgradeList.getLength(); ++j) {
                    Node upgrade = upgradeList.item(j);
                    if (upgrade.getNodeType() == Node.ELEMENT_NODE) {
                        Element upgradeElement = (Element) upgrade;
                        String currAttrType = upgradeElement.getAttribute("currency");
                        int level = Integer.parseInt(upgradeElement.getAttribute("level"));
                        int amt = Integer.parseInt(upgradeElement.getAttribute("amt"));
                        if (currAttrType.equals("dollar")) {
                            dollarCosts.put(level, amt);
                        } else if (currAttrType.equals("credit")) {
                            creditCosts.put(level, amt);
                        }
                    }
                }
            }

            connections.put("trailer", trailerNeighbors);
            connections.put("office", officeNeighbors);

            // FOR DEBUGGING

            // System.out.println("new stuff:");
            // for (String s : trailerNeighbors) {
            // System.out.println(s);
            // }

            // for (String s : officeNeighbors) {
            // System.out.println(s);
            // }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // return locations, adjacent locations, and upgrades information in list
        LinkedList<Object> returnList = new LinkedList<>();
        returnList.add(locations);
        returnList.add(connections);
        returnList.add(new Upgrades(dollarCosts, creditCosts));

        return returnList;
    }

    private LinkedList<SceneCard> readXMLCards(String infile) {

        LinkedList<SceneCard> cards = new LinkedList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {

            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(new File(infile));

            document.getDocumentElement().normalize();

            // for each card in XML
            NodeList cardList = document.getElementsByTagName("card");
            for (int i = 0; i < cardList.getLength(); ++i) {

                Node card = cardList.item(i);

                if (card.getNodeType() == Node.ELEMENT_NODE) {

                    Element cardElement = (Element) card;

                    // get name, budget, and scene
                    String cardName = cardElement.getAttribute("name");
                    int budget = Integer.parseInt(cardElement.getAttribute("budget"));

                    Node sceneNode = cardElement.getElementsByTagName("scene").item(0);
                    String sceneDesc = sceneNode.getTextContent();
                    int sceneNum = 0;

                    if (sceneNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element sceneElement = (Element) sceneNode;
                        sceneNum = Integer.parseInt(sceneElement.getAttribute("number"));
                    }

                    // for each part
                    NodeList partList = cardElement.getElementsByTagName("part");
                    LinkedList<Role> roles = new LinkedList<>();
                    for (int j = 0; j < partList.getLength(); ++j) {
                        Node part = partList.item(j);
                        if (part.getNodeType() == Node.ELEMENT_NODE) {
                            Element partElement = (Element) part;
                            // get name, level, and line
                            String partName = partElement.getAttribute("name");
                            int level = Integer.parseInt(partElement.getAttribute("level"));
                            String currLine = partElement.getElementsByTagName("line").item(0).getTextContent();
                            roles.add(new Role(partName, level, currLine, true));
                        }
                    }

                    // generate new card with details
                    SceneCard currCard = new SceneCard(cardName, budget, sceneNum, sceneDesc, roles);
                    cards.add(currCard);

                    // FOR DEBUGGING

                    // System.out.println("name: " + cardName);
                    // System.out.println("budget: " + budget);
                    // System.out.println("scenenum: "+ sceneNum);
                    // System.out.println("desc: " + sceneDesc);

                    // for (Role r : roles) {
                    // System.out.println("Role name: " + r.getRoleName());
                    // System.out.println("Role num: " + r.getDiceNum());
                    // System.out.println("Role line: " + r.getCatch());
                    // }

                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cards;
    }

    private void processCmd(String cmd) {

        Scanner tokenizer = new Scanner(cmd);
        String mainCmd = tokenizer.next();

        switch (mainCmd) {
            case "active":
                this.activePlayer(cmd);
                break;

            case "move":
                this.move(cmd);
                break;

            case "where":
                this.where(cmd);
                break;

            case "act":
                this.act();
                break;

            case "rehearse":
                this.rehearse();
                break;

            case "work":
                this.work(cmd);
                break;

            case "upgrade":
                this.upgrade();
                break;

            case "end":
                this.end();
                break;

            default:
                System.out.println("error: unrecognized command.");
        }
    }

    // prints out information about the active player, such as location, dollars, credits, and rank
    private void activePlayer(String cmd) {

        Scanner tokenizer = new Scanner(cmd);
        tokenizer.next();
        String cmdPiece = tokenizer.nextLine().substring(1);

        if (!cmdPiece.equals("player?")) {
            System.out.println("error: incorrect command usage.");
        }

        String activePlayerName = this.activePlayer.getName();
        String activePlayerGender = this.activePlayer.getGender();
        int activePlayerRank = this.activePlayer.getRank();
        int activePlayerDollars = this.activePlayer.getDollars();
        int activePlayerCredits = this.activePlayer.getCredits();

        String haveConj = activePlayerGender.equals("They") ? "have" : "has";
        String isConj = activePlayerGender.equals("They") ? "are" : "is";

        System.out.print("The active player is " + activePlayerName + ". ");
        System.out.print(activePlayerGender + " " + haveConj + " $" + activePlayerDollars + ", ");
        System.out.print(activePlayerCredits + " credits, and rank ");
        System.out.print(activePlayerRank + ". ");
        if (activePlayer.workingRole()) {

            Role activePlayerRole = this.activePlayer.currRole();
            String roleName = activePlayerRole.getRoleName();
            String catchPhrase = activePlayerRole.getCatch();

            System.out.print(activePlayerGender + " " + isConj + " playing " + roleName + ", ");
            System.out.println("\"" + catchPhrase + "\"");

        } else {

            String playerLoc = this.activePlayer.getLocation().getName();

            System.out.println(activePlayerGender + " " + isConj + " at " + playerLoc + ".");

        }
    }

    // moves active player to another location if conditions allow it
    private void move(String cmd) {

        if (this.activePlayer.workingRole()) {
            System.out.println("cannot move while working role.");
        } else if (!this.moveUsed && !this.worked) {
            Scanner tokenizer = new Scanner(cmd);
            tokenizer.next();
            String newLocation = tokenizer.nextLine().substring(1);

            if (this.board.moveLegal(this.activePlayer, newLocation)) {
                movePlayer(this.activePlayer, board.getLocation(newLocation));
                this.moveUsed = true;
            } else {
                System.out.println("move not legal.");
            }
        } else if (this.worked) {
            System.out.println("cannot move after working.");
        } else {
            System.out.println("move already used.");
        }
    }

    // where: displays location and card info of active player
    // where all: displays info about locations of all players
    private void where(String cmd) {

        Scanner tokenizer = new Scanner(cmd);
        String cmdPiece = null;

        // check if command is (where) or (where all)
        tokenizer.next();
        if (tokenizer.hasNextLine()) {
            cmdPiece = tokenizer.nextLine().substring(1);
        }

        // if command is just where
        if (cmdPiece == null) {
            this.whereHelper(this.activePlayer, false);
        } else if (cmdPiece.equals("all")) {
            System.out.println("------------ Player Locations -----------");
            for (Player player : this.players) {
                whereHelper(player, true);
            }
            System.out.println("-----------------------------------------");
        }

    }

    // processes information then prints out where command 
    private void whereHelper(Player player, boolean all) {
        Location playerLoc = player.getLocation();

        String placeName = playerLoc.getName();

        // If scene is not wrapped
        if (playerLoc.sceneAvailable()) {

            SceneCard card = playerLoc.getCard();
            String sceneName = card.getName();
            int sceneNum = card.getNum();

            // print player name if displaying all players
            if (all) {
                System.out.print(player.getName() + " is ");
            }
            System.out.println("in " + placeName + " shooting " + sceneName + " scene " + sceneNum);
        } else if (placeName.equals("trailer") || placeName.equals("office")) {
            // print player name if displaying all players
            if (all) {
                System.out.print(player.getName() + " is @ ");
            }
            System.out.println(placeName);
        } else {
            if (all) {
                System.out.print(player.getName() + " is @ ");
            }
            System.out.println(placeName + " wrapped");
        }
    }

    // allows player to begin a role
    private void work(String cmd) {

        Location playerLoc = this.activePlayer.getLocation();

        // get name of role from command
        Scanner tokenizer = new Scanner(cmd);
        tokenizer.next();
        String newRoleName = tokenizer.nextLine().substring(1);

        boolean workLegal = !playerLoc.getName().equals("office") && !playerLoc.getName().equals("trailer");
        workLegal = workLegal && playerLoc.sceneAvailable();

        // if not office or trailer and scene is not wrapped
        if (workLegal) {

            boolean workingRole = this.activePlayer.workingRole();

            // if not already working a role
            if (!workingRole) {
                this.activePlayer.getLocation().takeRole(this.activePlayer, newRoleName);
            } else {
                System.out.println("already working role.");
            }

        } else {
            System.out.println("cannot work role in " + this.activePlayer.getLocation().getName());
        }
    }

    // rolls dice and acts if player is working role
    private void act() {

        Location currLocation = this.activePlayer.getLocation();
        String locName = currLocation.getName();
        boolean workLegal = !locName.equals("trailer") && !locName.equals("office");
        workLegal = workLegal && this.activePlayer.workingRole();

        if (workLegal && !worked) {

            int currBudget = currLocation.getBudget();
            Role currRole = this.activePlayer.currRole();
            int roll = this.activePlayer.rollDie();
            roll += this.activePlayer.getChips();

            if (currRole.onCard()) {

                if (roll >= currBudget) {
                    System.out.println("success! you got $2 credits");
                    this.wrapped(currLocation.takeCounter(this.activePlayer));
                    this.activePlayer.updateCredits(2);
                } else {
                    System.out.println("fail! you get nothing");
                }

                this.worked = true;

            } else {

                if (roll >= currBudget) {
                    System.out.println("success! you got 1$ and 1 credit");
                    this.wrapped(currLocation.takeCounter(this.activePlayer));
                    this.activePlayer.updateDollars(1);
                    this.activePlayer.updateCredits(1);
                } else {
                    this.activePlayer.updateDollars(1);
                    System.out.println("fail! you only get $1");
                }

                this.worked = true;

            }
        } else if (!workLegal && !worked) {
            System.out.println("cannot work here");
        } else if (workLegal && worked) {
            System.out.println("can only work once per turn.");
        } else {
            System.out.println("cannot work here");
        }

    }

    // gives player practice token if working role
    private void rehearse() {

        Location currLocation = this.activePlayer.getLocation();
        String locName = currLocation.getName();
        boolean workLegal = !locName.equals("trailer") && !locName.equals("office");
        workLegal = workLegal && this.activePlayer.workingRole();

        if (workLegal && !worked) {
            if (this.activePlayer.incrementPracitce()) {
                this.worked = true;
            } else {
                this.worked = false;
            }
        } else if (!workLegal && !worked) {
            System.out.println("cannot work here");
        } else if (workLegal && worked) {
            System.out.println("can only work once per turn.");
        } else {
            System.out.println("can only work once per turn.");
        }

    }

    // displays information on upgrade costs and allows user to choose upgrades
    private void upgrade() {

        String currLocName = this.activePlayer.getLocation().getName();
        boolean upgradeAllowed = currLocName.equals("office");

        if (upgradeAllowed) {

            Upgrades upgrades = board.getUpgradeMap();
            HashMap<Integer, Integer> dollarCosts = upgrades.getDollarCosts();
            HashMap<Integer, Integer> creditCosts = upgrades.getCreditCosts();

            String rankText = "Rank";
            String dollarText = "Dollars";
            String creditText = "Credits";
            // print out information on upgrades
            System.out.printf("%8s %8s %8s\n", rankText, dollarText, creditText);
            for (int i = 0; i < 26; ++i) {
                System.out.print("-");
            }

            System.out.println("");

            for (int rank = 2; rank < 7; ++rank) {
                System.out.printf("%8d %8d %8d\n", rank, dollarCosts.get(rank), creditCosts.get(rank));
            }

            for (int i = 0; i < 26; ++i) {
                System.out.print("-");
            }
            System.out.println("");

            // ask whether paying with dollars or credits until correct input
            System.out.print("How would you like to pay? [D/C]: ");
            String payMethod = this.scan.next();
            while (!payMethod.equals("D") && !payMethod.equals("C")) {
                System.out.print("incorrect option, please try again");
                payMethod = this.scan.next();
            }

            // ask which rank until correct input
            System.out.print("Which rank would you like? [2-6]:");
            int rankChoice = this.scan.nextInt();
            while (rankChoice < 2 && rankChoice > 6) {
                System.out.print("incorrect rank, please choose between [2-6]");
                rankChoice = this.scan.nextInt();
            }

            int currRank = this.activePlayer.getRank();

            // if rank is lower or equal drop command
            if (rankChoice <= currRank) {
                System.out.println("command failed: cannot select rank smaller than or equal to current rank");
                return;
            }

            if (payMethod.equals("D")) {
                int cost = dollarCosts.get(rankChoice);
                int dollarFunds = this.activePlayer.getDollars();

                if (cost >= dollarFunds) {
                    this.activePlayer.updateDollars(-cost);
                    this.activePlayer.updateRank(rankChoice);
                } else {
                    System.out.println("not enough dollars! try again later.");
                }

            } else if (payMethod.equals("C")) {
                int cost = creditCosts.get(rankChoice);
                int creditFunds = this.activePlayer.getCredits();

                if (cost >= creditFunds) {
                    this.activePlayer.updateCredits(-cost);
                    this.activePlayer.updateRank(rankChoice);
                    System.out.println("Successfully ranked up: RANK " + rankChoice + "!");
                } else {
                    System.out.println("not enough credits! try again later.");
                }

            } else {
                System.out.println("error: invalid payment method");
                return;
            }

        } else {
            System.out.println("must be in office to upgrade");
        }
    }

    // runs when user ends command
    private void end() {
        this.finishTurn();
    }

    // runs game loop, processes command input, and changes day
    private void gameLoop() {

        Scanner input = new Scanner(System.in);
        String cmd;
        this.day = 0;

        for (int currDay = 0; currDay < this.maxDays; ++currDay) {

            while (true) {
                System.out.print("> ");
                cmd = input.nextLine();
                processCmd(cmd);

                if (this.scenesUsed == 9) {
                    this.scenesUsed = 0;
                    break;
                }
            }

            System.out.println("Day " + (currDay + 1) + " is over! That's a wrap.");
            board.newDay();
            board.setCards();
        }

        this.activateWin.activate();
    }

    // reset variables for next player's turn
    private void finishTurn() {
        int newActiveIndex = this.activeIndex + 1;
        if (newActiveIndex == players.size()) {
            newActiveIndex = 0;
        }

        this.activeIndex = newActiveIndex;
        this.activePlayer = this.players.get(this.activeIndex);
        this.moveUsed = false;
        this.worked = false;
    }

    // moves player object
    private void movePlayer(Player player, Location location) {
        player.movePlayer(location);
    }

    // checks how many scenes are wrapped
    private void wrapped(boolean isWrapped) {
        if (isWrapped) {
            this.scenesUsed++;
        }
    }
}