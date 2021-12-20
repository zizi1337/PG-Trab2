import pt.isel.canvas.*
import kotlin.random.Random

data class Game (val nave :  Spaceship, val enemy : List<Enemy>, val EndGame : Boolean, val points : Int) //Game

//function that draw all the elements of game
fun Canvas.drawAll (game : Game) :  Game{
    val jogo = game.copy(enemy = game.enemy.map{Enemy(it.x,moveEnemy(it),it.delta)})

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
// functions that calculates the interseption of both enemy and spaceship shots
fun interception (game : Game) : Game{
    if(game.nave.tiroy != null && game.nave.tirox != null){
        val shotLength =  7
        val shotWith = 4
        val delta = 23
        val xi = game.nave.tirox + delta
        val xf = game.nave.tirox + shotWith + delta
        val yi = game.nave.tiroy
        val yf = game.nave.tiroy + shotLength
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

    return game
}

//function that creates the enemy shot
fun Canvas.createEnemy (game : Game) : List<Enemy> {
    return  game.enemy + Enemy(Random.nextInt(23,677),0, Random.nextInt(1,4))
}