class Role {
    private String name;
    private int diceNum;
    private String catchPhrase;
    private boolean roleTaken;
    private Player herePlayer;
    private boolean onCard;

    public Role(String name, int diceNum, String catchPhrase, boolean onCard) {
        this.name = name;
        this.diceNum = diceNum;
        this.catchPhrase = catchPhrase;
        this.roleTaken = false;
        this.onCard = onCard;
        this.herePlayer = null;
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

    public void takeRole(Player player) {
        if (!this.roleTaken) {
            if (player.getRank() >= this.diceNum) {
                this.herePlayer = player;
                this.herePlayer.changeRole(this);
                this.roleTaken = true;
            } else {
                System.out.println("rank not high enough.");
            }
        } else {
            System.out.println("Role already taken.");
        }
    }

    public boolean onCard() {
        return this.onCard;
    }

    public void finishRole() {
        this.roleTaken = false;
        this.herePlayer = null;
    }

    public Player getPlayer() {
        return this.herePlayer;
    }

}