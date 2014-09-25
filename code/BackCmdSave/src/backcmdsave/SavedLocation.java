/***
 * Excerpted from "Learn to Program with Minecraft Plugins, CanaryMod Edition",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/ahmine2 for more book information.
***/
package backcmdsave;

import java.util.ArrayList;
import java.util.HashMap;
import net.canarymod.database.Column;
import net.canarymod.database.Column.DataType;
import net.canarymod.database.DataAccess;
import net.canarymod.database.exceptions.*;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.api.world.position.Location;
import net.canarymod.api.world.World;
import net.canarymod.database.Database;
import net.canarymod.Canary;

public class SavedLocation extends DataAccess {
  
  @Column(columnName = "player_name", dataType = DataType.STRING)
  public String player_name;
  
  @Column(columnName = "location_strings", 
			dataType = DataType.STRING, isList = true)
  public ArrayList<String> location_strings;
    
  private boolean must_insert = false;
  
  public SavedLocation() {
    super("saved_player_locations");
  }
  
  public DataAccess getInstance() { return new SavedLocation();} // Required

  public SavedLocation(String name) {
    super("saved_player_locations");
    player_name = name;
  }
    
  private boolean equalsIsh(Location loc1, Location loc2) {
    return ((int) loc1.getX()) == ((int) loc2.getX()) &&
           ((int) loc1.getZ()) == ((int) loc2.getZ());
  }
 
  private void myRead(final String name) {
    player_name = name;
    
    HashMap<String, Object> search = new HashMap<String, Object>();
    search.put("player_name", name);
    
    try {
      Database.get().load(this, search);
    } catch (DatabaseReadException e) {
      // Not necessarily an error, could be first one
    }
    if (location_strings == null) {
      player_name = name;
      must_insert = true;
      location_strings = new ArrayList<String>();
    }
  }
  
  private void mySave() {
    HashMap<String, Object> search = new HashMap<String, Object>();
    search.put("player_name", player_name);
    if (must_insert) {
      try {
        Database.get().insert(this);    
      } catch (DatabaseWriteException e) {
          //Error, couldn't write!
          System.err.println("Insert failed");
      }      
    } else {
      try {
        Database.get().update(this, search);    
      } catch (DatabaseWriteException e) {
        //Error, couldn't write!
        System.err.println("Update failed");
      }
    }
  }
  
  public void push(Location loc) {
    myRead(player_name);
    String s = locationToString(loc);
    location_strings.add(s);
    mySave();
  }
  
  private Location peek_stack() {
    if (location_strings.isEmpty()) {
      return null;
    }
    String s = location_strings.get(location_strings.size()-1);
    return stringToLocation(s);
  }
  
  private Location pop_stack() {
    Location loc = peek_stack();
    location_strings.remove(location_strings.size()-1);
    return loc;
  }
  
  public Location pop(Location here) {    
    myRead(player_name);
    if (location_strings.size() == 0) {
      return null;
    }
    
    Location loc = peek_stack();  
    while (equalsIsh(loc, here) && location_strings.size() > 1) {
      pop_stack();
      loc = peek_stack();
    }
            
    mySave();
    return loc;
  }
  
  private String locationToString(Location loc) {
    return loc.getX() + "|" +
            loc.getY() + "|" +
            loc.getZ();
  }
    
  private Location stringToLocation(String str) {
    String[] arr = str.split("\\|");
    double x = Double.parseDouble(arr[1]);
    double y = Double.parseDouble(arr[1]);
    double z = Double.parseDouble(arr[1]);
    return new Location(x,y, z);
  }

}

