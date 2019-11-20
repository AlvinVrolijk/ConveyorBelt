package nl.alvinvrolijk.conveyorbelt.utils;

import nl.alvinvrolijk.conveyorbelt.ConveyorBelt;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Repeater;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class ConveyorBeltChecker {
    public ConveyorBeltChecker() {
        if (new Config(ConveyorBelt.instance, false).get().getBoolean("enabled")) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(ConveyorBelt.instance, () -> {
                for (World world : Bukkit.getWorlds()) {
                    for (Entity entity : world.getEntities()) {
                        Block block = entity.getLocation().getBlock();

                        if (block.getType().equals(Material.REPEATER)) {
                            Repeater repeater = (Repeater) block.getBlockData();

                            int rate;
                            rate = repeater.getDelay();
                            if (repeater.isPowered()) {
                                rate = rate * 3;
                            }

                            entity.setVelocity(faceToForce(repeater.getFacing().getOppositeFace()).multiply(30 * rate).add(centerExcludeFace(entity.getLocation(), repeater.getFacing().getOppositeFace()).multiply(0.5)));
                        }

                        if (block.getLocation().subtract(0, 1, 0).getBlock().getType().equals(Material.PURPUR_STAIRS)) {
                            Stairs stairs = (Stairs) block.getLocation().subtract(0, 1, 0).getBlock().getBlockData();
                            entity.setVelocity(faceToForce(stairs.getFacing().getOppositeFace()).multiply(30 * 1.0).add(centerExcludeFace(entity.getLocation(), stairs.getFacing().getOppositeFace().getOppositeFace()).multiply(0.5)));
                        }

                        if (block.getLocation().subtract(0, 1, 0).getBlock().getType().equals(Material.SLIME_BLOCK) || block.getType().equals(Material.SLIME_BLOCK)) {
                            Vector vector = entity.getVelocity();
                            vector.setY(2.0);
                            entity.setVelocity(vector);
                        }
                    }
                }
            }, 10L, 1L);
        }
    }

    /**
     * Returns a vector that points to the center of the block excluding the
     * selected face
     *
     * @param align
     * @param face
     * @return Vector with the direction
     */
    public static Vector centerExcludeFace(Location align, BlockFace face) {
        Vector out = new Vector(0, 0, 0);
        if (face == BlockFace.NORTH || face == BlockFace.SOUTH) {
            if (align.getX() > align.getBlockX() + 0.5)
                out.setX(-0.1);
            if (align.getX() < align.getBlockX() + 0.5)
                out.setX(0.1);
        }

        if (face == BlockFace.EAST || face == BlockFace.WEST) {
            if (align.getZ() > align.getBlockZ() + 0.5)
                out.setZ(-0.1);
            if (align.getZ() < align.getBlockZ() + 0.5)
                out.setZ(0.1);
        }

        return out;
    }
    /**
     * Converts the provided face to a vector that is pointing in that direction
     *
     * @param face
     * @return Vector with the direction
     */
    public static Vector faceToForce(BlockFace face) {
        Vector out = new Vector(0, 0, 0);
        if (face == BlockFace.NORTH)
            out.setZ(-0.01);
        if (face == BlockFace.SOUTH)
            out.setZ(0.01);
        if (face == BlockFace.EAST)
            out.setX(0.01);
        if (face == BlockFace.WEST)
            out.setX(-0.01);
        if (face == BlockFace.UP)
            out.setY(0.01);
        if (face == BlockFace.DOWN)
            out.setY(-0.01);
        return out;
    }
}
