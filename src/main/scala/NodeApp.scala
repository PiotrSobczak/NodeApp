import java.io.File
import scala.io.Source.fromFile 
import java.io.FileInputStream
import java.nio.file.{Paths, Files}
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.ss.usermodel.DateUtil
import scala.collection.JavaConversions._
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileOutputStream
import scala.collection.JavaConversions._

object NodeApp {

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
    println(workbook)
    val mySheet = workbook.getSheetAt(0)
    val rowIterator = mySheet.iterator()

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
                print(cell.getNumericCellValue + "\t")
              }
              case Cell.CELL_TYPE_BOOLEAN => {
                print(cell.getBooleanCellValue + "\t")
              }
              case Cell.CELL_TYPE_BLANK => {
                print("null" + "\t")
              }
              case _ => throw new RuntimeException(" this error occured when reading ")
              //        case Cell.CELL_TYPE_FORMULA => {print(cell.getF + "\t")}
            }
        }
        println("")
    }
    
    println(csvPath)
  }  
}