package ca.mcgill.ecse321.group14.scorepion.datamanipulation;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse321.group14.scorepion.model.Game;
import ca.mcgill.ecse321.group14.scorepion.model.PlayerInfraction;
import ca.mcgill.ecse321.group14.scorepion.model.Team;
import ca.mcgill.ecse321.group14.scorepion.model.Shot;

/**
 * This static class contains methods for calculating various parameters related
 * to a game. Offloaded here for namespace separation and reduction of copy-pasted code.
 */
public class gameanalysistools {

	/**
	 * Given a complete game, returns the winning team.
	 * @param givenGame Game to analyze.
	 * @return A Team object that holds the winning team, or NULL if the game is a draw.
	 */
	public static List<Team> getGameWinningTeam (Game givenGame) {

		Team team1 = givenGame.getTeam(0);
		Team team2 = givenGame.getTeam(1);
		int team1Goals = 0;
		int team2Goals = 0;

		List<Shot> allShots = new ArrayList<>(givenGame.getShots());

		// Cycle through each shot in the game and check which player shot, and whether it
		// was a goal.
		// Could call methods below, but this way only go through list of shots once. Otherwise,
		// would go through twice!
		for (Shot shot : allShots) {
			if (shot.isIsScored()){
				if (team1.getPlayers().contains(shot.getPlayer())) {
					// team1 gets a goal
					team1Goals++;
				}
				else {
					// team2 gets a goal
					team2Goals++;
				}
			}
		}

		List<Team> returnList = new ArrayList<>();
		if (team1Goals > team2Goals) {
			returnList.add(team1);
			returnList.add(team2);
			return returnList;
		}
		else if (team1Goals == team2Goals) {
			return null;
		}
		else {
			returnList.add(team2);
			returnList.add(team1);
			return returnList;
		}

	}

	/**
	 * Given a complete game, returns the total points for a specified team.
	 * @param givenGame The game to evaluate.
	 * @param team The team for which to collect the total points (0 for 'left' team, 1 for 'right' team).
	 * @return The total goals for that team in the given game.
	 */
	public static int getTeamsGoalsForGame (Game givenGame, int team) {

		Team chosenTeam;
		int goals = 0;

		switch (team) {
			case 0:
				chosenTeam = givenGame.getTeam(0);
				break;
			case 1:
				chosenTeam = givenGame.getTeam(1);
				break;
			default:
				chosenTeam = givenGame.getTeam(0);
				break;
		}

		List<Shot> allShots = new ArrayList<>(givenGame.getShots());

		// Cycle through each shot in the game and check which player shot, and whether it
		// was a goal.
		for (Shot shot : allShots) {
			if (shot.isIsScored()){
				if (chosenTeam.getPlayers().contains(shot.getPlayer())) {
					// team1 gets a goal
					goals++;
				}
			}
		}

		return goals;

	}

	/**
	 * Given a complete game, returns the total penalties for a specified team.
	 * @param givenGame The game to evaluate.
	 * @param team The team for which to collect the total points (0 for 'left' team, 1 for 'right' team).
	 * @return The total penalties for that team in the given game.
	 */
	public static int getTeamsPenaltiesForGame (Game givenGame, int team) {

		Team chosenTeam;
		int penalties = 0;

		switch (team) {
			case 0:
				chosenTeam = givenGame.getTeam(0);
				break;
			case 1:
				chosenTeam = givenGame.getTeam(1);
				break;
			default:
				chosenTeam = givenGame.getTeam(0);
				break;
		}

		List<PlayerInfraction> allPenalties = new ArrayList<>(givenGame.getPlayerInfractions());

		// Cycle through each shot in the game and check which player shot, and whether it
		// was a goal.
		for (PlayerInfraction infraction : allPenalties) {
			if (chosenTeam.getPlayers().contains(infraction.getPlayer())) {
				// team1 gets a goal
				penalties++;
			}
		}

		return penalties;

	}

	/**
	 * Given a complete game, returns a list of the shots by a specified team in that game.
	 * @param givenGame The game to evaluate.
	 * @param team The team for which to collect shots. (0 for 'left' team, 1 for 'right' team).
	 * @return A List<Shot> of all shots by the specified team, during the given game.
	 */
	public static List<Shot> getShotsByTeam (Game givenGame, int team) {

		List<Shot> allShots = new ArrayList<>(givenGame.getShots());
		List<Shot> shotsToReturn = new ArrayList<>();

		// Cycle through each shot in the game and check which player shot it.
		for (Shot shot : allShots) {
			// If the player that made the shot is on the team requested, then add
			// the shot to shotsToReturn.
			if (givenGame.getTeam(team).getPlayers().contains(shot.getPlayer())) {
				shotsToReturn.add(shot);
			}
		}

		return shotsToReturn;

	}


}
