class Role {
    private String name;
    private int diceNum;
    private String catchPhrase;
    private boolean roleTaken;
    private Player herePlayer;

    public Role(String name, int diceNum, String catchPhrase) {
        this.name = name;
        this.diceNum = diceNum;
        this.catchPhrase = catchPhrase;
        this.roleTaken = false;
    }
    
    public String getRoleName() {
        return this.name;
    }

    public int getDiceNum() {
        return this.diceNum;
    }

    public String getCatch() {
        return this.catchPhrase;
    }

    public boolean takeRole(Player player) {
        if (!this.roleTaken) {
            this.herePlayer = player;
            this.roleTaken = true;
            return true;
        } else {
            System.out.println("Role already taken.");
            return false;
        }
    }

    public void finishRole() {
        this.roleTaken = false;
        this.herePlayer = null;
    }

}