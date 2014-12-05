import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.media.*;
import javax.microedition.io.file.*;
import javax.microedition.io.*;
import java.io.*;
import java.util.*;

public class music extends MIDlet implements PlayerListener, CommandListener {
  Form form;
  String fileName;
  Enumeration e;
  Random r = new Random();
  int i, j, fileCount = 590, playerNumber = 1;
  String[] FileNameArray = new String[fileCount];
  FileConnection dir, fc;
  Player player, player2;
  boolean started = true, paused = false;
  int debug = 0;
  
  // command buttons
  Command play_pause = new Command("Play/Pause", Command.ITEM, 1);
  Command next = new Command("Next", Command.ITEM, 1);
  
  // when the app first starts up
  public void startApp() {
      // boolean for when the app first starts up or resuming
      // true initially, set to false to prevent it from running when resuming from paused
      if(started) {
	  // form name
	  form = new Form("JP's Music Player");
	  // adds buttons
	  form.addCommand(play_pause);
	  form.addCommand(next);
	  // makes commands work
	  form.setCommandListener(this);
	  // sets form to display
	  Display.getDisplay(this).setCurrent(form);
	  // sets to false so startup only runs once
	  started = false;
	  getFolder();
	  mainLoop();
      }
  }
  
  // adds file names from the folder to the array
  public void getFolder() {
	try {
		// gets the folder with the music in it
		dir = (FileConnection) Connector.open("file:///MemoryCard/MUSIC/Pony/");
		// gets a listing of the files
		e = dir.list();
		i = 0;
		// itterates through the folder until the end
		while(e.hasMoreElements()) {
			// sets the name into the array
			FileNameArray[i] = (String) e.nextElement();
			i++;
		}
		// closes the resource or else there is a resource leak
		dir.close();
	} catch(Exception e) {
		form.append("Error opening folder");
	}
  }
  
  // the main loop that is executed every time a new song is queued up
  public void mainLoop() {
	try {
		// generate a random number so that we can get different songs to play
		j = r.nextInt(fileCount);
		// gets the name from the array
		fileName = FileNameArray[j];
		// adds the name to the form so we know what song is playing
		form.append("currently playing: " + fileName);
		// gets the file connection to the song selected
		fc = (FileConnection) Connector.open("file:///MemoryCard/MUSIC/Pony/" + fileName);
		if(playerNumber == 1) {
		    // creates a music player instance
		    player = Manager.createPlayer(fc.openInputStream(), "audio/mpeg");
		    // starts the music player
		    player.start();
		    // adds a listener
		    player.addPlayerListener(this);
		    
		    player2.close();
		}
		else {
		    // creates a music player instance
		    player2 = Manager.createPlayer(fc.openInputStream(), "audio/mpeg");
		    // starts the music player
		    player2.start();
		    // adds a listener
		    player2.addPlayerListener(this);
		    
		    player.close();
		}
		// closes the resource or else there is a resource leak
		fc.close();
		debug++;
	} catch(Exception e) {
		form.append("Debug int: " + debug);
	}
  }
  
  // this method is ran every time their is an update with the music player
  public void playerUpdate(Player player, String event, Object data) {
      // checks if the music has hit the end of the track
      if(event == PlayerListener.END_OF_MEDIA) {
	  try {
	    // closes the music player
	    // if this isnt done, then there is a resource leak that crashes
	    // the program and prevents new songs from being queued
	    if(playerNumber == 1)
		playerNumber = 2;
	    else
		playerNumber = 1;
	    // reruns the main loop to get a new song
	    mainLoop();
	  } catch(Exception e) {
	    form.append("Error in update");
	  }
      }
  }
  
  // this method is run every time one of the command buttons is hit
  public void commandAction(Command c, Displayable d) {
      // if the play_pause was hit
      if(c == play_pause) {
	  try {
	    // checks if the song was paused
	    if(paused) {
		// plays the song
		if(playerNumber == 1)
		    player.start();
		else
		    player2.start();
		// sets paused to false so that it will stop it next time
		paused = false;
	    }
	    // if the pause was playing
	    else {
		// stops the player
		if(playerNumber == 1)
		    player.stop();
		else
		    player2.stop();
		// sets paused to true so that it will play it next time
		paused = true;
	    }
	  } catch(Exception e) {
	    form.append("Could not play or pause");
	  }
      }
      // if the next button was hit
      if(c == next) {
	  try {
	    if(playerNumber == 1)
		playerNumber = 2;
	    else
		playerNumber = 1;
	    // reruns the main loop to get a new song
	    mainLoop();
	  } catch(Exception e) {
	    form.append("Could not play next");
	  }
      }
  }
  
  public int getTotalFiles() {
      int total = 0;
      while(e.hasMoreElements())
	  total++;
      return total;
  }
  
  public void pauseApp() {
      
  }

  public void destroyApp(boolean unconditional) {

  }

}