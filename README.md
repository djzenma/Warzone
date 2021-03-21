# Warzone Game
Advanced Programming Practices Project @ Concordia University

The game consists of 3 phases: 
<ul>
  <li> Map Editing which allows the user to create/edit/save a map </li>
  <li> Startup which loads a map and adds players to the game </li>
  <li> Gameplay which consists of the actual game </li>
</ul>

## Commands

### 1. Map Editing Phase
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

### 2. Startup Phase
<ul>
  <li>
  
  ``` 
  loadmap filename 
  ```
  Game starts by user selection of a user-saved map file, which loads the map as a connected directed graph
  </li>
  
  <li>
  
  ```
  gameplayer -add playername -remove playername
  ```
  User creates the players
  </li>
  
  <li>
  
  ```
  assigncountries
  ```
  All countries are randomly assigned to players
  </li>
</ul>

### 3. Gameplay Phase
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
</ul>
