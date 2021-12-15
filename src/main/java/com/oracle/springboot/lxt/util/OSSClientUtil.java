package com.oracle.springboot.lxt.util;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.Random;

/**
     * 阿里云 OSS工具类
     *
     * @author tuxu
     * @date 2017年9月30日下午3:38:09
     * @version 1.0
     */
 public class OSSClientUtil {

    public static final Logger logger = LoggerFactory.getLogger(OSSClientUtil.class);
    // endpoint  域名 机房
    private String endpoint = "oss-cn-beijing.aliyuncs.com";
    // accessKey  用户的Id
    private String accessKeyId = "LTAI5tS35eDcvXP7h8wNKvGR";
    // 用户的秘钥
    private String accessKeySecret = "3Cj0AFDf5nFM5f0fTfIsehkYpnfVxW";
    // 仓库
    private String bucketName = "lxt-oracle-test";
    // 文件存储目录
    private String filedir = "images/";

    private  OSSClient ossClient=new OSSClient(endpoint, accessKeyId, accessKeySecret);




    /**
     * 上传本地硬盘图片
     * [f:   pic   1.jpg]
     *
     * @param url
     */
    public  void uploadImg2Oss(String url,String newName) throws Exception {
        File fileOnServer = new File(url);
        FileInputStream fin;
        try {
            fin = new FileInputStream(fileOnServer);
            this.uploadFile2OSS(fin,newName);
        } catch (FileNotFoundException e) {
            throw new Exception("图片上传失败");
        }
    }
    /**
     上传springmvc的处理器转发过来的图片
     **/
    public  String uploadImg2Oss(MultipartFile file) throws Exception {
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new Exception("上传图片大小不能超过10M！");
        }
        String originalFilename = file.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        Random random = new Random();
        String name = random.nextInt(10000) + System.currentTimeMillis() + substring;
        try {
            InputStream inputStream = file.getInputStream();
            this.uploadFile2OSS(inputStream, name);
            return name;
        } catch (Exception e) {
            throw new Exception("图片上传失败");
        }
    }

    /**
     * 获得图片的访问路径
     *
     * @param fileUrl
     * @return
     */
    public  String getImgUrl(String fileUrl) {
        System.out.println(fileUrl);
        if (!StringUtils.isEmpty(fileUrl)) {
            String[] split = fileUrl.split("/");
            return this.getUrl(this.filedir + split[split.length - 1]);
        }
        return null;
    }

    /**
     * 上传到OSS服务器 如果同名文件会覆盖服务器上的
     *
     * @param instream
     *            文件流
     * @param fileName
     *            文件名称 包括后缀名
     * @return 出错返回"" ,唯一MD5数字签名
     */
    public String uploadFile2OSS(InputStream instream, String fileName) {
        String ret = "";
        try {
            // 创建上传Object的Metadata
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(instream.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(getcontentType(fileName.substring(fileName.lastIndexOf("."))));
            objectMetadata.setContentDisposition("inline;filename=" + fileName);
            if(!ossClient.doesBucketExist(bucketName)){
                CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
                ossClient.createBucket(createBucketRequest);
            }
            // 上传文件
            PutObjectResult putResult = ossClient.putObject(bucketName, filedir + fileName, instream, objectMetadata);
            ret = putResult.getETag();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                if (instream != null) {
                    instream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * Description: 判断OSS服务文件上传时文件的contentType
     *
     *        filenameExtension    文件后缀
     * @return String
     */
    public  String getcontentType(String filenameExtension) {
        if (filenameExtension.equalsIgnoreCase("bmp")) {
            return "image/bmp";
        }
        if (filenameExtension.equalsIgnoreCase("gif")) {
            return "image/gif";
        }
        if (filenameExtension.equalsIgnoreCase("jpeg") || filenameExtension.equalsIgnoreCase("jpg")
                || filenameExtension.equalsIgnoreCase("png")) {
            return "image/jpeg";
        }
        if (filenameExtension.equalsIgnoreCase("html")) {
            return "text/html";
        }
        if (filenameExtension.equalsIgnoreCase("txt")) {
            return "text/plain";
        }
        if (filenameExtension.equalsIgnoreCase("vsd")) {
            return "application/vnd.visio";
        }
        if (filenameExtension.equalsIgnoreCase("pptx") || filenameExtension.equalsIgnoreCase("ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (filenameExtension.equalsIgnoreCase("docx") || filenameExtension.equalsIgnoreCase("doc")) {
            return "application/msword";
        }
        if (filenameExtension.equalsIgnoreCase("xml")) {
            return "text/xml";
        }
        return "image/jpeg";
    }

    /**
     * 获得url链接
     *
     * @param fileName   文件名
     * @return
     */
    public  String getUrl(String fileName) {
        // 设置URL过期时间为10年 3600l* 1000*24*365*10

        Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
        // 生成URL
        URL url = ossClient.generatePresignedUrl(bucketName, filedir+fileName, expiration);
        if (url != null) {
            return url.toString();
        }
        return null;
    }

    /**
     * @Description: 删除文件
     * @Author:      tuxu
     * @Param:
     * @Return
     **/
    public  void deleteFile2OSS(String fileName){
        //删除文件，需要告诉阿里云文件地址： 仓库名/文件夹名/文件名
        ossClient.deleteObject(bucketName, filedir+fileName);
    }
      /**
       * @Description: 文件下载
       * @Author:      Administrator
       * @Param:       [fileName, response]
       * @Return       void
        **/
		public  void downloadFile2OSS(String fileName,HttpServletResponse response) throws Exception {
         // 调用ossClient.getObject返回一个OSSObject实例，该实例包含文件内容及文件元信息。
OSSObject ossObject = ossClient.getObject(bucketName, filedir+fileName);
// 调用ossObject.getObjectContent获取文件输入流，可读取此输入流获取其内容。
InputStream content = ossObject.getObjectContent();
if (content != null) {
    BufferedInputStream bufferedInputStream=new BufferedInputStream(content);

   byte[] buffer=new byte[1024];
   int len;
   OutputStream out=response.getOutputStream();
   while((len=bufferedInputStream.read(buffer))!=-1){
       out.write(buffer);
   }
   out.flush();
   bufferedInputStream.close();
   content.close();
   out.close();
}
// 关闭OSSClient。
ossClient.shutdown();            

		}

    }
