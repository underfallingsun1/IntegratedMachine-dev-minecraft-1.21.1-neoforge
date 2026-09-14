package com.afs.integratedMachine.compartment.blockGroup;

public class BlockGroupTypes {
    //特殊含义
    public static final BlockGroupType EXTERNAL = BlockGroupType.of("external");
    public static final BlockGroupType ANY = BlockGroupType.of("any");

    public static final BlockGroupType CONTROLLER = BlockGroupType.of("controller");
    public static final BlockGroupType WALL = BlockGroupType.of("wall");
    public static final BlockGroupType CORNER = BlockGroupType.of("corner");
    public static final BlockGroupType INTERIOR = BlockGroupType.of("interior");
}
