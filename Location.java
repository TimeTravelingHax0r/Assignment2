import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

public class Location {
    private String name;
    private SceneCard card;
    private boolean sceneAvailable;
    private boolean isDiscovered;
    public LinkedList<Role> offCardRoles;
    private int totalTakes;
    private int takesLeft;
    private HashMap<Integer, Take> takes;
    private boolean isSpecial; // used for trailer, casting office
    private int x, y, h, w;

    public Location(String name, LinkedList<Role> offCardRoles, int totalTakes, HashMap<Integer, Take> takes, int x, int y, int h, int w) {
        this.name = name;
        this.offCardRoles = offCardRoles;
        this.sceneAvailable = true;
        this.isDiscovered = false;
        this.totalTakes = totalTakes;
        this.takesLeft = this.totalTakes;
        this.takes = takes;
        this.isSpecial = false;
        this.x = x;
        this.y = y;
        this.h = h;
        this.w = w;
    }

    public Location(String name, int x, int y, int h, int w) {
        this.name = name;
        this.isSpecial = true;
        this.sceneAvailable = true;
        this.x = x;
        this.y = y;
        this.h = h;
        this.w = w;
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

    public LinkedList<Role> getAllRoles() {
        LinkedList<Role> retList = new LinkedList<>();
        if (!isSpecial) {
            retList.addAll(this.offCardRoles);
            retList.addAll(this.card.getRoles());
        } else {
            retList = null;
        }

        return retList;
    }

    public LinkedList<Take> getCurrTakes() {

        LinkedList<Take> retList = new LinkedList<>();

        if (this.sceneAvailable) {
            for (int i = this.takesLeft; i > 0; --i) {
                retList.add(this.takes.get(i));
            }
        } 

        return retList;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getH() {
        return this.h;
    }

    public int getW() {
        return this.w;
    }

    public void setDiscovery() {
        this.isDiscovered = true;
    }

    public void setCard(SceneCard card) {
        if (!isSpecial) {
            this.card = card;
        } else {
            System.out.println("Cannot put special card on this location.");
        }
    }

    // gets role from this location, sets player onto role
    public void takeRole(Player player, String roleName, String roleQuote) {

        if (!this.isSpecial) {
            Role playerRole = null;

            for (Role role : this.offCardRoles) {
                if (role.getRoleName().equals(roleName) && role.getCatch().equals(roleQuote)) {
                    playerRole = role;
                }
            }

            for (Role role : this.card.getRoles()) {
                if (role.getRoleName().equals(roleName) && role.getCatch().equals(roleQuote)) {
                    playerRole = role;
                }
            }

            if (playerRole != null) {
                boolean rankTooHigh = player.getRank() < playerRole.getDiceNum();
                if (!rankTooHigh) {
                    playerRole.takeRole(player);
                    player.changeRole(playerRole);
                } else {
                    System.out.println("rank not high enough.");
                }
            } else {
                System.out.println("error: no role found.");
            }
        } else {
            System.out.println("Cannot take role on this location [" + this.name + "].");
        }
    }

    public boolean sceneAvailable() {
        return this.sceneAvailable;
    }

    public boolean isDiscovered() {
        return this.isDiscovered;
    }

    // resets shot counter for next scene
    public void resetScene() {
        this.takesLeft = this.totalTakes;
        this.sceneAvailable = true;
        this.isDiscovered = false;
    }

    // take counter from board and wrap scene if final counter
    public boolean takeCounter(Player activePlayer) {
        if (this.takesLeft == 1) {
            this.wrapScene(activePlayer);
            return true;
        } else {
            this.takesLeft--;
            return false;
        }
    }

    // wraps up scene
    private void wrapScene(Player activePlayer) {

        LinkedList<String> sceneWrapMsgs = new LinkedList<>();

        sceneWrapMsgs.add("The scene " + this.card.getName() + " is wrapped!");

        int currBudget = this.getBudget();
        LinkedList<Integer> diceRolls = activePlayer.rollDice(currBudget);
        Collections.sort(diceRolls, Collections.reverseOrder());
        LinkedList<Role> rolesOnCard = this.card.getRoles();
        HashMap<Integer, Role> roleValues = new HashMap<>();

        // get all the roles to give each dice roll to
        for (Role role : rolesOnCard) {
            roleValues.put(role.getDiceNum(), role);
        }

        LinkedList<Integer> sortedKeys = new LinkedList<>(roleValues.keySet());
        Collections.sort(sortedKeys, Collections.reverseOrder());

        // for each roll
        int key = 0;
        for (int roll = 0; roll < diceRolls.size(); ++roll) {
            // get each player and apply dice award
            Role currRole = roleValues.get(sortedKeys.get(key));
            Player currPlayer = currRole.getPlayer();
            if (currPlayer != null) {

                String currName = currPlayer.getName();
                int award = diceRolls.get(roll);

                sceneWrapMsgs.add(currName + " is awarded $" + award + "!");

                currPlayer.updateDollars(award);
            }

            if (key + 1 == sortedKeys.size()) {
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

                sceneWrapMsgs.add(currName + " is awarded $" + award + "!");
                currPlayer.updateDollars(award);
            }
        }

        // clear players off roles and finish roles
        for (Role role : rolesOnCard) {
            Player currPlayer = role.getPlayer();
            if (currPlayer != null) {
                currPlayer.removeRole();
            }
            role.finishRole();
        }

        // clear players off roles and finish roles
        for (Role role : this.offCardRoles) {
            Player currPlayer = role.getPlayer();
            if (currPlayer != null) {
                currPlayer.removeRole();
            }
            role.finishRole();
        }

        this.sceneAvailable = false;
        activePlayer.setWrapped();
        activePlayer.setWrapMsgs(sceneWrapMsgs);
    }
}
