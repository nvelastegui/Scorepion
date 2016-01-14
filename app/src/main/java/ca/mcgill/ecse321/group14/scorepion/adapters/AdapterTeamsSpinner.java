package ca.mcgill.ecse321.group14.scorepion.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ca.mcgill.ecse321.group14.scorepion.R;
import ca.mcgill.ecse321.group14.scorepion.model.Player;
import ca.mcgill.ecse321.group14.scorepion.model.Team;

/**
 * This is a custom adapter for populating the spinner that allows users to select which
 * player has taken a shot.
 */
public class AdapterTeamsSpinner extends ArrayAdapter<Team> {

	private final Context context;

	// Default Constructor.
	public AdapterTeamsSpinner (Context context, List<Team> listTeamsGiven) {
		super(context, R.layout.layout_team_listview, listTeamsGiven);
		this.context = context;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {

		// Inflate the individual team row item layout for AlertDialogs.
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		@SuppressLint("ViewHolder") View listitem_team = inflater.inflate(R.layout.layout_team_listview, parent, false);

		// Retrieve the team name and set it to the text of the textview in the list row.
		((TextView) listitem_team.findViewById(R.id.textview_TeamName)).setText(getItem(position).getName());

		// Return the contact row view.
		return listitem_team;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// Inflate the individual team row item layout for AlertDialogs.
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		@SuppressLint("ViewHolder") View listitem_team = inflater.inflate(R.layout.layout_team_listview, parent, false);

		// Retrieve the team name and set it to the text of the textview in the list row.
		((TextView) listitem_team.findViewById(R.id.textview_TeamName)).setText(getItem(position).getName());

		// Return the contact row view.
		return listitem_team;

	}


}
