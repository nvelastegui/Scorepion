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
import ca.mcgill.ecse321.group14.scorepion.model.PlayerInfraction;
import ca.mcgill.ecse321.group14.scorepion.model.Shot;
import ca.mcgill.ecse321.group14.scorepion.model.Team;

/**
 * Custom adapter for displaying a list of infractions in a listview.
 */
public class AdapterInfractionsGameListView extends ArrayAdapter<PlayerInfraction> {

	private final Context context;

	public AdapterInfractionsGameListView (Context context, List<PlayerInfraction> listPlayerInfractionsGiven) {
		super(context, R.layout.layout_team_listview, listPlayerInfractionsGiven);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// Inflate the individual team row item layout for AlertDialogs.
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		@SuppressLint("ViewHolder") View listitem_team = inflater.inflate(R.layout.layout_team_listview, parent, false);

		String playerName = getItem(position).getPlayer().getName();
		Boolean hasRedCard = getItem(position).hasRedCard();
		Boolean hasYellowCard = getItem(position).hasYellowCard();
		Boolean hasPenaltyKick = getItem(position).hasPenaltyKick();
		String infractions = "";

		if (hasRedCard) infractions = infractions + "a red card, ";
		if (hasYellowCard) infractions = infractions + "a yellow card, ";
		if (hasPenaltyKick) infractions = infractions + "and a penalty kick";

		((TextView) listitem_team.findViewById(R.id.textview_TeamName)).setText(playerName + " received " + infractions + ".");

		// Return the contact row view.
		return listitem_team;

	}

}