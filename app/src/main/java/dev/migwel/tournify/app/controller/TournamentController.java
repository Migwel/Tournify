package dev.migwel.tournify.app.controller;

import dev.migwel.tournify.app.service.TournamentServiceFactory;
import dev.migwel.tournify.communication.commons.Player;
import dev.migwel.tournify.communication.commons.Tournament;
import dev.migwel.tournify.communication.response.ParticipantsResponse;
import dev.migwel.tournify.core.service.TournamentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.concurrent.Immutable;
import java.util.Set;

@RestController
@RequestMapping("/tournament")
@Immutable
public class TournamentController {

    private final TournamentServiceFactory tournamentServiceFactory;

    public TournamentController(TournamentServiceFactory tournamentServiceFactory) {
        this.tournamentServiceFactory = tournamentServiceFactory;
    }

    @RequestMapping(method= RequestMethod.GET)
    public Tournament getTournament(@RequestParam(name = "url", required = true) String tournamentUrl) {
        TournamentService tournamentService = tournamentServiceFactory.getTournamentService(tournamentUrl);
        return tournamentService.getTournament(tournamentUrl);
    }

    @RequestMapping(path="/participants", method= RequestMethod.GET)
    public ParticipantsResponse getParticipants(@RequestParam(name = "url", required = true) String tournamentUrl) {
        TournamentService tournamentService = tournamentServiceFactory.getTournamentService(tournamentUrl);
        Set<Player> participants = tournamentService.getParticipants(tournamentUrl);
        return new ParticipantsResponse(participants);
    }
}
