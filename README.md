# Warzone Game
Advanced Programming Practices Project @ Concordia University, Montreal, Canada.

## Purpose
This game is a demonstration of the correct usage of the following concepts:
<ul>
  <li>The following <strong>Design Patterns</strong>:</li>
  <ul>
    <li><strong>Adapter Pattern</strong></li>
    <li><strong>Command Pattern</strong></li>
    <li><strong>State **Pattern</strong></li>
    <li><strong>Observer Pattern</strong></li>
    <li><strong>MVC</strong></li>
  </ul>
  
  <li><strong>Continuous Integration using Github Actions</strong></li>
  <li><strong>Agile Development Process & Extreme Programming</strong>:</li>
  <li><strong>Unit Testing using JUnit</strong></li>
  <li>Proper <strong>Documentation</strong> using <strong>Javadoc</strong></li>
</ul>

## The Game
The game consists of 3 phases: 
<ul>
  <li> Map Editing which allows the user to create/edit/save a map </li>
  <li> Startup which loads a map and adds players to the game </li>
  <li> Gameplay which consists of the actual game </li>
</ul>

### Commands

#### 1. Map Editing Phase
<ul>
  <li>
  
  ```
  editcontinent -add continentID continentvalue -remove continentID
  ```
  Allows the user to add or remove continents and assign them a control value.
  </li>
  
  <li>
  
  ```
  editcountry -add countryID continentID -remove countryID
  ```
  Allows the user to add or remove countries and assign them to a continent
  </li>

  <li>
  
  ```
  editneighbor -add countryID neighborcountryID -remove countryID neighborcountryID
  ```
  Allows the user to add or remove neighbors to a certain country
  </li>
  
  <li>
  
  ```
  showmap
  ```
  Display the map as text
  </li>
  
  <li>
  
  ```
  savemap filename
  ```
  Save a map to a text file exactly as edited (using the “domination” game map format)
  </li>
  
  <li>
  
  ```
  editmap filename
  ```
  Load a map from an existing “domination” map file, or create a new map from scratch if the file does not exist
  </li>
  
  <li>
  
  ```
  validatemap
  ```
  Verification of map correctness. The map should be automatically validated upon loading and before saving (at least 3 types of incorrect maps) </br>
  The validatemap command can be triggered anytime during map editing
  </li>
</ul>

#### 2. Startup Phase
Before starting playing, you choose first whether you want to play one game (singlegame) or a tournament.

##### 2.1 Single Game Mode
<ul>
  <li>
  
  ``` 
  singlegame
  ```
  To create a single game (i.e, not a tournament).
  </li>
  
  <li>
  
  ``` 
  loadgame checkpointName
  ```
  If a previous game was saved using the savegame command, this command allows to resume it back on the saved checkpoint.
  </li>
   
  <li>
  
  ``` 
  loadmap filename 
  ```
  Game starts by user selection of a user-saved map file, which loads the map as a connected directed graph
  </li>
  
  <li>
  
  ```
  gameplayer -add playername strategy -remove playername
  ```
  User adds players. Every player has a name and a strategy. The strategy is one of the following 4:
  <ul>
  <li><strong>Human</strong>: This is a normal human player that enters gameplay commands to play.</li>
    
  <li><strong>Aggressive</strong>: This is a computer player strategy that focuses on centralization of forces and then attacks, i.e., it deploys on its strongest 
country, then always attack with its strongest country, then moves its armies from the weaker countries to the strongest one in order to maximize aggregation of forces in its strongest country.</li>
    
  <li><strong>Benevolent</strong>: computer player strategy that focuses on protecting its weak countries (deploys on its weakest country, never 
attacks, then moves its armies in order to reinforce its weaker country).</li>

  <li><strong>Random</strong>: computer player strategy that deploys on a random country, attacks random neighboring countries, and moves armies 
randomly between its countries.</li>

  <li><strong>Cheater</strong>: computer player strategy that conquers all the immediate neighboring enemy countries, and then doubles the number of armies on its countries that have enemy neighbors. Note that all of this happens during the issuing of orders, i.e., it doesn't wait until the execution of orders, it executes right away.</li>
  </ul>
  </li>
  
  <li>
  
  ```
  assigncountries
  ```
  All countries are randomly assigned to players
  </li>
</ul>

##### 2.2 Tournament Mode
<ul>
  <li>
  
  ``` 
  tournament -M map1 map2 ... map5 -P strategy1 strategy2 .... strategyM -G numGames -D MaxTurns
  ```
  To create a tournament. A tournament starts with the user choosing M = 1 to 5 different maps, P = 2 to 4 different computer players strategies (refer to the gameplayer command in the startup phase to know about what are strategies), G = 1 to 5 games to be played on each map, D = 10 to 50 maximum number of turns for each game. A tournament is then automatically played by playing G games on each of the M different maps between the chosen computer player strategies. In order to minimize run completion time, each game should be declared a draw after D turns. Once started, the tournament plays all the games automatically without user interaction. At the end of the tournament, a report of the results is displayed.
  </li>
</ul>

#### 3. Gameplay Phase
<ul>
  <li>
  
  ```
  deploy countryID numarmies
  ```
  Place "numarmies" from the current player's reinforcement pool on the "countryID" </br>
  The country specified by the "countryID" should be owned by the current player </br>
  example usage: `deploy Canada 10`
  </li>

  <li>
  
  ```
  advance countrynamefrom countynameto numarmies
  ```
  Move "numarmies" from one of the current player’s territories ("countrynamefrom") to an adjacent territory ("countynameto") </br>
  If the target country belongs to the current player, the armies are moved to the target territory </br>
  Otherwise, if the target territory belongs to another player, an attack happens between the two territories </br>
  example usage: advance Egypt Canada 20
  </li>

  <li>
  
  ```
  bomb countryID
  ```
  Destroy half of the armies located on an opponent’s territory ("countryID") that is adjacent to one of the current player’s territories </br>
  example usage: bomb Canada
  </li>

  <li>
  
  ```
  blockade countryID
  ```
  Triple the number of armies on one of the current player’s territories ("countryID") and make it a neutral territory </br>
  example usage: blockade Egypt
  </li>

  <li>
  
  ```
  airlift sourcecountryID targetcountryID numarmies
  ```
  Advance some armies from one of the current player’s territories ("sourcecountryID") to any another territory ("targetcountryID") </br>
  example usage: airlift Egypt Canada 20
  </li>

  <li>
  
  ```
  negotiate playerID
  ```
  Prevent attacks between the current player and another player ("playerID") until the end of the turn </br>
  example usage: negotiate Mazen </br>
  where Mazen is the name of an ennemi player
  </li>
  
  <li>
  
  ``` 
  savegame checkpointName
  ```
  This command saves the current game as a checkpoint to be able to resume it later using the loadgame command.
  </li>
</ul>
