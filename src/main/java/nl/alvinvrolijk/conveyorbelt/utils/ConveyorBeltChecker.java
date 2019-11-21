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
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ConveyorBeltChecker {

    public ConveyorBeltChecker() {
        if (new Config(ConveyorBelt.instance, false).get().getBoolean("enabled")) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(ConveyorBelt.instance, () -> {
                for (World world : Bukkit.getWorlds()) {
                    for (Entity entity : world.getEntities()) {
                        int speed = 0;
                        Vector direction = null;
                        Block block = entity.getLocation().getBlock();

                        powerBlock(entity.getLocation());

                        if (block.getType().equals(Material.REPEATER)) {
                            Repeater repeater = (Repeater) block.getBlockData();

                            int rate;
                            rate = repeater.getDelay();
                            if (repeater.isPowered()) {
                                rate = rate * 3;
                            }

                            speed = speed + rate;
                            direction = faceToForce(repeater.getFacing().getOppositeFace());
                        }

                        if (block.getLocation().subtract(0, 1, 0).getBlock().getType().equals(Material.PURPUR_STAIRS)) {
                            Stairs stairs = (Stairs) block.getLocation().subtract(0, 1, 0).getBlock().getBlockData();
                            speed = speed + 1;
                            direction = faceToForce(stairs.getFacing().getOppositeFace());
                        }

                        if (block.getLocation().subtract(0, 1, 0).getBlock().getType().equals(Material.SLIME_BLOCK) || block.getType().equals(Material.SLIME_BLOCK)) {
                            direction = new Vector(0, 0.02, 0);
                            speed = speed + 1;
                        }

                        if (speed != 0 && direction != null) {
                            if (entity.isOnGround() && (!(entity instanceof Player) || !((Player) entity).isSneaking()))
                                entity.setVelocity(direction.multiply(30 * speed));

                            if (block.getLocation().subtract(0, 1, 0).getBlock().getType().equals(Material.SLIME_BLOCK) || block.getType().equals(Material.SLIME_BLOCK)) {
                                Vector vector = entity.getVelocity();
                                vector.setY(2.0);
                                entity.setVelocity(vector);
                            }
                        }
                    }
                }
            }, 10L, 1L);
        }
    }

    /**
     * Converts the provided face to a vector that is pointing in that direction
     *
     * @param face
     * @return Vector with the direction
     */
    private static Vector faceToForce(BlockFace face) {
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

    /**
     * Powers the block activating any redstone around
     *
     * @param location Location
     */
    private void powerBlock(Location location) {
        int radius = new Config(ConveyorBelt.instance, false).get().getInt("radius");

        for (int x = -(radius); x <= radius; x++) {
            for (int y = -(radius); y <= radius; y++) {
                for (int z = -(radius); z <= radius; z++) {
                    double pX = location.getX();
                    double pY = location.getY();
                    double pZ = location.getZ();
                    Block block = location.getWorld().getBlockAt((int) pX + x, (int) pY + y, (int) pZ + z);
                    if (block.getType().equals(Material.PURPUR_BLOCK)) {
                        block.setType(Material.REDSTONE_BLOCK);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(ConveyorBelt.instance, () -> block.setType(Material.PURPUR_BLOCK), 5L);
                    }

                    Block block2 = location.getWorld().getBlockAt((int) pX + x, location.getBlockY(), (int) pZ + z);
                    if (block2.getType().equals(Material.RED_NETHER_BRICKS)) {
                        block2.setType(Material.REDSTONE_BLOCK);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(ConveyorBelt.instance, () -> block2.setType(Material.RED_NETHER_BRICKS), 5L);
                    }
                }
            }
        }
    }
}