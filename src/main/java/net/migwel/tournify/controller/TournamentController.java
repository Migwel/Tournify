package net.migwel.tournify.controller;

import net.migwel.tournify.data.Tournament;
import net.migwel.tournify.request.TournamentRequest;
import net.migwel.tournify.service.ServiceFactory;
import net.migwel.tournify.service.TournamentService;
import net.migwel.tournify.store.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tournament")
public class TournamentController {

    @Autowired
    private ServiceFactory serviceFactory;

    @Autowired
    private TournamentRepository tournamentRepository;

    public TournamentController(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    @RequestMapping(method= RequestMethod.GET)
    public Tournament getTournament(@RequestBody TournamentRequest request) throws Exception {
        TournamentService tournamentService = serviceFactory.getTournamentService(request.getUrl());
        if(tournamentService == null) {
            throw new Exception("Couldn't find any service for url: "+ request.getUrl()); //TODO: Send more specific exception (and send it inside serviceFactory?)
        }
        return tournamentService.getTournament(request.getUrl());
    }
}
