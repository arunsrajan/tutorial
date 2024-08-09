import logging
import os

from pyspark import SparkContext
from pyspark.sql import SparkSession
from pyspark.sql.types import StructType, StructField, LongType, DoubleType, StringType

logging.basicConfig(level=logging.INFO, format="%(asctime)s - %(name)s - %(levelname)s - %(message)s")
logger = logging.getLogger("MinIOSparkJob")

spark = SparkSession.builder.getOrCreate()
#.config("spark.driver.extraJavaOptions", "-Djavax.net.debug=all -Djavax.net.ssl.trustStore=C:/DEVELOPMENT/spark/spark-3.5.1-bin-hadoop3/bin/truststore.jks -Djavax.net.ssl.trustStorePassword=admin12345$").config("spark.executor.extraJavaOptions", "-Djavax.net.debug=all -Djavax.net.ssl.trustStore=C:/DEVELOPMENT/spark/spark-3.5.1-bin-hadoop3/bin/truststore.jks -Djavax.net.ssl.trustStorePassword=admin12345$").getOrCreate()
#.config("spark.hadoop.fs.s3a.endpoint","https://127.0.0.1:9000").config("spark.hadoop.fs.s3a.access.key","XmnwPrE0powsLEQvLO2c").config("spark.hadoop.fs.s3a.secret.key","OwhMKq8jQQIzo6J0zoWE7bOlWoSGr2sfTZYuQhdb").config("spark.hadoop.fs.s3a.connection.ssl.enabled","true").config("spark.hadoop.fs.s3.endpoint","https://127.0.0.1:9000").config("spark.hadoop.fs.s3.awsAccessKeyId", "XmnwPrE0powsLEQvLO2c").config("spark.hadoop.fs.s3.awsSecretAccessKey","OwhMKq8jQQIzo6J0zoWE7bOlWoSGr2sfTZYuQhdb").config("spark.hadoop.fs.s3.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem").config("spark.hadoop.fs.s3a.aws.credentials.provider","org.apache.hadoop.fs.s3a.AnonymousAWSCredentialsProvider")


# def load_config(spark_context: SparkContext):
  #   spark_context._jsc.hadoopConfiguration().set("com.amazonaws.services.s3.disableGetObjectMD5Validation", "true")
  #   spark_context._jsc.hadoopConfiguration().set("fs.s3.endpoint", "https://127.0.0.1:9000")
  #   spark_context._jsc.hadoopConfiguration().set("fs.s3.awsAccessKeyId", "XmnwPrE0powsLEQvLO2c")
  #   spark_context._jsc.hadoopConfiguration().set("fs.s3.awsSecretAccessKey",
    #                                              "OwhMKq8jQQIzo6J0zoWE7bOlWoSGr2sfTZYuQhdb")
  #   spark_context._jsc.hadoopConfiguration().set("fs.s3.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
  #   spark_context._jsc.hadoopConfiguration().set("fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
 #    spark_context._jsc.hadoopConfiguration().set("fs.s3a.access.key", "XmnwPrE0powsLEQvLO2c")
 #    spark_context._jsc.hadoopConfiguration().set("fs.s3a.secret.key",
    #                                              "OwhMKq8jQQIzo6J0zoWE7bOlWoSGr2sfTZYuQhdb")
   #  spark_context._jsc.hadoopConfiguration().set("fs.s3a.endpoint", "https://127.0.0.1:9000")
  #   spark_context._jsc.hadoopConfiguration().set("fs.s3a.ssl.enabled", "true")
  #   spark_context._jsc.hadoopConfiguration().set("fs.s3a.ssl.keystore.location", "C:/DEVELOPMENT/spark/spark-3.5.1-bin-hadoop3/bin/truststore.jks")
   #  spark_context._jsc.hadoopConfiguration().set("fs.s3a.ssl.keystore.password", "admin12345$")
  #   spark_context._jsc.hadoopConfiguration().set("fs.s3a.ssl.truststore.location", "C:/DEVELOPMENT/spark/spark-3.5.1-bin-hadoop3/bin/truststore.jks")
   #  spark_context._jsc.hadoopConfiguration().set("fs.s3a.ssl.truststore.password", "admin12345$")
  #   spark_context._jsc.hadoopConfiguration().set("fs.s3a.connection.ssl.enabled", "true")
   #  spark_context._jsc.hadoopConfiguration().set("fs.s3a.path.style.access", "true")
   #  spark_context._jsc.hadoopConfiguration().set("fs.s3a.attempts.maximum", "1")
   #  spark_context._jsc.hadoopConfiguration().set("fs.s3a.connection.establish.timeout", "5000")
   #  spark_context._jsc.hadoopConfiguration().set("fs.s3a.connection.timeout", "5000")


# load_config(spark.sparkContext)

# Define schema for NYC Taxi Data
schema = StructType([
    StructField('VendorID', LongType(), True),
    StructField('tpep_pickup_datetime', StringType(), True),
    StructField('tpep_dropoff_datetime', StringType(), True),
    StructField('passenger_count', DoubleType(), True),
    StructField('trip_distance', DoubleType(), True),
    StructField('RatecodeID', DoubleType(), True),
    StructField('store_and_fwd_flag', StringType(), True),
    StructField('PULocationID', LongType(), True),
    StructField('DOLocationID', LongType(), True),
    StructField('payment_type', LongType(), True),
    StructField('fare_amount', DoubleType(), True),
    StructField('extra', DoubleType(), True),
    StructField('mta_tax', DoubleType(), True),
    StructField('tip_amount', DoubleType(), True),
    StructField('tolls_amount', DoubleType(), True),
    StructField('improvement_surcharge', DoubleType(), True),
    StructField('total_amount', DoubleType(), True)])

# Read CSV file from MinIO
df = spark.read.option("header", "true").schema(schema).csv(
    "s3a://test/taxi-data.csv")
# Filter dataframe based on passenger_count greater than 3
large_passengers_df = df.filter(df.passenger_count > 3)

total_rows_count = df.count()
filtered_rows_count = large_passengers_df.count()
# File Output Committer is used to write the output to the destination (Not recommended for Production)
large_passengers_df.write.format("csv").option("header", "true").save(
    "s3a://test/taxi-small/")

logger.info(f"Total Rows for NYC Taxi Data: {total_rows_count}")
logger.info(f"Total Rows for Passenger Count > 6: {filtered_rows_count}")
