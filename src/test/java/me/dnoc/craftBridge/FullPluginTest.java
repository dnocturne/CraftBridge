package me.dnoc.craftBridge;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
/**
 * More comprehensive tests for CraftBridge
 */
public class FullPluginTest {

    @Test
    public void testServerStatusCreation() {
        // Create ServerStatus object using reflection
        try {
            // Get the ServerStatus class
            Class<?> serverStatusClass = Class.forName("me.dnoc.craftBridge.CraftBridge$ServerStatus");
            
            // Get constructor
            Constructor<?> constructor = serverStatusClass.getDeclaredConstructor(int.class, int.class, String.class);
            constructor.setAccessible(true);
            
            // Create several instances with different values
            Object status1 = constructor.newInstance(10, 100, "1.21.4");
            Object status2 = constructor.newInstance(20, 50, "1.21.0");
            Object status3 = constructor.newInstance(0, 200, "1.20.0");
            
            // Verify field values in each instance
            Field onlinePlayersField = serverStatusClass.getDeclaredField("onlinePlayers");
            Field maxPlayersField = serverStatusClass.getDeclaredField("maxPlayers");
            Field versionField = serverStatusClass.getDeclaredField("version");
            
            onlinePlayersField.setAccessible(true);
            maxPlayersField.setAccessible(true);
            versionField.setAccessible(true);
            
            assertEquals(10, onlinePlayersField.getInt(status1));
            assertEquals(100, maxPlayersField.getInt(status1));
            assertEquals("1.21.4", versionField.get(status1));
            
            assertEquals(20, onlinePlayersField.getInt(status2));
            assertEquals(50, maxPlayersField.getInt(status2));
            assertEquals("1.21.0", versionField.get(status2));
            
            assertEquals(0, onlinePlayersField.getInt(status3));
            assertEquals(200, maxPlayersField.getInt(status3));
            assertEquals("1.20.0", versionField.get(status3));
            
            // Test serialization
            Gson gson = new Gson();
            String json1 = gson.toJson(status1);
            String json2 = gson.toJson(status2);
            
            assertTrue(json1.contains("\"onlinePlayers\":10"));
            assertTrue(json1.contains("\"maxPlayers\":100"));
            assertTrue(json1.contains("\"version\":\"1.21.4\""));
            
            assertTrue(json2.contains("\"onlinePlayers\":20"));
            assertTrue(json2.contains("\"maxPlayers\":50"));
            assertTrue(json2.contains("\"version\":\"1.21.0\""));
            
        } catch (Exception e) {
            fail("Exception during test: " + e.getMessage());
        }
    }
    
    @Test
    public void testPlayerInfoCreation() {
        // Create PlayerInfo objects using reflection
        try {
            // Get the PlayerInfo class
            Class<?> playerInfoClass = Class.forName("me.dnoc.craftBridge.CraftBridge$PlayerInfo");
            
            // Get constructor
            Constructor<?> constructor = playerInfoClass.getDeclaredConstructor(
                    String.class, String.class, double.class, int.class, String.class);
            constructor.setAccessible(true);
            
            // Create multiple instances
            String uuid1 = UUID.randomUUID().toString();
            String uuid2 = UUID.randomUUID().toString();
            
            Object playerInfo1 = constructor.newInstance("Player1", uuid1, 20.0, 10, "world");
            Object playerInfo2 = constructor.newInstance("Player2", uuid2, 15.5, 30, "nether");
            
            // Test field values
            Field nameField = playerInfoClass.getDeclaredField("name");
            Field uuidField = playerInfoClass.getDeclaredField("uuid");
            Field healthField = playerInfoClass.getDeclaredField("health");
            Field levelField = playerInfoClass.getDeclaredField("level");
            Field worldField = playerInfoClass.getDeclaredField("world");
            
            nameField.setAccessible(true);
            uuidField.setAccessible(true);
            healthField.setAccessible(true);
            levelField.setAccessible(true);
            worldField.setAccessible(true);
            
            assertEquals("Player1", nameField.get(playerInfo1));
            assertEquals(uuid1, uuidField.get(playerInfo1));
            assertEquals(20.0, healthField.getDouble(playerInfo1));
            assertEquals(10, levelField.getInt(playerInfo1));
            assertEquals("world", worldField.get(playerInfo1));
            
            assertEquals("Player2", nameField.get(playerInfo2));
            assertEquals(uuid2, uuidField.get(playerInfo2));
            assertEquals(15.5, healthField.getDouble(playerInfo2));
            assertEquals(30, levelField.getInt(playerInfo2));
            assertEquals("nether", worldField.get(playerInfo2));
            
            // Test serialization
            Gson gson = new Gson();
            String json1 = gson.toJson(playerInfo1);
            String json2 = gson.toJson(playerInfo2);
            
            assertTrue(json1.contains("\"name\":\"Player1\""));
            assertTrue(json1.contains("\"uuid\":\"" + uuid1 + "\""));
            assertTrue(json1.contains("\"health\":20.0"));
            assertTrue(json1.contains("\"level\":10"));
            assertTrue(json1.contains("\"world\":\"world\""));
            
            assertTrue(json2.contains("\"name\":\"Player2\""));
            assertTrue(json2.contains("\"uuid\":\"" + uuid2 + "\""));
            assertTrue(json2.contains("\"health\":15.5"));
            assertTrue(json2.contains("\"level\":30"));
            assertTrue(json2.contains("\"world\":\"nether\""));
            
        } catch (Exception e) {
            fail("Exception during test: " + e.getMessage());
        }
    }
    
    @Test
    public void testResponseCreation() {
        // Create Response objects using reflection
        try {
            // Get the Response class
            Class<?> responseClass = Class.forName("me.dnoc.craftBridge.CraftBridge$Response");
            
            // Get constructor
            Constructor<?> constructor = responseClass.getDeclaredConstructor(String.class);
            constructor.setAccessible(true);
            
            // Create instances
            Object response1 = constructor.newInstance("Success");
            Object response2 = constructor.newInstance("Error occurred");
            
            // Test field values
            Field messageField = responseClass.getDeclaredField("message");
            messageField.setAccessible(true);
            
            assertEquals("Success", messageField.get(response1));
            assertEquals("Error occurred", messageField.get(response2));
            
            // Test serialization
            Gson gson = new Gson();
            String json1 = gson.toJson(response1);
            String json2 = gson.toJson(response2);
            
            assertTrue(json1.contains("\"message\":\"Success\""));
            assertTrue(json2.contains("\"message\":\"Error occurred\""));
            
        } catch (Exception e) {
            fail("Exception during test: " + e.getMessage());
        }
    }
} 