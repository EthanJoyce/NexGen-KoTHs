package com.mrlolethan.nexgenkoths;

import org.bukkit.permissions.Permissible;

public final class Permissions {
	
	public static final boolean bypassCaptureCooldown(Permissible permissible) { return permissible.hasPermission("nexgenkoths.entercooldown.bypass"); }
	public static final boolean performCommand(Permissible permissible, String cmdId) { return permissible.hasPermission("nexgenkoths.cmd." + cmdId); }
	
}
