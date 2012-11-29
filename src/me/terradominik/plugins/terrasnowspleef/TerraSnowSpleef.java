package me.terradominik.plugins.terrasnowspleef;

//Imports
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import me.terradominik.plugins.terrasnowspleef.Listener.BlockBreakListener;
import me.terradominik.plugins.terrasnowspleef.Listener.BlockPlaceListener;
import me.terradominik.plugins.terrasnowspleef.Listener.EntityDamageListener;
import me.terradominik.plugins.terrasnowspleef.Listener.PlayerQuitListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Die Main Klasse von TerraSnowSpleef
 *
 * @author Terradominik / Tauncraft
 * @version 0.1
 */
public class TerraSnowSpleef extends JavaPlugin {

    BlockBreakListener blockBreakListener = new BlockBreakListener(this);
    BlockPlaceListener blockPlaceListener = new BlockPlaceListener(this);
    EntityDamageListener entityDamageListener = new EntityDamageListener(this);
    PlayerQuitListener playerQuitListener = new PlayerQuitListener(this);
    Spiel spiel;
     
    /**
     * Was beim einschalten des Plugins passiert
     */
    @Override
    public void onEnable() {
        final PluginManager pm = getServer().getPluginManager();
        final Commands commands = new Commands(this);
        this.loadConfiguration();
        //Commands werden registriert
        spiel = new Spiel(this);
        this.getCommand("tss").setExecutor(new CommandExecutor() {
            /**
             * wenn das Command eingegeben wird
             */
            @Override
            public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings) {
                if (cs instanceof Player) {
                    Player spieler = (Player) cs;
                    if (strings.length == 0) {
                        TerraSnowSpleef.sendMessage(spieler, "TerraSnowSpleef von Terradominik für Tauncraft");
                    } else {
                        strings[0] = strings[0].toLowerCase();


                        switch (strings[0]) {

                            //short Befehle
                            case "j":
                                if (spieler.hasPermission("terrasnowspleef.spieler")) {
                                    commands.beitreten(spieler);
                                }
                                break;

                            case "h":
                                commands.hilfe(spieler);
                                break;
                                
                            case "l":
                                commands.liste(spieler);
                                break;

                            case "s":
                                if (strings.length > 1) {
                                    commands.statistik(spieler, strings[1]);
                                } else {
                                    commands.statistik(spieler);
                                }
                                break;

                            //deutsche Befehle
                            case "beitreten":
                                if (spieler.hasPermission("terrasnowspleef.spieler")) {
                                    commands.beitreten(spieler);
                                }
                                break;

                            case "hilfe":
                                commands.hilfe(spieler);
                                break;
                                
                            case "liste":
                                commands.liste(spieler);
                                break;

                            case "statistik":
                                if (strings.length > 1) {
                                    commands.statistik(spieler, strings[1]);
                                } else {
                                    commands.statistik(spieler);
                                }
                                break;

                            //englische Befehle
                            case "join":
                                if (spieler.hasPermission("terrasnowspleef.spieler")) {
                                    commands.beitreten(spieler);
                                }
                                break;

                            case "help":
                                commands.hilfe(spieler);
                                break;
                               
                            case "list":
                                commands.liste(spieler);
                                break;

                            case "stats":
                                if (strings.length > 1) {
                                    commands.statistik(spieler, strings[1]);
                                } else {
                                    commands.statistik(spieler);
                                }
                                break;

                            //admincommands
                            case "set":
                            if (spieler.isOp()) {
                                    if (strings != null && strings.length > 0) {
                                        if (strings.length < 3) commands.set(spieler, strings[1].toLowerCase(), "");
                                        else commands.set(spieler, strings[1].toLowerCase(), strings[2]);
                                    }
                                }
                            default:
                                TerraSnowSpleef.sendMessage(spieler, "TerraSnowSpleef von Terradominik für Tauncraft");
                                break;

                        }
                    }
                }
                return true;
            }
        });

        //Listener registration
        pm.registerEvents(this.blockBreakListener, this);
        pm.registerEvents(this.blockPlaceListener, this);
        pm.registerEvents(this.entityDamageListener, this);
        pm.registerEvents(this.playerQuitListener, this);
         
        //Config laden
        Filer.loadConfig();
        //Enabled Message
        System.out.println(this.toString() + " enabled");
    }

    @Override
    public void onDisable() {

        //Saved beide Files
        this.saveConfig();
        try {
            Filer.saveConfig();
        } catch (IOException ex) {
            Logger.getLogger(TerraSnowSpleef.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Disabled Message
        System.out.println(this + " disabled!");
    }

    /**
     * Ladet Config
     */
    public void loadConfiguration() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    /**
     * Zum Ausgeben einer Plugin-Systemmeldung an einen Spieler
     */
    public static void sendMessage(Player spieler, String nachricht) {
        spieler.sendMessage(ChatColor.DARK_AQUA + "[TSS]: " + ChatColor.GRAY + nachricht);
    }

    /**
     * Zum Ausgeben einer Plugin-Systemmeldung an alle
     */
    public static void broadcastMessage(String nachricht) {
        Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "[TSS]: " + ChatColor.GRAY + nachricht);
    }
    
    public Spiel getSpiel() {
        return spiel;
    }
}