package com.example.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import java.io.IOException;

public class HadoopUtils {
    public static void saveData(String data, String filePath) {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://192.168.10.138:9000");
        try {
            FileSystem fs = FileSystem.get(conf);
            Path path = new Path(filePath);
            org.apache.hadoop.fs.FSDataOutputStream outputStream = fs.create(path);
            outputStream.write(data.getBytes());
            outputStream.close();
            fs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
