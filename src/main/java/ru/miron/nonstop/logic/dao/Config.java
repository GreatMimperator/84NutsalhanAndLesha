package ru.miron.nonstop.logic.dao;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import ru.miron.nonstop.logic.util.json.JSON;
import ru.miron.nonstop.logic.util.json.types.JSONValue;
import ru.miron.nonstop.logic.util.json.types.values.JSONMap;

public class Config {
    public final String hostname;
    public final int port;
    public final long connectionTriesCount;
    public final int connectionTryWaiting;
    
    public Config(String hostname, int port, long connectionTriesCount, int connectionTryWaiting) {
        this.hostname = hostname;
        this.port = port;
        this.connectionTriesCount = connectionTriesCount;
        this.connectionTryWaiting = connectionTryWaiting;
    }

    public static Config init(Path jsonPath) throws IOException, IllegalStateException {
        String json = new String(Files.readAllBytes(jsonPath), StandardCharsets.UTF_8);
        JSONValue jsonValue = JSON.parse(json);
        if (jsonValue.isMap()) {
            JSONMap jsonMap = jsonValue.asMap();
            try {
                JSONMap socketAddressConfig = jsonMap.get("socketAddress").asMap();
                String hostname = socketAddressConfig.get("host").asString().getValue();
                int port = (int) socketAddressConfig.get("port").asInteger().getValue();
                long connectionTriesCount = socketAddressConfig.get("connectionTriesCount").asInteger().getValue();
                int connectionTryWaiting = (int) socketAddressConfig.get("connectionTryWaiting").asInteger().getValue();
                return new Config(hostname, port, connectionTriesCount, connectionTryWaiting);
            } catch (NullPointerException ex) {
                throw new NullPointerException("Config file structure is broken");
            }
        } else {
            throw new IllegalStateException("File is not a map of config values");
        }
    }
}
