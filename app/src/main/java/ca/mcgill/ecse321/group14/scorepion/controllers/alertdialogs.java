package ca.mcgill.ecse321.group14.scorepion.controllers;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import ca.mcgill.ecse321.group14.scorepion.R;
import ca.mcgill.ecse321.group14.scorepion.adapters.AdapterInfractionsGameListView;
import ca.mcgill.ecse321.group14.scorepion.adapters.AdapterPlayersSpinner;
import ca.mcgill.ecse321.group14.scorepion.adapters.AdapterShotsGameListView;
import ca.mcgill.ecse321.group14.scorepion.model.Game;
import ca.mcgill.ecse321.group14.scorepion.model.PenaltyKick;
import ca.mcgill.ecse321.group14.scorepion.model.Player;
import ca.mcgill.ecse321.group14.scorepion.model.PlayerInfraction;
import ca.mcgill.ecse321.group14.scorepion.model.RedCard;
import ca.mcgill.ecse321.group14.scorepion.model.Shot;
import ca.mcgill.ecse321.group14.scorepion.model.YellowCard;

/**
 * This class provides some static methods for generating alert dialogs in live/batch modes. (Side Note:
 * Some work still needs to be done to move more alertDialog generation to this class.)
 */
public class alertdialogs {

	/**
	 * Returns an alertDialog builder for an alert dialog showing a list of Shots.
	 * @param shots Shots to display in the list in the alert dialog.
	 * @param context Activity from which this method is called and where the dialog will be displayed.
	 * @param inflater Inflater from activity.
	 * @return An alertdialog builder that may be (strategically) modified by calling class before being displayed.
	 */
	public static AlertDialog.Builder getAlertDialogListOfShots (List<Shot> shots, Context context, LayoutInflater inflater) {

		// Declare new instance of an alert dialog builder
		final AlertDialog.Builder dialogToReturn = new AlertDialog.Builder(context);
		dialogToReturn.setTitle("Shots");

		@SuppressLint("InflateParams") View layout_alertdialog_chooseteam = inflater.inflate(R.layout.layout_alertdialog_list_shots, null);

		ListView listview_shots = (ListView) layout_alertdialog_chooseteam.findViewById(R.id.listview_shots);
		listview_shots.setAdapter(new AdapterShotsGameListView(
				context,
				shots
		));

		// Set the display layout for the AlertDialog
		dialogToReturn.setView(layout_alertdialog_chooseteam);

		// Set up the CANCEL button:
		dialogToReturn.setNegativeButton("OK", null);

		return dialogToReturn;

	}

	/**
	 * Returns an alertDialog builder for an alert dialog showing a list of infractions.
	 * @param infractions Infractions to display in the list in the alert dialog.
	 * @param context Activity from which this method is called and where the dialog will be displayed.
	 * @param inflater Inflater from activity.
	 * @return An alertdialog builder that may be (strategically) modified by calling class before being displayed.
	 */
	public static AlertDialog.Builder getAlertDialogListOfInfractions (List<PlayerInfraction> infractions, Context context, LayoutInflater inflater) {

		// Declare new instance of an alert dialog builder
		final AlertDialog.Builder dialogToReturn = new AlertDialog.Builder(context);
		dialogToReturn.setTitle("Infractions");

		@SuppressLint("InflateParams") View layout_alertdialog_chooseteam = inflater.inflate(R.layout.layout_alertdialog_list_shots, null);

		ListView listview_shots = (ListView) layout_alertdialog_chooseteam.findViewById(R.id.listview_shots);
		listview_shots.setAdapter(new AdapterInfractionsGameListView(
				context,
				infractions
		));

		// Set the display layout for the AlertDialog
		dialogToReturn.setView(layout_alertdialog_chooseteam);

		// Set up the CANCEL button:
		dialogToReturn.setNegativeButton("OK", null);

		return dialogToReturn;

	}

	/**
	 * Returns an alert dialog builder for an alert dialog allowing user to add a shot to a game by a
	 * player on a specified team.
	 * @param givenGame The game for which the shot will be added.
	 * @param Team The team from which the shot's player will be selected.
	 * @param context The calling activity.
	 * @param inflater The calling activity's inflater.
	 * @return Alert dialog builder that may be strategically modified before being displayed.
	 */
	public static AlertDialog.Builder getAlertDialogAddShot (final Game givenGame, int Team, final Context context, LayoutInflater inflater) {

		// Create alertdialog builder...
		final AlertDialog.Builder addShotDialog = new AlertDialog.Builder(context);

		@SuppressLint("InflateParams") View layout_alertdialog_addshot = inflater.inflate(R.layout.layout_alertdialog_addshot, null);

		// Set up the dropdown for SCORED.
		final Spinner spinnerScoredTF = (Spinner) layout_alertdialog_addshot.findViewById(R.id.spinner_ScoredTF);
		ArrayAdapter<CharSequence> adapter_spinnerScoredTF = ArrayAdapter.createFromResource(
				 context,
				R.array.array_truefalse, android.R.layout.simple_spinner_item);
		adapter_spinnerScoredTF.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerScoredTF.setAdapter(adapter_spinnerScoredTF);

		// Set up the dropdown for ONGOAL.
		final Spinner spinnerOnGoalTF = (Spinner) layout_alertdialog_addshot.findViewById(R.id.spinner_OnGoalTF);
		ArrayAdapter<CharSequence> adapter_spinnerOnGoalTF = ArrayAdapter.createFromResource(
				context,
				R.array.array_truefalse, android.R.layout.simple_spinner_item);
		adapter_spinnerOnGoalTF.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerOnGoalTF.setAdapter(adapter_spinnerOnGoalTF);

		// Set up the spinner for Team's players.
		final Spinner spinner_PlayerSelection = (Spinner) layout_alertdialog_addshot.findViewById(R.id.spinner_ShotByPlayerSelection);

		spinner_PlayerSelection.setAdapter(new AdapterPlayersSpinner(
				context,
				givenGame.getTeam(Team).getPlayers()
		));

		// Set the display layout for the AlertDialog
		addShotDialog.setView(layout_alertdialog_addshot);

		// Set up the ADD button
		addShotDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				// Grab user selections and add to a new shot.
				boolean scored;
				switch (spinnerScoredTF.getSelectedItemPosition()) {
					case 0:
						scored = true;
						break;
					case 1:
						scored = false;
						break;
					default:
						Toast.makeText(context, "Error. Try again.", Toast.LENGTH_SHORT).show();
						return;
				}

				boolean ongoal;
				switch (spinnerOnGoalTF.getSelectedItemPosition()) {
					case 0:
						ongoal = true;
						break;
					case 1:
						ongoal = false;
						break;
					default:
						Toast.makeText(context, "Error. Try again.", Toast.LENGTH_SHORT).show();
						return;
				}

				Player chosenPlayer = (Player) spinner_PlayerSelection.getSelectedItem();
				if (chosenPlayer == null) {
					Toast.makeText(context, "Error. No player selected.", Toast.LENGTH_SHORT).show();
					return;
				}

				// If the player scored, add a point to his sumScore
				if (scored) chosenPlayer.setSumGoals(chosenPlayer.getSumGoals() + 1);

				givenGame.addShot(scored, ongoal, chosenPlayer);

				// Add to live game feed (if in live game mode, will show on screen.)
				Date todayDate = Calendar.getInstance().getTime();
				String currentTime = todayDate.getHours() + ":" + todayDate.getMinutes();
				if (scored) {
					livegamefeed.getInstance().add(currentTime + ": " + chosenPlayer.getName() + " shot and scored.");
				}
				else {
					livegamefeed.getInstance().add(currentTime + ": " + chosenPlayer.getName() + " shot and missed.");
				}

				Toast.makeText(context, "Added shot by " + chosenPlayer.getName() + ".", Toast.LENGTH_SHORT).show();

			}
		});

		// Set up the CANCEL button
		addShotDialog.setNegativeButton("Cancel", null);

		return addShotDialog;

	}

	/**
	 * Returns an alert dialog builder for an alert dialog allowing the user to add an infraction to a given game.
	 * @param givenGame The game for which the infraction will be added.
	 * @param context Calling activity.
	 * @param inflater Calling activity's inflater.
	 * @return Alert dialog builder that may be modified before displaying.
	 */
	public static AlertDialog.Builder getAlertDialogAddInfraction (final Game givenGame, final Context context, LayoutInflater inflater) {

		final AlertDialog.Builder addInfractionDialog = new AlertDialog.Builder(context);

		// Set title of dialog box accordingly.
		addInfractionDialog.setTitle("Add Infraction");

		@SuppressLint("InflateParams") View layout_alertdialog_addinfraction = inflater.inflate(R.layout.layout_alertdialog_addinfraction, null);

		// Grab references to all the checkboxes.
		final CheckBox checkbox_redcard = (CheckBox) layout_alertdialog_addinfraction.findViewById(R.id.checkbox_redcard);
		final CheckBox checkbox_yellowcard = (CheckBox) layout_alertdialog_addinfraction.findViewById(R.id.checkbox_yellowcard);
		final CheckBox checkbox_penaltykick = (CheckBox) layout_alertdialog_addinfraction.findViewById(R.id.checkbox_penaltykick);

		// Set the adapter for the spinner.
		final Spinner spinner_InfractionByPlayerSelection = (Spinner) layout_alertdialog_addinfraction.findViewById(R.id.spinner_InfractionByPlayerSelection);

		// Join the player lists.
		List<Player> playersBothTeams = new LinkedList<Player>();

		for (Player player : givenGame.getTeam(0).getPlayers()) {
			playersBothTeams.add(player);
		}
		for (Player player : givenGame.getTeam(1).getPlayers()) {
			playersBothTeams.add(player);
		}

		// Set the adapter with the joined lists.
		spinner_InfractionByPlayerSelection.setAdapter(new AdapterPlayersSpinner(
				context,
				playersBothTeams
		));

		// Set the display layout for the AlertDialog
		addInfractionDialog.setView(layout_alertdialog_addinfraction);

		addInfractionDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				if ((!checkbox_redcard.isChecked() && !checkbox_yellowcard.isChecked() && !checkbox_penaltykick.isChecked())) {
					Toast.makeText(context, "Please select at least one infraction.", Toast.LENGTH_SHORT).show();
					return;
				}

				// Here is where we create a new player infraction and add it to the game.

				// Grab reference for chosen player from dropdown menu.
				Player chosenPlayer = (Player) spinner_InfractionByPlayerSelection.getSelectedItem();

				// Declare a new playerInfraction using chosen player.
				// Note that by simply creating this infraction, we have already added it to the game.
				//																				vv add to our prospective game
				//																							vv use our chosen player
				PlayerInfraction prospectiveInfraction = new PlayerInfraction(0, "Infraction", givenGame, chosenPlayer);

				// Set the infraction types.
				if (checkbox_redcard.isChecked()) {
					RedCard redcard = new RedCard(prospectiveInfraction);
					prospectiveInfraction.setRedCard(redcard);
				}
				if (checkbox_yellowcard.isChecked()) {
					YellowCard yellowcard = new YellowCard(prospectiveInfraction);
					prospectiveInfraction.setYellowCard(yellowcard);
				}
				if (checkbox_penaltykick.isChecked()) {
					PenaltyKick penaltykick = new PenaltyKick(prospectiveInfraction);
					prospectiveInfraction.setPenaltyKick(penaltykick);
				}

				// Add penalty to player's sum of penalties.
				chosenPlayer.setSumInfractions(chosenPlayer.getSumInfractions() + 1);

				// Update data relevant to live game mode screen.
				Date todayDate = Calendar.getInstance().getTime();
				String currentTime = todayDate.getHours() + ":" + todayDate.getMinutes();
				livegamefeed.getInstance().add(currentTime + ": " + chosenPlayer.getName() + " got an infraction.");

				Toast.makeText(context, "Added infraction by " + chosenPlayer.getName() + ".", Toast.LENGTH_SHORT).show();

			}
		});

		addInfractionDialog.setNegativeButton("Cancel", null);

		return addInfractionDialog;

	}

}
