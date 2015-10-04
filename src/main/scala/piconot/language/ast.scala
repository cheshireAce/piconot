package piconot.language

/**
 * @author dhouck apinson
 */

// We couldn’t get Enumeration to work
abstract sealed class Direction(val absolute: Boolean) {
  def of(that: Direction): Direction = this
}
case object North extends Direction(true)
case object East extends Direction(true)
case object South extends Direction(true)
case object West extends Direction(true)
case object Forward extends Direction(false) {
  override def of(that: Direction): Direction = that
}
case object Back extends Direction(false) {
  override def of(that: Direction): Direction = Left of(Left of(that))
}
case object Left extends Direction(false) {
  override def of(that: Direction): Direction = that match {
    case North => West
    case West => South
    case South => East
    case East => North
    case Forward => Left
    case Left => Back
    case Back => Right
    case Right => Forward
  }
  
}
case object Right extends Direction(false) {
  override def of(that: Direction): Direction = Left of(Back of(that))
}

abstract sealed class Action
case class Go(direction: Direction) extends Action {
  override def toString: String = "go " + (direction toString)
}
case class Turn(direction: Direction) extends Action {
  override def toString: String = "turn " + (direction toString)
}


class Rule(val surroundings: Map[Direction, Boolean], val actions: Seq[Action],
    val transition: Option[Name]) {
  override def toString: String = {
    val surroundingString = surroundings map {
      case (dir, open) =>
        val letter = (dir toString) charAt 0
        val description = if (open) "open" else "closed"
        letter + description
    }
    val actionString = (actions reverse) mkString " "
    val transitionString = transition map {"transition \"" + _ + "\""} getOrElse ""
    s"rule $surroundingString $actionString $transitionString"
  }
}

class State(val name: Name, val rules: List[Rule]) {
  override def toString: String = {
    "State \"" + name + "\"\n\t" + ((rules reverse) mkString "\n\t") + "\n"
  }
}
