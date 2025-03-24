package me.dnoc.craftBridge;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests for the data model classes in CraftBridge
 */
public class EndpointResponseTest {

    @Test
    public void testServerStatusModel() throws Exception {
        // Get the inner class
        Class<?> serverStatusClass = Class.forName("me.dnoc.craftBridge.CraftBridge$ServerStatus");
        
        // Create an instance using reflection
        Constructor<?> constructor = serverStatusClass.getDeclaredConstructor(int.class, int.class, String.class);
        constructor.setAccessible(true);
        Object serverStatus = constructor.newInstance(42, 100, "1.21.4");
        
        // Verify fields
        Field onlinePlayersField = serverStatusClass.getDeclaredField("onlinePlayers");
        onlinePlayersField.setAccessible(true);
        assertEquals(42, onlinePlayersField.getInt(serverStatus));
        
        Field maxPlayersField = serverStatusClass.getDeclaredField("maxPlayers");
        maxPlayersField.setAccessible(true);
        assertEquals(100, maxPlayersField.getInt(serverStatus));
        
        Field versionField = serverStatusClass.getDeclaredField("version");
        versionField.setAccessible(true);
        assertEquals("1.21.4", versionField.get(serverStatus));
    }
    
    @Test
    public void testPlayerInfoModel() throws Exception {
        // Get the inner class
        Class<?> playerInfoClass = Class.forName("me.dnoc.craftBridge.CraftBridge$PlayerInfo");
        
        // Create an instance using reflection
        Constructor<?> constructor = playerInfoClass.getDeclaredConstructor(
                String.class, String.class, double.class, int.class, String.class);
        constructor.setAccessible(true);
        
        String uuid = UUID.randomUUID().toString();
        Object playerInfo = constructor.newInstance("TestPlayer", uuid, 20.0, 5, "world");
        
        // Verify fields
        Field nameField = playerInfoClass.getDeclaredField("name");
        nameField.setAccessible(true);
        assertEquals("TestPlayer", nameField.get(playerInfo));
        
        Field uuidField = playerInfoClass.getDeclaredField("uuid");
        uuidField.setAccessible(true);
        assertEquals(uuid, uuidField.get(playerInfo));
        
        Field healthField = playerInfoClass.getDeclaredField("health");
        healthField.setAccessible(true);
        assertEquals(20.0, healthField.getDouble(playerInfo));
        
        Field levelField = playerInfoClass.getDeclaredField("level");
        levelField.setAccessible(true);
        assertEquals(5, levelField.getInt(playerInfo));
        
        Field worldField = playerInfoClass.getDeclaredField("world");
        worldField.setAccessible(true);
        assertEquals("world", worldField.get(playerInfo));
    }
    
    @Test
    public void testCommandClass() throws Exception {
        // Get the inner class
        Class<?> commandClass = Class.forName("me.dnoc.craftBridge.CraftBridge$Command");
        assertNotNull(commandClass);
        
        // Verify field exists
        Field commandField = commandClass.getDeclaredField("command");
        assertNotNull(commandField);
    }
    
    @Test
    public void testMessageClass() throws Exception {
        // Get the inner class
        Class<?> messageClass = Class.forName("me.dnoc.craftBridge.CraftBridge$Message");
        assertNotNull(messageClass);
        
        // Verify field exists
        Field messageField = messageClass.getDeclaredField("message");
        assertNotNull(messageField);
    }
    
    @Test
    public void testResponseModel() throws Exception {
        // Get the inner class
        Class<?> responseClass = Class.forName("me.dnoc.craftBridge.CraftBridge$Response");
        
        // Create an instance using reflection
        Constructor<?> constructor = responseClass.getDeclaredConstructor(String.class);
        constructor.setAccessible(true);
        Object response = constructor.newInstance("Operation successful");
        
        // Verify field
        Field messageField = responseClass.getDeclaredField("message");
        messageField.setAccessible(true);
        assertEquals("Operation successful", messageField.get(response));
    }
} 