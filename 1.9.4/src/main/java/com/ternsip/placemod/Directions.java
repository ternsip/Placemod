package com.ternsip.placemod;

import net.minecraft.block.*;
import net.minecraft.init.Blocks;

import java.util.HashMap;
import java.util.Map;


/**
 * This class transforms block metadata (rotate n times and flip)
 * @author Ternsip (ternsip@gmail.com)
 */
public class Directions {

    public static final int UNKNOWN = 0x00;
    public static final int SOUTH = 0x01;
    public static final int WEST = 0x02;
    public static final int EAST = 0x04;
    public static final int NORTH = 0x08;
    public static final int UP = 0x10;
    public static final int DOWN = 0x20;

    private static final Map<BlockType, HashMap<Integer, Integer>> metaToDirection = new HashMap<BlockType, HashMap<Integer, Integer>>();
    private static final Map<BlockType, HashMap<Integer, Integer>> directionToMeta = new HashMap<BlockType, HashMap<Integer, Integer>>();
    private static final Map<BlockType, Integer> masks = new HashMap<BlockType, Integer>();

    public enum BlockType {
        LOG, DISPENSER, BED, RAIL_NORMAL, RAIL_CURVE, RAIL_ASC, RAIL_POWERED, RAIL_POWERED_ASC, TORCH, STAIR, CHEST, SIGNPOST,
        DOOR, LEVER, BUTTON, REDSTONE_REPEATER, TRAPDOOR, VINE, SKULL, ANVIL,
        MUSHROOM, MUSHROOM_CAP_CORNER, MUSHROOM_CAP_SIDE, IDLE
    }

    public static BlockType getBlockType(Block block, int meta) {
        if (block instanceof BlockBed || block instanceof BlockPumpkin || block instanceof BlockFenceGate || block instanceof BlockEndPortalFrame || block instanceof BlockTripWireHook || block instanceof BlockCocoa) {
            return BlockType.BED;
        }
        if (block instanceof BlockRail) {
            return (meta < 0x2) ? BlockType.RAIL_NORMAL : (meta < 0x6) ? BlockType.RAIL_ASC : BlockType.RAIL_CURVE;
        }
        if (block instanceof BlockRailPowered || block instanceof BlockRailDetector) {
            return (meta < 0x2) ? BlockType.RAIL_POWERED : BlockType.RAIL_POWERED_ASC;
        }
        if (block instanceof BlockStairs) {
            return BlockType.STAIR;
        }
        if (block instanceof BlockChest || block instanceof BlockEnderChest || block instanceof BlockFurnace || block instanceof BlockLadder || Block.getIdFromBlock(block) == Block.getIdFromBlock(Blocks.WALL_SIGN)) {
            return BlockType.CHEST;
        }
        if (Block.getIdFromBlock(block) == Block.getIdFromBlock(Blocks.STANDING_SIGN)) {
            return BlockType.SIGNPOST;
        }
        if (block instanceof BlockDoor) {
            return BlockType.DOOR;
        }
        if (block instanceof BlockButton) {
            return BlockType.BUTTON;
        }
        if (block instanceof BlockRedstoneRepeater || block instanceof BlockRedstoneComparator) {
            return BlockType.REDSTONE_REPEATER;
        }
        if (block instanceof BlockTrapDoor) {
            return BlockType.TRAPDOOR;
        }
        if (block instanceof BlockVine) {
            return BlockType.VINE;
        }
        if (block instanceof BlockSkull) {
            return BlockType.SKULL;
        }
        if (block instanceof BlockAnvil) {
            return BlockType.ANVIL;
        }
        if (block instanceof BlockLog) {
            return BlockType.LOG;
        }
        if (block instanceof BlockDispenser || block instanceof BlockPistonBase || block instanceof BlockPistonExtension || block instanceof BlockHopper) {
            return BlockType.DISPENSER;
        }
        if (block instanceof BlockTorch) {
            return BlockType.TORCH;
        }
        if (block instanceof BlockLever) {
            return BlockType.LEVER;
        }
        if (block instanceof BlockHugeMushroom) {
            return (meta == 0x0 || meta > 0x9 || meta == 0x5) ? BlockType.MUSHROOM : (meta % 0x2 == 0 ? BlockType.MUSHROOM_CAP_SIDE : BlockType.MUSHROOM_CAP_CORNER);
        }
        return BlockType.IDLE;
    }

    static boolean isDoubleDirected(BlockType blockType) {
        return blockType == BlockType.MUSHROOM_CAP_CORNER || blockType == BlockType.RAIL_CURVE;
    }

    static Integer getDirection(int meta, BlockType blockType) {
        if (metaToDirection.containsKey(blockType)) {
            HashMap<Integer, Integer> metaToDir = metaToDirection.get(blockType);
            if (metaToDir.containsKey(meta)) {
                return metaToDir.get(meta);
            }
        }
        return UNKNOWN;
    }

    static Integer getMeta(int defaultMeta, int direction, BlockType blockType) {
        if (directionToMeta.containsKey(blockType)) {
            HashMap<Integer, Integer> biMap = directionToMeta.get(blockType);
            if (biMap.containsKey(direction)) {
                return biMap.get(direction);
            }
        }
        return defaultMeta;
    }

    static int getMask(BlockType blockType) {
        return masks.containsKey(blockType) ? masks.get(blockType) : 0;
    }

    private static void addMappings(HashMap<Integer, Integer> metaToDir, BlockType blockType, int mask) {
        HashMap<Integer, Integer> dirToMeta = new HashMap<Integer, Integer>();
        for (Map.Entry<Integer, Integer> entry : metaToDir.entrySet()) {
            dirToMeta.put(entry.getValue(), entry.getKey());
        }
        metaToDirection.put(blockType, metaToDir);
        directionToMeta.put(blockType, dirToMeta);
        masks.put(blockType, mask);
    }

    static {
        HashMap<Integer, Integer> metaToDir;

        metaToDir = new HashMap<Integer, Integer>();
        metaToDir.put(0x0, UP);
        metaToDir.put(0x4, EAST);
        metaToDir.put(0x8, SOUTH);
        addMappings(metaToDir, BlockType.LOG, 0xC);

        metaToDir = new HashMap<Integer, Integer>();
        metaToDir.put(0x0, DOWN);
        metaToDir.put(0x1, UP);
        metaToDir.put(0x2, NORTH);
        metaToDir.put(0x3, SOUTH);
        metaToDir.put(0x4, WEST);
        metaToDir.put(0x5, EAST);
        addMappings(metaToDir, BlockType.CHEST, 0x7);

        metaToDir = new HashMap<Integer, Integer>();
        metaToDir.put(0x0, DOWN);
        metaToDir.put(0x1, UP);
        metaToDir.put(0x2, NORTH);
        metaToDir.put(0x3, SOUTH);
        metaToDir.put(0x4, WEST);
        metaToDir.put(0x5, EAST);
        addMappings(metaToDir, BlockType.SKULL, 0x7);

        metaToDir = new HashMap<Integer, Integer>();
        metaToDir.put(0x0, DOWN);
        metaToDir.put(0x1, UP);
        metaToDir.put(0x2, NORTH);
        metaToDir.put(0x3, SOUTH);
        metaToDir.put(0x4, WEST);
        metaToDir.put(0x5, EAST);
        addMappings(metaToDir, BlockType.DISPENSER, 0x7);

        metaToDir = new HashMap<Integer, Integer>();
        metaToDir.put(0x0, WEST);
        metaToDir.put(0x1, SOUTH);
        metaToDir.put(0x2, EAST);
        metaToDir.put(0x3, NORTH);
        addMappings(metaToDir, BlockType.TRAPDOOR, 0x3);

        metaToDir = new HashMap<Integer, Integer>();
        metaToDir.put(0x0, SOUTH);
        metaToDir.put(0x1, WEST);
        metaToDir.put(0x2, NORTH);
        metaToDir.put(0x3, EAST);
        addMappings(metaToDir, BlockType.BED, 0x3);

        metaToDir = new HashMap<Integer, Integer>();
        metaToDir.put(0x1, SOUTH);
        metaToDir.put(0x2, WEST);
        metaToDir.put(0x4, NORTH);
        metaToDir.put(0x8, EAST);
        addMappings(metaToDir, BlockType.VINE, 0xF);

        metaToDir = new HashMap<Integer, Integer>();
        metaToDir.put(0x0, NORTH);
        metaToDir.put(0x1, EAST);
        addMappings(metaToDir, BlockType.RAIL_NORMAL, 0xF);

        metaToDir = new HashMap<Integer, Integer>();
        metaToDir.put(0x2, EAST);
        metaToDir.put(0x3, WEST);
        metaToDir.put(0x4, NORTH);
        metaToDir.put(0x5, SOUTH);
        addMappings(metaToDir, BlockType.RAIL_ASC, 0xF);

        metaToDir = new HashMap<Integer, Integer>();
        metaToDir.put(0x6, EAST);
        metaToDir.put(0x7, SOUTH);
        metaToDir.put(0x8, WEST);
        metaToDir.put(0x9, NORTH);
        addMappings(metaToDir, BlockType.RAIL_CURVE, 0xF);

        metaToDir = new HashMap<Integer, Integer>();
        metaToDir.put(0x0, NORTH);
        metaToDir.put(0x1, EAST);
        addMappings(metaToDir, BlockType.RAIL_POWERED, 0x7);

        metaToDir = new HashMap<Integer, Integer>();
        metaToDir.put(0x2, EAST);
        metaToDir.put(0x3, WEST);
        metaToDir.put(0x4, NORTH);
        metaToDir.put(0x5, SOUTH);
        addMappings(metaToDir, BlockType.RAIL_POWERED_ASC, 0x7);

        metaToDir = new HashMap<Integer, Integer>();
        metaToDir.put(0x0, EAST);
        metaToDir.put(0x1, WEST);
        metaToDir.put(0x2, SOUTH);
        metaToDir.put(0x3, NORTH);
        addMappings(metaToDir, BlockType.STAIR, 0x3);

        metaToDir = new HashMap<Integer, Integer>();
        metaToDir.put(0x1, EAST);
        metaToDir.put(0x2, WEST);
        metaToDir.put(0x3, SOUTH);
        metaToDir.put(0x4, NORTH);
        metaToDir.put(0x5, UP);
        addMappings(metaToDir, BlockType.TORCH, 0xF);

        metaToDir = new HashMap<Integer, Integer>();
        metaToDir.put(0x0, DOWN);
        metaToDir.put(0x1, EAST);
        metaToDir.put(0x2, WEST);
        metaToDir.put(0x3, SOUTH);
        metaToDir.put(0x4, NORTH);
        metaToDir.put(0x5, UP);
        addMappings(metaToDir, BlockType.BUTTON, 0x7);

        metaToDir = new HashMap<Integer, Integer>();
        metaToDir.put(0x0, DOWN);
        metaToDir.put(0x1, EAST);
        metaToDir.put(0x2, WEST);
        metaToDir.put(0x3, SOUTH);
        metaToDir.put(0x4, NORTH);
        metaToDir.put(0x5, UP);
        metaToDir.put(0x6, UP);
        metaToDir.put(0x7, DOWN);
        addMappings(metaToDir, BlockType.LEVER, 0x7);

        metaToDir = new HashMap<Integer, Integer>();
        metaToDir.put(0x0, WEST);
        metaToDir.put(0x1, NORTH);
        metaToDir.put(0x2, EAST);
        metaToDir.put(0x3, SOUTH);
        addMappings(metaToDir, BlockType.DOOR, 0x3);

        metaToDir = new HashMap<Integer, Integer>();
        metaToDir.put(0x0, NORTH);
        metaToDir.put(0x1, EAST);
        metaToDir.put(0x2, SOUTH);
        metaToDir.put(0x3, WEST);
        addMappings(metaToDir, BlockType.REDSTONE_REPEATER, 0x3);

        metaToDir = new HashMap<Integer, Integer>();
        metaToDir.put(0x0, SOUTH);
        metaToDir.put(0x1, EAST);
        addMappings(metaToDir, BlockType.ANVIL, 0x1);

        metaToDir = new HashMap<Integer, Integer>();
        addMappings(metaToDir, BlockType.MUSHROOM, 0xF);

        metaToDir = new HashMap<Integer, Integer>();
        metaToDir.put(0x1, WEST);
        metaToDir.put(0x3, NORTH);
        metaToDir.put(0x7, SOUTH);
        metaToDir.put(0x9, EAST);
        addMappings(metaToDir, BlockType.MUSHROOM_CAP_CORNER, 0xF);

        metaToDir = new HashMap<Integer, Integer>();
        metaToDir.put(0x2, NORTH);
        metaToDir.put(0x4, WEST);
        metaToDir.put(0x6, EAST);
        metaToDir.put(0x8, SOUTH);
        addMappings(metaToDir, BlockType.MUSHROOM_CAP_SIDE, 0xF);


    }


}
