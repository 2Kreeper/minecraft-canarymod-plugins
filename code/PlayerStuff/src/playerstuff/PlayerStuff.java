/***
 * Excerpted from "Learn to Program with Minecraft Plugins, CanaryMod Edition",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/ahmine2 for more book information.
***/
package playerstuff;

import net.canarymod.plugin.Plugin;
import net.canarymod.logger.Logman;
import net.canarymod.Canary;
import net.canarymod.commandsys.*;
import net.canarymod.api.world.position.Location;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.api.entity.living.humanoid.Player;
import com.pragprog.ahmine.ez.EZPlugin;

public class PlayerStuff extends EZPlugin {

  @Command(aliases = { "playerstuff" },
            description = "Displays information about the player.",
            permissions = { "" },
            toolTip = "/playerstuff")
  public void playerStuffCommand(MessageReceiver caller, String[] parameters) {
      if (caller instanceof Player) { 
        Player me = (Player)caller;
        String msg = "Your display name is " + me.getDisplayName();
        me.chat(msg);
        me.getWorld().setRaining(true);
        me.getWorld().setRainTime(100); // 5 secs
        float exp = me.getExperience();
        int food = me.getHunger();
        float health = me.getHealth();
        Location loc = me.getLocation();
        boolean sleeping = me.isSleeping();
        String sleepMsg = "";
        if (!sleeping) {
          sleepMsg = "not "; //(1)
        }
        me.chat("Your experience points are " + exp);
        me.chat("\tfood is " + food);
        me.chat("\thealth is " + health);
        me.chat("\tyou are at " + printLoc(loc));
        me.chat("\twater falls from the sky ");
        me.chat("and you are " + sleepMsg + "sleeping.");
    }
  }
}
