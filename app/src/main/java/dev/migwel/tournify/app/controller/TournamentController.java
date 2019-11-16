package dev.migwel.tournify.app.controller;

import dev.migwel.tournify.app.service.TournamentServiceFactory;
import dev.migwel.tournify.communication.commons.Player;
import dev.migwel.tournify.communication.request.TournamentRequest;
import dev.migwel.tournify.communication.response.ParticipantsResponse;
import dev.migwel.tournify.core.data.Tournament;
import dev.migwel.tournify.core.service.TournamentService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    public Tournament getTournament(@RequestBody TournamentRequest request) {
        TournamentService tournamentService = tournamentServiceFactory.getTournamentService(request.getUrl());
        return tournamentService.getTournament(request.getUrl());
    }

    @RequestMapping(path="/participants", method= RequestMethod.POST)
    public ParticipantsResponse getParticipants(@RequestBody TournamentRequest request) {
        TournamentService tournamentService = tournamentServiceFactory.getTournamentService(request.getUrl());
        Set<Player> participants = tournamentService.getParticipants(request.getUrl());
        return new ParticipantsResponse(participants);
    }
}
