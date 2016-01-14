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
import ca.mcgill.ecse321.group14.scorepion.model.Team;

/**
 * This is a custom adapter for populating a listview with a List<Team>. It is used in the analysis
 * mode and it provides an orderType parameter for specifying by which parameter the teams are sorted.
 */
public class AdapterTeamsRankingListView extends ArrayAdapter<Team> {

	private final Context context;
	private final int orderType;

	// Default Constructor.
	// Takes context, array of Contacts, and sets the layoutContext to 0 (DEFAULT).
	public AdapterTeamsRankingListView (Context context, List<Team> listTeamsGiven, int orderType) {
		super(context, R.layout.layout_team_ranking_listview, listTeamsGiven);
		this.context = context;
		this.orderType = orderType;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// Inflate the individual team row item layout for AlertDialogs.
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		@SuppressLint("ViewHolder") View listitem_team = inflater.inflate(R.layout.layout_team_ranking_listview, parent, false);

		// Retrieve the team name and set it to the text of the textview in the list row.
		((TextView) listitem_team.findViewById(R.id.textview_teamranking_name)).setText(getItem(position).getName());

		// Set the points..
		switch (orderType) {
			case 0:
				((TextView) listitem_team.findViewById(R.id.textview_teamranking_param)).setText(""+getItem(position).getSumPoints());
				break;
			case 1:
				((TextView) listitem_team.findViewById(R.id.textview_teamranking_param)).setText(""+getItem(position).getSumGoals());
				break;
			case 2:
				((TextView) listitem_team.findViewById(R.id.textview_teamranking_param)).setText(""+getItem(position).getSumPenalties());
				break;
			default:
				((TextView) listitem_team.findViewById(R.id.textview_teamranking_param)).setText(""+getItem(position).getSumPoints());
				break;
		}


		// Return the contact row view.
		return listitem_team;

	}

}