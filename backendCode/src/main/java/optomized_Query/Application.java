
package optomized_Query;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.functions;

public class Application {

	public static void main(String[] args) {

		SparkConf conf = new SparkConf().setAppName("Text").setMaster("local").set("spark.executor.memory", "1g");
		JavaSparkContext sparkContxt = new JavaSparkContext(conf);
		SQLContext sqlContext = new SQLContext(sparkContxt);  //the user can then use it in order to perform various “sql-like” operations over Datasets and Dataframes.
		

		Dataset<Row> Distributors = sqlContext.read()
				.format("org.apache.spark.sql.execution.datasources.v2.csv.CSVDataSourceV2")
				.option("header", "true")// it will allow to disply header of the source file
				.load("D:/Eclipse Projects/backendCode/src/main/resources/Distributors.csv") // retriving the source distributor file
				.withColumnRenamed("ID", "DistributorID");   // renaiming the id to distributorID to differentiat further in code

		Dataset<Row> Inventory = sqlContext.read()
				.format("org.apache.spark.sql.execution.datasources.v2.csv.CSVDataSourceV2")
				.option("header", "true")// it will allow to display header of the source file
			    .load("D:/Eclipse Projects/backendCode/src/main/resources/Inventory.csv")// Retrieving the source Inventory file
			    .withColumnRenamed("ID", "InventoryID");// Renaming the id to InventoryID to differentiate further in code
		
		
		Dataset<Row> LowStock =Inventory.withColumn("Available", Inventory.col("Stock").divide(Inventory.col("Capacity")).multiply(100));
		            // the above condition creating new column with named "Available" where getting percentage of stock over capacity and storing in Available column
		             LowStock = LowStock.where("Available < 25").drop("Available"); // applying filter to fetch only records below 25%

		
		Dataset<Row> restock_cost = LowStock.join(Distributors,LowStock.col("InventoryID").equalTo(Distributors.col("DistributorID")));
		             // here joining the LowStock result to distributors to get those data which is similar
		             restock_cost = restock_cost.groupBy("InventoryID").agg(functions.min("Cost_per_unit").alias("lowestPrice"));
		             // it give the lowest of every DistributorID
		             restock_cost = restock_cost.withColumnRenamed("InventoryID","id");
		             //then further Renaming to ID
		             restock_cost = restock_cost.join(Inventory,restock_cost.col("id").equalTo(Inventory.col("InventoryID"))).drop("InventoryID");
		             // this join again fetch the data from inventory table 
		             restock_cost = restock_cost.withColumn("limit", restock_cost.col("Capacity").minus(restock_cost.col("Stock")));
		             // this limit column contain the result of capacity minus Stock
		             restock_cost = restock_cost.selectExpr("id","Name","Stock","Capacity","lowestPrice","limit");
		             // Reording the column
	
		LowStock.coalesce(1).write().format("org.apache.spark.sql.execution.datasources.v2.csv.CSVDataSourceV2")
		.option("header", "true")                // it allow header to the file
		.save("D:/New folder (5)/LowStock.csv"); //target file name with directory for low-stock
		
		restock_cost.coalesce(1).write().format("org.apache.spark.sql.execution.datasources.v2.csv.CSVDataSourceV2")
		.option("header", "true")               // it allow header to the file
		.save("D:/New folder (5)/restock_cost.csv");//target file name with directory for restock-cost
	
	
	}
}


