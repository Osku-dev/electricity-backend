package com.example.electricity_backend.connection;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import org.springframework.stereotype.Component;

import graphql.relay.ConnectionCursor;
import graphql.relay.DefaultConnectionCursor;
import graphql.relay.Edge;

@Component
public class CursorUtil {

    public ConnectionCursor createCursorWith(String timestamp) {
        if (timestamp == null) {
            return null;
        }
        return new DefaultConnectionCursor(Base64.getEncoder().encodeToString(timestamp.getBytes(StandardCharsets.UTF_8)));
    }

    public String decodeCursor(String cursor) {
        if (cursor == null) {
            return null;
        }
        return new String(Base64.getDecoder().decode(cursor.getBytes(StandardCharsets.UTF_8)));
    }
    

    public <T> ConnectionCursor getFirstCursorFrom(List<Edge<T>> edges) {
        return edges.isEmpty() ? null : edges.get(0).getCursor();
    }
    
    public <T> ConnectionCursor getLastCursorFrom(List<Edge<T>> edges) {
        return edges.isEmpty() ? null : edges.get(edges.size() - 1).getCursor();
    }
}
