**The objetive of this project is not necessarily to create an usefull app, but only a functional one that integrates the necessary tools to show mastery over the required tools for the Vanderbilt University Android App Development Course**

The objetive of this app is to create a simple idle game using all required tools for project completion.

**The project is in the planning fase right now, so most of what is written are ideas that are not necessarily implemented yet and may change.**

# Main game idea

This app will be an idle game where the objective is to make as much money as possible, to do so there will be a button that every time it is pressed you will bottle and automatically sell water. With your earnings you will be able to purchase upgrades and water bottle factories to earn even more, be it by clicking or with automatic revenue from your factories.

### Activity

The MainActivity will start the screen with a "Start" button, after pressing it the game will load old data or create new data depending if it is your first game.

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

The image is pretty big to show here, look for the class_diagram.png file.

I will give a simple and brief overview of everything so as not to bore people.

The MainActivity starts the whole thing by collecting any data from an old save or creating new data if it is your first game.

The ActivityFragment handles all screen assets, since it was not part of the course I did not bother to make this really optimized or pretty only functional, there you can see that it can start a thread for the game timer thus handling the factories profits, sends the intent for pausing the music and starts the music service.

The Backgroundmusic is the code that starts the music for the game, it is a sticky background service and if the player does not pause the music before stopping the game it will continue THIS IS BY DESIGN.

The HttpURLConnectionActivity has the code for the http request, it basically goes to a site and collects the page elements and stores the line with the price of the water bottle in a string, this value is used in game, which is then treated at the MainActivity.

For the ContentProvider we have to look at three separate java files, GameContentOps, GameContentProvider and GameDatabaseHelper. Starting with the GameContentOps is the simplest, it basically has some helper functions for easier acess to the SQLite table, having methods for querying, updating and inserting data. Second we have the GameDataBaseHelper, which helps us make a table so that we can store our data, the table has two columns name and value being that what is necessary for all data used for saving the game. Last we have the GameContentProvider, this is the java code that initializes the cursor and handles the data we need to collect and store.

So what we have in total is
  - Activities
  - A BroadcastReceiver
  - A started Service 
  - A ContentProvider
