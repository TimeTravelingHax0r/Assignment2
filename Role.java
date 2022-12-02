class Role {
    private String name;
    private int diceNum;
    private String catchPhrase;
    private boolean roleTaken;
    private Player herePlayer;
    private boolean onCard;
    private int x, y, h, w;

    public Role(String name, int diceNum, String catchPhrase, boolean onCard, int x, int y, int h, int w) {
        this.name = name;
        this.diceNum = diceNum;
        this.catchPhrase = catchPhrase;
        this.roleTaken = false;
        this.onCard = onCard;
        this.herePlayer = null;
        this.x = x;
        this.y = y;
        this.h = h;
        this.w = w;
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

    public Player getPlayer() {
        return this.herePlayer;
    }

    public boolean onCard() {
        return this.onCard;
    }

    public boolean isTaken() {
        return this.roleTaken;
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

    public void finishRole() {
        this.roleTaken = false;
        this.herePlayer = null;
    }
}