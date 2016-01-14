package ca.mcgill.ecse321.group14.scorepion.datamanipulation;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.thoughtworks.xstream.XStream;

/**
 * This static class provides the application-wide tools for writing and reading current/previous
 * GameManager states with XStream.
 */
public class Persistence {

	private static String fileName = "saveState.xml";

	/**
	 * Public method call for saveStateProtected.
	 */
	public static int saveState (Object objectToSave, Context contextFrom) {
		return saveStateProtected(objectToSave, contextFrom);
	}


	/**
	 * Saves the object given to memory.
	 * @param objectToSave The object that we want to serialize and save.
	 * @param contextFrom The context from which the request is sent (so that the code knows which
	 *                    package is being used to store the saved file).
	 * @return 0 for successful operation / else for unsuccessful operation
	 */
	protected static int saveStateProtected (Object objectToSave, Context contextFrom) {

		try {

			// Serialize the given object
			XStream xStream = new XStream();
			xStream.setMode(XStream.ID_REFERENCES);
			String objectToSaveString = xStream.toXML(objectToSave);

			// Generate reference to file using the given context.
			File fileToWrite = new File(contextFrom.getFilesDir(), fileName);

			// Write serialized object to the file.
			FileWriter toWrite = new FileWriter(fileToWrite, false);
			toWrite.write(objectToSaveString);

			// Close reader.
			toWrite.close();

			Log.d("debug", "PERSISTENCE: Wrote game manager successfully.");
		}
		catch (java.io.IOException e) {
			Log.d("debug", "PERSISTENCE: Write Failed:\n"+e.getMessage());
			e.printStackTrace();
			return 1;
		}

		return 0;
	}

	/**
	 * Public call to loadStateProtected
	 * @param contextFrom
	 * @return
	 */
	public static Object loadState (Context contextFrom) {
		return loadStateProtected(contextFrom);
	}

	/**
	 * Loads the saved object from disk, given a context.
	 * @param contextFrom The context from which the request is sent (so that the code knows which
	 *                    package is being used to find/load the saved file).
	 * @return The object, if found, or null if no object is found.
	 */
	protected static Object loadStateProtected (Context contextFrom) {

		try {

			// Get XStream reference
			XStream xStream = new XStream();
			xStream.setMode(XStream.ID_REFERENCES);

			// Generate reference to file using the context provided.
			File fileToRead = new File(contextFrom.getFilesDir(), fileName);

			// Generate file reader using file
			FileReader toRead = new FileReader(fileToRead);

			// Use XStream to load the XML into an object
			Object objectToLoad = xStream.fromXML(toRead);
			toRead.close();

			Log.d("debug", "PERSISTENCE: Loaded game manager successfully.");

    		return objectToLoad;
		}
		catch (Exception e) {
			Log.d("debug", "PERSISTENCE: Read Failed:\n"+e.getMessage());
			e.printStackTrace();
			return null;
		}

	}


}

