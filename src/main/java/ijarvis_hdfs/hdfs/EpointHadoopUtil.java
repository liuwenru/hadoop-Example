package ijarvis_hdfs.hdfs;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

public class EpointHadoopUtil
{

    /**
     * hdfsurl
     * 
     *  如：hdfs://192.168.147.17:8020
     */
    private String hdfsUrl;

    private Configuration conf;

    private String user;

    private String tmp;

    /**
     * @param hdfsUrl  hdfs://192.168.147.17:8020;hdfs;/tmp
     */
    public EpointHadoopUtil(String hdfsUrl) {
        this.conf = new Configuration();
        if (hdfsUrl.split(";").length > 2) {
            this.hdfsUrl = hdfsUrl.split(";")[0];
            this.user = hdfsUrl.split(";")[1];
            this.tmp = hdfsUrl.split(";")[2];
        }
    }

    /**
     *  以流的方式上传
     *  @param in
     *  @param fileName
     *  @param newFolder 自定义文件夹名，例:tmp/newFolder/文件名
     *  @return
     */
    public String upload(InputStream in, String fileName, String newFolder) {
        String result = hdfsUrl.substring(0, hdfsUrl.lastIndexOf(":"));
        URI uri;
        try {
            uri = new URI(hdfsUrl);
            FileSystem fs = FileSystem.get(uri, conf, user);
            FSDataOutputStream outputStream = fs.create(new Path(tmp + "/" + newFolder + "/" + fileName), true);
            IOUtils.copyBytes(in, outputStream, conf, true);
            result += tmp + "/" + newFolder + "/";
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     *  本地上传文件
     *  @param local 本地文件路径
     *  @param newFolder /tem/newFloder
     *  @return  上传后的文件地址
     */
    public String upload(String local, String newFolder) {
        String result = hdfsUrl.substring(0, hdfsUrl.lastIndexOf(":"));
        URI uri;
        try {
            uri = new URI(hdfsUrl);
            FileSystem fs = FileSystem.get(uri, conf, user);
            Path resP = new Path(local);
            String dest = tmp;
            if (!newFolder.equals("")) {
                if (newFolder.contains("/")) {
                    dest += newFolder;
                }
                else {
                    dest += "/" + newFolder;
                }
            }
            Path destP = new Path(dest);

            if (!fs.exists(destP)) {
                fs.mkdirs(destP);
            }
            fs.copyFromLocalFile(resP, destP);
            fs.close();
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     *  文件下载
     *  @param dest   下载路径
     *  @return 文件流
     */
    public InputStream getDownloadStream(String dest) {
        FileSystem fs;
        FSDataInputStream fsdi = null;
        try {
            fs = FileSystem.get(URI.create(dest), conf, user);
            fsdi = fs.open(new Path(dest));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return fsdi;
    }

    /**
     *  下载文件到本地
     *  @param dest   hdfs路径：hdfs://192.168.147.17/tmp/userSessionuserguid/123.xls
     *  @param local  本地目标路径D://b.xls
     */
    public void downloadLocal(String dest, String local) {
        try {
            FileSystem fs = FileSystem.get(URI.create(dest), conf, user);
            FSDataInputStream fsdi = fs.open(new Path(dest));
            OutputStream output = new FileOutputStream(local);
            IOUtils.copyBytes(fsdi, output, 4096, true);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *  删除
     *  @param filePath 
     *  hdfs://192.168.147.17/tmp/文件名
     *  @return    
     */
    public String deleteFile(String filePath) {
        try {
            Configuration conf = new Configuration();
            //设置ugi，采用程序删除hdfs文件，要设置ugi，避免权限不足
            // conf.set("hadoop.job.ugi", "zaixing,cug-tbauction,#taobao1234");
            FileSystem fs = FileSystem.get(URI.create(filePath), conf);
            fs.delete(new Path(filePath), true);
            fs.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    /**
     *  修改文件名
     *  @param oldPath  全路径
     *  @param newPath  全路径
     */
    public void renameFile(String oldPath, String newPath) {
        FileSystem fs = null;
        try {
            fs = FileSystem.get(new URI(hdfsUrl), conf, user);
            Path srcPath = new Path(oldPath);
            Path dstPath = new Path(newPath);
            fs.rename(srcPath, dstPath);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            if (fs != null) {
                try {
                    fs.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
