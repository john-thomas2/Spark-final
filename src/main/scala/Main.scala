import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._
import org.apache.hadoop.shaded.org.eclipse.jetty.websocket.common.frames.DataFrame
import java.sql.Timestamp
import scala.collection.mutable.ListBuffer
import org.apache.spark.sql.Dataset
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import scala.io.Source

case class configclass(ccode:Int ,pk:String)

object Main {

  def main(args: Array[String]): Unit = {
    val input = args(0)
    val output = args(1)
    val config = args(2)
    val epoch = args(3).toLong

    val spark = SparkSession.builder().appName("Spark-final").getOrCreate()
    spark.conf.set("spark.sql.session.timeZone", "UTC")
    spark.conf.set("spark.sql.sources.partitionOverwriteMode","dynamic")

    val fs = FileSystem.get(spark.sparkContext.hadoopConfiguration)
    val inputStream = fs.open(new Path(config))
    val jsonString = Source.fromInputStream(inputStream).mkString

    import spark.implicits._
    implicit val formats = DefaultFormats

    val dfinp = spark.read.option("header","true").csv(input)
    
    val reqdf = dfinp.withColumn("epoch",lit(epoch)).filter(col("TimeStamp")<=to_timestamp(col("epoch")))

    val configList = parse(jsonString).extract[List[JObject]].map { jsonObj =>
    val ceiCode = (jsonObj \ "cei_code").extractOpt[Int].getOrElse(-1) 
    val primaryKey = (jsonObj \ "primary_key").extract[String]

    configclass(ceiCode, primaryKey)
    }

    configList.foreach { config =>
   val ccode = config.ccode
   val pk = config.pk

      val wSpec = Window.partitionBy(pk).orderBy($"TimeStamp".desc)
      val fdf = reqdf.where(col("cei_code") === ccode).withColumn("Rank", row_number().over(wSpec))

      val rank1DF = fdf.filter(col("Rank") === 1).withColumn("updated_at",lit(col("TimeStamp"))).drop("primary_key","Rank","TimeStamp","epoch")

      rank1DF.show()

      rank1DF.write.partitionBy("updated_at","cei_code").mode("overwrite").csv(output)
      
    }

    spark.stop()
}
}