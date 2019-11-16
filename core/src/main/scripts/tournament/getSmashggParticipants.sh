url=""

if [[ $# -eq 0 ]];
then
  echo "No argument"
  url="https://smash.gg/tournament/wanted-saison-2-chapitre-3-bataille-sur-corneria/events/single-1vs1/overview"
else
  echo "Argument found"
  url=$1
fi

curl -H "Content-Type: application/json" -X POST -d '{"url":"'$url'"}' http://tournify.migwel.dev/tournament/participants | json_pp
