package ca.mcgill.ecse321.group14.scorepion.datamanipulation;

import java.util.Comparator;

import ca.mcgill.ecse321.group14.scorepion.model.PenaltyKick;
import ca.mcgill.ecse321.group14.scorepion.model.Player;
import ca.mcgill.ecse321.group14.scorepion.model.Team;

/**
 * This class provides comparators for various data structures in the model. These comparators
 * are used for sorting in the generation of lists for the player and league analysis modes.
 * (List<Team>, List<Player>, etc.)
 */
public class comparators {

	/**
	 * For sorting teams by total points.
	 */
	public static Comparator<Team> getComparatorTotalPoints () {
		return new Comparator<Team>(){
			@Override
			public int compare(final Team team1, final Team team2){
				// return >0 if team1 has more points.
				// return 0 if team1 and team2 have same number of points
				// return <0 if team2 has more points.
				return (team1.getSumPoints() - team2.getSumPoints())*(-1);
				// multiple by -1 so that descending order comes out.
			}
		};
	}

	/**
	 * For sorting teams by total goals.
	 */
	public static Comparator<Team> getComparatorTotalGoals () {
		return new Comparator<Team>() {
			@Override
			public int compare(Team team1, Team team2) {
				// return >0 if team1 has more goals.
				// return 0 if team1 and team2 have same number of goals
				// return <0 if team2 has more goals.
				return (team1.getSumGoals() - team2.getSumGoals())*(-1);
				// multiple by -1 so that descending order comes out.
			}
		};
	}

	/**
	 * For sorting teams by total penalties.
	 */
	public static Comparator<Team> getComparatorTotalPenalties () {
		return new Comparator<Team>() {
			@Override
			public int compare(Team team1, Team team2) {
				// return >0 if team1 has more penalties.
				// return 0 if team1 and team2 have same number of penalties
				// return <0 if team2 has more penalties.
				return (team1.getSumPenalties() - team2.getSumPenalties())*(-1);
				// multiple by -1 so that descending order comes out.
			}
		};
	}

	/**
	 * For sorting players by total score.
	 */
	public static Comparator<Player> getComparatorTotalScore () {
		return new Comparator<Player>() {
			@Override
			public int compare(Player player1, Player player2) {
				// return >0 if player1 has more goals.
				// return 0 if player1 and player2 have same number of goals
				// return <0 if player2 has more goals.
				return (player1.getSumGoals() - player2.getSumGoals())*(-1);
				// multiply by -1 so that descending order comes out.
			}
		};
	}

	/**
	 * For sorting players by total penalties.
	 */
	public static Comparator<Player> getComparatorTotalPenaltiesPlayer () {
		return new Comparator<Player>() {
			@Override
			public int compare(Player player1, Player player2) {
				// return >0 if player1 has more goals.
				// return 0 if player1 and player2 have same number of goals
				// return <0 if player2 has more goals.
				return (player1.getSumInfractions() - player2.getSumInfractions())*(-1);
				// multiply by -1 so that descending order comes out.
			}
		};
	}

}
