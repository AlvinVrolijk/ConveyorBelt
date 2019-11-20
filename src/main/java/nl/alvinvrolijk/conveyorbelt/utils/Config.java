package nl.alvinvrolijk.conveyorbelt.utils;

import nl.alvinvrolijk.conveyorbelt.ConveyorBelt;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config {

    // Create file and file configuration variable
    private File file;
    private FileConfiguration fileConfiguration;

    /**
     * @param instance instance of ConveyorBelt (main) class
     */
    public Config(ConveyorBelt instance, boolean startup) {
        if (startup) {
            this.file = new File(instance.getDataFolder(), "config.yml");
            this.fileConfiguration = YamlConfiguration.loadConfiguration(this.file);
            loadDefault(instance);
        } else {
            this.file = new File(instance.getDataFolder(), "config.yml");
            this.fileConfiguration = YamlConfiguration.loadConfiguration(this.file);
        }
    }

    // Save config.yml file
    public void save() {
        // Try to save the config.yml file configuration
        try {
            this.fileConfiguration.save(this.file);
        } catch (IOException exception) {
            exception.printStackTrace();

        }
    }

    // Get config.yml file
    public FileConfiguration get() {
        // Return config.yml file configuration
        return this.fileConfiguration;
    }

    // Reload config.yml file
    public void reload() {
        // Try statement to prevent plugin failures
        try {
            this.fileConfiguration = YamlConfiguration.loadConfiguration(file); // Reload config
            ConveyorBelt.instance.logger.info("config.yml has been reloaded!"); // Send to console
            save(); // Save config
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    // Load config.yml defaults (if file is deleted or corrupt)
    public void loadDefault(ConveyorBelt instance) {
        // If plugin folder doesn't exists, create one
        if (!instance.getDataFolder().exists()) {
            instance.getDataFolder().mkdir();
        }

        this.file = new File(instance.getDataFolder(), "config.yml");

        // Check whether the file exists or not
        // If not, the file will be created and 'created-message' will be sent to the console
        // If so, the file loaded message will be sent to the console
        if (!file.exists()) {
            try {
                // Try creating a new file
                file.createNewFile();
                // Saving the default config.yml from the jar
                instance.saveResource("config.yml", true);
                // Send to console
                instance.logger.info("config.yml has been created!");
            } catch (IOException e) {
                // Error message (creating new file)
                instance.logger.warning("Couldn't create config.yml!");
            }
        } else {
            this.fileConfiguration = YamlConfiguration.loadConfiguration(file); // Load config
            instance.logger.info("config.yml has been loaded!"); // Send to console
        }
    }
}
