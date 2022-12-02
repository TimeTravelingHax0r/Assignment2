/*

   Deadwood GUI helper file
   Author: Moushumi Sharmin
   This file shows how to create a simple GUI using Java Swing and Awt Library
   Classes Used: JFrame, JLabel, JButton, JLayeredPane

*/

import java.awt.*;
import javax.swing.*;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import java.awt.event.*;
import java.text.CollationElementIterator;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import javax.swing.JOptionPane;

public class BoardLayersListener extends JFrame {

   boardMouseListener bml;
   private GameController gc;
   private Board board;

   private static String[] playerColors = {"blue", "cyan", "green", "orange", "pink", "red", "violet", "white", "yellow"};

   private LinkedList<JLabel> cards;
   private LinkedList<JLabel> cardBacks;
   private LinkedList<JLabel> currTakes;

   // JLabels
   JLabel boardlabel;
   JLabel cardlabel;
   JLabel playerlabel;
   JLabel mLabel;
   JLabel warningLabel;

   // score board
   JTable infoTable;
   
   //JButtons
   JButton bWork;
   JButton bEnd;
   JButton bUpgrade;
   JButton bMove;

   // Player selection button
   JButton confButton;

   // Role Buttons
   JButton yesButton;
   JButton noButton;

   // Work Buttons
   JButton actButton;
   JButton rehearseButton;
   
   // JLayered Pane
   JLayeredPane bPane;

   // JTextField

   JTextField textField;

   LinkedList<JButton> buttons;
   LinkedList<JButton> locButtons;
   LinkedList<JButton> roleButtons;
   LinkedList<JButton> numButtons;
   LinkedList<JLabel> infoLabels;
   LinkedList<JLabel> wrapInfoMsgs;
   LinkedList<Component> upgradeComps;

   int iconWidth;
   
   // Constructor
   
   public BoardLayersListener(GameController gc, Board board) {
         
   // Set the title of the JFrame
   super("Deadwood");
   // Set the exit option for the JFrame
   setDefaultCloseOperation(EXIT_ON_CLOSE);
   
   // Create the JLayeredPane to hold the display, cards, dice and buttons
   bPane = getLayeredPane();
   this.buttons = new LinkedList<>();
   this.locButtons = new LinkedList<>();
   this.roleButtons = new LinkedList<>();
   this.numButtons = new LinkedList<>();
   this.infoLabels = new LinkedList<>();
   this.cards = new LinkedList<>();
   this.cardBacks = new LinkedList<>();
   this.currTakes = new LinkedList<>();
   this.wrapInfoMsgs = new LinkedList<>();
   this.upgradeComps = new LinkedList<>();
   this.gc = gc;
   this.board = board;
   this.bml = new boardMouseListener(this, this.playerColors, gc);

   // Create the deadwood board
   boardlabel = new JLabel();
   ImageIcon icon =  new ImageIcon("images/board.jpg");
   boardlabel.setIcon(icon); 
   boardlabel.setBounds(0,0,icon.getIconWidth(),icon.getIconHeight());
   this.iconWidth = icon.getIconWidth();
   
   // Add the board to the lowest layer
   bPane.add(boardlabel, 0);
   
   // Set the size of the GUI
   setSize(icon.getIconWidth()+300,icon.getIconHeight());
   
   // Create the Menu for action buttons
   mLabel = new JLabel("MENU");
   mLabel.setBounds(icon.getIconWidth()+40,0,180,20);
   bPane.add(mLabel, 2);

   this.warningLabel = new JLabel();
   this.warningLabel.setBounds(icon.getIconWidth()+40,120,300,20);
   this.warningLabel.setVisible(false);
   bPane.add(this.warningLabel, 2);

   // Create Action buttons
   bWork = new JButton("WORK");
   bWork.setBackground(Color.white);
   bWork.setBounds(icon.getIconWidth()+10, 30,140, 20);
   bWork.addMouseListener(this.bml);

   bEnd = new JButton("END TURN");
   bEnd.setBackground(Color.white);
   bEnd.setBounds(icon.getIconWidth()+10, 30,140, 20);
   bEnd.addMouseListener(this.bml);
   bEnd.setVisible(false);
   
   bUpgrade = new JButton("UPGRADE");
   bUpgrade.setBackground(Color.white);
   bUpgrade.setBounds(icon.getIconWidth()+10,60,140, 20);
   bUpgrade.addMouseListener(this.bml);
   
   bMove = new JButton("MOVE");
   bMove.setBackground(Color.white);
   bMove.setBounds(icon.getIconWidth()+10,90,140, 20);
   bMove.addMouseListener(this.bml);

   // Place the action buttons in the top layer
   this.bPane.add(bWork, 2);
   this.buttons.add(bWork);
   this.bPane.add(bEnd, 2);
   this.buttons.add(bEnd);
   this.bPane.add(bUpgrade, 2);
   this.buttons.add(bUpgrade);
   this.bPane.add(bMove, 2);
   this.buttons.add(bMove);

   this.generatePlayerNumOpts();
   this.generatePlayerNamesOpts();
   this.generateOnRoleOpts();
   this.generateOffRoleOpts();

   this.bml.setLabel(this.mLabel);
   this.bml.setTextField(this.textField);
}

public String getCurrCmd() {
   return this.bml.getText();
}

public JLabel getTextBox() {
   return this.mLabel;
}

public LinkedList<Player> getPlayers() {
   return this.bml.getPlayers();
}

public String getColor(int whichColor) {
   return this.playerColors[whichColor-1];
}

public void setNumRankCredits(int numPlayers, int rank, int credits) {
   this.bml.updatePlayerNum(numPlayers);
   this.bml.updateRank(rank);
   this.bml.updateCredit(credits);
}

public void setLabelFront(JLabel label) {
   this.bPane.moveToFront(label);
}

public void updateTextColor(int playerNum) {
   this.textField.setText(this.playerColors[playerNum-1]);
}

public void initPlayerDice() {
   LinkedList<Player> players = this.getPlayers();
   int xOffSet = 0, yOffSet = 0;
   for (Player player : players) {
      String currImgName = "images/dice/" + player.getImgName() + player.getRank() + ".png";
      JLabel diceLabel = new JLabel();
      ImageIcon diceIcon =  new ImageIcon(currImgName);
      diceLabel.setIcon(diceIcon);

      Location currLoc = player.getLocation();
      int x = currLoc.getX(), y = currLoc.getY(), h = currLoc.getH(), w = currLoc.getW();
      diceLabel.setBounds(x+xOffSet,y+yOffSet,diceIcon.getIconWidth(),diceIcon.getIconHeight());

      diceLabel.setVisible(true);
      this.bPane.add(diceLabel);
      this.bPane.moveToFront(diceLabel);
      player.setLabel(diceLabel);

      xOffSet += 50;
      if (xOffSet > 194) {
         xOffSet = 0;
         yOffSet += 50;
      }
   }
}

public void updateLocationState(LinkedList<Location> locations) {

   for (JLabel label : this.cards) {
      this.bPane.remove(label);
   }

   for (JLabel label : this.cardBacks) {
      this.bPane.remove(label);
   }

   for (JLabel label : this.currTakes) {
      this.bPane.remove(label);
   }

   this.cards.clear();
   this.cardBacks.clear();
   this.currTakes.clear();

   for (Location location : locations) {
      JLabel cardLabel = new JLabel();
      ImageIcon cIcon =  new ImageIcon("images/cards/" + location.getCard().getImg());
      cardLabel.setIcon(cIcon); 
      cardLabel.setBounds(location.getX(),location.getY(),cIcon.getIconWidth()+2,cIcon.getIconHeight());

      JLabel cardLabelBack = new JLabel();
      ImageIcon cIconBack =  new ImageIcon("images/CardBack-small.jpg");
      cardLabelBack.setIcon(cIconBack); 
      cardLabelBack.setBounds(location.getX(),location.getY(),cIconBack.getIconWidth()+2,cIconBack.getIconHeight());
      
      this.cards.add(cardLabel);
      this.cardBacks.add(cardLabelBack);

      if (!location.isDiscovered()) {
         cardLabel.setVisible(false);
         cardLabelBack.setVisible(true);
      } else if (location.sceneAvailable()) {
         cardLabel.setVisible(true);
         cardLabelBack.setVisible(false);
         LinkedList<Take> takes = location.getCurrTakes();
         for (Take take : takes) {
            JLabel takeLabel = new JLabel(); 
            ImageIcon shotIcon = new ImageIcon("images/shot.png");
            takeLabel.setIcon(shotIcon);
            takeLabel.setBounds(take.getX(),take.getY(),take.getH(),take.getW());
            takeLabel.setVisible(true);
            this.currTakes.add(takeLabel);
            this.bPane.add(takeLabel);
         }
      } else {
         cardLabel.setVisible(false);
         cardLabelBack.setVisible(false);
      }

      this.bPane.add(cardLabel);
      this.bPane.add(cardLabelBack);
      this.bPane.moveToFront(cardLabel);
      this.bPane.moveToFront(cardLabelBack);

   }

   for (JLabel take : this.currTakes) {
      this.bPane.moveToFront(take);
   }
}

public void updatePlayerInfo() {
   LinkedList<Player> players = this.getPlayers();

   for (JLabel currLabel : this.infoLabels) {
      this.bPane.remove(currLabel);
   }

   this.infoLabels.clear();

   String[] colNames = {"Name", "Rank", "Dollars", "Credits"};

   JLabel infoCols = new JLabel(String.format("%-8s | %-4s | %-7s | %-7s", colNames[0], colNames[1], colNames[2], colNames[3]));
   infoCols.setBackground(Color.white);
   infoCols.setBounds(this.iconWidth+10, 150,200, 20);
   infoCols.addMouseListener(this.bml);
   this.infoLabels.add(infoCols);
   this.bPane.add(infoCols);

   int i = 0;
   int labelYOffset = 180;
   for (Player player : players) {
      String name = player.getName();
      int rank = player.getRank();
      int dollars = player.getDollars();
      int credits = player.getCredits();

      JLabel playerInfo = new JLabel(String.format("%-8s | %-4d | %-7d | %-7d", name, rank, dollars, credits));
      playerInfo.setBackground(Color.white);
      playerInfo.setBounds(this.iconWidth+10, labelYOffset,200, 20);
      playerInfo.addMouseListener(this.bml);
      this.infoLabels.add(playerInfo);
      this.bPane.add(playerInfo);

      labelYOffset += 30;

      i++;
   }
}

public void toggleWarning(String warning) {
   this.warningLabel.setText(warning);
   this.warningLabel.setVisible(true);
}

public void toggleHowManyOpts() {
   this.clearButtons();
   this.bml.clearButtons();

   this.mLabel.setText("How many players?");

   this.bml.updateNumButtons(this.numButtons);

   for (JButton button : this.numButtons) {
      button.setVisible(true);
   }
}

public void togglePlayerNameOpts() {
   this.clearButtons();
   this.bml.clearButtons();

   this.textField.setText(this.playerColors[0]);
   this.mLabel.setText("Player 1, please enter name");

   this.textField.setVisible(true);
   this.confButton.setVisible(true);
}

public void initialTurnOpts() {
   this.clearButtons();
   this.bml.clearButtons();

   gc.startGame(this.bml.getPlayers());
   this.updateLocationState(this.board.getCurrLocs());
   this.initPlayerDice();
   this.toggleTurnOpts();
}

public void toggleTurnOpts() {
   this.clearButtons();
   Player player = gc.getActivePlayer();

   String playerName = player.getName();
   this.mLabel.setText(playerName + ", select an action");

   bWork.setVisible(true);
   bUpgrade.setVisible(true);
   bMove.setVisible(true);
}

public void toggleEndTurn() {
   this.clearButtons();
   this.mLabel.setText("End turn?");
   Player activePlayer = this.gc.getActivePlayer();
   if (activePlayer.justWrapped()) {
      this.toggleWarning(activePlayer.getMsgs().get(0));
   } else {
      this.toggleWarning(this.gc.getActivePlayer().getWarning());
   }
   this.bEnd.setVisible(true);
}

public void toggleMoveOpts() {
   this.clearButtons();
   this.bml.clearButtons();

   Player player = this.gc.getActivePlayer();

   this.mLabel.setText("Choose a place to move");

   LinkedList<String> locations = this.board.getAdjLocations(player);

   int buttonYLoc = 30;

   for (String locName : locations) {
      JButton locButt = new JButton(locName);
      locButt.setBackground(Color.white);
      locButt.setBounds(this.iconWidth+10, buttonYLoc,140, 20);
      locButt.addMouseListener(this.bml);
      this.locButtons.add(locButt);
      this.buttons.add(locButt);

      buttonYLoc += 30;
   }

   this.bml.updateLocButtons(this.locButtons);

   for (JButton button : this.locButtons) {
      this.bPane.add(button, 3);
   }
}

public void toggleTakeRoleOpts() {
   this.clearButtons();
   this.bml.clearButtons();

   Player player = this.gc.getActivePlayer();

   this.mLabel.setText("Choose a role to act");

   LinkedList<Role> roles = player.getLocation().getAllRoles();

   int rank = player.getRank();

   int buttonYLoc = 30;

   boolean noRoles = true;
   for (Role role : roles) {
      if (role.getDiceNum() <= rank && !role.isTaken()) {
         noRoles = false;
         JButton roleButt = new JButton(role.getRoleName() + "\n\"" + role.getCatch() + "\"");
         roleButt.setBackground(Color.white);
         roleButt.setBounds(this.iconWidth+10, buttonYLoc,280, 20);
         roleButt.addMouseListener(this.bml);
         this.roleButtons.add(roleButt);
         this.buttons.add(roleButt);

         buttonYLoc += 30;
      }
   }

   if (noRoles) {
      if (this.gc.moveUsed()) {
         this.gc.updateGameState("end");
      } else {
         this.toggleTurnOpts();
         this.toggleWarning("No roles available here");
         return;
      }
   }

   this.bml.updateRoleButtons(this.roleButtons);

   for (JButton button : this.roleButtons) {
      this.bPane.add(button, 4);
   }
}

public void toggleWorkOnRoleOpts() {
   this.clearButtons();
   this.bml.clearButtons();

   this.mLabel.setText("act or rehearse?");

   this.actButton.setVisible(true);
   this.rehearseButton.setVisible(true);
}

public void toggleWorkOffRoleOpts() {
   this.clearButtons();
   this.bml.clearButtons();

   this.mLabel.setText("Do you want to take a role?");

   this.yesButton.setVisible(true);
   this.noButton.setVisible(true);
}

public void toggleUpgrade() {
   this.clearButtons();
   this.bml.clearButtons();

   for (JLabel label : this.infoLabels) {
      this.bPane.remove(label);
   }

   Upgrades upgradeMap = board.getUpgradeMap();
   HashMap<Integer, Integer> dollarCosts = upgradeMap.getDollarCosts();
   HashMap<Integer, Integer> creditCosts = upgradeMap.getCreditCosts();

   Player activePlayer = this.gc.getActivePlayer();
   int rank = activePlayer.getRank();
   int dollars = activePlayer.getDollars();
   int credits = activePlayer.getCredits();

   int buttonYLoc = 150;
   boolean buttonExits = false;
   for (int i = 2; i < 7; ++i) {
      if (i > rank && (dollarCosts.get(i) <= dollars || creditCosts.get(i) <= credits)) {
         buttonExits = true;
         
         JLabel rankLabel = new JLabel("Rank " + i);
         rankLabel.setBackground(Color.white);
         rankLabel.setBounds(this.iconWidth+10, buttonYLoc,80, 20);
         rankLabel.addMouseListener(this.bml);
         this.upgradeComps.add(rankLabel);

         JButton dollarButton = new JButton(i + ": $" + dollarCosts.get(i));
         dollarButton.setBackground(Color.white);
         dollarButton.setBounds(this.iconWidth+100, buttonYLoc,80, 20);
         dollarButton.addMouseListener(this.bml);
         this.upgradeComps.add(dollarButton);

         JButton creditButton = new JButton(i + ": " + dollarCosts.get(i) + " credits");
         creditButton.setBackground(Color.white);
         creditButton.setBounds(this.iconWidth+190, buttonYLoc,80, 20);
         creditButton.addMouseListener(this.bml);
         this.upgradeComps.add(creditButton);

         this.bPane.add(rankLabel);
         this.bPane.add(dollarButton);
         this.bPane.add(creditButton);

         buttonYLoc += 30;
      }

      if (!buttonExits) {
         this.gc.getActivePlayer().setWarning("No upgrades currently available");
         this.toggleEndTurn();
      }
   }

   this.bml.updateComps(this.upgradeComps);
}

private void clearButtons() {
   for (JButton button : this.buttons) {
      button.setVisible(false);
   }

   for (JButton button : this.locButtons) {
      this.bPane.remove(button);
   }

   for (JButton button : this.roleButtons) {
      this.bPane.remove(button);
   }

   for (JLabel label : this.wrapInfoMsgs) {
      this.bPane.remove(label);
   }

   for (Component comp : upgradeComps) {
      this.bPane.remove(comp);
   }

   this.wrapInfoMsgs.clear();
   this.locButtons.clear();
   this.roleButtons.clear();
   this.upgradeComps.clear();
   this.textField.setVisible(false);
   this.warningLabel.setVisible(false);
}

private void generatePlayerNumOpts() {

   int buttonYLoc = 30;

   for (int i = 2; i < 9; ++i) {
      JButton numButton = new JButton(Integer.toString(i));
      numButton.setBackground(Color.white);
      numButton.setBounds(this.iconWidth+10, buttonYLoc,140, 20);
      this.buttons.add(numButton);
      numButton.addMouseListener(this.bml);
      this.numButtons.add(numButton);
      this.bPane.add(numButton, 5);
      numButton.setVisible(false);

      buttonYLoc += 30;
   }
}

private void generatePlayerNamesOpts() {

   this.textField = new JTextField(20);
   this.textField.setBounds(this.iconWidth+10,30,140,20);
   this.bPane.add(this.textField, 6);
   this.textField.setVisible(false);

   this.confButton = new JButton("CONFIRM NAME");
   this.confButton.setBackground(Color.white);
   this.confButton.setBounds(this.iconWidth+10, 60,140, 20);
   this.confButton.addMouseListener(this.bml);
   this.buttons.add(this.confButton);
   this.bPane.add(this.confButton, 6);
   this.confButton.setVisible(false);
}

private void generateOnRoleOpts() {
   this.actButton = new JButton("ACT");
   this.actButton.setBackground(Color.white);
   this.actButton.setBounds(this.iconWidth+10, 30, 140, 20);
   this.actButton.addMouseListener(this.bml);
   this.buttons.add(this.actButton);
   this.bPane.add(this.actButton);
   this.actButton.setVisible(false);

   this.rehearseButton = new JButton("REHEARSE");
   this.rehearseButton.setBackground(Color.white);
   this.rehearseButton.setBounds(this.iconWidth+10, 60, 140, 20);
   this.rehearseButton.addMouseListener(this.bml);
   this.buttons.add(this.rehearseButton);
   this.bPane.add(this.rehearseButton);
   this.rehearseButton.setVisible(false);
}

private void generateOffRoleOpts() {
   this.yesButton = new JButton("YES");
   this.yesButton.setBackground(Color.white);
   this.yesButton.setBounds(this.iconWidth+10, 30,140, 20);
   this.yesButton.addMouseListener(this.bml);
   this.buttons.add(this.yesButton);
   this.bPane.add(this.yesButton, 4);
   this.yesButton.setVisible(false);

   this.noButton = new JButton("NO");
   this.noButton.setBackground(Color.white);
   this.noButton.setBounds(this.iconWidth+10,60,140, 20);
   this.noButton.addMouseListener(this.bml);
   this.buttons.add(this.noButton);
   this.bPane.add(this.noButton, 7);
   this.noButton.setVisible(false);
}
   
   // This class implements Mouse Events
   class boardMouseListener implements MouseListener{

      private GameController gc;

      private BoardLayersListener bll;
      private JLabel label;
      private JTextField textField;

      String[] playerColors;

      private LinkedList<JButton> locButtons;
      private LinkedList<JButton> roleButtons;
      private LinkedList<JButton> numButtons;
      private LinkedList<Player> players;
      private LinkedList<Component> comps;
      private String selectedText;
      private int numPlayers;
      private int currPlayer;
      private int startRank;
      private int startCredits;

      public boardMouseListener(BoardLayersListener bll, String[] playerColors, GameController gc) {

         this.bll = bll;
         this.playerColors = playerColors;
         this.gc = gc;

         this.locButtons = new LinkedList<>();
         this.roleButtons = new LinkedList<>();
         this.numButtons = new LinkedList<>();
         this.players = new LinkedList<>();
         this.selectedText = null;
         this.numPlayers = 0;
         this.currPlayer = 1;
         this.startRank = 0;
         this.startCredits = 0;
      }

      public String getText() {
         return this.selectedText;
      }

      public LinkedList<Player> getPlayers() {
         return this.players;
      }

      public void setTextField(JTextField textField) {
         this.textField = textField;
      }

      public void setLabel(JLabel label) {
         this.label = label;
      }

      public void addGameController(GameController gc) {
         this.gc = gc;
      }

      public void updatePlayerNum(int numPlayers) {
         this.numPlayers = numPlayers;
      }

      public void updateRank(int rank) {
         this.startRank = rank;
      }

      public void updateCredit(int credits) {
         this.startCredits = credits;
      }

      public void updateLocButtons(LinkedList<JButton> locButtons) {
         this.locButtons = locButtons;
      }

      public void updateRoleButtons(LinkedList<JButton> roleButtons) {
         this.roleButtons = roleButtons;
      }

      public void updateNumButtons(LinkedList<JButton> numButtons) {
         this.numButtons = numButtons;
      }

      public void updateComps(LinkedList<Component> comps) {
         this.comps = comps;
      }

      public void clearButtons() {
         this.locButtons.clear();
         this.roleButtons.clear();
         this.numButtons.clear();
      }

      // Code for the different button clicks
      public void mouseClicked(MouseEvent e) {
         
         if (e.getSource() == bWork){
            Player activePlayer = this.gc.getActivePlayer();
            Location currLoc = activePlayer.getLocation();
            String locName = currLoc.getName();
            if (locName.equals("trailer") || locName.equals("office")) {
               if (this.gc.moveUsed()) {
                  this.gc.updateGameState("end");
                  this.bll.toggleTurnOpts();
               } else {
                  this.bll.toggleWarning("Cannot work here");
               }
            } else if (!currLoc.sceneAvailable()) {
               this.bll.toggleWarning("Scene wrapped!");
            } else {
               if (!activePlayer.workingRole()) {
                  this.bll.toggleWorkOffRoleOpts();
               } else {
                  this.bll.toggleWorkOnRoleOpts();
               }
            }
         } else if (e.getSource() == bEnd) { 
            this.gc.updateGameState("end");
            this.bll.toggleTurnOpts();
         } else if (e.getSource() == bUpgrade){
            Player activePlayer = this.gc.getActivePlayer();
            if (activePlayer.getLocation().getName().equals("office")) {
               this.bll.toggleUpgrade();
            } else {
               this.bll.toggleWarning("You can only upgrade at the office!");
            }
         } else if (e.getSource() == bMove){
            Player activePlayer = this.gc.getActivePlayer();
            if (activePlayer.workingRole()) {
               this.bll.toggleWarning("Cannot move while working!");
            } else if (!this.gc.moveUsed()) {
                  this.bll.toggleMoveOpts();
            } else {
               this.bll.toggleWarning("Already moved!");
            }
         } else if (e.getSource() == yesButton) {
            this.bll.toggleTakeRoleOpts();
         } else if (e.getSource() == noButton) {
            this.gc.updateGameState("end");
            this.bll.toggleTurnOpts();
         } else if (e.getSource() == actButton) {
            Player activePlayer = gc.getActivePlayer();
            if (activePlayer.getLocation().sceneAvailable()) {
               this.gc.updateGameState("act");
               this.bll.toggleEndTurn();
            } else {
               this.bll.toggleWarning("Scene already wrapped");
            }
            
         } else if (e.getSource() == rehearseButton) {
            Player activePlayer = gc.getActivePlayer();
            if (activePlayer.getLocation().sceneAvailable()) {
               if (activePlayer.getChips() >= 5) {
                  this.bll.toggleTurnOpts();
                  this.bll.toggleWarning("Already have maximum practice tokens");
               } else {
                  this.gc.updateGameState("rehearse");
                  this.gc.updateGameState("end");
                  this.bll.toggleTurnOpts();
               }
            } else {
               this.bll.toggleWarning("Scene already wrapped");
            }
         } else if (e.getSource() == confButton) {
            String diceColor = this.bll.getColor(this.currPlayer).substring(0,1);
            this.players.add(new Player(this.textField.getText(), this.startRank, this.startCredits, "He", diceColor));
            this.currPlayer++;
            this.bll.updateTextColor(this.currPlayer);
            this.label.setText("Player " + this.currPlayer + ", please enter name");
            if (this.currPlayer > this.numPlayers) {
               bll.initialTurnOpts();
            }
         }  else if (this.numButtons.contains(e.getSource())) {
            JButton currButton = (JButton) e.getSource();
            this.selectedText = currButton.getText();
            this.gc.setPlayerNum(Integer.parseInt(this.selectedText));
         } else if (this.locButtons.contains(e.getSource())) {
            JButton currButton = (JButton) e.getSource();
            this.selectedText = currButton.getText();
            String cmd = "move " +  this.selectedText;
            this.gc.updateGameState(cmd);
            Player activePlayer = this.gc.getActivePlayer();
            Location currLoc = activePlayer.getLocation();
            if (currLoc.getName().equals("trailer") || !currLoc.sceneAvailable()) {
               activePlayer.setWarning("No more options here");
               this.bll.toggleEndTurn();
            } else {
               this.bll.toggleTurnOpts();
            }
         } else if (this.roleButtons.contains(e.getSource())) {
            JButton currButton = (JButton) e.getSource();
            this.selectedText = currButton.getText();
            Scanner tokenizer = new Scanner(this.selectedText);
            String roleName = tokenizer.nextLine();
            String quote = tokenizer.nextLine();
            quote = quote.substring(1,quote.length()-1);
            String cmd = "work " + roleName + "\n" + quote;
            this.gc.updateGameState(cmd);
            this.gc.updateGameState("end");
            this.bll.toggleTurnOpts();
         } else if (this.comps.contains(e.getSource())) {
            JButton currButton = (JButton) e.getSource();
            this.selectedText = currButton.getText();
            Scanner tokenizer = new Scanner(this.selectedText);
            String rank = tokenizer.next(); 
            tokenizer.next();
            rank = rank.substring(0,1);
            if (tokenizer.hasNext()) {
               //credit
               this.gc.updateGameState("upgrade C " + rank);
            } else {
               this.gc.updateGameState("upgrade D " + rank);
            }
            this.gc.getActivePlayer().setWarning("Successfully upgraded to rank " + rank);
            this.bll.toggleEndTurn();
         } else {
            System.out.println("error: button not found");
         }
      }
      public void mousePressed(MouseEvent e) {
      }
      public void mouseReleased(MouseEvent e) {
      }
      public void mouseEntered(MouseEvent e) {
      }
      public void mouseExited(MouseEvent e) {
      }
   }
}