package net.migwel.tournify.core.controller;

import jdk.nashorn.internal.ir.annotations.Immutable;
import net.migwel.tournify.communication.request.TournamentRequest;
import net.migwel.tournify.core.data.Player;
import net.migwel.tournify.core.data.Tournament;
import net.migwel.tournify.core.service.ServiceFactory;
import net.migwel.tournify.core.service.TournamentService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
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
    public Collection<String> getParticipants(@RequestBody TournamentRequest request) {
        TournamentService tournamentService = serviceFactory.getTournamentService(request.getUrl());
        Collection <Player> participants = tournamentService.getParticipants(request.getUrl());
        return participants.stream().map(Player::getDisplayUsername).collect(Collectors.toList());
    }
}
