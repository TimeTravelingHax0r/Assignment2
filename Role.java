class Role {
    private int diceNum;
    private boolean roleTaken;
    private String catchPhrase;

    public Role(int diceNum, String catchPhrase) {
        this.diceNum = diceNum;
        this.catchPhrase = catchPhrase;
        this.roleTaken = false;
    }

    public int getDiceNum() {
        return this.diceNum;
    }

    public void takeRole() {
        this.roleTaken = true;
    }

    public void finishRole() {
        this.roleTaken = false;
    }

}