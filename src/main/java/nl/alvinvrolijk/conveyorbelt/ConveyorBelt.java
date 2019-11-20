package nl.alvinvrolijk.conveyorbelt;

import nl.alvinvrolijk.conveyorbelt.utils.Config;
import nl.alvinvrolijk.conveyorbelt.utils.ConveyorBeltChecker;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class ConveyorBelt extends JavaPlugin {

    public static ConveyorBelt instance; // Instance

    public Logger logger = Logger.getLogger(getDescription().getName()); // Set console logger

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this; // Set instance

        new Config(this, true); // Get config file

        new ConveyorBeltChecker();

        logger.info("Plugin enabled"); // Inform console that the plugin is enabled
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        instance = null; // Set instance to null

        logger.info("Plugin disabled"); // Inform console that the plugin is disabled
    }
}
