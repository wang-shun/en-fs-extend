package com.chinacreator.c2.qyb.fs.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tika.mime.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinacreator.c2.fs.FileInput;
import com.chinacreator.c2.fs.FileMetadata;
import com.chinacreator.c2.fs.util.ContentTypeUtil;
import com.chinacreator.c2.fs.util.PathFormat;

/**
 * 本地简单目录文件服务器实现
 * @author hushowly
 */
public abstract class DynamicPathDirFileServer extends QybDirFileServer{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DynamicPathDirFileServer.class);
	private boolean valid = false;
	private String pathFormat;
	private Long maxSize;
	private String allowFiles;
	
	/**
	 * 创建简单目录文件服务器
	 * @param rootPath
	 */
	public DynamicPathDirFileServer(String rootPath) {
		super(rootPath);
	}
	
	
	/**
	 * 创建简单目录文件服务器
	 * @param rootPath
	 */
	public DynamicPathDirFileServer(String rootPath,String pathFormat) {
		super(rootPath,pathFormat);
	}
	
	
	/**
	 * 创建简单目录文件服务器
	 * @param rootPath
	 */
	public DynamicPathDirFileServer(String rootPath,String pathFormat,Long maxSize){
		super(rootPath,pathFormat,maxSize);
	}
	
	/**
	 * 创建简单目录文件服务器
	 * @param rootPath
	 */
	public DynamicPathDirFileServer(String rootPath,String pathFormat,Long maxSize,String allowFiles){
		super(rootPath,pathFormat,maxSize,allowFiles);
	}
	
	public abstract String getDynamicPath(FileInput fileInput, Map params); 
	
	public FileMetadata add(InputStream content,FileMetadata metadata,String path)
			throws IOException {
		FileOutputStream outputStream =null;
		File file=null;

		try {
			
			String fileName=metadata.getName();
			//如果有path 并且没有pathformat 那么就存这个path
			String filePath=path;
			//创建父目录
			if(StringUtils.isNotEmpty(this.pathFormat)){
				
				if(filePath.lastIndexOf(".")!=-1){
					String suffix = fileName.substring(fileName.lastIndexOf("."));
					String preName=fileName.substring(0,fileName.lastIndexOf("."));
					filePath=PathFormat.parse(this.pathFormat,preName);
					filePath+=suffix;
				}else{
					filePath=PathFormat.parse(this.pathFormat,fileName);
				}
				
				file=new File(this.root,filePath);
				
				File parentFile = file.getParentFile();
		        if (parentFile != null&&!parentFile.exists()) {
		            if (!parentFile.mkdirs() && !parentFile.isDirectory()) {
		                throw new IOException("Destination '" + parentFile + "' directory cannot be created");
		            }
		        }
			}else{
				file=new File(this.root,filePath);
			}
			
			//pathFomart后存在重名，自动重命名
			int i=1;
			while(file.exists()){
				String reName=filePath;
				if(filePath.lastIndexOf(".")!=-1){
					String pre=filePath.substring(0,filePath.lastIndexOf("."));
					String sub= filePath.substring(filePath.lastIndexOf("."),filePath.length());
					reName=pre+"["+i+"]"+sub;
				}else{
					reName+="["+i+"]";
				}
				i++;
				file=new File(this.root,reName);
				if(!file.exists()){
					filePath=reName;
					break;
				}
			}
			File parentFile = file.getParentFile();
			while(!parentFile.exists()){
		        if (parentFile != null&&!parentFile.exists()) {
		            if (!parentFile.mkdirs() && !parentFile.isDirectory()) {
		                throw new IOException("Destination '" + parentFile + "' directory cannot be created");
		            }
		        }
			}			
			
			outputStream=new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = content.read(buffer)) != -1) {
				outputStream.write(buffer, 0, length);
			}
			
			metadata.setPath(filePath);
			metadata.setName(file.getName());
			metadata.setFilesize(file.length());
			MediaType type = ContentTypeUtil.getContentType(
					new BufferedInputStream(content),metadata.getName());
			metadata.setMimetype(type.toString());
		} catch (Exception exception) {
			throw new RuntimeException("写文件时出现导常",exception);
		} finally {
			if(null!=outputStream)outputStream.close();
		}
		return metadata;
	}
}
