**The objetive of this project is not necessarily to create an usefull app, but only a functional one that integrates the necessary tools to show mastery over the required tools for the Vanderbilt University Android App Development Course**

The objetive of this app is to create a simple idle game using all required tools for project completion.

**The project is in the planning fase right now, so most of what is written are ideas that are not necessarily implemented yet and may change.**

# Main game idea

This app will be an idle game where the objective is to make as much money as possible, to do so there will be a button that every time it is pressed you will bottle and automatically sell water. With your earnings you will be able to purchase upgrades and water bottle factories to earn even more, be it by clicking or with automatic revenue from your factories.

### Activity

It will start the main game screen immediatly, so no main menu screen because I find that makes the game more dynamic. The idea is that the game will automatically load your local save and resume from where you were.

A separete menu will be created for upgrades and to purchase water bottle factories.

### BroadcastReceiver

There will be a button in the main game screen that buys a water bottle factory, this buttton will send an intent to the upgrade screen for a quick buy so that the user does not require to change screens.

The Pause button for the music uses sends an intent to a BroadcastReceiver in the BackGroundMusicService.

### Service

A background service will be created to play music while you play the game.

In the UI of the main game loop you will find a play and pause button for the music, if you can't hear it turn up the device volume.

### ContentProvider

This is used to share game data across all fragments. For example the upgrade screen and main game loop screen both need to know how much money you have.


### Web service

I will be making an http request to collect the price of [bottled water in dollars](https://www.globalproductprices.com/USA/mineral_water_prices/#)

This uses the java.net.HttpURLConnection import.

### Wire Frame
![alt text](https://github.com/AdornedT/IdleGameAPP/blob/master/game_wire_frame.png)

As can be seen the UI is simple, there are only two screens one for the main game loop where you can collect money, by cicking "Bottle Water", and see your stats. The second screen is where you can purchase upgrades that increase the value of your bottled water. In both screens you can purchase factories.

### Class Diagram
![class diagram](https://github.com/AdornedT/IdleGameAPP/blob/master/game_wire_frame.png)