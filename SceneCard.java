import java.util.LinkedList;

public class SceneCard {
    private String name;
    private int budget;
    private int sceneNum;
    private String sceneDesc;
    private LinkedList<Role> roles;
    String img;

    public SceneCard(String name, int budget, int sceneNum, String sceneDesc, LinkedList<Role> roles, String img) {
        this.name = name;
        this.budget = budget;
        this.sceneNum = sceneNum;
        this.sceneDesc = sceneDesc;
        this.roles = roles;
        this.img = img;
    }

    public String getName() {
        return this.name;
    }

    public String getDesc() {
        return this.sceneDesc;
    }

    public int getNum() {
        return this.sceneNum;
    }

    public int getBudget() {
        return this.budget;
    }

    public LinkedList<Role> getRoles() {
        return this.roles;
    }

    public String getImg() {
        return this.img;
    }

}
