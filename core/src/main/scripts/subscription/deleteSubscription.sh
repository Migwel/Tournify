subscriptionId=""

if [[ $# -eq 0 ]];
then
  echo "Please specify id to be deleted"
  exit 1
else
  subscriptionId=$1
fi

curl -X "DELETE"  https://tournify.migwel.dev/subscribe/$subscriptionId