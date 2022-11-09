import java.util.LinkedList;

public class SceneCard {
    private int budget;
    private LinkedList<Role> roles;

    public SceneCard(int budget, LinkedList<Role> roles) {
        this.budget = budget;
        this.roles = roles;
    }

    public int getBudget() {
        return this.budget;
    }

    public LinkedList<Role> getRoles() {
        return this.roles;
    }

}
