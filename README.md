# DEADWOOD | DIGITAL BOARD GAME

## Compiling Instructions
* Compile all *.java files in single java project. 
* Use Deadwood.java as entry point into program.

## Starting Game
### Example first turn:
```
Welcome to Deadwood!
Please enter the number of players: 
2
Please enter the name of player 1: blue
Please enter the gender of [blue] [M/F/GN]: 
GN
Please enter the name of player 2: red
Please enter the gender of [red] [M/F/GN]: 
GN
> active player?
The active player is blue. They have $0, 0 credits, and rank 1. They are at trailer.
> where all
------------ Player Locations -----------
blue is @ trailer
red is @ trailer
-----------------------------------------
> move Saloon
> where
in Saloon shooting Swing 'em Wide scene 35
> work Reluctant Farmer
> act
fail! you only get $1
> end
> 
```
### How to begin game
* Enter number of players
* Enter name and gender of each player
* \<active player\?\> command can tell which player goes first
* On turn one: player can move, act, or rehearse

## Commands

### \<active player\?\>
> This command shows which player is currently active. It also reveals stats 
> such as: amount of dollars and credits in wallet, rank, and location.

### \<move \[location\]\> 
> This command moves the active player to a new location if possible. The command
> will prevent movement if player is has already moved this turn, is currently working a role, or has just wrapped a scene.
>> * \<move office\> takes player to office if office is adjacent
>> * \<move trailer\> takes player to trailer if office is adjacent 

### \<where \[all\]\>
> The where command (without the \[all\] flag) shows where the active player is. 
> If on a space with cards, it will also show which scene player is on. 
> With the \[all\] flag, all previous information is shown plus name value for
> all players.

### \<work \[role\]\> 
> This command puts a player on a role in their current location. This command
> will prevent this action if player is already acting a role, or on a space 
> without roles.

### \<act\>
> This command actives the act sequence if active player is currently working a 
> role. If act is successful, a token is removed from the location. If this 
> token is the final one, scene wrap sequence begins. Any players who earned
> money or credits will receive reward.

### \<rehearse\>
> This command will give active player a practice token if currently working a role. This maxes to 5 tokens per player.

### \<upgrade\>
> This command displays a chart and asks for which upgrade active player would like
> if they are currently in the office. Active player is then prompted to enter 
> payment method and required rank number. Command exits if rank is lower than or
> equal to rank of current player.

#### Example Upgrade Code
```
> move office
> active player?
The active player is red. They have $14, 10 credits, and rank 2. They are at office.
> upgrade
    Rank  Dollars  Credits
--------------------------
       2        4        5
       3       10       10
       4       18       15
       5       28       20
       6       40       25
--------------------------
How would you like to pay? [D/C]: D
Which rank would you like? [2-6]: 3
> active player?
The active player is red. They have $4, 10 credits, and rank 3. They are at office.
> end
```




