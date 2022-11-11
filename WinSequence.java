public class winSequence{
	private int highScore = 0;
	pirvate int winners = 0;
	private int[] scores = new int[gameManagers.size()];
	public winSequence(){
		for(player i : players i){
			scores[i] = players(i).getDollars + players(i).getCredits + (5 * players(i).getRank);
			if(iscores[i]  == highScores){
				winners++;
			}elif (scores[i] > highscore){
				Winners = 1;
				highScore = scores[i]
            }
		}	
	}
	if(winners > 1){
	    System.out.print(“The winners are  “);
    for(player i : players i){
        if(i.getScore == highScore){
	        System.out.println(i.getName + “ “);
        }
    }
    System.out.println(“ with “ + highScore + “ points.”);
}else{
	System.out.print(“The winner is  “);
    for(player i : players i){
	    if(i.getScore == highScore){
		    System.out.println(i.getName);
        }
    }
    System.out.println(“ with “ +highScore + “ points.”);
}

