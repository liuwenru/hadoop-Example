package epoint.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class Apps {
    private static Logger logger=Logger.getLogger(Apps.class);
    public static void main(String[] args) throws IOException {
//        String hdfsuri="hdfs://192.168.188.131:9000";
        String hdfsuri=args[0];
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", hdfsuri);
        FileSystem fs = FileSystem.get(URI.create(hdfsuri), conf);
        Path filepath=new Path(hdfsuri + "/" + args[1]);
        FSDataInputStream inputStream = fs.open(filepath);
        byte[] b = new byte[1024];
        int numBytes = 0;
        while ((numBytes = inputStream.read(b)) > 0) {
            System.out.println(new String(b));
        }
        //fs.copyFromLocalFile(new Path(args[1]),new Path(hdfsuri+"/"));
    }
}
