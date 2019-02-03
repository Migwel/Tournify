url=""

if [[ $# -eq 0 ]];
then
  echo "No argument"
  url="https://smash.gg/tournament/nhl18-community-tournament-series-at-square-one/events/nhl-10-5/overview"
else
  echo "Argument found"
  url=$1
fi

curl -H "Content-Type: application/json" -X GET -d '{"url":"'$url'"}' http://localhost:8090/tournament/participants | json_pp
