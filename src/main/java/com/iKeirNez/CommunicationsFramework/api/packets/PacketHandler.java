package com.iKeirNez.CommunicationsFramework.api.packets;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Methods which listen for packets being received must have this annotation.
 *
 * @author iKeirNez
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface PacketHandler {
}
