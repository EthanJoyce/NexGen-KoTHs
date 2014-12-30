package com.mrlolethan.nexgenkoths.commands.proc;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
public @interface Cmd {
	
	public CommandSenderType senderType();
	public int argsRequired() default 0;
	
}
