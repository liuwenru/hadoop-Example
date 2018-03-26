package ijarvis_hdfs.hdfs;

//对HDFS文件系统的测试


import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class AppTestHDFS {
    //测试从本地上传文件
    @Test
    public void TestFileUpload(){
        EpointHadoopUtil epointHadoopUtil=new EpointHadoopUtil("hdfs://192.168.208.60:9000;root;/epoint_frame_test");
        epointHadoopUtil.upload("/Users/ijarvis/VirtualBoxVMs/iso/CentOS-7-x86_64-Minimal-1708.iso","/epoint_frame_test");
    }


    //测试使用本地文件流的形式上传
    @Test
    public void TestFileUploadWithInputStream() throws FileNotFoundException {
        EpointHadoopUtil epointHadoopUtil=new EpointHadoopUtil("hdfs://192.168.208.60:9000;root;/epoint_frame_test");
        epointHadoopUtil.upload(new FileInputStream("/Users/ijarvis/VirtualBoxVMs/iso/CentOS-7-x86_64-Minimal-1708.iso"),"test.iso","testfolder");
    }
}
