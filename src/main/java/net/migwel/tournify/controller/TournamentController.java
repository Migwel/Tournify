package net.migwel.tournify.controller;

import jdk.nashorn.internal.ir.annotations.Immutable;
import net.migwel.tournify.data.Tournament;
import net.migwel.tournify.request.TournamentRequest;
import net.migwel.tournify.service.ServiceFactory;
import net.migwel.tournify.service.TournamentService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
}
