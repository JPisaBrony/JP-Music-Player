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
  int i, j, fileCount = 590;
  String[] FileNameArray = new String[fileCount];
  FileConnection dir, fc;
  Player player;
  boolean started = true, paused = false;
  int debug = 0;
  
  Command play_pause = new Command("Play/Pause", Command.ITEM, 1);
  Command next = new Command("Next", Command.ITEM, 1);
  
  public void startApp() {
      if(started) {
	  form = new Form("JP's Music Player");
	  form.addCommand(play_pause);
	  form.addCommand(next);
	  form.setCommandListener(this);
	  Display.getDisplay(this).setCurrent(form);
	  started = false;
	  getFolder();
	  mainLoop();
      }
  }
  
  public void getFolder() {
	try {
		dir = (FileConnection) Connector.open("file:///MemoryCard/MUSIC/Pony/");
		e = dir.list();
		i = 0;
		while(e.hasMoreElements()) {
			FileNameArray[i] = (String) e.nextElement();
			i++;
		}
		dir.close();
	} catch(Exception e) {
		form.append("Error opening folder");
	}
  }
  
  public void mainLoop() {
	try {
		j = r.nextInt(fileCount);
		fileName = FileNameArray[j];
		form.append("currently playing: " + fileName);
		fc = (FileConnection) Connector.open("file:///MemoryCard/MUSIC/Pony/" + fileName);
		player = Manager.createPlayer(fc.openInputStream(), "audio/mpeg");
		debug++;
		player.start();
		debug++;
		player.addPlayerListener(this);
		fc.close();
		debug++;
	} catch(Exception e) {
		form.append("Debug int: " + debug);
	}
  }
  
  public void playerUpdate(Player player, String event, Object data) {
      if(event == PlayerListener.END_OF_MEDIA) {
	  try {
	    player.close();
	    mainLoop();
	  } catch(Exception e) {
	    form.append("Error in update");
	  }
      }
  }
  
  public void commandAction(Command c, Displayable d) {
      if(c == play_pause) {
	  try {
	    if(paused) {
		player.start();
		paused = false;
	    }
	    else {
		player.stop();
		paused = true;
	    }
	  } catch(Exception e) {
	    form.append("Could not play or pause");
	  }
      }
      if(c == next) {
	  try {
	    player.close();
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