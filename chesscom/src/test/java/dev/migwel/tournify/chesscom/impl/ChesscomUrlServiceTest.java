package dev.migwel.tournify.chesscom.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChesscomUrlServiceTest {

    private ChesscomUrlService chesscomUrlService = new ChesscomUrlService();
    private String[] validChesscomUrls = {"https://api.chess.com/pub/tournament/-33rd-chesscom-quick-knockouts-1401-1600",
            "api.chess.com/pub/tournament/-33rd-chesscom-quick-knockouts-1401-1600",
            "https://www.chess.com/tournament/-33rd-chesscom-quick-knockouts-1401-1600",
            "www.chess.com/tournament/-33rd-chesscom-quick-knockouts-1401-1600",
            "https://chess.com/tournament/-33rd-chesscom-quick-knockouts-1401-1600",
            "chess.com/tournament/-33rd-chesscom-quick-knockouts-1401-1600",
            "chess.com/tournament/-33rd-chesscom-quick-knockouts-1401-1600/1",
    };

    private String[] expectedChesscomUrls = {"https://api.chess.com/pub/tournament/-33rd-chesscom-quick-knockouts-1401-1600",
            "https://api.chess.com/pub/tournament/-33rd-chesscom-quick-knockouts-1401-1600",
            "https://api.chess.com/pub/tournament/-33rd-chesscom-quick-knockouts-1401-1600",
            "https://api.chess.com/pub/tournament/-33rd-chesscom-quick-knockouts-1401-1600",
            "https://api.chess.com/pub/tournament/-33rd-chesscom-quick-knockouts-1401-1600",
            "https://api.chess.com/pub/tournament/-33rd-chesscom-quick-knockouts-1401-1600",
            "https://api.chess.com/pub/tournament/-33rd-chesscom-quick-knockouts-1401-1600",
    };

    @Test
    public void formatUrl_chesscomUrls() {
        for(int i = 0; i < validChesscomUrls.length; i++) {
            assertEquals(expectedChesscomUrls[i],
                    chesscomUrlService.normalizeUrl(validChesscomUrls[i]),
                    "Format Chesscom URL "+ validChesscomUrls[i]);
        }
    }

}