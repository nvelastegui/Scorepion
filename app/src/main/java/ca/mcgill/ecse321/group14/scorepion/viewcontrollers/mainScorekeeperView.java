package ca.mcgill.ecse321.group14.scorepion.viewcontrollers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import ca.mcgill.ecse321.group14.scorepion.R;
import ca.mcgill.ecse321.group14.scorepion.adapters.AdapterPlayersSpinner;
import ca.mcgill.ecse321.group14.scorepion.adapters.AdapterTeamsListView;
import ca.mcgill.ecse321.group14.scorepion.adapters.AdapterTeamsSpinner;
import ca.mcgill.ecse321.group14.scorepion.controllers.alertdialogs;
import ca.mcgill.ecse321.group14.scorepion.controllers.listviewcontroller;
import ca.mcgill.ecse321.group14.scorepion.controllers.livegamefeed;
import ca.mcgill.ecse321.group14.scorepion.datamanipulation.gameanalysistools;
import ca.mcgill.ecse321.group14.scorepion.model.GameManager;
import ca.mcgill.ecse321.group14.scorepion.model.Game;
import ca.mcgill.ecse321.group14.scorepion.model.PenaltyKick;
import ca.mcgill.ecse321.group14.scorepion.model.Player;
import ca.mcgill.ecse321.group14.scorepion.model.PlayerInfraction;
import ca.mcgill.ecse321.group14.scorepion.model.RedCard;
import ca.mcgill.ecse321.group14.scorepion.model.Shot;
import ca.mcgill.ecse321.group14.scorepion.model.Team;
import ca.mcgill.ecse321.group14.scorepion.model.YellowCard;

/**
 * This is the view controller for the scorekeeper view.
 */
public class mainScorekeeperView extends ActionBarActivity implements ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link FragmentPagerAdapter} derivative, which will keep every
	 * loaded fragment in memory. If this becomes too memory intensive, it
	 * may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	AlertDialog currentAlertDialog = null;

	// Variables: LIVE Mode
	private static Game liveGame;

	// Variables: BATCH Mode
	private static String calendarDate;
	private static String startTime;
	private static String endTime;
	private static Team Team1;
	private static Team Team2;
	private static Game batchGame;

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

		setContentView(R.layout.activity_main_scorekeeper_view);

		// Load saved state.
		manager.loadSavedState(getApplicationContext());

		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(
					actionBar.newTab()
							.setText(mSectionsPagerAdapter.getPageTitle(i))
							.setTabListener(this));
		}

		Log.d("debug", "mainScorekeeperView: onCreate ran");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main_scorekeeper_view, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		// TODO: set player/league analysis to update listview on onTabSelected (creatvie already ran... wiht possibly old data)
		mViewPager.setCurrentItem(tab.getPosition());
		Log.d("debug", "mainScorekeeperView: onTabSelected ran.");
	}

	/**
	 * When a tab is unselected, we want to clear all the data entered by the user that has not
	 * been saved (see development diary for why this is so). How we do this depends on the which tab
	 * we are leaving. In the case of the analysis tabs, nothing has to be done because no data is changed.
	 * However, in the case of the live and batch modes, some things need to be done.
	 * @param tab
	 * @param fragmentTransaction
	 */
	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		Log.d("debug", "mainScorekeeperView: onTabUnselected ran.");

		switch (tab.getPosition()) {
			case 0:
				// CASE 0: Unselecting LIVE mode
				// Call data reset
				clearProspectiveLiveGame();
				break;
			case 1:
				// CASE 1: Unselecting BATCH mode.
				// Call data reset.
				clearProspectiveBatchGame();
				// Reset buttons and their variables.
				resetButtons();
				break;
			case 2:
				break;
			case 3:
				break;
		}


	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		Log.d("debug", "mainScorekeeperView: onTabReselected");
		//Toast.makeText(getApplicationContext(), "Teehee! That tickles!", Toast.LENGTH_SHORT).show();
	}

	/**
	 * Resets a prospective batch game. That is: rolls back data to before prospective (batch) game
	 * was added. See development diaries for details on why rollbacks are necessary.
	 * @param button The button that called the method.
	 */
	public void setGameResetFromButton (View button) {
		clearProspectiveBatchGame();
		resetButtons();
	}

	/**
	 * Clears all data related to a prospective batch game in the event of a
	 * view switch without preemptively saving the game.
	 */
	protected void clearProspectiveBatchGame () {

		// Always reset game and teams to null.
		batchGame = null;
		Team1 = null;
		Team2 = null;

		// Erase all data added since last load/save.
		manager.resetToEmpty();
		manager.loadSavedState(getApplicationContext());

	}

	/**
	 * Clears all data related to a prospective live game in the event of a
	 * view switch without preemptively saving the game. Also resets view elements.
	 */
	protected void clearProspectiveLiveGame () {

		// Reset everything.
		liveGame = null;
		changeLiveTeamTextViews("Team 1", "Team 2");
		changeLiveModeTeam1Goals(0);
		changeLiveModeTeam2Goals(0);
		switchToLiveGameInActiveLayout();

		// Clear memory and reload from storage.
		manager.resetToEmpty();
		manager.loadSavedState(getApplicationContext());

	}

	/**
	 * In the event of a log out, clears ALL data that is unsaved (could be from batch
	 * or live mode).
	 */
	protected void clearAllProspectiveData () {
		liveGame = null;
		batchGame = null;
		Team1 = null;
		Team2 = null;
		manager.resetToEmpty();
		manager.loadSavedState(getApplicationContext());
	}

	/**
	 * Called by the logout menu button in the actionbar. This method calls clearAllProspectiveData
	 * to dump anything that is not saved, and it switches to the login activity ("logs out").
	 * @param item Reference to the menu item that calls the method.
	 */
	public void performLogOut(MenuItem item) {
		clearAllProspectiveData();
		startActivity(new Intent(getApplicationContext(), login.class));
		finish();
	}

	/**
	 * Resets GameManager singleton data to template1.
	 * @param item Reference to the menu item that calls the method.
	 */
	public void resetDataToTemplate1(MenuItem item) {
		manager.setTemplate1(getApplicationContext());
		Toast.makeText(getApplicationContext(), "Reset Game Manager to Template 1.", Toast.LENGTH_SHORT).show();
	}

	/**
	 * This is a batch mode related method. This method is called to reset the text on the calendar,
	 * start time, and end time buttons. It also returns the calendarDate, startTime, and endTime
	 * variables to their defaults (null).
	 */
	public void resetButtons () {

		Button btn_setBatchGameDate = (Button) findViewById(R.id.btn_setBatchGameDate);
		btn_setBatchGameDate.setText("Set");
		calendarDate = null;

		Button btn_setBatchGameStartTime = (Button) findViewById(R.id.btn_setBatchGameStartTime);
		btn_setBatchGameStartTime.setText("Set");
		startTime = null;

		Button btn_setBatchEndTime = (Button) findViewById(R.id.btn_setBatchGameEndTime);
		btn_setBatchEndTime.setText("Set");
		endTime = null;

	}

	/**
	 * This method is called by a button in fragment_main_scorekeeper_view_batch.xml. It displays
	 * a dialog allowing the user to select a date for the batch game being input.
	 * @param view Reference to the button that was clicked.
	 */
	public void setBatchGameDate (View view) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "datePickerBatchGameDate");
	}

	/**
	 * This is a custom DialogFragment class that is used to present the user with a
	 * date-specific input format for entering the date of a batch game.
	 */
	public static class DatePickerFragment extends DialogFragment
			implements DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			int monthh = month + 1;
			calendarDate = monthh + "/" + day + "/" + year;
			Button btn_inputDate = (Button) getActivity().findViewById(R.id.btn_setBatchGameDate);
			btn_inputDate.setText(month + "/" + day + "/" + year);
		}
	}

	/**
	 * This method is called by a button in fragment_main_scorekeeper_view_batch.xml.
	 * @param view The reference to the button that was clicked.
	 */
	public void setBatchGameStartTime(View view) {
		DialogFragment newFragment = new TimePickerFragment("timePickerBatchGameStartTime");
		newFragment.show(getFragmentManager(), "timePickerBatchGameStartTime");
	}

	/**
	 * This method is called by a button in fragment_main_scorekeeper_view_batch.xml.
	 * @param view The reference to the button that was clicked.
	 */
	public void setBatchGameEndTime (View view) {
		DialogFragment newFragment = new TimePickerFragment("timePickerBatchGameEndTime");
		newFragment.show(getFragmentManager(), "timePickerBatchGameEndTime");
	}

	/**
	 * This is a custom DialogFragment class that is used to present the user with a
	 * time-specific input format for entering the start and end times of a batch game.
	 * The constructor is modified to require an argument. This is fine since this class
	 * is only ever instantiated from a context where an argument will be given (by a method
	 * called on by a button). The argument specifies whether the start time or the end time
	 * is being selected by the user.
	 */
	@SuppressLint("ValidFragment")
	public static class TimePickerFragment extends DialogFragment
			implements TimePickerDialog.OnTimeSetListener {

		private static String buttonType = "default";

		@SuppressLint("ValidFragment")
		public TimePickerFragment (String type) {
			buttonType = type;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);

			// Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), this, hour, minute,
					DateFormat.is24HourFormat(getActivity()));
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

			switch (buttonType) {
				case "timePickerBatchGameStartTime": {
					startTime = hourOfDay + ":" + minute;
					@SuppressWarnings("ConstantConditions") Button btn_startTime = (Button) getActivity().findViewById(R.id.btn_setBatchGameStartTime);
					CharSequence seq = hourOfDay + ":" + minute;
					btn_startTime.setText(seq);
					break;
				}
				case "timePickerBatchGameEndTime": {
					endTime = hourOfDay + ":" + minute;
					@SuppressWarnings("ConstantConditions") Button btn_startTime = (Button) getActivity().findViewById(R.id.btn_setBatchGameEndTime);
					CharSequence seq = hourOfDay + ":" + minute;
					btn_startTime.setText(seq);
					break;
				}
				default:
					Toast.makeText(getActivity(), "Error. Time not set.", Toast.LENGTH_SHORT).show();
					break;
			}

		}
	}

	/**
	 * General purpose method called by 'set team' buttons in batch game mode. This method displays an
	 * alertdialog to the user prompting them to select a team for either team 1 or team 2, depending on
	 * which button calls the method.
	 * @param button Button that calls method. Contains tag to signal which team to add shot to.
	 */
	public void setBatchGameTeam (View button) {

		// Grab 'final' variable for button type.
		final String teambeingChosen = button.getTag().toString();

		switch (teambeingChosen) {
			case "team1":
				if (Team1 != null) {
					Toast.makeText(getApplicationContext(), "Team 1 is already set to " + Team1.getName() + ". Click reset to enter new details.", Toast.LENGTH_SHORT).show();
					return;
				}
				break;
			case "team2":
				if (Team2 != null) {
					Toast.makeText(getApplicationContext(), "Team 2 is already set to " + Team2.getName() + ". Click reset to enter new details.", Toast.LENGTH_SHORT).show();
					return;
				}
				break;
			default:
				break;
		}

		// Declare new instance of an alert dialog builder
		final AlertDialog.Builder setTeamDialog = new AlertDialog.Builder(this);

		// Set title of dialog box accordingly.
		switch (teambeingChosen) {
			case "team1":
				setTeamDialog.setTitle("Select Team 1");
				break;
			case "team2":
				setTeamDialog.setTitle("Select Team 2");
				break;
			default:
				setTeamDialog.setTitle("Select Team");
				break;
		}

		@SuppressLint("InflateParams") View layout_alertdialog_chooseteam = (this.getLayoutInflater()).inflate(R.layout.layout_alertdialog_chooseteam, null);

		ListView listview_GameTeamSelection = (ListView) layout_alertdialog_chooseteam.findViewById(R.id.listview_GameTeamSelection);
		listview_GameTeamSelection.setAdapter(new AdapterTeamsListView(
				this,
				(GameManager.getInstance().getTeams())
		));

		listview_GameTeamSelection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				// Close the AlertDialog using the global object reference currentAlertDialog. (See assignment of variable below)
				currentAlertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).performClick();

				// Now, gather the required data and send it to sendRequestNewMember
				Team selectedTeam = GameManager.getInstance().getTeam(position);

				switch (teambeingChosen) {
					case "team1":
						Team1 = selectedTeam;
						if (Team2 != null) {
							batchGame = new Game(calendarDate, startTime, endTime, GameManager.getInstance(), Team1, Team2);
						}
						Toast.makeText(getApplicationContext(), "Team 1 set to " + Team1.getName(), Toast.LENGTH_SHORT).show();
						// TODO: Change the labels on screen to the assigned teams.
						break;
					case "team2":
						Team2 = selectedTeam;
						if (Team1 != null) {
							batchGame = new Game(calendarDate, startTime, endTime, GameManager.getInstance(), Team1, Team2);
						}
						Toast.makeText(getApplicationContext(), "Team 2 set to " + Team2.getName(), Toast.LENGTH_SHORT).show();
						break;
					default:
						Toast.makeText(getApplicationContext(), "Error occurred selecting team. Try again.", Toast.LENGTH_LONG).show();
						break;
				}

			}
		});

		// Set the display layout for the AlertDialog
		setTeamDialog.setView(layout_alertdialog_chooseteam);

		// Set up the CANCEL button:
		setTeamDialog.setNegativeButton("Cancel", null);

		AlertDialog built_setTeamDialog = setTeamDialog.create();
		built_setTeamDialog.show();
  		currentAlertDialog = built_setTeamDialog; // Set this AlertDialog as the current one.

	}

	/**
	 * This method launches the 'add a shot' procedure in batch mode.
	 * It creates a shot for either team 1 or team 2, depending on the button that calls it.
	 * @param button Button that calls method. Contains tag to signal which team to add shot to.
	 */
	public void addBatchGameShot (View button) {

		// If either team is not set, then disallow this operation.
		if (Team1 == null || Team2 == null) {
			Toast.makeText(getApplicationContext(), "Please select teams first.", Toast.LENGTH_SHORT).show();
			return;
		}

		final AlertDialog.Builder addShotDialog = new AlertDialog.Builder(this);
		final String shotByTeam = button.getTag().toString();

		// Set title of dialog box accordingly.
		switch (shotByTeam) {
			case "team1":
				addShotDialog.setTitle("Add Shot By Team 1");
				break;
			case "team2":
				addShotDialog.setTitle("Add Shot By Team 2");
				break;
			default:
				addShotDialog.setTitle("Add Shot By Team");
				break;
		}

		@SuppressLint("InflateParams") View layout_alertdialog_addshot = (this.getLayoutInflater()).inflate(R.layout.layout_alertdialog_addshot, null);

		// Set up the dropdown for SCORED.
		final Spinner spinnerScoredTF = (Spinner) layout_alertdialog_addshot.findViewById(R.id.spinner_ScoredTF);
		ArrayAdapter<CharSequence> adapter_spinnerScoredTF = ArrayAdapter.createFromResource(
				getApplicationContext(),
				R.array.array_truefalse, android.R.layout.simple_spinner_item);
		adapter_spinnerScoredTF.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerScoredTF.setAdapter(adapter_spinnerScoredTF);

		// Set up the dropdown for ONGOAL.
		final Spinner spinnerOnGoalTF = (Spinner) layout_alertdialog_addshot.findViewById(R.id.spinner_OnGoalTF);
		ArrayAdapter<CharSequence> adapter_spinnerOnGoalTF = ArrayAdapter.createFromResource(
				getApplicationContext(),
				R.array.array_truefalse, android.R.layout.simple_spinner_item);
		adapter_spinnerOnGoalTF.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerOnGoalTF.setAdapter(adapter_spinnerOnGoalTF);

		// Set up the spinner for Team's players.
		final Spinner spinner_PlayerSelection = (Spinner) layout_alertdialog_addshot.findViewById(R.id.spinner_ShotByPlayerSelection);
		switch (shotByTeam) {
			case "team1":
				spinner_PlayerSelection.setAdapter(new AdapterPlayersSpinner(
						this,
						batchGame.getTeam(0).getPlayers()
				));
				break;
			case "team2":
				spinner_PlayerSelection.setAdapter(new AdapterPlayersSpinner(
						this,
						batchGame.getTeam(1).getPlayers()
				));
				break;
			default:
				Toast.makeText(getApplicationContext(), "Error. Try again.", Toast.LENGTH_SHORT).show();
				return;
		}

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
						Toast.makeText(getApplicationContext(), "Error. Try again.", Toast.LENGTH_SHORT).show();
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
						Toast.makeText(getApplicationContext(), "Error. Try again.", Toast.LENGTH_SHORT).show();
						return;
				}

				Player chosenPlayer = (Player) spinner_PlayerSelection.getSelectedItem();
				if (chosenPlayer == null) {
					Toast.makeText(getApplicationContext(), "Error. No player selected.", Toast.LENGTH_SHORT).show();
					return;
				}

				// If the player scored, add a point to his sumScore
				if (scored) chosenPlayer.setSumGoals(chosenPlayer.getSumGoals() + 1);

				batchGame.addShot(scored, ongoal, chosenPlayer);

				Toast.makeText(getApplicationContext(), "Added shot by " + chosenPlayer.getName() + ".", Toast.LENGTH_SHORT).show();

			}
		});

		// Set up the CANCEL button
		addShotDialog.setNegativeButton("Cancel", null);

		AlertDialog built_addShotDialog = addShotDialog.create();
		built_addShotDialog.show();

	}

	/**
	 * Displays an alertdialog that shows the user the shots that have been added to the batch game already.
	 * @param button Button that calls method. Contains tag to signal which team's shots to display.
	 */
	public void viewBatchGameShots (View button) {

		// If either team is not set, then disallow this operation.
		if (Team1 == null || Team2 == null) {
			Toast.makeText(getApplicationContext(), "Please select teams first.", Toast.LENGTH_SHORT).show();
			return;
		}

		AlertDialog.Builder toShow;
		final String shotsByTeam = button.getTag().toString();

		// Set title of dialog box accordingly.
		switch (shotsByTeam) {
			case "team1":
				List<Shot> shotsByTeam1 = new ArrayList<>(gameanalysistools.getShotsByTeam(batchGame, 0));
				toShow = alertdialogs.getAlertDialogListOfShots(shotsByTeam1, this, this.getLayoutInflater());
				break;
			case "team2":
				List<Shot> shotsByTeam2 = new ArrayList<>(gameanalysistools.getShotsByTeam(batchGame, 1));
				toShow = alertdialogs.getAlertDialogListOfShots(shotsByTeam2, this, this.getLayoutInflater());
				break;
			default:
				toShow = alertdialogs.getAlertDialogListOfShots(batchGame.getShots(), this, this.getLayoutInflater());
				break;
		}

		toShow.create().show();

	}

	/**
	 * This method launches the 'add an infraction' procedure in batch mode.
	 * It creates a shot for either team 1 or team 2, depending on the button that calls it.
	 * @param button Button that calls method. Contains tag to signal which team to add shot to.
	 */
	public void addBatchGameInfraction (View button) {

		// If either team is not set, then disallow this operation.
		if (Team1 == null || Team2 == null) {
			Toast.makeText(getApplicationContext(), "Please select teams first.", Toast.LENGTH_SHORT).show();
			return;
		}

		final AlertDialog.Builder addInfractionDialog = new AlertDialog.Builder(this);

		// Set title of dialog box accordingly.
		addInfractionDialog.setTitle("Add Infraction");

		@SuppressLint("InflateParams") View layout_alertdialog_addinfraction = (this.getLayoutInflater()).inflate(R.layout.layout_alertdialog_addinfraction, null);

		// Grab references to all the checkboxes.
		final CheckBox checkbox_redcard = (CheckBox) layout_alertdialog_addinfraction.findViewById(R.id.checkbox_redcard);
		final CheckBox checkbox_yellowcard = (CheckBox) layout_alertdialog_addinfraction.findViewById(R.id.checkbox_yellowcard);
		final CheckBox checkbox_penaltykick = (CheckBox) layout_alertdialog_addinfraction.findViewById(R.id.checkbox_penaltykick);

		// Set the adapter for the spinner.
		final Spinner spinner_InfractionByPlayerSelection = (Spinner) layout_alertdialog_addinfraction.findViewById(R.id.spinner_InfractionByPlayerSelection);

		// Join the player lists.
		// TODO: figure out how to join the two lists...
		List<Player> playersBothTeams = new LinkedList<Player>();

		for (Player player : batchGame.getTeam(0).getPlayers()) {
			playersBothTeams.add(player);
		}
		for (Player player : batchGame.getTeam(1).getPlayers()) {
			playersBothTeams.add(player);
		}

		// Set the adapter with the joined lists.
		spinner_InfractionByPlayerSelection.setAdapter(new AdapterPlayersSpinner(
				this,
				playersBothTeams
		));

		// Set the display layout for the AlertDialog
		addInfractionDialog.setView(layout_alertdialog_addinfraction);

		addInfractionDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				if ((!checkbox_redcard.isChecked() && !checkbox_yellowcard.isChecked() && !checkbox_penaltykick.isChecked())) {
					Toast.makeText(getApplicationContext(), "Please select at least one infraction.", Toast.LENGTH_SHORT).show();
					return;
				}

				// Here is where we create a new player infraction and add it to the game.

				// Grab reference for chosen player from dropdown menu.
				Player chosenPlayer = (Player) spinner_InfractionByPlayerSelection.getSelectedItem();

				// Declare a new playerInfraction using chosen player.
				// Note that by simply creating this infraction, we have already added it to the game.
				//																				vv add to our prospective game
				//																							vv use our chosen player
				PlayerInfraction prospectiveInfraction = new PlayerInfraction(0, "Infraction", batchGame, chosenPlayer);

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

				Toast.makeText(getApplicationContext(), "Added infraction by " + chosenPlayer.getName() + ".", Toast.LENGTH_SHORT).show();

			}
		});

		addInfractionDialog.setNegativeButton("Cancel", null);

		AlertDialog built_addInfractiontDialog = addInfractionDialog.create();
		built_addInfractiontDialog.show();

	}

	/**
	 * Displays an alertdialog that shows the user the infractions that have been added to the batch game already.
	 * @param button Button that calls method.
	 */
	public void viewBatchGameInfractions (View button) {

		// If either team is not set, then disallow this operation.
		if (Team1 == null || Team2 == null) {
			Toast.makeText(getApplicationContext(), "Please select teams first.", Toast.LENGTH_SHORT).show();
			return;
		}

		AlertDialog.Builder toShow = alertdialogs.getAlertDialogListOfInfractions(
				batchGame.getPlayerInfractions(),
				this,
				this.getLayoutInflater()
		);

		toShow.create().show();

	}

	/**
	 * Saves the current prospective batch game by writing the Game Manager currently
	 * in memory onto storage.
	 * @param button Reference to button that called the method.
	 */
	public void submitBatchGame (View button) {

		// If either team is not set, then disallow this operation.
		// (If both teams are already set, then a game already exists. Games do not require shots
		//	or infractions.)
		if (Team1 == null || Team2 == null) {
			Toast.makeText(getApplicationContext(), "Please select teams first.", Toast.LENGTH_SHORT).show();
			return;
		}

		final AlertDialog.Builder submitBatchGameDialog = new AlertDialog.Builder(this);

		// Set title of dialog box accordingly.
		submitBatchGameDialog.setTitle("Are you sure?");

		submitBatchGameDialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				// Now we have to add the appropriate amounts to each team's rankeable parameters.

				// (1)
				// Save increase in sumGoals for each team
				int goalsTeam0 = gameanalysistools.getTeamsGoalsForGame(batchGame, 0);
				batchGame.getTeam(0).setSumGoals(
						batchGame.getTeam(0).getSumGoals() + goalsTeam0
				);
				int goalsTeam1 = gameanalysistools.getTeamsGoalsForGame(batchGame, 1);
				batchGame.getTeam(1).setSumGoals(
						batchGame.getTeam(1).getSumGoals() + goalsTeam1
				);

				// (2)
				// Save increase in points for each team
				// POINTS: 3 points for wining a match, 0 points for losing, 1 point for a draw
				if (goalsTeam0 == goalsTeam1) {
					// TIE: Increase team counters accordingly.
					batchGame.getTeam(0).setSumPoints(
							batchGame.getTeam(0).getSumPoints() + 1
					);
					batchGame.getTeam(1).setSumPoints(
							batchGame.getTeam(1).getSumPoints() + 1
					);
				}
				else if (goalsTeam0 > goalsTeam1) {
					// Team0 wins.
					batchGame.getTeam(0).setSumPoints(
							batchGame.getTeam(0).getSumPoints() + 3
					);
				}
				else {
					// Team1 wins.
					batchGame.getTeam(1).setSumPoints(
							batchGame.getTeam(1).getSumPoints() + 3
					);

				}

				// (3)
				// Save increase in penalties for each team
				batchGame.getTeam(0).setSumPenalties(
						batchGame.getTeam(0).getSumPenalties() + gameanalysistools.getTeamsPenaltiesForGame(batchGame, 0)
				);
				batchGame.getTeam(1).setSumPenalties(
						batchGame.getTeam(1).getSumPenalties() + gameanalysistools.getTeamsPenaltiesForGame(batchGame, 1)
				);

				// At this point, the game is technically already in the game manager. The last
				// thing to do is to save this game manager - ie. overwrite the last save state.
				manager.saveState(getApplicationContext());

				// Reset batchGame and team variables.
				batchGame = null;
				Team1 = null;
				Team2 = null;

				// Reset buttons and date variables.
				resetButtons();

				// Reset current Game Manager.
				manager.resetToEmpty();

				// Set it to the saved one.
				manager.loadSavedState(getApplicationContext());

			}
		});
		submitBatchGameDialog.setNegativeButton("Cancel", null);

		submitBatchGameDialog.create().show();

	}

	/**
	 * Switches live mode tab to live game active layout by hiding and showing relevant
	 * views.
	 */
	protected void switchToLiveGameActiveLayout () {

		// Set the live-mode inactive button to gone.
		(findViewById(R.id.btn_beginLiveMode)).setVisibility(View.GONE);

		// Set the live-mode active buttons to visible.
		(findViewById(R.id.btn_addGameEvent)).setVisibility(View.VISIBLE);
		(findViewById(R.id.btn_submitLiveGame)).setVisibility(View.VISIBLE);
		(findViewById(R.id.btn_cancelLiveGame)).setVisibility(View.VISIBLE);

		// Put in required elements on the view.
		(findViewById(R.id.layout_livemodeplayingTeams)).setVisibility(View.VISIBLE);
		(findViewById(R.id.layout_livemodescoretime)).setVisibility(View.VISIBLE);
		(findViewById(R.id.layout_divider_livemode_1)).setVisibility(View.VISIBLE);
		(findViewById(R.id.layout_currentLiveGameStats)).setVisibility(View.VISIBLE);

	}

	/**
	 * Switches live mode tab to live game inactive layout by hiding and showing relevant
	 * views.
	 */
	protected void switchToLiveGameInActiveLayout () {

		// Set the live-mode active buttons to gone.
		(findViewById(R.id.btn_addGameEvent)).setVisibility(View.GONE);
		(findViewById(R.id.btn_submitLiveGame)).setVisibility(View.GONE);
		(findViewById(R.id.btn_cancelLiveGame)).setVisibility(View.GONE);

		// Take out required elements on the view.
		(findViewById(R.id.layout_livemodeplayingTeams)).setVisibility(View.GONE);
		(findViewById(R.id.layout_livemodescoretime)).setVisibility(View.GONE);
		(findViewById(R.id.layout_divider_livemode_1)).setVisibility(View.GONE);
		(findViewById(R.id.layout_currentLiveGameStats)).setVisibility(View.GONE);

		// Set the live-mode inactive button to visible.
		(findViewById(R.id.btn_beginLiveMode)).setVisibility(View.VISIBLE);

	}

	/**
	 * Switches the team name texts in the live game active layout to the names of the given teams.
	 * @param team1 left team
	 * @param team2 right team
	 */
	protected void changeLiveTeamTextViews (String team1, String team2) {
		((TextView) findViewById(R.id.textview_playingTeams_Team1)).setText(team1);
		((TextView) findViewById(R.id.textview_playingTeams_Team2)).setText(team2);
	}

	/**
	 * Change the goal counter for team 1 (left) in active live mode.
	 * @param goals What the new text should be.
	 */
	protected void changeLiveModeTeam1Goals (int goals) {
		((TextView) findViewById(R.id.textview_playingTeams_Team1_SCORE)).setText("" + goals);
	}

	/**
	 * Change the goal counter for team 2 (right) in active live mode.
	 * @param goals What the new text should be.
	 */
	protected void changeLiveModeTeam2Goals (int goals) {
		((TextView) findViewById(R.id.textview_playingTeams_Team2_SCORE)).setText("" + goals);
	}

	/**
	 * Analyzes live game data and updates the goals on the screen.
	 */
	public void updateLiveModeGoals () {
		int team1Goals = gameanalysistools.getTeamsGoalsForGame(liveGame, 0);
		changeLiveModeTeam1Goals(team1Goals);

		int team2Goals = gameanalysistools.getTeamsGoalsForGame(liveGame, 1);
		changeLiveModeTeam2Goals(team2Goals);
	}

	/**
	 * This method is launched when a new live game is started. It sets up the listview for
	 * the live game feed.
	 */
	protected void generateLiveGameFeed () {

		// Grab listview reference
		ListView list = (ListView) findViewById(R.id.listview_LiveGameFeed);

		// Grab current time (live game start time).
		Date todayDate = Calendar.getInstance().getTime();
		String liveStartTime = todayDate.getHours() + ":" + todayDate.getMinutes();

		// Need to make sure livegamefeed singleton is cleared.
		livegamefeed.getInstance().clear();

		// Now add the first entry.
		livegamefeed.getInstance().add(liveStartTime + ": Game started.");

		// Finally, set the listview adapter.
		list.setAdapter(new ArrayAdapter<String>(
				this,
				android.R.layout.simple_list_item_1,
				livegamefeed.getInstance().getList()
		));

	}

	/**
	 * This method can be run when a live game is active. It is called after either a goal or an infraction
	 * is added to the game, adn it updates the live game feed to show the new goal or infraction.
	 */
	protected void updateLiveGameFeed () {
		// TODO: Implement this more efficiently (without regenerating entire adapter...).

		// Grab listview reference
		ListView list = (ListView) findViewById(R.id.listview_LiveGameFeed);

		// Set the listview adapter.
		list.setAdapter(new ArrayAdapter<String>(
				this,
				android.R.layout.simple_list_item_1,
				livegamefeed.getInstance().getList()
		));

	}

	/**
	 * Begins a new live game in the live mode tab.
	 * @param button Reference to button that called the method.
	 */
	public void startLiveGame (View button) {

		Log.d("debug", "current number of games in singleton: " + GameManager.getInstance().numberOfGames());

		// Get Today's date and the start time.
		Calendar today = Calendar.getInstance();
		Date todayDate = today.getTime();

		final String liveCalendarDate = todayDate.getMonth() + "/" + todayDate.getDay() + "/" + todayDate.getYear();
		final String liveStartTime = todayDate.getHours() + ":" + todayDate.getMinutes();

		// Push an Alert Dialog for user to select teams...

		AlertDialog.Builder builderChooseTeams = new AlertDialog.Builder(this);
		builderChooseTeams.setTitle("Choose Teams");

		@SuppressLint("InflateParams") View layout_alertdialog_chooseteam_livemode = (this.getLayoutInflater()).inflate(R.layout.layout_alertdialog_chooseteam_livemode, null);

		final Spinner spinner_team1 = (Spinner) layout_alertdialog_chooseteam_livemode.findViewById(R.id.spinner_ChooseTeam1_LiveMode);
		final Spinner spinner_team2 = (Spinner) layout_alertdialog_chooseteam_livemode.findViewById(R.id.spinner_ChooseTeam2_LiveMode);

		AdapterTeamsSpinner sharedAdapter = new AdapterTeamsSpinner(
				this,
				GameManager.getInstance().getTeams()
		);

		spinner_team1.setAdapter(sharedAdapter);
		spinner_team2.setAdapter(sharedAdapter);

		builderChooseTeams.setView(layout_alertdialog_chooseteam_livemode);

		builderChooseTeams.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				// If the teams selected are the same... then we can't make a game!
				if (spinner_team1.getSelectedItem() == spinner_team2.getSelectedItem()) {
					Toast.makeText(getApplicationContext(), "Select two different teams, you must.", Toast.LENGTH_SHORT).show();
					return;
				}

				// Create new game and store it in associated global variable.
				liveGame = new Game(
						liveCalendarDate,
						liveStartTime,
						"Running",
						GameManager.getInstance(),
						(Team) spinner_team1.getSelectedItem(),
						(Team) spinner_team2.getSelectedItem()
				);

				// Switch layout to live game active layout.
				switchToLiveGameActiveLayout();

				// Set teams texts
				changeLiveTeamTextViews(
						liveGame.getTeam(0).getName(),
						liveGame.getTeam(1).getName()
				);

				// Populate live feed for the first time.
				generateLiveGameFeed();

			}
		});

		builderChooseTeams.setNegativeButton("Cancel", null);

		builderChooseTeams.create().show();

	}

	/**
	 * Ends a currently running live game.
	 * @param button Reference to button that called the method.
	 */
	public void endLiveGame (View button) {

		Log.d("debug", "Number of games in memory: " + GameManager.getInstance().numberOfGames());

		final AlertDialog.Builder endLiveGameDialog = new AlertDialog.Builder(this);

		// Set title of dialog box accordingly.
		endLiveGameDialog.setTitle("Are you sure?");

		endLiveGameDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				// Get the end time and set the live game's end time appropriately.
				Date todayDate = Calendar.getInstance().getTime();
				final String liveEndTime = todayDate.getHours() + ":" + todayDate.getMinutes();
				liveGame.setEndTime(liveEndTime);

				// Save the game that has been made so far.

				// (1)
				// Save increase in sumGoals for each team
				int goalsTeam0 = gameanalysistools.getTeamsGoalsForGame(liveGame, 0);
				liveGame.getTeam(0).setSumGoals(
						liveGame.getTeam(0).getSumGoals() + goalsTeam0
				);

				int goalsTeam1 = gameanalysistools.getTeamsGoalsForGame(liveGame, 1);
				liveGame.getTeam(1).setSumGoals(
						liveGame.getTeam(1).getSumGoals() + goalsTeam1
				);

				// (2)
				// Save increase in points for each team
				// POINTS: 3 points for wining a match, 0 points for losing, 1 point for a draw
				if (goalsTeam0 == goalsTeam1) {
					// TIE: Increase team counters accordingly.
					liveGame.getTeam(0).setSumPoints(
							liveGame.getTeam(0).getSumPoints() + 1
					);
					liveGame.getTeam(1).setSumPoints(
							liveGame.getTeam(1).getSumPoints() + 1
					);
				} else if (goalsTeam0 > goalsTeam1) {
					// Team0 wins.
					liveGame.getTeam(0).setSumPoints(
							liveGame.getTeam(0).getSumPoints() + 3
					);
				} else {
					// Team1 wins.
					liveGame.getTeam(1).setSumPoints(
							liveGame.getTeam(1).getSumPoints() + 3
					);

				}

				// (3)
				// Save increase in penalties for each team
				liveGame.getTeam(0).setSumPenalties(
						liveGame.getTeam(0).getSumPenalties() + gameanalysistools.getTeamsPenaltiesForGame(liveGame, 0)
				);
				liveGame.getTeam(1).setSumPenalties(
						liveGame.getTeam(1).getSumPenalties() + gameanalysistools.getTeamsPenaltiesForGame(liveGame, 1)
				);

				// At this point, the game is technically already in the game manager. The last
				// thing to do is to save this game manager - ie. overwrite the last save state.
				manager.saveState(getApplicationContext());

				// Reset all the parameters.
				liveGame = null;

				// Reset the view elements.
				changeLiveTeamTextViews("Team 1", "Team 2");
				changeLiveModeTeam1Goals(0);
				changeLiveModeTeam2Goals(0);

				// Reset the live mode view layout!
				switchToLiveGameInActiveLayout();

				// Clear game manager in memory and load saved one.
				manager.resetToEmpty();
				manager.loadSavedState(getApplicationContext());

				Log.d("debug", "current number of games in singleton: " + GameManager.getInstance().numberOfGames());
			}
		});

		endLiveGameDialog.setNegativeButton("Cancel", null);
		endLiveGameDialog.create().show();

	}

	/**
	 * Cancels the current live game.
	 * @param button Reference to the button that called the method.
	 */
	public void cancelLiveGame (View button) {

		Log.d("debug", "Number of games in memory: " + GameManager.getInstance().numberOfGames());

		// So we have to cancel the game... but it's already added to game manager. So
		// we have to clear the active mode from the screen and reload the last save-state
		// from the XML.

		final AlertDialog.Builder cancelLiveGameDialog = new AlertDialog.Builder(this);

		// Set title of dialog box accordingly.
		cancelLiveGameDialog.setTitle("Are you sure?");

		cancelLiveGameDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Reset everything.
				liveGame = null;
				changeLiveTeamTextViews("Team 1", "Team 2");
				changeLiveModeTeam1Goals(0);
				changeLiveModeTeam2Goals(0);
				switchToLiveGameInActiveLayout();

				// Clear memory and reload from storage.
				manager.resetToEmpty();
				manager.loadSavedState(getApplicationContext());

				Log.d("debug", "Number of games in memory: " + GameManager.getInstance().numberOfGames());
			}
		});

		cancelLiveGameDialog.setNegativeButton("Cancel", null);
		cancelLiveGameDialog.create().show();

	}

	/**
	 * Opens user menu that allows user to select whether he/she wants to add a goal or an infraction
	 * to the current live game.
	 * @param button The button that called the method.
	 */
	public void addLiveGameEvent (View button) {

		AlertDialog.Builder dialogAddLiveGameEvent = new AlertDialog.Builder(this);
		dialogAddLiveGameEvent.setTitle("Add New");

		@SuppressLint("InflateParams") View layout_chooseAddLiveEvent = (this.getLayoutInflater()).inflate(R.layout.layout_alertdialog_chooseteam, null);

		final ListView options = (ListView) layout_chooseAddLiveEvent.findViewById(R.id.listview_GameTeamSelection);

		options.setAdapter(new ArrayAdapter<>(
				this,
				android.R.layout.simple_list_item_1,
				getResources().getStringArray(R.array.array_live_addwhat)));

		dialogAddLiveGameEvent.setView(layout_chooseAddLiveEvent);

		options.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				switch (position) {
					case 0:
						currentAlertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).performClick();
						launchAddShotLiveGame();
						break;
					case 1:
						currentAlertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).performClick();
						launchAddInfractionLiveGame();
						break;
					default:
						currentAlertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).performClick();
						Toast.makeText(getApplicationContext(), "Error.", Toast.LENGTH_SHORT).show();
						break;
				}

			}
		});

		dialogAddLiveGameEvent.setNegativeButton("Cancel", null);

		AlertDialog built_dialogAddLiveGameEvent = dialogAddLiveGameEvent.create();

		currentAlertDialog = built_dialogAddLiveGameEvent;

		built_dialogAddLiveGameEvent.show();

	}

	/**
	 * This method is called from addLiveGameEvent when the user selects to add a shot. It displays
	 * an alertdialog prompting the user to select which team to add the shot for.
	 */
	protected void launchAddShotLiveGame () {

		AlertDialog.Builder dialogAddLiveGameEvent = new AlertDialog.Builder(this);
		dialogAddLiveGameEvent.setTitle("Add Shot By Team");
		@SuppressLint("InflateParams") View layout_chooseAddLiveEvent = (this.getLayoutInflater()).inflate(R.layout.layout_alertdialog_chooseteam, null);
		final ListView options = (ListView) layout_chooseAddLiveEvent.findViewById(R.id.listview_GameTeamSelection);
		options.setAdapter(new ArrayAdapter<>(
				this,
				android.R.layout.simple_list_item_1,
				getResources().getStringArray(R.array.array_live_chooseteam)));
		dialogAddLiveGameEvent.setView(layout_chooseAddLiveEvent);
		options.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				switch (position) {
					case 0:
						currentAlertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).performClick();
						launchAddShotLiveGameWithTeam(0);
						break;
					case 1:
						currentAlertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).performClick();
						launchAddShotLiveGameWithTeam(1);
						break;
					default:
						currentAlertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).performClick();
						Toast.makeText(getApplicationContext(), "Error.", Toast.LENGTH_SHORT).show();
						break;
				}

			}
		});

		dialogAddLiveGameEvent.setNegativeButton("Cancel", null);
		AlertDialog built_dialogAddLiveGameEvent = dialogAddLiveGameEvent.create();
		currentAlertDialog = built_dialogAddLiveGameEvent;
		built_dialogAddLiveGameEvent.show();

	}

	/**
	 * This method is called from launchAddShotLiveGame when the user selects which team to add a
	 * shot for. It displays an alertdialog prompting the user to select/enter parameters relevant
	 * to the shot.
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	protected void launchAddShotLiveGameWithTeam (int Team) {
		AlertDialog.Builder showAddShotLiveGame = alertdialogs.getAlertDialogAddShot(liveGame, Team, this, this.getLayoutInflater());
		showAddShotLiveGame.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				// Update elements on the screen after the dialog is closed.
				updateLiveModeGoals();
				updateLiveGameFeed();
			}
		});
		showAddShotLiveGame.create().show();
	}

	/**
	 * This method is called from addLiveGameEvent when the user select to add an infraction. It displays an
	 * alertdialog where the user enters the details relevant to the infraction.
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	protected void launchAddInfractionLiveGame () {
		AlertDialog.Builder showAddInfractionLiveGame = alertdialogs.getAlertDialogAddInfraction(liveGame, this, this.getLayoutInflater());
		showAddInfractionLiveGame.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				// Update relevant elements on the screen after the dialog is closed.
				updateLiveGameFeed();
			}
		});
		showAddInfractionLiveGame.create().show();
	}

	/**
	 * Automatically created class: A {@link FragmentPagerAdapter} that returns a fragment
	 * corresponding to one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class below).
			return PlaceholderFragment.newInstance(position + 1);
		}

		@Override
		public int getCount() {
			// Show 4 total pages.
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
				case 0:
					return getString(R.string.title_live).toUpperCase(l);
				case 1:
					return getString(R.string.title_batch).toUpperCase(l);
				case 2:
					return getString(R.string.title_player).toUpperCase(l);
				case 3:
					return getString(R.string.title_league).toUpperCase();
			}
			return null;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section
		 * number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		/**
		 * The onCreateView method is overrided to allow modification of the views before they are created.
		 * @param inflater
		 * @param container
		 * @param savedInstanceState
		 * @return
		 */
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
								 Bundle savedInstanceState) {

			View rootView;
			Bundle args = getArguments();

			switch (args.getInt(ARG_SECTION_NUMBER)) {
				// Fragments/Tabs start at 1 not 0 here.
				case 1:
					rootView = inflater.inflate(R.layout.fragment_main_scorekeeper_view_live, container, false);
					break;
				case 2:
					rootView = inflater.inflate(R.layout.fragment_main_scorekeeper_view_batch, container, false);
					break;
				case 3:
					rootView = inflater.inflate(R.layout.fragment_main_scorekeeper_view_player, container, false);
					listviewcontroller.generatePlayerAnalysis(rootView, getActivity());
					break;
				case 4:
					rootView = inflater.inflate(R.layout.fragment_main_scorekeeper_view_league, container, false);
					listviewcontroller.generateLeagueAnalysis(rootView, getActivity());
					break;
				default:
					rootView = inflater.inflate(R.layout.fragment_main_scorekeeper_view_batch, container, false);
					break;
			}

			Log.d("debug", "mainScorekeeprView: onCreateView ran for view: " + args.getInt(ARG_SECTION_NUMBER));
			return rootView;
		}

	}



}
