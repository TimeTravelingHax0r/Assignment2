import java.util.LinkedList;

public class SceneCard {
    private String name;
    private int budget;
    private LinkedList<Role> roles;

    public SceneCard(String name, int budget, LinkedList<Role> roles) {
        this.name = name;
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
