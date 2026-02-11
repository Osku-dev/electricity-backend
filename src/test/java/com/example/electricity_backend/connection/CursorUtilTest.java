package com.example.electricity_backend.connection;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import graphql.relay.ConnectionCursor;
import graphql.relay.DefaultConnectionCursor;
import graphql.relay.DefaultEdge;
import graphql.relay.Edge;

class CursorUtilTest {

    private CursorUtil cursorUtil;

    @BeforeEach
    void setUp() {
        cursorUtil = new CursorUtil();
    }

    @Test
    void createCursorWithEncodesTimestamp() {
        String timestamp = "2025-01-01T00:00:00Z";

        ConnectionCursor cursor = cursorUtil.createCursorWith(timestamp);

        assertNotNull(cursor);
        assertEquals(
                timestamp,
                cursorUtil.decodeCursor(cursor.getValue())
        );
    }

    @Test
    void createCursorWithNullReturnsNull() {
        assertNull(cursorUtil.createCursorWith(null));
    }

    @Test
    void decodeCursorReturnsNullWhenNull() {
        assertNull(cursorUtil.decodeCursor(null));
    }

    @Test
    void decodeCursorThrowsOnInvalidBase64() {
        assertThrows(
                IllegalArgumentException.class,
                () -> cursorUtil.decodeCursor("not-base64")
        );
    }

    @Test
    void getFirstCursorFromReturnsFirst() {
        ConnectionCursor c1 = new DefaultConnectionCursor("c1");
        ConnectionCursor c2 = new DefaultConnectionCursor("c2");

        Edge<String> e1 = new DefaultEdge<>("A", c1);
        Edge<String> e2 = new DefaultEdge<>("B", c2);

        ConnectionCursor result =
                cursorUtil.getFirstCursorFrom(List.of(e1, e2));

        assertEquals(c1, result);
    }

    @Test
    void getFirstCursorFromEmptyReturnsNull() {
        assertNull(cursorUtil.getFirstCursorFrom(List.of()));
    }

    @Test
    void getLastCursorFromReturnsLast() {
        ConnectionCursor c1 = new DefaultConnectionCursor("c1");
        ConnectionCursor c2 = new DefaultConnectionCursor("c2");

        Edge<String> e1 = new DefaultEdge<>("A", c1);
        Edge<String> e2 = new DefaultEdge<>("B", c2);

        ConnectionCursor result =
                cursorUtil.getLastCursorFrom(List.of(e1, e2));

        assertEquals(c2, result);
    }

    @Test
    void getLastCursorFromEmptyReturnsNull() {
        assertNull(cursorUtil.getLastCursorFrom(List.of()));
    }
}
