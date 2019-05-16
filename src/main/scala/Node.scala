
class Node(val _name: String) {
  var name: String = _name
  var childNodes: List[Node] = List()
  
  def addSubnode(_node: Node) {
    childNodes = _node :: childNodes
  }
  
  def isChild(_node: Node) : Boolean = {
    return (name.substring(0, name.length()-1) == _node.name)
  }
  
  override def toString() : String = {
    var message = "Node(name=" + name + ", childNodes=["
    for (childNode <- childNodes) {
      message = message + childNode.toString
    }
    message = message +  "])"
    return message;
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