package me.dnoc.craftBridge;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests basic plugin structure via reflection
 */
public class PluginTest {

    @Test
    public void testPluginStructure() throws Exception {
        // Verify class exists
        Class<?> pluginClass = Class.forName("me.dnoc.craftBridge.CraftBridge");
        assertNotNull(pluginClass);
        
        // Verify critical fields exist
        Field portField = pluginClass.getDeclaredField("port");
        assertNotNull(portField);
        
        Field apiKeyField = pluginClass.getDeclaredField("apiKey");
        assertNotNull(apiKeyField);
        
        Field gsonField = pluginClass.getDeclaredField("gson");
        assertNotNull(gsonField);
        
        // Verify critical methods exist via reflection
        Method onEnableMethod = pluginClass.getDeclaredMethod("onEnable");
        assertNotNull(onEnableMethod);
        
        Method onDisableMethod = pluginClass.getDeclaredMethod("onDisable");
        assertNotNull(onDisableMethod);
        
        Method loadConfigurationMethod = pluginClass.getDeclaredMethod("loadConfiguration");
        assertNotNull(loadConfigurationMethod);
        
        Method setupEndpointsMethod = pluginClass.getDeclaredMethod("setupEndpoints");
        assertNotNull(setupEndpointsMethod);
    }
} 