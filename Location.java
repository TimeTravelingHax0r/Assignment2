import java.util.LinkedList;

public class Location {
    private String name;
    private SceneCard card;
    private boolean sceneAvailable;
    public LinkedList<Role> offCardRoles;
    private int totalShots;
    private boolean isSpecial; // used for trailer, casting office

    public Location(String name, LinkedList<Role> offCardRoles, int shots) {
        this.name = name;
        this.offCardRoles = offCardRoles;
        this.sceneAvailable = true;
        this.totalShots = shots;
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

    public boolean takeRole(Player player, String roleName) {
        
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
                return playerRole.takeRole(player);
            } else {
                System.out.println("error: no role found.");
                return false;
            }
        } else {
            System.out.println("Cannot take role on this location [" + this.name + "].");
            return false;
        }
    }

    public boolean sceneAvailable() {
        return this.sceneAvailable;
    }

    @Override
    public boolean equals(Object o) {
        // self check
        if (this == o) {
            return true;
        }
        // null check
        if (o == null) {
            return false;
        }
        // type check and cast
        if (getClass() != o.getClass()) {
            return false;
        }

        Location otherLoc = (Location) o;

        if (this.name.equals(otherLoc.getName())) {
            return true;
        } else {
            return false;
        }
    }

}
