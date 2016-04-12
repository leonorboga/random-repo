package Configuration

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Setup of the Spark environment
  */
object Spark {
  var context:SparkContext = null

  def spark(): SparkContext ={

    if(context == null) {
      val conf = new SparkConf().setAppName("LunaTech").setMaster("local[*]")
      context = new SparkContext(conf)
    }
    context
  }
}
