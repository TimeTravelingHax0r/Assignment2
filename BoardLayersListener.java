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
   
   public BoardLayersListener() {
         
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
   
   this.bml = new boardMouseListener();

   // Create the deadwood board
   boardlabel = new JLabel();
   ImageIcon icon =  new ImageIcon("images/board.jpg");
   boardlabel.setIcon(icon); 
   boardlabel.setBounds(0,0,icon.getIconWidth(),icon.getIconHeight());
   this.iconWidth = icon.getIconWidth();
   
   // Add the board to the lowest layer
   bPane.add(boardlabel, 0);
   
   // Set the size of the GUI
   setSize(icon.getIconWidth()+200,icon.getIconHeight());
   
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
   mLabel.setBounds(icon.getIconWidth()+40,0,100,20);
   bPane.add(mLabel, 2);

   // Create Action buttons
   bAct = new JButton("ACT");
   bAct.setBackground(Color.white);
   bAct.setBounds(icon.getIconWidth()+10, 30,100, 20);
   bAct.addMouseListener(this.bml);
   
   bRehearse = new JButton("REHEARSE");
   bRehearse.setBackground(Color.white);
   bRehearse.setBounds(icon.getIconWidth()+10,60,100, 20);
   bRehearse.addMouseListener(this.bml);
   
   bMove = new JButton("MOVE");
   bMove.setBackground(Color.white);
   bMove.setBounds(icon.getIconWidth()+10,90,100, 20);
   bMove.addMouseListener(this.bml);

   // Place the action buttons in the top layer
   this.bPane.add(bAct, 2);
   this.buttons.add(bAct);
   this.bPane.add(bRehearse, 2);
   this.buttons.add(bRehearse);
   this.bPane.add(bMove, 2);
   this.buttons.add(bMove);

   this.generateOffRoleOpts();
   this.generatePlayerNumOpts();
   this.generatePlayerNamesOpts();
}

public void toggleHowManyOpts() {
   this.clearButtons();

   this.mLabel.setText("How many players?");

   this.bml.updateNumButtons(this.numButtons);

   for (JButton button : this.numButtons) {
      button.setVisible(true);
   }
}

public void togglePlayerNameOpts() {
   this.clearButtons();

   this.textField.setVisible(true);
   this.maleButton.setVisible(true);
   this.femaleButton.setVisible(true);
   this.gnButton.setVisible(true);
}

public void toggleTurnOpts(Player player) {
   this.clearButtons();

   String playerName = player.getName();
   this.mLabel.setText(playerName + ", select an action");

   bAct.setVisible(true);
   bRehearse.setVisible(true);
   bMove.setVisible(true);
}

public void toggleMoveOpts(Player player, Board board) {
   this.clearButtons();

   this.mLabel.setText("Choose a place to move");

   LinkedList<String> locations = board.getAdjLocations(player);

   int buttonYLoc = 30;

   for (String locName : locations) {
      JButton locButt = new JButton(locName);
      locButt.setBackground(Color.white);
      locButt.setBounds(this.iconWidth+10, buttonYLoc,100, 20);
      locButt.addMouseListener(this.bml);
      this.locButtons.add(locButt);
      this.buttons.add(locButt);

      buttonYLoc += 30;
   }

   this.bml.updateLocButtons(this.locButtons);

   for (JButton button : this.locButtons) {
      this.bPane.add(button, 5);
   }
}

public void toggleTakeRoleOpts(Player player, Board board) {
   this.clearButtons();

   this.mLabel.setText("Choose a role to act");

   LinkedList<Role> roles = player.getLocation().getCard().getRoles();
   roles.addAll(player.getLocation().getOffCard());

   int rank = player.getRank();

   int buttonYLoc = 30;

   for (Role role : roles) {
      if (role.getDiceNum() <= rank) {
         JButton roleButt = new JButton(role.getRoleName());
         roleButt.setBackground(Color.white);
         roleButt.setBounds(this.iconWidth+10, buttonYLoc,100, 20);
         roleButt.addMouseListener(this.bml);
         this.roleButtons.add(roleButt);
         this.buttons.add(roleButt);

         buttonYLoc += 30;
      }
   }

   this.bml.updateRoleButtons(this.roleButtons);

   for (JButton button : this.roleButtons) {
      this.bPane.add(button, 6);
   }
}

public void toggleWorkOnRoleOpts(Player player) {
   this.clearButtons();

   this.mLabel.setText("act or rehearse?");

   this.actButton.setVisible(true);
   this.rehearseButton.setVisible(true);
}

public void toggleWorkOffRoleOpts() {
   this.clearButtons();

   this.mLabel.setText("act or rehearse?");

   this.yesButton.setVisible(true);
   this.noButton.setVisible(true);
}

public void toggleUpgrade(Player player, Upgrades upgrades) {
   this.clearButtons();
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

   for (JButton button : this.numButtons) {
      this.bPane.remove(button);
   }

   this.locButtons.clear();
   this.roleButtons.clear();
   this.numButtons.clear();
}

private void generatePlayerNumOpts() {

   int buttonYLoc = 30;

   for (int i = 0; i < 8; ++i) {
      JButton numButton = new JButton(Integer.toString(i));
      numButton.setBackground(Color.white);
      numButton.setBounds(this.iconWidth+10, buttonYLoc,100, 20);
      numButton.addMouseListener(this.bml);
      this.numButtons.add(numButton);
      this.buttons.add(numButton);
      this.bPane.add(numButton, 3);

      buttonYLoc += 30;
   }
}

private void generatePlayerNamesOpts() {

   this.textField = new JTextField(15);
   this.textField.setBackground(Color.white);
   this.textField.setLocation(this.iconWidth+10, 30);
   this.bPane.add(this.textField);
   this.textField.setVisible(false);

   this.maleButton = new JButton("MALE");
   this.maleButton.setBackground(Color.white);
   this.maleButton.setBounds(this.iconWidth+10, 60,100, 20);
   this.maleButton.addMouseListener(this.bml);
   this.buttons.add(this.maleButton);
   this.bPane.add(this.maleButton, 4);
   this.maleButton.setVisible(false);

   this.femaleButton = new JButton("FEMALE");
   this.femaleButton.setBackground(Color.white);
   this.femaleButton.setBounds(this.iconWidth+10, 60,100, 20);
   this.femaleButton.addMouseListener(this.bml);
   this.buttons.add(this.femaleButton);
   this.bPane.add(this.femaleButton, 4);
   this.femaleButton.setVisible(false);

   this.gnButton = new JButton("GENDER NEUTRAL");
   this.gnButton.setBackground(Color.white);
   this.gnButton.setBounds(this.iconWidth+10, 60,100, 20);
   this.gnButton.addMouseListener(this.bml);
   this.buttons.add(this.gnButton);
   this.bPane.add(this.gnButton, 4);
   this.gnButton.setVisible(false);
}

private void generateOffRoleOpts() {
   this.yesButton = new JButton("YES");
   this.yesButton.setBackground(Color.white);
   this.yesButton.setBounds(this.iconWidth+10, 30,100, 20);
   this.yesButton.addMouseListener(this.bml);
   this.buttons.add(this.yesButton);
   this.bPane.add(this.yesButton, 4);
   this.yesButton.setVisible(false);

   this.noButton = new JButton("NO");
   this.noButton.setBackground(Color.white);
   this.noButton.setBounds(this.iconWidth+10,60,100, 20);
   this.noButton.addMouseListener(this.bml);
   this.buttons.add(this.noButton);
   this.bPane.add(this.noButton, 4);
   this.noButton.setVisible(false);
}
   
   // This class implements Mouse Events
   class boardMouseListener implements MouseListener{

      private LinkedList<JButton> locButtons;
      private LinkedList<JButton> roleButtons;
      private LinkedList<JButton> numButtons;
      private String selectedText;

      public boardMouseListener() {
         this.locButtons = new LinkedList<>();
         this.roleButtons = new LinkedList<>();
         this.numButtons = new LinkedList<>();
         this.selectedText = null;
      }

      public String getText() {
         return this.selectedText;
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
         
         if (e.getSource()== bAct){
            playerlabel.setVisible(true);
            System.out.println("Acting is Selected\n");
         }
         else if (e.getSource()== bRehearse){
            System.out.println("Rehearse is Selected\n");
         }
         else if (e.getSource()== bMove){
            System.out.println("Move is Selected\n");
         } else if (this.numButtons.contains(e.getSource())) {
            JButton currButton = (JButton) e.getSource();
            this.selectedText = currButton.getText();
         } else if (this.locButtons.contains(e.getSource())) {
            JButton currButton = (JButton) e.getSource();
            this.selectedText = currButton.getText();
         } else if (this.roleButtons.contains(e.getSource())) {
            JButton currButton = (JButton) e.getSource();
            this.selectedText = currButton.getText();
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