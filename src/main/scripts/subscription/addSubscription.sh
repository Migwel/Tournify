curl -H "Content-Type: application/json" -X POST -d '{"tournamentUrl":"https://smash.gg/tournament/spectrum-smash-57/events/spectrum-smash-56-singles/overview", "callbackUrl":"http://localhost:8091/notification"}' http://localhost:8090/subscribe | json_pp
