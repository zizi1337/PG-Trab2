import pt.isel.canvas.*

data class Spaceship (val xCenter : Int, val tirox : Int?, val tiroy : Int?) //Spaceship

//function that draws the spaceship
fun Canvas.drawSapceship (xCenter : Int) {
    this.drawRect(xCenter, 450, 50, 10, GREEN) // Draws spaceship rectangle (green)
    this.drawRect(xCenter + 23, 450 - 5, 4, 5, YELLOW) // Draws spaceship rectangle (yellow)
}

//function that draws the spaceship shot
fun Canvas.drawShot (tirox: Int, tiroy: Int) {
    drawRect(tirox + 23,tiroy,4,7, WHITE)
}

//function that sum offsetVector and tiroy
fun Canvas.positionShot (tiroy: Int) : Int? {
    if (tiroy > -10)
        return (tiroy - 4)
    else
        return null
}




