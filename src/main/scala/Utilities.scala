import java.io.FileInputStream
import java.nio.file.{Paths, Files}
import java.io.File
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.xssf.usermodel.{XSSFWorkbook, XSSFSheet}

object Utilities {
  def printSheet(sheet: XSSFSheet) {
    println("-------------------------------------------------------------")
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
    println("-------------------------------------------------------------")
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
  
  def loadSheet(csvPath : String) : XSSFSheet = {
    assert(Files.exists(Paths.get(csvPath)), "csv file does not exist!")
    val myFile = new File(csvPath)
    val fis = new FileInputStream(myFile)
    val workbook = new XSSFWorkbook(fis)
    val mySheet = workbook.getSheetAt(0)
    return mySheet
  }
}