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
import java.util.LinkedList;

import javax.swing.JOptionPane;

public class BoardLayersListener extends JFrame {

   boardMouseListener bml;
   private GameController gc;
   private Board board;

   private static String[] playerColors = {"blue", "cyan", "green", "orange", "pink", "red", "violet", "white"};

   // JLabels
   JLabel boardlabel;
   JLabel cardlabel;
   JLabel playerlabel;
   JLabel mLabel;
   
   //JButtons
   JButton bAct;
   JButton bRehearse;
   JButton bMove;

   // Player selection buttons
   JButton maleButton;
   JButton femaleButton;
   JButton gnButton;

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
   
   // Add a scene card to this room
   cardlabel = new JLabel();
   ImageIcon cIcon =  new ImageIcon("images/01.png");
   cardlabel.setIcon(cIcon); 
   cardlabel.setBounds(20,65,cIcon.getIconWidth()+2,cIcon.getIconHeight());
   cardlabel.setOpaque(true);
   
   // Add the card to the lower layer
   bPane.add(cardlabel, 1);
   
   // Add a dice to represent a player. 
   // Role for Crusty the prospector. The x and y co-ordiantes are taken from Board.xml file
   playerlabel = new JLabel();
   ImageIcon pIcon = new ImageIcon("images/dice/r2.png");
   playerlabel.setIcon(pIcon);
   //playerlabel.setBounds(114,227,pIcon.getIconWidth(),pIcon.getIconHeight());  
   playerlabel.setBounds(114,227,46,46);
   playerlabel.setVisible(false);
   bPane.add(playerlabel, 3);
   
   // Create the Menu for action buttons
   mLabel = new JLabel("MENU");
   mLabel.setBounds(icon.getIconWidth()+40,0,180,20);
   bPane.add(mLabel, 2);

   // Create Action buttons
   bAct = new JButton("ACT");
   bAct.setBackground(Color.white);
   bAct.setBounds(icon.getIconWidth()+10, 30,140, 20);
   bAct.addMouseListener(this.bml);
   
   bRehearse = new JButton("REHEARSE");
   bRehearse.setBackground(Color.white);
   bRehearse.setBounds(icon.getIconWidth()+10,60,140, 20);
   bRehearse.addMouseListener(this.bml);
   
   bMove = new JButton("MOVE");
   bMove.setBackground(Color.white);
   bMove.setBounds(icon.getIconWidth()+10,90,140, 20);
   bMove.addMouseListener(this.bml);

   // Place the action buttons in the top layer
   this.bPane.add(bAct, 2);
   this.buttons.add(bAct);
   this.bPane.add(bRehearse, 2);
   this.buttons.add(bRehearse);
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

public void setNumRankCredits(int numPlayers, int rank, int credits) {
   this.bml.updatePlayerNum(numPlayers);
   this.bml.updateRank(rank);
   this.bml.updateCredit(credits);
}

public void updateTextColor(int playerNum) {
   this.textField.setText(this.playerColors[playerNum-1]);
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
   this.mLabel.setText("Player 1, please enter you name, and confirm with gender");

   this.textField.setVisible(true);
   this.maleButton.setVisible(true);
   this.femaleButton.setVisible(true);
   this.gnButton.setVisible(true);
}

public void initialTurnOpts() {
   this.clearButtons();
   this.bml.clearButtons();

   Player activePlayer = gc.startGame(this.bml.getPlayers());
   this.toggleTurnOpts(activePlayer);
}

public void toggleTurnOpts(Player player) {
   this.clearButtons();
   gc.startGame(this.bml.getPlayers());

   String playerName = player.getName();
   this.mLabel.setText(playerName + ", select an action");

   bAct.setVisible(true);
   bRehearse.setVisible(true);
   bMove.setVisible(true);
}

public void toggleMoveOpts() {
   this.clearButtons();
   this.bml.clearButtons();

   Player player = this.gc.getActivePlayer();

   if (gc.moveUsed()) {
      this.toggleTurnOpts(player);
   } else {
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
}

public void toggleTakeRoleOpts() {
   this.clearButtons();
   this.bml.clearButtons();

   Player player = this.gc.getActivePlayer();

   this.mLabel.setText("Choose a role to act");

   LinkedList<Role> roles = player.getLocation().getCard().getRoles();
   roles.addAll(player.getLocation().getOffCard());

   int rank = player.getRank();

   int buttonYLoc = 30;

   for (Role role : roles) {
      if (role.getDiceNum() <= rank) {
         JButton roleButt = new JButton(role.getRoleName());
         roleButt.setBackground(Color.white);
         roleButt.setBounds(this.iconWidth+10, buttonYLoc,140, 20);
         roleButt.addMouseListener(this.bml);
         this.roleButtons.add(roleButt);
         this.buttons.add(roleButt);

         buttonYLoc += 30;
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

   Player player = this.gc.getActivePlayer();

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

public void toggleUpgrade(Player player, Upgrades upgrades) {
   this.clearButtons();
   this.bml.clearButtons();
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

   this.locButtons.clear();
   this.roleButtons.clear();
   this.textField.setVisible(false);
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

   this.maleButton = new JButton("MALE");
   this.maleButton.setBackground(Color.white);
   this.maleButton.setBounds(this.iconWidth+10, 60,140, 20);
   this.maleButton.addMouseListener(this.bml);
   this.buttons.add(this.maleButton);
   this.bPane.add(this.maleButton, 6);
   this.maleButton.setVisible(false);

   this.femaleButton = new JButton("FEMALE");
   this.femaleButton.setBackground(Color.white);
   this.femaleButton.setBounds(this.iconWidth+10, 90,140, 20);
   this.femaleButton.addMouseListener(this.bml);
   this.buttons.add(this.femaleButton);
   this.bPane.add(this.femaleButton, 6);
   this.femaleButton.setVisible(false);

   this.gnButton = new JButton("GENDER NEUTRAL");
   this.gnButton.setBackground(Color.white);
   this.gnButton.setBounds(this.iconWidth+10, 120,140, 20);
   this.gnButton.addMouseListener(this.bml);
   this.buttons.add(this.gnButton);
   this.bPane.add(this.gnButton, 6);
   this.gnButton.setVisible(false);
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

      public void clearButtons() {
         this.locButtons.clear();
         this.roleButtons.clear();
         this.numButtons.clear();
      }

      // Code for the different button clicks
      public void mouseClicked(MouseEvent e) {
         
         if (e.getSource() == bAct){
            playerlabel.setVisible(true);
            
         }
         else if (e.getSource() == bRehearse){
            System.out.println("Rehearse is Selected\n");
         } else if (e.getSource() == bMove){
            System.out.println("Move is Selected\n");
            this.bll.toggleMoveOpts();
         } else if (e.getSource() == yesButton) {
            System.out.println("Yes is Selected\n");
         } else if (e.getSource() == noButton) {
            System.out.println("No is Selected\n");
         } else if (e.getSource() == actButton) {
            System.out.println("Act sub button is Selected\n");
         } else if (e.getSource() == rehearseButton) {
            System.out.println("Rehearse sub button is Selected\n"); 
         } else if (e.getSource() == maleButton) {
            System.out.println("Male is Selected\n");
            System.out.println(this.currPlayer + " " + this.numPlayers);
            if (this.currPlayer < this.numPlayers) {
               this.players.add(new Player(this.textField.getText(), this.startRank, this.startCredits, "He"));
               this.currPlayer++;
               this.bll.updateTextColor(this.currPlayer);
               this.label.setText("Player " + this.currPlayer + " enter name and confirm with gender");
            } else {
               System.out.println("why am I here");
               bll.initialTurnOpts();
            }
         } else if (e.getSource() == femaleButton) {
            System.out.println("Female is Selected\n");
            System.out.println(this.currPlayer + " " + this.numPlayers);
            if (this.currPlayer < this.numPlayers) {
               this.players.add(new Player(this.textField.getText(), this.startRank, this.startCredits, "She"));
               this.currPlayer++;
               this.bll.updateTextColor(this.currPlayer);
               this.label.setText("Player " + this.currPlayer + " enter name and confirm with gender");
            } else {
               bll.initialTurnOpts();
            }
         } else if (e.getSource() == gnButton) {
            System.out.println("Gender Neutal is Selected\n");
            System.out.println(this.currPlayer + " " + this.numPlayers);
            if (this.currPlayer < this.numPlayers) {
               this.players.add(new Player(this.textField.getText(), this.startRank, this.startCredits, "They"));
               this.currPlayer++;
               this.bll.updateTextColor(this.currPlayer);
               this.label.setText("Player " + this.currPlayer + " enter name and confirm with gender");
            } else {
               bll.initialTurnOpts();
            }
         } else if (this.numButtons.contains(e.getSource())) {
            JButton currButton = (JButton) e.getSource();
            this.selectedText = currButton.getText();
            this.gc.setPlayerNum(Integer.parseInt(this.selectedText));
            System.out.println(this.selectedText);
         } else if (this.locButtons.contains(e.getSource())) {
            JButton currButton = (JButton) e.getSource();
            this.selectedText = "move " + currButton.getText();
            System.out.println(this.selectedText);
         } else if (this.roleButtons.contains(e.getSource())) {
            JButton currButton = (JButton) e.getSource();
            this.selectedText = currButton.getText();
            System.out.println(this.selectedText);
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

      private Player makePlayer(String currPlayer, int startRank, int startCredits, String genderChoice) {
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

            return new Player(currPlayer, startRank, startCredits, playerGender);
      }
   }
}