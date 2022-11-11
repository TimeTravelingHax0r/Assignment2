import java.util.HashMap;
import java.util.LinkedList;

public class WinSequence {

	private LinkedList<Player> players;

	public WinSequence(LinkedList<Player> players) {
		this.players = players;
	}

	public void activate() {

		HashMap<Player, Integer> playerScores = calcScore(this.players);
		LinkedList<Player> winners = getWinners(playerScores);
		this.displayWinner(winners, playerScores);
	}
	
	private HashMap<Player, Integer> calcScore(LinkedList<Player> players){
		
		HashMap<Player, Integer> playerScores = new HashMap<>();
		
		for(Player player : players){

			int score = player.getDollars();
			score += player.getCredits();
			score += player.getRank() * 5;

			playerScores.put(player, score);
		}

		return playerScores;
	}

	private LinkedList<Player> getWinners(HashMap<Player, Integer> playerScores){
		
		int highestScore = 0;
		LinkedList<Player> winners = new LinkedList<>();

		for (int score : playerScores.values()) {
			if (score > highestScore) {
				highestScore = score;
			}
		}

		for (Player player : playerScores.keySet()) {
			if (playerScores.get(player) == highestScore) {
				winners.add(player);
			}
		}

		return winners;
	}

	private void displayWinner(LinkedList<Player> winners, HashMap<Player, Integer> playerScores) {
		if (winners.size() == 1) {
			System.out.println("The winner is " + winners.get(0).getName() + "! With a score of " + playerScores.get(winners.get(0)) + "!");
		} else {
			System.out.print("The winners are ");

			for (int i = 0; i < winners.size(); ++i) {

				if (i != (winners.size()-1)) {
					System.out.print(winners.get(i).getName());
					System.out.print(", ");
				} else {
					System.out.print("and ");
					System.out.print(winners.get(i).getName());
				}
			}

			System.out.println("!");
			System.out.println("With scores of " + playerScores.get(winners.get(0)));

			System.exit(0);
		}
	}
}

