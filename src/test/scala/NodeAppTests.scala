import org.scalatest._
import spray.json._
import org.scalatest.Matchers._

class NodeAppTests extends FlatSpec with Matchers {

  "NodeApp" should "create nested list of nodes" in {
    val topLevelNodes : List[Node] = List(new Node(1, "A"), new Node(2, "B"))
    val mideLevelNodes : List[Node] = List(new Node(3, "AA"), new Node(4, "AB"))
    val bottomLevelNodes : List[Node] = List(new Node(5, "AAA"), new Node(6, "AAB"))
    
    val nodes = NodeApp.matchNodes(NodeApp.matchNodes(bottomLevelNodes, mideLevelNodes), topLevelNodes)
    
    assert(nodes.length == 2)
    val aNode = nodes.filter(x => x.name == "A")(0)
    assert(aNode.childNodes.length == 2)
    val aaNode = aNode.childNodes.filter(x => x.name == "AA")(0)
    assert(aaNode.childNodes.length == 2)       
  }  
  
  "NodeApp" should "not match unrelated nodes" in {
    val topLevelNodes : List[Node] = List(new Node(1, "A"), new Node(2, "B"))
    val mideLevelNodes : List[Node] = List(new Node(3, "AA"), new Node(4, "AB"))
    val bottomLevelNodes : List[Node] = List(new Node(5, "AAA"), new Node(6, "AAB"))
    
    val bottomLevelNodesMatchedWithMid = NodeApp.matchNodes(mideLevelNodes, bottomLevelNodes)
    val bottomLevelNodesMatchedWithTop = NodeApp.matchNodes(topLevelNodes, bottomLevelNodes)
    val aaaNode1 = bottomLevelNodesMatchedWithMid.filter(x => x.name == "AAA")(0)
    val aaaNode2 = bottomLevelNodesMatchedWithTop.filter(x => x.name == "AAA")(0)
    
    assert(aaaNode1.childNodes.length == 0)
    assert(aaaNode2.childNodes.length == 0)      
  }   
}