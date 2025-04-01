package com.toki.web.admin.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.toki.common.config.MinioProperties;
import com.toki.web.admin.service.FileService;
import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

/**
 * @author toki
 */
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    @Override
    public String upload(MultipartFile file) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String bucketName = minioProperties.getBucketName();
        // 检查bucket是否存在
        final boolean isExist = minioClient.bucketExists(
                BucketExistsArgs.builder()
                        .bucket(bucketName)
                        .build()
        );
        // 不存在则创建
        if (!isExist) {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(bucketName)
                            .build()
            );
            // 设置访问策略
            minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
                    .bucket(bucketName)
                    .config(createBucketPolicyConfig(bucketName))
                    .build());
        }

        // 上传文件 putObject方法可以读取流
        // 构建文件名： 日期+UUID+文件名
        String fileName = DateUtil.format(LocalDateTime.now(), "yyyyMMdd")
                + "/" + IdUtil.fastSimpleUUID()
                + "-" + file.getOriginalFilename();
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        // 获取文件流、文件大小、每次读取字节数 -1表自动设置
                        .stream(file.getInputStream(), file.getSize(), -1)
                        // 设置对应的内容类型，保证浏览器可以正确显示
                        .contentType(file.getContentType())
                        .object(fileName)
                        .build()
        );
        // uploadObject方法只能上传本地磁盘文件
//            minioClient.uploadObject(
//                    UploadObjectArgs.builder()
//                            .bucket(bucketName)
//                            // 文件名
//                            .filename(file.getOriginalFilename())
//                            // 文件在Minio中的新名称
//                            .object(file.getOriginalFilename())
//                            .build()
//            );

        // 返回文件URL
        return StrUtil.join("/", minioProperties.getEndpoint(), bucketName, fileName);
    }

    /**
     * 创建bucket访问策略JSON字符串 %s为传入的bucket名称(占位符)
     * 只允许bucket创建者写，其他用户读
     **/
    private String createBucketPolicyConfig(String bucketName) {
        return """
                {
                  "Statement" : [ {
                    "Action" : "s3:GetObject",
                    "Effect" : "Allow",
                    "Principal" : "*",
                    "Resource" : "arn:aws:s3:::%s/*"
                  } ],
                  "Version" : "2012-10-17"
                }
                """.formatted(bucketName);
    }
}
