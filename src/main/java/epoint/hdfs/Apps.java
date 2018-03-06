package epoint.hdfs;

import com.sun.jdi.connect.Connector;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URI;

public class Apps {
    private static Logger logger=Logger.getLogger(Apps.class);
    public static void main(String[] args) throws IOException {
        String hdfsuri="hdfs://192.168.188.50:9000";
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", hdfsuri);
        FileSystem fs = FileSystem.get(URI.create(hdfsuri), conf);

        Path filepath=new Path(hdfsuri + "/" + "anaconda-ks.cfg");
        FSDataInputStream inputStream = fs.open(filepath);
        byte[] b = new byte[1024];
        int numBytes = 0;
        while ((numBytes = inputStream.read(b)) > 0) {
            System.out.println(new String(b));
        }


        //fs.copyFromLocalFile(new Path("/Users/ijarvis/VirtualBoxVMs/iso/CentOS-7-x86_64-Minimal-1708.iso"),new Path(hdfsuri+"/"));



    }



}
