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
import ca.mcgill.ecse321.group14.scorepion.model.Shot;
import ca.mcgill.ecse321.group14.scorepion.model.Team;

/**
 * Custom adapter to populate a listview with a list of shots that also specifying whether shots
 * were scored and by who they were performed.
 */
public class AdapterShotsGameListView extends ArrayAdapter<Shot> {

	private final Context context;

	public AdapterShotsGameListView (Context context, List<Shot> listTeamsGiven) {
		super(context, R.layout.layout_team_listview, listTeamsGiven);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// Inflate the individual team row item layout for AlertDialogs.
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		@SuppressLint("ViewHolder") View listitem_team = inflater.inflate(R.layout.layout_team_listview, parent, false);

		String playerName = getItem(position).getPlayer().getName();
		Boolean isScored = getItem(position).getIsScored();
		Boolean onGoal = getItem(position).getOnGoal();

		if (isScored) {
			((TextView) listitem_team.findViewById(R.id.textview_TeamName)).setText(playerName + " shot and scored.");
		}
		else if (onGoal) {
			((TextView) listitem_team.findViewById(R.id.textview_TeamName)).setText(playerName + " shot and was blocked.");
		}
		else {
			((TextView) listitem_team.findViewById(R.id.textview_TeamName)).setText(playerName + " shot and missed.");
		}

		// Return the contact row view.
		return listitem_team;

	}

}