curl -H "Content-Type: application/json" -X PUT -d '{"tournamentUrl":"https://api.smash.gg/tournament/nhl18-community-tournament-series-at-square-one", "callbackUrl":"http://localhost:8090"}' http://localhost:8090/subscribe | json_pp