
object NodeApp {
  /** Matching child nodes with their parents */
  def matchNodes(childNodes: List[Node], parentNodes: List[Node]) : List[Node] = {
    var parentNodesMatched : List[Node] = List()
    for(parentNode <- parentNodes) {
      for(childNode <- childNodes) {
        if (childNode.isChild(parentNode)) {
          parentNode.addChild(childNode)
        }
      }
      parentNodesMatched = parentNode ::parentNodesMatched
    }
    return parentNodesMatched
  }
  
  /** Parsing nodes to json */
  def serializeNodes(nodes: List[Node]) : String = {
    val nodesJsons = nodes.map(x => x.toJson())
    val nodesJson = "[" + nodesJsons.mkString(", ") + "]"
    return nodesJson
  }
  
  /** Parsing sheet path */
  def parseArgs(args: Array[String]) : String = {
    val usage = """Usage: nodeApp --csv path-to-csv""";
    assert(args.length == 2, "Invalid usage! " + usage)
    
    var sheetPath : String = ""
    args.sliding(2, 2).toList.collect {
      case Array("--csv", argCsvPath: String) => sheetPath = argCsvPath
    }
    return sheetPath
  }
  
  def main(args: Array[String]) {
    val sheetPath = parseArgs(args)
    val sheet =  Utilities.loadSheet(sheetPath)
    val nodeTuples = Utilities.parseSheet(sheet)
    val nodesByLevel = nodeTuples.groupBy(_._1).mapValues(x => x.map(y => new Node(y._2, y._3)))    
    val nodesMatched = matchNodes(matchNodes(nodesByLevel(3), nodesByLevel(2)), nodesByLevel(1))  
    
    println(serializeNodes(nodesMatched))
  }  
}