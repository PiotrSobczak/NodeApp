
object NodeApp {
  /** Matching child nodes with their parents */
  def matchNodes(lowerNodes: List[Node], upperNodes: List[Node]) : List[Node] = {
    var upperNodesWithLower : List[Node] = List()
    for(upperNode <- upperNodes) {
      for(lowerNode <- lowerNodes) {
        if (lowerNode.isChild(upperNode)) {
          upperNode.addChild(lowerNode)
        }
      }
      upperNodesWithLower = upperNode ::upperNodesWithLower
    }
    return upperNodesWithLower
  }
  
  /** Creating list of node objects from tuples */
  def createNodes(nodes : List[(Int, String)]) : List[Node] = {    
    val firstLevelNodes = nodes.filter(x => x._1 == 1).map(x => new Node(x._2))
    val secondLevelNodes = nodes.filter(x => x._1 == 2).map(x => new Node(x._2))
    val thirdLevelNodes = nodes.filter(x => x._1 == 3).map(x => new Node(x._2))

    val matchedNodes = matchNodes(matchNodes(thirdLevelNodes, secondLevelNodes), firstLevelNodes)  
    return matchedNodes
  }
  
  /** Parsing nodes to json */
  def serializeNodes(nodes: List[Node]) : String = {
    val nodesJsons = nodes.map(x => x.toJson())
    val nodesJson = "{[" + nodesJsons.mkString(", ") + "]}"
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
    Utilities.printSheet(sheet)
    val nodeTuples = Utilities.parseSheet(sheet)
    val nodes = createNodes(nodeTuples)
    println(serializeNodes(nodes))

  }  
}