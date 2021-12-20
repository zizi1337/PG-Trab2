import pt.isel.canvas.*
import kotlin.random.Random



fun main() {
    var game = Game(Spaceship(325,null,null),emptyList(),false, 0) // will create the game
    val arena = Canvas(700,500, BLACK) //will create a panel
    onStart {
        arena.onMouseMove { mouse: MouseEvent -> // if you move your mouse
            if (!game.EndGame && mouse.x in 23..677) { //if the game isnt ended yet (EndGame = False) and the spaceship is between the limits of panel
                game =
                    game.copy(nave = game.nave.copy(xCenter = mouse.x - 23))   //makes the spaceship move along the mouse movement
                arena.drawAll(game)
            }

        }

        arena.onMouseDown { //if any mouse button is clicked
            if (game.nave.tiroy == null && !game.EndGame) { //if the spaceship is still in the screen and the game isnt over yet
                game = game.copy(
                    nave = game.nave.copy(
                        tiroy = 435,
                        tirox = game.nave.xCenter
                    )
                ) //Changes placement along the x axis of the spaceship
            }
        }

        arena.onKeyPressed { k: KeyEvent ->
            if (k.text == "Space" && game.nave.tiroy == null && !game.EndGame) // if spacebar is pressed does the same as if mouse button is clicked
                game = game.copy(nave = game.nave.copy(tiroy = 435, tirox = game.nave.xCenter))
        }
    }
    onFinish {

    }
    //every 500ms an enemy shot is created and sent on a random x location from the top to the bottom of the panel
    arena.onTimeProgress(500){
        val i = (0..99).random()
            if (i <= 50) {
                game = game.copy(enemy = arena.createEnemy(game))
                }
            }

    arena.onTimeProgress(25){
        if (game.nave.tiroy != null){
            if (game.nave.tiroy!! > -10)
                game = Game(Spaceship(game.nave.xCenter, game.nave.tirox, arena.positionShot(game.nave.tiroy!!)),game.enemy,game.EndGame,game.points)
            else
                game = Game(Spaceship(game.nave.xCenter, game.nave.tirox, null),game.enemy,game.EndGame,game.points)
        }
        game = arena.drawAll(game)
    }

}