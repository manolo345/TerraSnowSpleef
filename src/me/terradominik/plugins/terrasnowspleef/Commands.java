package me.terradominik.plugins.terrasnowspleef;

import java.util.Iterator;
import me.terradominik.plugins.terraworld.TerraWorld;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * die Command Klasse von TerraSnowSpleef hier werden alle Commands bearbeitet
 *
 * @author Terradominik
 */
public class Commands {

    private TerraSnowSpleef plugin;

    /**
     * der Konstruktor von "Commands"
     *
     * @param plugin
     */
    public Commands(TerraSnowSpleef plugin) {
        this.plugin = plugin;
    }

    /**
     * lässt den Spieler beitreten
     *
     * @param spieler
     */
    public void beitreten(Player spieler) {

        //Promote
        if (!spieler.hasPermission("terraworld.spieler")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user " + spieler.getName() + " group set spiele");
        }

        Spiel spiel = plugin.getSpiel();
        if (!plugin.getConfig().getBoolean("event.ist")) {
            if (!spiel.getJoinCountdown()) {
                if (!spiel.getSpiel() && !spiel.getStartCountdown()) {
                    spiel = new Spiel(plugin);
                    spiel.starteJoinCountdown();
                    this.addSpieler(spieler);
                } else {
                    plugin.sendMessage(spieler, "Das Spiel hat leider schon begonnen :(");
                }
            } else {
                this.addSpieler(spieler);
            }
        } else {
            plugin.sendMessage(spieler, "Bitte melde dich noch schnell bei einem Admin falls du noch nicht eingetragen bist");
        }
    }

    public void addSpieler(Player spieler) {
        if (plugin.getSpiel().getSpielerSet().add(spieler.getName())) {
            plugin.broadcastMessage(ChatColor.GOLD + spieler.getName() + ChatColor.GRAY + " hat SnowSpleef betreten");
            TerraWorld.addSpieler(spieler);
            String tabname = ChatColor.WHITE + "SS " + ChatColor.AQUA + spieler.getName();
            if (tabname.length() > 16) {
                spieler.setPlayerListName(tabname.substring(0, 14) + "..");
            } else {
                spieler.setPlayerListName(tabname);
            }
        }
        if (Filer.getConfig().getString(spieler.getName() + ".GespielteRunden") == null) {
            Filer.getConfig().set(spieler.getName() + ".GespielteRunden", 0);
        }
        if (Filer.getConfig().getString(spieler.getName() + ".GewonneneRunden") == null) {
            Filer.getConfig().set(spieler.getName() + ".GewonneneRunden", 0);
        }

    }

    /**
     * das hilfe Command
     *
     * @param spieler
     */
    public void hilfe(Player spieler) {
        spieler.sendMessage(ChatColor.DARK_AQUA + "[TSS]: " + ChatColor.GRAY + "Folgende Commands stehen dir zur Verfügung:");
        spieler.sendMessage(ChatColor.GOLD + "  /tss beitreten " + ChatColor.GRAY + "Lässt dich das Spiel beitreten");
        spieler.sendMessage(ChatColor.GOLD + "  /tss statistik [Spieler] " + ChatColor.GRAY + "Ruft eine Statistik des Spielers auf");
        spieler.sendMessage(ChatColor.GOLD + "  /tss hilfe " + ChatColor.GRAY + "Um diese Hilfe anzuzeigen");
    }

    /**
     * ruft die Statistik des Spielers auf
     *
     * @param spieler
     */
    public void statistik(Player spieler) {
        String target = spieler.getName();

        int z1, z2;
        if (Filer.getConfig().getString(target + ".GewonneneRunden") != null) {
            z1 = Integer.parseInt(Filer.getConfig().getString(target + ".GewonneneRunden"));
        } else {
            z1 = 0;
        }
        if (Filer.getConfig().getString(target + ".GespielteRunden") != null) {
            z2 = Integer.parseInt(Filer.getConfig().getString(target + ".GespielteRunden"));
        } else {
            z2 = 0;
        }

        spieler.sendMessage(ChatColor.DARK_AQUA + "[TSS]: " + ChatColor.GOLD + target + ChatColor.GRAY + ": ");
        spieler.sendMessage(ChatColor.GRAY + "  Gespielte Runden: " + ChatColor.GOLD + z1);
        spieler.sendMessage(ChatColor.GRAY + "  Gewonnene Runden: " + ChatColor.GOLD + z2);
    }

    /**
     * ruft die Statistik eines anderen Spielers auf
     *
     * @param spieler
     * @param target
     */
    public void statistik(Player spieler, String target) {
        if (plugin.getServer().getPlayer(target) != null) {
            target = plugin.getServer().getPlayer(target).getName();
        } else {
            target = plugin.getServer().getOfflinePlayer(target).getName();
        }

        int z1, z2;
        if (Filer.getConfig().getString(target + ".GewonneneRunden") != null) {
            z1 = Integer.parseInt(Filer.getConfig().getString(target + ".GewonneneRunden"));
        } else {
            z1 = 0;
        }
        if (Filer.getConfig().getString(target + ".GespielteRunden") != null) {
            z2 = Integer.parseInt(Filer.getConfig().getString(target + ".GespielteRunden"));
        } else {
            z2 = 0;
        }

        spieler.sendMessage(ChatColor.DARK_AQUA + "[TSS]: " + ChatColor.GOLD + target + ChatColor.GRAY + ": ");
        spieler.sendMessage(ChatColor.GRAY + "  Gespielte Runden: " + ChatColor.GOLD + z1);
        spieler.sendMessage(ChatColor.GRAY + "  Gewonnene Runden: " + ChatColor.GOLD + z2);
    }

    /**
     * setzt verschiedene Config Variablen
     *
     * @param spieler
     * @param cmd
     * @param param
     */
    public void set(Player spieler, String cmd, String param) {
        Location loc = spieler.getTargetBlock(null, 500).getLocation();
        switch (cmd) {
            case "p1":
                plugin.getConfig().set("xPunkt", loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ());
                plugin.getConfig().set("Welt", loc.getWorld().getName());
                plugin.saveConfig();
                plugin.reloadConfig();
                plugin.sendMessage(spieler, "Punkt1 wurde erfolgreich gesetzt");
                break;
            case "p2":
                plugin.getConfig().set("yPunkt", loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ());
                plugin.saveConfig();
                plugin.reloadConfig();
                plugin.sendMessage(spieler, "Punkt2 wurde erfolgreich gesetzt");
                break;
            case "ebenen-anzahl":
                try {
                    plugin.getConfig().set("Ebenen-Anzahl", Integer.parseInt(param));
                    plugin.saveConfig();
                    plugin.reloadConfig();
                    plugin.sendMessage(spieler, "Die Ebenen-Anzahl wurde erfolgreich auf "
                            + ChatColor.GOLD + Integer.parseInt(param) + ChatColor.GRAY + " gesetzt");
                } catch (NumberFormatException nfe) {
                    plugin.sendMessage(spieler, "Gib einen Zahl ein!");
                }
                break;
            case "boden":
                plugin.getConfig().set("Boden", loc.getBlockY());
                plugin.saveConfig();
                plugin.reloadConfig();
                plugin.sendMessage(spieler, "Die Bodenhöhe wurde erfolgreich gesetzt");
                break;
            case "totspawn":
                Location totloc = spieler.getLocation();
                plugin.getConfig().set("TotSpawn",
                        totloc.getWorld().getName() + ","
                        + totloc.getBlockX() + ","
                        + totloc.getBlockY() + ","
                        + totloc.getBlockZ() + ","
                        + totloc.getYaw() + ","
                        + totloc.getPitch());
                plugin.saveConfig();
                plugin.reloadConfig();
                plugin.sendMessage(spieler, "Totspawn erfolgreich gesetzt");
                break;
        }
    }

    /**
     * ruft eine Liste der Spieler im Spiel auf
     *
     * @param spieler
     */
    public void liste(Player spieler) {
        String spielerliste = "";
        Iterator<String> it = plugin.getSpiel().getSpielerSet().iterator();
        while (it.hasNext()) {
            spielerliste += it.next() + ", ";
        }

        spieler.sendMessage(ChatColor.DARK_AQUA + "[TSS]: " + ChatColor.GRAY + "Folgende Spieler sind noch im Spiel:");
        spieler.sendMessage(ChatColor.GRAY + spielerliste);
    }

    /**
     * Ein Command welches zum reinen testen dient
     *
     * @param spieler
     * @param param
     */
    public void test(Player spieler, String param) {
        switch (param) {
            case "spielfeld":
                TerraSnowSpleef.sendMessage(spieler, "DEBUG: " + plugin.getSpiel().getSpielfeld().inSpielfeld(spieler.getLocation()));
                break;
            case "ebenen":
                TerraSnowSpleef.sendMessage(spieler, plugin.getSpiel().getSpielfeld().getEbenenID(spieler.getLocation().getBlockY())+ "");
                break;
        }
    }
}
