
class Node(val _name: String) {
  var name: String = _name
  var childNodes: List[Node] = List()
  
  def addChild(_node: Node) {
    childNodes = _node :: childNodes
  }
  
  def isChild(_node: Node) : Boolean = {
    return (name.substring(0, name.length()-1) == _node.name)
  }
  
   def toJson() : String = {
    var message = "{\"name\":" + name + ", \"childNodes\": ["
    for ((childNode, i) <- childNodes.zipWithIndex) {
      message = message + childNode.toJson
      if (i != childNodes.length - 1) {
        message = message + ", "
      }
    }
    message = message +  "]}"
    return message;
  }
}