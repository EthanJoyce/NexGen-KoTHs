package com.mrlolethan.nexgenkoths.commands;


public enum NexGenCommand {
    
    HELP("help", HelpCmd.class, "Shows this help text.", ""),
    WAND("wand", WandCmd.class, "Gives the player the current selector item.", ""),
    CREATE("create", CreateCmd.class, "Create a KoTH at the selected points.", "<Name>"),
    LIST("list", ListCmd.class, "Shows a list of all loaded KoTHs.", ""),
    SET_FLAG("setflag", SetFlagCmd.class, "Sets the flag value of a KoTH.", "<Name> <Flag Name> <Value>"),
    VIEW_FLAGS("viewflags", ViewFlagsCmd.class, "View the flags of the specified KoTH.", "<Name>"),
    DELETE("delete", DeleteCmd.class, "Deletes the specified KoTH.", "<Name>"),
    SAVE_ALL("saveall", SaveAllCmd.class, "Saves all KoTHs.", ""),
    START("start", StartCmd.class, "Starts the specified KoTH.", "<Name>"),
    STOP("stop", StopCmd.class, "Stops the specified KoTH.", "<Name>"),
    RELOAD("reload", ReloadCmd.class, "Reloads the plugin.", ""),
    SET_LOOT_TABLE("setloottable", SetLootTableCmd.class, "Sets the LootTable that the KoTH uses.", "<Name> <LootTable Name>"),
    VIEW_LOOT_TABLE("viewloottable", ViewLootTableCmd.class, "Shows the LootTable that the KoTH uses.", "<Name>"),
    LIST_LOOT_TABLES("listloottables", ListLootTablesCmd.class, "Shows a list of the loaded LootTables.", ""),
    LOOT_TABLE_CONTENTS("loottablecontents", LootTableContentsCmd.class, "Shows the contents of the specified LootTable.", "<Name>"),
    LIST_ITEM_COLLECTIONS("listitemcollections", ListItemCollectionsCmd.class, "Shows a list of the loaded ItemCollections.", ""),
    ITEM_COLLECTION_CONTENTS("itemcollectioncontents", ItemCollectionContentsCmd.class, "Shows the contents of the specified ItemCollection.", "<Name>"),
    VERSION("version", VersionCmd.class, "Shows the current plugin version.", ""),
    VIEW_TIMERS("viewtimers", ViewTimersCmd.class, "Shows the timers on a KoTH.", "<Name>"),
    UPDATE("update", UpdateCmd.class, "Checks Bukkit Dev for an update.", ""),
    SET_MESSAGE("setmessage", SetMessageCmd.class, "Sets a capture time message to be broadcast at a specific time for the KoTH.", "<Name> <Time> <Message>"),
    UNSET_MESSAGE("unsetmessage", UnsetMessageCmd.class, "Removes the capture time message at the given time for the specified KoTH.", "<Name> <Time>"),
    VIEW_MESSAGES("viewmessages", ViewMessagesCmd.class, "Shows the capture time messages that are broadcast at specific times for the KoTH.", "<Name>"),
    SET_ZONE("setzone", SetZoneCmd.class, "Allows you to redefine the specified KoTH's capture zone.", "<Name>");
    
    private String cmd;
    private Class<? extends NexGenCmd> clazz;
    private String desc;
    private String arguments;
    
    NexGenCommand(String cmd, Class<? extends NexGenCmd> clazz, String desc, String arguments) {
        this.cmd = cmd;
        this.clazz = clazz;
        this.desc = desc;
        this.arguments = arguments;
    }
    
    public String getCmd() {
        return cmd;
    }
    
    public Class<? extends NexGenCmd> getCmdClass() {
        return clazz;
    }
    
    public String getDescription() {
        return desc;
    }
    
    public String getArguments() {
        return arguments;
    }
    
    
    public static NexGenCommand getCommand(String cmd) {
        for(NexGenCommand c : values())
            if(c.getCmd().equalsIgnoreCase(cmd))
                return c;
        
        return null;
    }
    
    
}
