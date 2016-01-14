package ca.mcgill.ecse321.group14.scorepion.controllers;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.mcgill.ecse321.group14.scorepion.R;
import ca.mcgill.ecse321.group14.scorepion.adapters.AdapterPlayersRankingListView;
import ca.mcgill.ecse321.group14.scorepion.adapters.AdapterTeamsRankingListView;
import ca.mcgill.ecse321.group14.scorepion.datamanipulation.comparators;
import ca.mcgill.ecse321.group14.scorepion.model.GameManager;
import ca.mcgill.ecse321.group14.scorepion.model.Player;
import ca.mcgill.ecse321.group14.scorepion.model.Team;

/**
 * This class provides general methods for refreshing and generating the list views in
 * the analysis modes. Grouped into a single class to reduce duplicated code.
 */
public class listviewcontroller {

	/**
	 * This function is called to populate the league analysis view.
	 * @param rootView The view which is to be loaded/populated. Must be inflated from fragment_main_scorekeeper_view_league.xml
	 */
	public static void generateLeagueAnalysis(View rootView, final Context context) {

		// >>> ------------------------------------------------------------
		// Set up spinner to choose rank option.

		// Grab references to the spinner and the listview
		Spinner spinnerTopTeamsByOptions = (Spinner) rootView.findViewById(R.id.spinner_topTeamsByOptions);
		final ListView listviewTopTeams = (ListView) rootView.findViewById(R.id.listview_topTeamsBy);
		final ProgressBar progressBarTopTeams = (ProgressBar) rootView.findViewById(R.id.listview_topTeamsBy_progressbar);

		// TODO: Customize colouring of dropdown.

		// Set an array adapter to use the options stored in strings.xml
		ArrayAdapter<CharSequence> adapter_spinnerTopTeamsByOptions = ArrayAdapter.createFromResource(
				context,
				R.array.array_topTeamsByOptions, android.R.layout.simple_gallery_item);

		// Use default view styles...
		adapter_spinnerTopTeamsByOptions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// Apply adapter to spinnerTopTeamsByOptions
		spinnerTopTeamsByOptions.setAdapter(adapter_spinnerTopTeamsByOptions);

		spinnerTopTeamsByOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
					case 0:
					case 1:
					case 2:
						refreshTopTeamsListView(listviewTopTeams, progressBarTopTeams, position, context);
						break;
					default:
						Toast.makeText(context, "Error. Try again.", Toast.LENGTH_SHORT).show();
						break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		// >>> ------------------------------------------------------------
		// Set up default ranking for list view teams.
		refreshTopTeamsListView(listviewTopTeams, progressBarTopTeams, 0, context);

	}

	/**
	 * This method refreshes the list view for ranked teams.
	 * @param listViewReference Reference to the listview.
	 * @param progressBar Reference to the progress bar that signals that the listview is loading.
	 * @param orderBy Parameter to signal how to order items.
	 */
	public static void refreshTopTeamsListView (ListView listViewReference, ProgressBar progressBar, int orderBy, Context context) {

		// First grab a copy of all the teams.
		List<Team> allTeams = new ArrayList<>(GameManager.getInstance().getTeams());

		// Notify user that data is loading.
		progressBar.setVisibility(View.VISIBLE);

		// Sort Data
		switch (orderBy) {
			case 0:
				Collections.sort(allTeams, comparators.getComparatorTotalPoints());
				break;
			case 1:
				Collections.sort(allTeams, comparators.getComparatorTotalGoals());
				break;
			case 2:
				Collections.sort(allTeams, comparators.getComparatorTotalPenalties());
				break;
			default:
				Collections.sort(allTeams, comparators.getComparatorTotalPoints());
				break;
		}

		listViewReference.setAdapter(new AdapterTeamsRankingListView(
				context,
				allTeams,
				orderBy
		));

		// Remove progress bar
		progressBar.setVisibility(View.GONE);

	}

	/**
	 * This function is called to populate the player analysis view.
	 * @param rootView The view which is to be loaded/populated. Must be inflated from fragment_main_scorekeeper_view_league.xml
	 */
	public static void generatePlayerAnalysis(View rootView, final Context context) {

		// Grab references to spinner/listview/progressbar in view.
		final Spinner spinnerTopPlayersByOptions = (Spinner) rootView.findViewById(R.id.spinner_TopPlayersByOptions);
		final ListView listviewTopPlayers = (ListView) rootView.findViewById(R.id.listview_topPlayers);
		final ProgressBar listviewTopPlayersBy_progressbar = (ProgressBar) rootView.findViewById(R.id.listview_topPlayersBy_progressbar);

		// Set an array adapter to use the options stored in strings.xml
		ArrayAdapter<CharSequence> adapter_spinnerTopPlayersByOptions = ArrayAdapter.createFromResource(
				context,
				R.array.array_topPlayersByOptions, android.R.layout.simple_gallery_item);

		// Use default view styles...
		adapter_spinnerTopPlayersByOptions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// Apply adapter to spinnerTopTeamsByOptions
		spinnerTopPlayersByOptions.setAdapter(adapter_spinnerTopPlayersByOptions);

		spinnerTopPlayersByOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
					case 0:
					case 1:
						refreshTopPlayersListView(listviewTopPlayers, listviewTopPlayersBy_progressbar, position, context);
						break;
					default:
						Toast.makeText(context, "Error. Try again.", Toast.LENGTH_SHORT).show();
						break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		// >>> ------------------------------------------------------------
		// Set up default ranking for list view players.
		refreshTopTeamsListView(listviewTopPlayers, listviewTopPlayersBy_progressbar, 0, context);

	}

	/**
	 * This method refreshes the list view for ranked players.
	 * @param listViewReference Reference to the listview.
	 * @param progressBar Reference to the progress bar that signals that the listview is loading.
	 * @param orderBy Parameter to signal how to order items.
	 */
	public static void refreshTopPlayersListView (ListView listViewReference, ProgressBar progressBar, int orderBy, Context context) {

		// First, grab a list of all players in the league.
		List<Player> allPlayers = new ArrayList<>(GameManager.getInstance().getPlayers());

		// Set the progressBar to refreshing...
		progressBar.setVisibility(View.VISIBLE);

		// Sort the list of players based on orderBy.
		switch (orderBy) {
			case 0:
				Collections.sort(allPlayers, comparators.getComparatorTotalScore());
				break;
			case 1:
				Collections.sort(allPlayers, comparators.getComparatorTotalPenaltiesPlayer());
				break;
			default:
				Collections.sort(allPlayers, comparators.getComparatorTotalScore());
				break;
		}

		// With list sorted, set the adapter for the listview.
		listViewReference.setAdapter(new AdapterPlayersRankingListView(
				context,
				allPlayers,
				orderBy
		));

		// Remove progress bar.
		progressBar.setVisibility(View.GONE);

	}


}
