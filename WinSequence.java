public class winSequence{
	private int winners = 0;
	
	private int calcScore(LinkedList<Player> players){
		int highScore = 0;
		for(Player i : players){
			if((i.getDollars() + i.getCredits() + (5*i.getRank())) > highScore){
				highScore = (i.getDollars() + i.getCredits() + (5*i.getRank());
			}
		}
		return highScore;
	}
					     
	private String getWinner(LinkedList<Player> players){
		String[] winners;
		int num = 0;
		int highScore = calcScore(players);
		for(Player i : players){
			if((i.getDollars() + i.getCredits() + (5*i.getRank())) = highScore){
				winners[num] = i.getName();
				num++;
			}	
		}
		if(winners.length > 1){
			System.out.print("The winners are: ");
			for(int j=0;j<winners.length;j++){
				System.out.println(winners[j] + " ");	
			}
			 System.out.println(" with "+highScore+" points."
		}else{
			System.out.println("The winner is: "+winners[0]+" with "+highScore+" points.");
		}
	}
					    
	private void setWinner(){
	}
				     
}

