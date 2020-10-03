# Disclaimer
Tournify is still under heavy development and is currently in an alpha phase. I'd be very happy if you give a try and provide feedback about what you like, don't like, features you'd like to see and bugs that you find. But keep in mind that the API may and will change in the future. I plan on eventually release a stable version but it is currently too early to do so.

# What is Tournify?
Tournify is a service aimed at helping people to follow tournament results. The goal is two-fold:
* Provide a unified API for all (well, at least multiple) tournament hosts. That way, clients do not need to learn and implement the API of all those hosts but can only integrate with Tournify. Additionaly, when new tournament hosts are added to Tournify, they will be automatically available to clients, without any change needed on their end. The same is true if one of the hosts change their API.
* Provide a notification service to clients which allows them to provide a callback url to which tournament updates will be communicated. That way, these services do not need to poll the tournament hosts on a regular basis to check for updates. Anytime a a tournament is updated (typically, anytime a game is over), a notification is sent to the url provided by the client to alert them of the update.

# Data representation
* Tournament: This is the tournament which is requested by the client. It is composed of the following information:
  * name: the name of the tournament. For example Genesis 3
  * gameType: the type of game played for that tournament. For example, Super Smash Bros. Melee
  * address: the address where the tournament takes place
  * url: the url where that tournament can be followed
  * date: the date at which the tournament takes place
  * phases: a collection of Phases
  * done: a boolean which is true once the tournament is over
* Phase: This is a sub-part of a tournament. For example, tournaments often start with a pool phases followed by a bracket phase. Such a tournament would contain two phases. A phase is composed of the following information:
  * externalId: the phase id at the tournament host
  * name: the name of the phase. For example Top 8
  * done: a boolean which is true once the phase is over
  * sets: a collection of Sets
* Set: This is a sub-part of a phase and represents games played between two players. It is composed of the following information:
  * externalId: the set id at the tournament host
  * winner: the Player who won the set
  * name: the name of the set. For example Winners Finals
  * done: a boolean which is true once the phase is over
  * players: a collections of Players
* Player: this a quite self-describing. It is composed of the following information:
  * prefix: the prefix of the player (usually, their sponsor). For example C9
  * username: the username of the Player. For example Mew2King

# Endpoints
The following endpoints are available:
* **/tournament**: A GET request with a url sent to that endpoint will return the tournament hosted at the provided url
* **/participants**: A GET request with a url sent to that endpoint will return the list of players participating at the tournament hosted at the provided url
* **/subscribe**: A POST request with a url and a callbackUrl will let you subscribe to update from the tournament hosted at the provided url. Once an update occurs, a API call will be made to the callbackUrl, informing you of the update.
* **/subscribe**: A DELETE request with the id of the subscription will delete the subscription. This means that no notification will be sent anymore to the callbackUrl

A server is running and available for testing at https://tournify.migwel.dev

# Supported Tournament Hosts
Currently, the following tournament hosts are supported:
* [smash.gg](https://smash.gg/): A tournament in Tournify is an event in smash.gg. It is then not possible to follow a smash.gg tournament (for example, Genesis 3) but only smash.gg events (for example, SSBM at Genesis 3).
* [challonge.com](https://challonge.com/): A Challonge tournament only has one phase in Tournify as Challonge doesn't support multi-phases tournaments (yet?)

If you have suggestions of tournament hosts that you would like to see being supported, feel free to open an issue and let me know. Tournify is currently "optimized" for video game tournament happening over the span of a week-end but this may change in the future if the need is there.

# Existing clients
Here's a list of clients that integrate with Tournify:
* [tournify-discord-bot](https://github.com/Migwel/tournify_discord_bot): A discord bot that will post update in your Discord from the tournament you want to follow. Created by me :) 

# Getting in touch
If you have questions or suggestions, feel free to join the discord channel using [this link](https://discord.gg/D6GvMuR)