package ca.mcgill.ecse321.group14.scorepion.viewcontrollers;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ca.mcgill.ecse321.group14.scorepion.R;

/**
 * This is the view controller for the login view. It contains slight modifications from the
 * original automatically-generated version.
 */
public class login extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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


	/**
	 * This method launches the dialog box for the user to sign in as a scorekeeper.
	 * @param view Button pressed to activate method.
	 */
	public void launchLoginAsScorekeeper(View view) {
		// Generate an alert dialog for signing in as a Scorekeeper and display it.
		// TODO: Customize the "setPositiveButton" with another method.
		AlertDialog.Builder alertDialogLogin = generateLoginAlertDialog(0);
		alertDialogLogin.show();
	}

	/**
	 * This method launches the dialog box for the user to sign in as a fan.
	 * @param view Button pressed to activate method.
	 */
	public void launchLoginAsFan (View view) {
		// Generate an alert dialog for signing in as a Fan and display it.
		// TODO: Customize the "setPositiveButton" with another method.
		AlertDialog.Builder alertDialogLogin = generateLoginAlertDialog(1);
		alertDialogLogin.show();
	}

	/**
	 * This method generate a generic login alert dialog.
	 * @param type Determines heading for dialog (Scorekeeper, Fan, Generic).
	 * @return Generic login alert dialog.
	 */
	private AlertDialog.Builder generateLoginAlertDialog (final int type) {
		AlertDialog.Builder signInWindow = new AlertDialog.Builder(this);
		switch (type) {
			case 0:
				signInWindow.setTitle("Enter Scorekeeper ID.");
				break;
			case 1:
				signInWindow.setTitle("Enter Fan ID.");
				break;
			default:
				signInWindow.setTitle("Enter ID.");
				break;
		}

		LayoutInflater inflater = this.getLayoutInflater();
		@SuppressLint("InflateParams") View layout_alertdialog_login = inflater.inflate(R.layout.layout_alertdialog_login, null);
		signInWindow.setView(layout_alertdialog_login);

		final EditText editTextID = (EditText) layout_alertdialog_login.findViewById(R.id.editTextID);

		signInWindow.setPositiveButton("Log In", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String input = "ID: " + editTextID.getText().toString();

				if (type == 0) {
					startActivity(new Intent(getApplicationContext(), mainScorekeeperView.class));
					finish();

				}
				else if (type == 1) {
					startActivity(new Intent(getApplicationContext(), mainFanView.class));
					finish();
				}
				else {
					Toast.makeText(getApplicationContext(), "Error. Restart application.", Toast.LENGTH_LONG).show();
				}

			}
		});
		// If the user clicks Cancel, the dialog just closes.
		signInWindow.setNegativeButton("Cancel", null);

		return signInWindow;
	}

}
