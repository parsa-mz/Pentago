# Pentago


Pentago is two-player board game which is played on a 6x6 board divided into four 3x3 quadrants. Like this:

     ---------+---------
    | .  .  . | .  .  . |
    | .  Q1 . | .  Q2 . |
    | .  .  . | .  .  . |
     ---------+---------
    | .  .  . | .  W  . |
    | .  Q3 . | .  Q4 . |
    | .  .  . | .  .  . |
     ---------+---------

You can player with 2 players or a player and a bot

Each turn every player must 
- place a marble in one of the empty spaces on board
- rotate one of the quadrants by 90 degrees clockwise or anti-clockwise


for W marble on top board the input is:
```
4/2 3R
# 2nd place on 4th quadrant and turn 3rd quadrant clockwise
```


players win if they get five of their marbles in a vertical, horizontal or diagonal row (before or after the rotation).


More information about the game can be found on [its wikipedia page](http://en.wikipedia.org/wiki/Pentago).

## Algorithm
Bot uses Minmax and Alpha-Beta Puring algorithms for decision making. [Wikipedia](http://en.wikipedia.org/wiki/Minimax)

Alpha-Beta puring is a optimization of the minmax algorithm that decreases the number of nodes that are evaluated by the minimax algorithm in its search tree and therefore it runs much faster and allows us to go into deeper levels in the game tree.


## License
This project is licensed under the [MIT License](http://www.opensource.org/licenses/mit-license.php).
