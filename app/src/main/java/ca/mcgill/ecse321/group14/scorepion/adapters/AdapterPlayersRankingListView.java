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

/**
 * This is a custom adapter for populating a listview with a List<Player>. It is used in the analysis
 * mode and it provides an orderType parameter for specifying by which parameter the players are sorted.
 */
public class AdapterPlayersRankingListView extends ArrayAdapter<Player> {

	private final Context context;
	private final int orderType;

	// Default Constructor.
	// Takes context, array of Contacts, and sets the layoutContext to 0 (DEFAULT).
	public AdapterPlayersRankingListView (Context context, List<Player> listPlayersGiven, int orderType) {
		super(context, R.layout.layout_player_ranking_listview, listPlayersGiven);
		this.context = context;
		this.orderType = orderType;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		@SuppressLint("ViewHolder") View listitem_player = inflater.inflate(R.layout.layout_player_ranking_listview, parent, false);

		((TextView) listitem_player.findViewById(R.id.textview_playerranking_name)).setText(getItem(position).getName());

		switch (orderType) {
			case 0:
				((TextView) listitem_player.findViewById(R.id.textview_playerranking_param)).setText("" + getItem(position).getSumGoals());
				break;
			case 1:
				((TextView) listitem_player.findViewById(R.id.textview_playerranking_param)).setText(""+getItem(position).getSumInfractions());
				break;
			default:
				((TextView) listitem_player.findViewById(R.id.textview_playerranking_param)).setText(""+getItem(position).getSumGoals());
				break;
		}

		return listitem_player;

	}

}