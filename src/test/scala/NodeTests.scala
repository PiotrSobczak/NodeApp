import org.scalatest._
import spray.json._
import org.scalatest.Matchers._

class NodeTests extends FlatSpec with Matchers {

  "Node" should "match child node with parent Node" in {
    val child = new Node(0, "AA")
    val parent = new Node(1, "A")
    val child2 = new Node(2, "AB")
    val parent2 = new Node(3, "A")
    val child3 = new Node(4, "CDEFG")
    val parent3 = new Node(5, "CDEF")
    assert(child.isChild(parent))
    assert(child2.isChild(parent2))
    assert(child3.isChild(parent3))
  }
 
  it should " not match node with non-parent Node" in {
    val child = new Node(1, "AAA")
    val parent = new Node(2, "A")
    val child2 = new Node(3, "ABC")
    val parent2 = new Node(4, "AC")
    val child3 = new Node(5, "AA")
    val parent3 = new Node(6, "AA")
    val child4 = new Node(7, "A")
    val parent4 = new Node(8, "AA")
    assert(!child.isChild(parent))
    assert(!child2.isChild(parent2))
    assert(!child3.isChild(parent3))
    assert(!child3.isChild(parent3))
    assert(!child4.isChild(parent4))
  }
  
  it should " serialize into valid json" in {
    val node1 = new Node(1, "AA")
    var node2 = new Node(2, "A")
    node2.addChild(node1)

    noException should be thrownBy {
      node1.toJson().parseJson
    }
    
    noException should be thrownBy {
      node2.toJson().parseJson
    }
  }
  
   it should " add child node" in {
    val childNode = new Node(0, "AA")
    var parentNode = new Node(1, "A")
    parentNode.addChild(childNode)
    assert(parentNode.childNodes.length == 1)
    
    parentNode.addChild(new Node(2, "AB"))
    parentNode.addChild(new Node(3, "AC"))
    assert(parentNode.childNodes.length == 3)
  }
  
}