import java.io.File;
import java.io.IOException;
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
        this.readXML("board.xml");
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

    private void readXML(String infile) {

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
                    System.out.println("Set name " + setElement.getAttribute("name"));

                    NodeList children = set.getChildNodes();

                    for (int j = 0; j < children.getLength(); ++j) {

                        Node child = children.item(i);

                        if (child.getNodeType() == Node.ELEMENT_NODE) {
                            
                            Element childElement = (Element) child;

                            System.out.println(childElement.getTagName());
                        }

                    }
                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
