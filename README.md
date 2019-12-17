# Location Information Bot

This bot connects to a custom atom, which provides the Location Information (or li, pi, etc.) tag. It then provides the client with useful information about the desired location via a direct message. 

# RubberDuckyStudio

Team: [RubberDuckyStudio](https://github.com/WoN-Hackathon-2019/won-LocationInformationBot)

Members:
* https://github.com/Zai-shen
* https://github.com/kevingufler
* https://github.com/Schlizohr


## Projects
### LI Bot
The [LocationInformation-Bot](https://github.com/WoN-Hackathon-2019/won-LocationInformationBot) provides information about a given location. 
It reacts to Atoms with one of the following tags: [location information, locationinformation, location-information,
            position information, positioninformation, position-information, pi, li]


## Cooperations

Translation Bot: Gather translation for some important words/sentences for the searched city/location

--- --- ---



# Running the bot

## Useful HowTo's
[HowTo](https://github.com/researchstudio-sat/webofneeds/tree/master/documentation/howto)

### Prerequisites

- [Openjdk 8](https://adoptopenjdk.net/index.html) - the method described here does **not work** with the Oracle 8 JDK!
- Maven framework set up

### On the command line

```
cd bot-skeleton
export WON_NODE_URI="https://hackathonnode.matchat.org/won"
mvn clean package
java -jar target/bot.jar
```
Now go to [What's new](https://hackathon.matchat.org/owner/#!/overview) to find your bot, connect and [create an atom](https://hackathon.matchat.org/owner/#!/create) to see the bot in action.

### In Intellij Idea
1. Create a run configuration for the class `won.bot.skeleton.SkeletonBotApp`
2. Add the environment variables

  * `WON_NODE_URI` pointing to your node uri (e.g. `https://hackathonnode.matchat.org/won` without quotes)
  
  to your run configuration.
  
3. Run your configuration

If you get a message indicating your keysize is restricted on startup (`JCE unlimited strength encryption policy is not enabled, WoN applications will not work. Please consult the setup guide.`), refer to [Enabling Unlimited Strength Jurisdiction Policy](https://github.com/open-eid/cdoc4j/wiki/Enabling-Unlimited-Strength-Jurisdiction-Policy) to increase the allowed key size.

##### Optional Parameters for both Run Configurations:
- `WON_KEYSTORE_DIR` path to folder where `bot-keys.jks` and `owner-trusted-certs.jks` are stored (needs write access and folder must exist) 

## Start coding

Once the skeleton bot is running, you can use it as a base for implementing your own application. 

## Setting up
- Download or clone this repository
- Add config files

Please refer to the general [Bot Readme](https://github.com/researchstudio-sat/webofneeds/blob/master/webofneeds/won-bot/README.md) for more information on Web of Needs Bot applications.

