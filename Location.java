import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Location {
    private String name;
    private SceneCard card;
    private boolean sceneAvailable;
    public LinkedList<Role> offCardRoles;
    private int totalShots;
    private int shotsLeft;
    private boolean isSpecial; // used for trailer, casting office

    public Location(String name, LinkedList<Role> offCardRoles, int shots) {
        this.name = name;
        this.offCardRoles = offCardRoles;
        this.sceneAvailable = true;
        this.totalShots = shots;
        this.shotsLeft = this.totalShots;
        this.isSpecial = false;
    }

    public Location(String name) {
        this.name = name;
        this.isSpecial = true;
    }

    public void setCard(SceneCard card) {
        if (!isSpecial) {
            this.card = card;
        } else {
            System.out.println("Cannot put special card on this location.");
        }
    }

    public String getName() {
        return this.name;
    }

    public int getBudget() {
        if (!isSpecial) {
            return card.getBudget();
        } else {
            return -1;
        }
    }

    public SceneCard getCard() {
        if (!isSpecial) {
            return this.card;
        } else {
            return null;
        }
    }

    public LinkedList<Role> getOffCard() {
        if (!isSpecial) {
            return this.offCardRoles;
        } else {
            return null;
        }
    }

    public void takeRole(Player player, String roleName) {
        
        if (!this.isSpecial) {
            Role playerRole = null;
        
            for (Role role : this.offCardRoles) {
                if (role.getRoleName().equals(roleName)) {
                    playerRole = role;
                }
            }

            for (Role role : this.card.getRoles()) {
                if (role.getRoleName().equals(roleName)) {
                    playerRole = role;
                }
            }

            if (playerRole != null) {
                playerRole.takeRole(player);
            } else {
                System.out.println("error: no role found.");
            }
        } else {
            System.out.println("Cannot take role on this location [" + this.name + "].");
        }
    }

    public void resetScene() {
        this.shotsLeft = this.totalShots;
        this.sceneAvailable = true;
    }

    public boolean takeCounter(Player activePlayer) {
        if (this.shotsLeft == 1) {
            this.wrapScene(activePlayer);
            return true;
        } else {
            this.shotsLeft--;
            return false;
        }
    }

    private void wrapScene(Player activePlayer) {

        System.out.println("The scene " + this.card.getName() + " is wrapped!");

        int currBudget = this.getBudget();
        LinkedList<Integer> diceRolls = activePlayer.rollDice(currBudget);
        Collections.sort(diceRolls, Collections.reverseOrder());
        LinkedList<Role> rolesOnCard = this.card.getRoles();
        HashMap<Integer, Role> roleValues = new HashMap<>();

        for (Role role : rolesOnCard) {
            roleValues.put(role.getDiceNum(), role);
        }

        LinkedList<Integer> sortedKeys = new LinkedList<>(roleValues.keySet());
        Collections.sort(sortedKeys, Collections.reverseOrder());

        int key = 0;

        for (int roll = 0; roll < diceRolls.size(); ++roll) {
            Role currRole = roleValues.get(sortedKeys.get(key));
            Player currPlayer = currRole.getPlayer();
            if (currPlayer != null) {

                String currName = currPlayer.getName();
                int award = diceRolls.get(roll);

                System.out.println(currName + " is awarded $" + award + "!");

                currPlayer.updateDollars(award);
            }

            if (key+1 == sortedKeys.size()) {
                key = 0;
            } else {
                key++;
            }
        }

        // Off the card bonuses

        for (Role role : this.offCardRoles) {

            Player currPlayer = role.getPlayer();

            if (currPlayer != null) {

                String currName = currPlayer.getName();
                int award = role.getDiceNum();

                System.out.println(currName + " is awarded $" + award + "!");
                currPlayer.updateDollars(award);
            }
        }

        for (Role role : rolesOnCard) {
            Player currPlayer = role.getPlayer();
            if (currPlayer != null) {
                currPlayer.removeRole();
            }
            role.finishRole();
        }

        for (Role role : this.offCardRoles) {
            Player currPlayer = role.getPlayer();
            if (currPlayer != null) {
                currPlayer.removeRole();
            }
            role.finishRole();
        }

        this.sceneAvailable = false;
    }

    public boolean sceneAvailable() {
        return this.sceneAvailable; 
    }
}
