import org.apache.spark.sql.SparkSession

object ProjetoScala {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().appName(name = "Porjeto Scala").getOrCreate()

    val juros = spark.read.json("hdfs://namenode:8020/user/manoel/data/exercises-data/juros_selic/juros_selic.json")
    juros.collect()
    juros.write.mode(saveMode = "overwrite").parquet(path = "hdfs://namenode:8020/user/manoel/projeto_scala")
    Thread.sleep(100000)

    spark.stop()
  }

}
