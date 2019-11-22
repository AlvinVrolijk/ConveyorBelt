package nl.alvinvrolijk.conveyorbelt.utils;

import nl.alvinvrolijk.conveyorbelt.ConveyorBelt;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemMergeEvent;

public class Listeners implements Listener {

    private ConveyorBelt instance;

    public Listeners(ConveyorBelt instance) {
        this.instance = instance;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onItemMerge(ItemMergeEvent e) {
        e.setCancelled(true);
    }
}
