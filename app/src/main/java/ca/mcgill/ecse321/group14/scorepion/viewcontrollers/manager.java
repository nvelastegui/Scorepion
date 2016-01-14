package ca.mcgill.ecse321.group14.scorepion.viewcontrollers;

import android.content.Context;
import android.util.Log;

import ca.mcgill.ecse321.group14.scorepion.datamanipulation.Persistence;
import ca.mcgill.ecse321.group14.scorepion.model.Game;
import ca.mcgill.ecse321.group14.scorepion.model.GameManager;
import ca.mcgill.ecse321.group14.scorepion.model.Player;
import ca.mcgill.ecse321.group14.scorepion.model.Shot;
import ca.mcgill.ecse321.group14.scorepion.model.Team;

/**
 * This static class provides simple public methods for setting a template game manager, loading
 * the game manager from disk, saving the memory game manager to disk, or clearing the game manager
 * in memory. Major advantage to having this class is reduction of copy-pasted code.
 */
public class manager {

	/**
	 * Writes the Game Manager currently in memory to disk.
	 * @param givenContext
	 */
	public static void saveState (Context givenContext) {
		Persistence.saveState(GameManager.getInstance(), givenContext);
	}

	/**
	 * Loads the Game Manager currently on disk to memory.
	 * @param givenContext
	 */
	public static void loadSavedState (Context givenContext) {
		GameManager.getInstance().setGM((GameManager) Persistence.loadState(givenContext));
	}

	/**
	 * Resets the Game Manager currently in memory to empty.
	 */
	public static void resetToEmpty() {
		(GameManager.getInstance()).delete();
	}

	/**
	 * Sets the Game Manager in memory to a specific template.
	 * @param context The activity from which the method is called.
	 */
	public static void setTemplate1(Context context) {

		GameManager.getInstance().delete();

		GameManager gm = GameManager.getInstance();

		// Team 1
		Team team1 = new Team("Watchmen",10,1,1,2,gm);
		Player newPlayer = new Player("Silk Spectre II", "Forward", 6, 3, gm, team1);
		Player newPlayer2 = new Player("Dr Manhatten", "God", 3, 4, gm, team1);
		Player newPlayer3 = new Player("Ozymandias", "Forward", 0, 2, gm, team1);
		Player newPlayer4 = new Player("The Comedian", "Receiver", 13, 102, gm, team1);
		Player newPlayer5 = new Player("Nite Owl", "Receiver", 5, 1, gm, team1);
		Player newPlayer6 = new Player("Rorshach", "Receiver", 6, 0, gm, team1);

		// Team 2
		Team team2 = new Team("Marvel",20,2,2,4,gm);
		Player newPlayer8 = new Player("Tony Stark", "Forward", 1, 3, gm, team2);
		Player newPlayer10 = new Player("Hulk", "Receiver", 2, 2, gm, team2);
		Player newPlayer11 = new Player("Black Widow", "Receiver", 1, 0, gm, team2);
		Player newPlayer12 = new Player("Thor", "Receiver", 4, 0, gm, team2);
		Player newPlayer13 = new Player("Captain America", "Receiver", 3, 0, gm, team2);
		Player newPlayer14 = new Player("Spiderman", "Receiver", 7, 7, gm, team2);

		// Team 3
		Team team3 = new Team("DC", 1,3,3,5, gm);
		Player newPlayer15 = new Player("Player 1", "unknown", 0, 0, gm, team3);
		Player newPlayer16 = new Player("Player 2", "unknown", 0, 0, gm, team3);
		Player newPlayer17 = new Player("Player 3", "unknown", 0, 0, gm, team3);
		Player newPlayer18 = new Player("Player 4", "unknown", 0, 0, gm, team3);
		Player newPlayer19 = new Player("Player 5", "unknown", 0, 0, gm, team3);
		Player newPlayer20 = new Player("Player 6", "unknown", 0, 0, gm, team3);

		Team team4 = new Team("Germany", 2,3,3,6, gm);
		Player newPlayer22 = new Player("Ron-Robert Zieler", "unknown", 5, 0, gm, team4);
		Player newPlayer23 = new Player("Bernd Leno", "unknown", 1, 0, gm, team4);
		Player newPlayer24 = new Player("Kevin Trapp", "unknown", 3, 0, gm, team4);
		Player newPlayer31 = new Player("Shkodran Mustafi", "unknown", 4, 0, gm, team4);
		Player newPlayer32 = new Player("Sebastian Rudy", "unknown", 3, 0, gm, team4);
		Player newPlayer33 = new Player("Mats Hummels", "unknown", 1, 0, gm, team4);

		Team team5 = new Team("Spain", 4,5,5,3, gm);
		Player newPlayer25 = new Player("Iker Casillas", "unknown", 0, 1, gm, team5);
		Player newPlayer26 = new Player("David de Gea", "unknown", 2, 0, gm, team5);
		Player newPlayer27 = new Player("Sergio Rico", "unknown", 3, 2, gm, team5);
		Player newPlayer28 = new Player("Cesar Azpilicueta", "unknown", 4, 3, gm, team5);
		Player newPlayer29 = new Player("Gerard Piqué", "unknown", 1, 4, gm, team5);
		Player newPlayer30 = new Player("Marc Bartra", "unknown", 0, 0, gm, team5);

		Team team6 = new Team("Italy", 5,6,6,1, gm);
		Team team7 = new Team("Brazil", 5,7,7,8, gm);
		Team team8 = new Team("France", 8,4,4,3, gm);
		Team team9 = new Team("Argentina", 9,8,8,7, gm);
		Team team10 = new Team("Canada", 10,3,1000,9001, gm);

		// Save new game manager.
		saveState(context);

	}

}
