# Footbal Torunament API

### Description

Rest application that holds information about the matches, <br />their results and the teams with their players which
have entered the European Championship.
<br/> The main task is to get the pair of players who have played the longest from the same team in the same match.

### Input Data

The input data comes from csv files which have the following format:<br />

**players.csv**<br/>
ID, TeamNumber, Position, FullName, TeamID<br />
1, 1, GK, Manuel Neuer, 1 <br />
2, 2, DF, Antonio Rüdiger, 1<br />
3, 3, DF, David Raum, 1<br />
4, 4, DF, Jonathan Tah, 1<br />
5, 5, MF, Pascal Groß, 1<br />
6, 6, DF, Joshua Kimmich, 1<br />

**team.csv**<br/>
ID, Name, ManagerFullName, Group<br />
1, Germany, Julian Nagelsmann, A<br />
2, Scotland, Steve Clarke , A<br />
3, Hungary, Marco Rossi, A<br />
4, Switzerland, Murat Yakin, A<br />
5, Spain, Luis de la Fuente, B<br />

**match.csv**<br/>
ID,ATeamID,BTeamID,Date,Score<bre/br
1, 1, 2, 6/14/2024, 5-1<br />
2, 3, 4, 6/15/2024, 1-3<br />

**records.csv**<br/>
ID,PLayerID,MatchId,fromMinutes,toMinutes<br/>
1, 1, 1, 0, NULL<br />
2, 2, 1, 0, 90<br />
3, 6, 1, 0, 90<br />
4, 4, 1, 0, 90<br />
8, 23, 1, 0, 46<br />
9, 5, 1, 46, 90<br />
10, 10, 1, 0, 74<br />

### Specific Requirements

1) **Data information** <br/>

  <ul>toMinutes can be NULL, equivalent to End of Match e.g. 90 min.</ul>
  <ul>The input data must be loaded to the program from a CSV file without using external libraries</ul>

2) **Date Formats**<br/>
  <ul>The application should support multiple date formats. Extra points will be given if the application supports all date formats </ul>

3) **Clean code** <br/>
 <ul> Follow best practise for clean code</ul>

4) **Read.me** <br/>
 <ul>The README file should explain the task and describe the algorithm used to implement the functionalities. </ul>


### Application 

<ul> 
The application is build like a REST application. To use the app you need to get the <br/>
csv files from the Repository and upload them on the <br/>following url: http://localhost:8080/api/csv/upload. <br/>
You can place the files all together or one by one, but you need to upload them in <br/>
the following sequence: teams, players, matches and records. <br/>
This is needed because of the relation between the different classes.<br/>
For database I am using MySQl, when you start the application the schema will automatically generate<br/>
but you need to change the username and password in application.properties.<br/>
I am using Spring Framework to create the different tables and the relations between them.<br/>
The csv files are loaded without using any external libraries. <br/>
The main functionality of the app is to return the pair of football players<br/>
from the same team, which have played together in the same match<br/>
and the time they have played together on the field.<br/>
You can see all pairs on the following url: http://localhost:8080/api/player-pairs/longest</ul>