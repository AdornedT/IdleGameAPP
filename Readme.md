**The objetive of this project is not necessarily to create a usefull app, but only a functional one that integrates the necessary tools to show mastery over the required tools for the Vanderbilt University Android App Development Course**

The objetive of this app is to create a simple idle game using all required tools for project completion.

**The project is in the planning fase right now, so most of what is written are ideas that are not necessarily implemented yet and may change.**

# Main game idea

This app will be an idle game where the objective is to make as much money as possible, to do so there will be a button that every time it is pressed you will bottle and automatically sell water. With your earnings you will be able to purchase upgrades and water bottle factories to earn even more, be it by clicking or with automatic revenue from your factories.

## Activity

It will start the main game screen immediatly, so no main menu screen because I find that makes the game more dynamic. The idea is that the game will automatically load your local save and resume from where you were.

A separete menu will be created for upgrades and to purchase water bottle factories.

### BroadcastReceiver

There will be a button in the main game screen that buys a water bottle factory, this buttton will send an intent to the upgrade screen for a quick buy so that the user does not require to change screens.

#### Service

A background service will be created to play music while you play the game.

In the UI of the main game loop you will find a play and pause button for the music, if you can't hear it turn up the device volume.

##### ContentProvider

This will be used so that the main game loop screen knows what the price for bottled water, the price for the bottled water is collected online in dollars via an url request, what upgrades and how many factories the player has.


###### Web service

I will be making an http request to collect the price of [bottled water in dollars](https://www.globalproductprices.com/USA/mineral_water_prices/#:~:text=The%20price%20is%200.81%20USD.)

This uses the java.net.HttpURLConnection import.

####### Wire Frame
