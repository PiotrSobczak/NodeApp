import java.io.File
import scala.io.Source.fromFile 
import java.io.FileInputStream
import java.nio.file.{Paths, Files}
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.ss.usermodel.DateUtil
import scala.collection.JavaConversions._
import org.apache.poi.xssf.usermodel.{XSSFWorkbook, XSSFSheet}
import java.io.FileOutputStream
import scala.collection.JavaConversions._

object NodeApp {  
  def printSheet(sheet: XSSFSheet) {    
    val rowIterator = sheet.iterator()
    while(rowIterator.hasNext) {
      val row = rowIterator.next()
        val cellIterator = row.cellIterator()
        while(cellIterator.hasNext) {
          val cell = cellIterator.next()
            cell.getCellType match {
              case Cell.CELL_TYPE_STRING => {
                print(cell.getStringCellValue + "\t")
              }
              case Cell.CELL_TYPE_NUMERIC => {
                print(cell.getNumericCellValue.toInt + "\t")
              }
              case Cell.CELL_TYPE_BOOLEAN => {
                print(cell.getBooleanCellValue + "\t")
              }
              case Cell.CELL_TYPE_BLANK => {
                print("\t")
              }
              case _ => throw new RuntimeException(" this error occured when reading ")
              //        case Cell.CELL_TYPE_FORMULA => {print(cell.getF + "\t")}
              
            }         
        }
        println("")
    }    
  }
  
  def matchNodes(lowerNodes: List[Node], upperNodes: List[Node]) : List[Node] = {
    var upperNodesWithLower : List[Node] = List()
    for(upperNode <- upperNodes) {
      for(lowerNode <- lowerNodes) {
        if (lowerNode.isChild(upperNode)) {
          upperNode.addSubnode(lowerNode)
        }
      }
      upperNodesWithLower = upperNode ::upperNodesWithLower
    }
    return upperNodesWithLower
  }
  
  def parseSheet(sheet: XSSFSheet) : List[(Int, String)] = {
    var nodes : List[(Int, String)] = List()
    
    val rowIterator = sheet.iterator()
    var rowId : Int = 0
    while(rowIterator.hasNext) {
      val row = rowIterator.next()
        val cellIterator = row.cellIterator()
        var colId : Int = 0
        while(cellIterator.hasNext) {
          val cell = cellIterator.next()
          colId += 1
          if (cell.getCellType == Cell.CELL_TYPE_STRING && rowId != 0) {
            nodes = nodes:+((colId, cell.getStringCellValue))
          }          
        }
        rowId += 1  
    }
    return nodes
  }
  
  def groupNodes(nodes : List[(Int, String)]) : List[Node] = {    
    val firstLevelNodes = nodes.filter(x => x._1 == 1).map(x => new Node(x._2))
    val secondLevelNodes = nodes.filter(x => x._1 == 2).map(x => new Node(x._2))
    val thirdLevelNodes = nodes.filter(x => x._1 == 3).map(x => new Node(x._2))

    val matchedNodes = matchNodes(matchNodes(thirdLevelNodes, secondLevelNodes), firstLevelNodes)  
    return matchedNodes
  }
  
  def nodesToJson(nodes: List[Node]) : String = {
    val nodesJsons = nodes.map(x => x.toJson())
    val nodesJson = "{[" + nodesJsons.mkString(", ") + "]}"
    return nodesJson
  }
  
  /** Our main function where the action happens */
  def main(args: Array[String]) {
    val usage = """Usage: nodeApp --csv path-to-csv""";
    assert(args.length == 2, "Invalid usage! " + usage)
    
    var csvPath : String = ""
    args.sliding(2, 2).toList.collect {
      case Array("--csv", argCsvPath: String) => csvPath = argCsvPath
    }
    
    assert(Files.exists(Paths.get(csvPath)), "csv file does not exist!" + usage)
    
    val myFile = new File(csvPath)
    val fis = new FileInputStream(myFile)
    val workbook = new XSSFWorkbook(fis)

    val mySheet = workbook.getSheetAt(0)
    printSheet(mySheet)
    val nodeTuples = parseSheet(mySheet)
    val nodes = groupNodes(nodeTuples)
    
    println(nodesToJson(nodes))

  }  
}