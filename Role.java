class Role {
    private String name;
    private int diceNum;
    private String catchPhrase;
    private boolean roleTaken;

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

    public void takeRole() {
        this.roleTaken = true;
    }

    public void finishRole() {
        this.roleTaken = false;
    }

}