package dev.migwel.tournify.app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PenaltyBoxTest {

    @Test
    public void testAddToPenaltyBox() {
        String url = "https://www.migwel.dev";
        PenaltyBox penaltyBox = new PenaltyBox();
        penaltyBox.failure(url);
        assertTrue(penaltyBox.isBlocked(url));
    }

    @Test
    public void testAddToPenaltyBoxThenRemove() {
        String url = "https://www.migwel.dev";
        PenaltyBox penaltyBox = new PenaltyBox();
        penaltyBox.failure(url);
        penaltyBox.success(url);
        assertFalse(penaltyBox.isBlocked(url));
    }
}