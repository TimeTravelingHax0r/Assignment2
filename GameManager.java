import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



// useful: https://www.youtube.com/watch?v=w3WibDOie1Y

public class GameManager {
    private Board board;
    private LinkedList<Player> players;
    private int numPlayers;
    private int maxDays;
    private int startCredits;
    private int startRank;
    private int day;
    private WinSequence activateWin;
    private Scanner scan;

    public GameManager() {
        this.scan = new Scanner(System.in);
    }

    public void startGame() {
        System.out.println("Welcome to Deadwood!");
        LinkedList<Object> boardData = this.readXMLBoard("board.xml");
        LinkedList<SceneCard> cardData = this.readXMLCards("cards.xml");

        LinkedList<Location> locations = (LinkedList<Location>) boardData.get(0);
        HashMap<String, LinkedList<String>> connections = (HashMap<String, LinkedList<String>>) boardData.get(1);
        Upgrades upgrades = (Upgrades) boardData.get(2);
        Location trailer = new Location("trailer");
        Location castingOffice = new Location("office");

        this.board = new Board(locations, connections, upgrades, trailer, castingOffice);

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
        System.out.println("Please enter the number of players");
        this.numPlayers = Integer.parseInt(scan.nextLine());

        if (2 <= this.numPlayers && this.numPlayers <= 8) {

            switch (this.numPlayers) {
                case 2: this.maxDays = 3;
                        this.startCredits = 0;
                        this.startRank = 1;
                        break;
                case 3: this.maxDays = 3;
                        this.startCredits = 0;
                        this.startRank = 1;
                        break;
                case 4: this.maxDays = 4;
                        this.startCredits = 0;
                        this.startRank = 1;
                        break;
                case 5: this.maxDays = 4;
                        this.startCredits = 2;
                        this.startRank = 1;
                        break;
                case 6: this.maxDays = 4;
                        this.startCredits = 4;
                        this.startRank = 1;
                        break;
                case 7: this.maxDays = 4;
                        this.startCredits = 0;
                        this.startRank = 2;
                case 8: this.maxDays = 4;
                        this.startCredits = 0;
                        this.startRank = 2;
                default: System.out.println("Unrecognized player amount!");

                for (int i = 0; i < this.numPlayers; ++i) {
                    System.out.print("Please enter the name of player " + (i+1) + ":");
                    String currPlayer = this.scan.next();
                    this.players.add(new Player(currPlayer, this.startRank, this.startCredits));
                }
            }

        } else {
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

            NodeList setList = document.getElementsByTagName("set");
            for (int i = 0; i < setList.getLength(); ++i) {
                
                Node set = setList.item(i);

                if (set.getNodeType() == Node.ELEMENT_NODE) {

                    Element setElement = (Element) set;

                    String currName = setElement.getAttribute("name");

                    System.out.println("Set name " + currName);

                    Node neighbors = setElement.getElementsByTagName("neighbors").item(0);
                    Element neighsElement = (Element) neighbors;
                    NodeList neighList = neighsElement.getElementsByTagName("neighbor");
                    LinkedList<String> neighborsList = new LinkedList<>();
                    for (int j = 0; j < neighList.getLength(); ++j) {
                        Node neighbor = neighList.item(j);
                        if (neighbor.getNodeType() == Node.ELEMENT_NODE) {
                            Element neighElement = (Element) neighbor;
                            String currNeighName =  neighElement.getAttribute("name");
                            neighborsList.add(currNeighName);
                        }
                    }

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
                            roles.add(new Role(partName, level, currLine));
                        }
                    }

                    Location currLocation = new Location(currName, roles, highestTake);
                    locations.add(currLocation);
                    connections.put(currName, neighborsList);

                    // FOR DEBUGGING

                    // for (String s : neighborsList) {
                    //     System.out.println(s);
                    // }

                    // System.out.println(highestTake);

                    // for (Role r : roles) {
                    //     System.out.println("Role name: " + r.getRoleName());
                    //     System.out.println("Role num: " + r.getDiceNum());
                    //     System.out.println("Role line: " + r.getCatch());
                    // }
                }
            }

            Node trailer = document.getElementsByTagName("trailer").item(0);
            Node office = document.getElementsByTagName("office").item(0);
            LinkedList<String> trailerNeighbors = new LinkedList<>();
            LinkedList<String> officeNeighbors = new LinkedList<>();

            if (trailer.getNodeType() == Node.ELEMENT_NODE) {

                Element trailerElement = (Element) trailer;

                Node neighbors = trailerElement.getElementsByTagName("neighbors").item(0);
                Element neighsElement = (Element) neighbors;
                NodeList neighList = neighsElement.getElementsByTagName("neighbor");
                for (int j = 0; j < neighList.getLength(); ++j) {
                    Node neighbor = neighList.item(j);
                    if (neighbor.getNodeType() == Node.ELEMENT_NODE) {
                        Element neighElement = (Element) neighbor;
                        String currNeighName =  neighElement.getAttribute("name");
                        trailerNeighbors.add(currNeighName);
                    }
                }
            }

            if (office.getNodeType() == Node.ELEMENT_NODE) {

                Element officeElement = (Element) office;

                Node neighbors = officeElement.getElementsByTagName("neighbors").item(0);
                Element neighsElement = (Element) neighbors;
                NodeList neighList = neighsElement.getElementsByTagName("neighbor");
                for (int j = 0; j < neighList.getLength(); ++j) {
                    Node neighbor = neighList.item(j);
                    if (neighbor.getNodeType() == Node.ELEMENT_NODE) {
                        Element neighElement = (Element) neighbor;
                        String currNeighName =  neighElement.getAttribute("name");
                        officeNeighbors.add(currNeighName);
                    }
                }

                Node upgrades = officeElement.getElementsByTagName("upgrades").item(0);
                Element upgradesElement = (Element) upgrades;
                NodeList upgradeList = upgradesElement.getElementsByTagName("upgrade");
                for (int j = 0; j < upgradeList.getLength(); ++j) {
                    Node upgrade = upgradeList.item(j);
                    if (upgrade.getNodeType() == Node.ELEMENT_NODE) {
                        Element upgradeElement = (Element) upgrade;
                        String currAttrType =  upgradeElement.getAttribute("currency");
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
            //     System.out.println(s);
            // }

            // for (String s : officeNeighbors) {
            //     System.out.println(s);
            // }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

            NodeList cardList = document.getElementsByTagName("card");
            for (int i = 0; i < cardList.getLength(); ++i) {
                
                Node card = cardList.item(i);

                if (card.getNodeType() == Node.ELEMENT_NODE) {

                    Element cardElement = (Element) card;

                    String cardName = cardElement.getAttribute("name");
                    int budget = Integer.parseInt(cardElement.getAttribute("budget"));

                    Node sceneNode = cardElement.getElementsByTagName("scene").item(0);
                    String sceneDesc = sceneNode.getTextContent();
                    int sceneNum = 0;

                    if (sceneNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element sceneElement = (Element) sceneNode;
                        sceneNum = Integer.parseInt(sceneElement.getAttribute("number"));
                    }

                    NodeList partList = cardElement.getElementsByTagName("part");
                    LinkedList<Role> roles = new LinkedList<>();
                    for (int j = 0; j < partList.getLength(); ++j) {
                        Node part = partList.item(j);
                        if (part.getNodeType() == Node.ELEMENT_NODE) {
                            Element partElement = (Element) part;
                            String partName = partElement.getAttribute("name");
                            int level = Integer.parseInt(partElement.getAttribute("level"));
                            String currLine = partElement.getElementsByTagName("line").item(0).getTextContent();
                            roles.add(new Role(partName, level, currLine));
                        }
                    }

                    SceneCard currCard = new SceneCard(cardName, budget, sceneNum, sceneDesc, roles);
                    cards.add(currCard);

                    // FOR DEBUGGING 

                    // System.out.println("name: " + cardName);
                    // System.out.println("budget: " + budget);
                    // System.out.println("scenenum: "+ sceneNum);
                    // System.out.println("desc: " + sceneDesc);

                    // for (Role r : roles) {
                    //     System.out.println("Role name: " + r.getRoleName());
                    //     System.out.println("Role num: " + r.getDiceNum());
                    //     System.out.println("Role line: " + r.getCatch());
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

    private void gameLoop() {

    }

    private boolean processCmd(String cmd) {

        return false;
    }

    private boolean startTurn() {
        return true;
    }

    private boolean finishTurn() {
        return true;
    } 

    private boolean movePlayer(Player player, Location location) {
        return true;
    }

    private boolean work(Player player, String type) {
        return true;
    }

    private void rehearse(Player player) {

    }

    private void upgrade(Player player, int rank) {

    }

    private int calcPayerout(Player player, boolean onCard) {
        return 0;
    }

    private void win() {
        
    }

}
