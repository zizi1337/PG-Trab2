import pt.isel.canvas.*
import kotlin.math.*
import kotlin.random.Random

fun main() {
    var game = Game(Spaceship(325,null,null),emptyList<Enemy>(),false, 0) // will create the game
    val arena = Canvas(700,500, BLACK) //will create a panel
    onStart {
        arena.onMouseMove { mouse: MouseEvent -> // if you move your mouse
            if (mouse.x in 23..677) { //limits the spaceship between the borders of the program
                if (!game.EndGame) { //if the game isnt ended yet (EndGame = False)
                    game = game.copy(nave = game.nave.copy(xCenter = mouse.x -23))   //makes the spaceship move along the mouse movement
                    arena.drawAll(game)
                }
            }
        }

        arena.onMouseDown { mouse : MouseEvent -> //if any mouse button is clicked
           //println("${game.nave.tiroy}")
            if (game.nave.tiroy == null && !game.EndGame){ //if the spaceship is still in the screen and the game isnt over yet
                println("funfou")
                game = game.copy(nave = game.nave.copy(tiroy = 435, tirox = game.nave.xCenter)) //Changes placement along the x axis of the spaceship
            }
        }

        arena.onKeyPressed { k : KeyEvent ->
            if (k.text == "Space" && game.nave.tiroy == null && !game.EndGame)
                game = game.copy(nave = game.nave.copy(tiroy = 435, tirox = game.nave.xCenter))
        }
    }
    onFinish {

    }
    //every 500ms an enemy shot is created and sent on a random x location from the top to the bottom of the panel
    arena.onTimeProgress(500){
        game = game.copy(enemy = arena.createEnemy(game))
    }
    //every 25ms checks if the shot (?)
    arena.onTimeProgress(25){
        if (game.nave.tiroy != null){
            if (game.nave.tiroy!! > -10){
                //game = arena.drawAll(game.copy(nave = game.nave.copy(tiroy = arena.positionShot(game.nave.tiroy!!))))
                game = Game(Spaceship(game.nave.xCenter, game.nave.tirox, arena.positionShot(game.nave.tiroy!!)),game.enemy,game.EndGame,game.points)
            }
            else
                game = Game(Spaceship(game.nave.xCenter, game.nave.tirox, null),game.enemy,game.EndGame,game.points)
        }
        game = arena.drawAll(game)
    }

}

//function that draws the spaceship
fun Canvas.drawSapceship (xCenter : Int) {
    this.drawRect(xCenter,450,50,10, GREEN) // Draws spaceship rectangle (green)
    this.drawRect(xCenter + 23,450-5,4,5, YELLOW) // Draws spaceship rectangle (yellow)
}

//function that sum offsetVector and tiroy
fun Canvas.positionShot (tiroy: Int) : Int? {
    if (tiroy > -10)
        return (tiroy - 4)
    else
        return null
}

//function that draws the spaceship shot
fun Canvas.drawShot (tirox: Int, tiroy: Int) {
    drawRect(tirox + 23,tiroy,4,7, WHITE)
}

//function that draw all the elements of game
fun Canvas.drawAll (game : Game) :  Game{
    val jogo = game.copy(enemy = game.enemy.map{Enemy(it.x,MoveEnemy(it),it.delta)})

    if (!interceptionShip(jogo).EndGame){ //if the game is still running
        erase()
        val jogoFinal = drawEnemy(jogo)
        if (jogoFinal.nave.tiroy !=  null)
            drawShot(jogoFinal.nave.tirox!!,jogoFinal.nave.tiroy)

        drawSapceship(jogo.nave.xCenter)
        drawText(0,30, "Pontos ${jogoFinal.points}", YELLOW, 20) // draws points count on left sidebar
        return jogoFinal
    }
    else { //if the game ends
        drawText(250,240, "GAME", YELLOW, 70)
        drawText(255,310,"OVER", YELLOW, 70)
        return game.copy(EndGame = true)
    }

}


//function that draws the enemy shot
fun Canvas.drawEnemy (jogo : Game) : Game{
    val game = interception(jogo)
    game.enemy.forEach{
        drawRect(it.x,it.y,4,7, RED)
    }
    return game
}

//function that eliminates the enemy when spaceship shot intercects enemy shot
fun Canvas.interception (game : Game) : Game{
    //tiro
    if(game.nave.tiroy != null && game.nave.tirox != null){
        val shotLength =  7
        val shotWith = 4
        val delta = 23
        val xi = game.nave.tirox + delta
        val xf = game.nave.tirox + shotWith + delta
        val yi = game.nave.tiroy
        val yf = game.nave.tiroy + shotLength
        // if spaceship shot and enemy shot intercects
        val jogo = game.copy(enemy = game.enemy.mapNotNull {if (it.x in xi-2..xf+2 && it.y in yi..yf) null else it}) // add points
        return  jogo.copy(points = jogo.points +  (game.enemy.size - jogo.enemy.size)*10)
    }
    return game


}
//function that calculates when the spaceship intercects enemy shot
fun interceptionShip (game: Game) : Game{
    val nave = game.nave.xCenter
    game.enemy.forEach{
        if (it.x in nave..nave+50 && it.y in 450..460) //nave..nave+50 since the spaceship width is 50, 450..460 since the spaceship length is 10
            return game.copy(EndGame = true)  //calls the end of the game
    }

    return game //recieves the values
}

//function that creates the enemy shot
fun Canvas.createEnemy (game : Game) : List<Enemy> { return  game.enemy + Enemy(Random.nextInt(0,700),0, Random.nextInt(1,4)) }

//function that makes the enemy shot move along the y axis
fun Canvas.MoveEnemy (enemy : Enemy) : Int { return  enemy.y + enemy.delta }

//Dataclasses
data class Spaceship (val xCenter : Int, val tirox : Int?, val tiroy : Int?) //Spaceship
data class OffsetVector (val dx : Int, val dy : Int) //OffsetVector
data class Enemy (val x: Int, val y :Int, val delta :Int) //Enemy
data class Game (val nave :  Spaceship, val enemy : List<Enemy>, val EndGame : Boolean, val points : Int) { //Game
}

data class BoundingBox (val connerx : Int, val connery : Int, val width : Int, val length : Int) //