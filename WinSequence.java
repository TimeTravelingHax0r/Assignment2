import java.util.LinkedList;
public class WinSequence {

	private LinkedList<Player> players;
	private String[] winners;

	public WinSequence(LinkedList<Player> players) {
		this.players = players;
		this.winners = new String[players.size()];
	}
	
	private int calcScore(LinkedList<Player> players){
		int highScore = 0;
		for(Player i : players){
			if((i.getDollars() + i.getCredits() + (5*i.getRank())) > highScore){
				highScore = (i.getDollars() + i.getCredits() + (5*i.getRank()));
			}
		}
		return highScore;
	}
					     
	public String[] getWinner(LinkedList<Player> players){
		return this.winners;
	}
					    
	public void setWinner() {
		int num = 0;
		int highScore = calcScore(this.players);
		for(Player i : this.players){
			if((i.getDollars() + i.getCredits() + (5*i.getRank())) == highScore){
				winners[num] = i.getName();
				num++;
			}	
		}
		if(this.winners.length > 1){
			System.out.print("The winners are: ");
			for(int j=0;j<this.winners.length;j++){

				System.out.println(this.winners[j] + " ");	

			}

			System.out.println(" with " + highScore + " points.");

		} else {
			System.out.println("The winner is: "+this.winners[0]+" with "+highScore+" points.");
		}   
	}
				     
}

