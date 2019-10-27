package dev.migwel.tournify.app.controller;

import dev.migwel.tournify.app.service.ServiceFactory;
import dev.migwel.tournify.communication.request.TournamentRequest;
import dev.migwel.tournify.communication.response.ParticipantsResponse;
import dev.migwel.tournify.core.data.Player;
import dev.migwel.tournify.core.data.Tournament;
import dev.migwel.tournify.core.service.TournamentService;
import jdk.nashorn.internal.ir.annotations.Immutable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tournament")
@Immutable
public class TournamentController {

    private final ServiceFactory serviceFactory;

    public TournamentController(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    @RequestMapping(method= RequestMethod.GET)
    public Tournament getTournament(@RequestBody TournamentRequest request) {
        TournamentService tournamentService = serviceFactory.getTournamentService(request.getUrl());
        return tournamentService.getTournament(request.getUrl());
    }

    @RequestMapping(path="/participants", method= RequestMethod.POST)
    public ParticipantsResponse getParticipants(@RequestBody TournamentRequest request) {
        TournamentService tournamentService = serviceFactory.getTournamentService(request.getUrl());
        Collection <Player> participants = tournamentService.getParticipants(request.getUrl());
        List<String> participantsStr =  participants.stream().map(Player::getDisplayUsername).collect(Collectors.toList());
        return new ParticipantsResponse(participantsStr);
    }
}