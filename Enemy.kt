import pt.isel.canvas.Canvas
import kotlin.random.Random

data class Enemy (val x: Int, val y :Int, val delta :Int) //Enemy

//function that makes the enemy shot move along the y axis
fun moveEnemy (enemy : Enemy) : Int {
    return  enemy.y + enemy.delta
}