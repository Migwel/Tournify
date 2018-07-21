url=""

if [[ $# -eq 0 ]];
then
  echo "No argument"
  url="https://api.smash.gg/tournament/nhl18-community-tournament-series-at-square-one"
else
  echo "Argument found"
  url=$1
fi

curl -H "Content-Type: application/json" -X GET -d '{"url":"'$url'"}' http://localhost:8090/tournament | json_pp