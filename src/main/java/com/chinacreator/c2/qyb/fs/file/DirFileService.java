package com.chinacreator.c2.qyb.fs.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinacreator.c2.fs.FileInput;
import com.chinacreator.c2.fs.FileMetadata;
import com.chinacreator.c2.fs.FileServer;
import com.chinacreator.c2.fs.dir.impl.SimpleDirFileServer;
import com.chinacreator.c2.ioc.ApplicationContextManager;
import com.chinacreator.c2.qyb.fs.dir.process.CommonUploadFileProcess;

@Service
public class DirFileService {
	FileServer simpleDirFileServer;
	@Autowired
	CommonUploadFileProcess commonUploadFileProcess;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addFileAttach(File file, String path, String businessType,
			String businessKey,String businessKey1,String businessKey2,
			String businessKey3,String fileServer,Map params) {
		if(params == null){
			params = new HashMap();
		}
		params.put(CommonUploadFileProcess.PARAM_TYPE, new String[] { businessType });
		params.put(CommonUploadFileProcess.PARAM_KEY, new String[] { businessKey });
		params.put(CommonUploadFileProcess.PARAM_KEY1, new String[] { businessKey1 });		
		params.put(CommonUploadFileProcess.PARAM_KEY2, new String[] { businessKey2 });	
		params.put(CommonUploadFileProcess.PARAM_KEY3, new String[] { businessKey3 });	
		params.put(CommonUploadFileProcess.PARAM_SERVER, new String[] { fileServer });
		if(path == null){ //从fileServer获取path
			
		}
		params.put(CommonUploadFileProcess.PARAM_PATH, new String[] { path });
		addFileAttach(file, path, params);
	}	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addFileAttach(File file, String path, Map params) {
		FileMetadata meta = new FileMetadata();
		meta.setFilesize(file.getTotalSpace());
		meta.setName(file.getName());
		meta.setPath(path);
		// meta.setMimetype(file.g);

		List fileInputList = new ArrayList();
		
		FileInput fileInput = new FileInput();
		fileInput.setFileMetadata(meta);
		try {
			fileInput.setInputStream(new FileInputStream(file));
			fileInputList.add(fileInput);
			
			commonUploadFileProcess.processUpload(fileInputList, params);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	public void addFileAttach(List<File> files, String path, Map params) {
		for(File file:files){
			addFileAttach(file,path,params);
		}
	}	
	
	public void addFileDir(File file, String path, Map params) {
		FileMetadata meta = new FileMetadata();
		meta.setFilesize(file.getTotalSpace());
		meta.setName(file.getName());
		meta.setPath(path);
		// meta.setMimetype(file.g);
		try {
			 InputStream in = new FileInputStream(file);
			 simpleDirFileServer = getDirFileServer(params);
			 if(simpleDirFileServer instanceof DynamicPathDirFileServer){
				 simpleDirFileServer = (DynamicPathDirFileServer) simpleDirFileServer;
				 ((DynamicPathDirFileServer) simpleDirFileServer).add(in, meta, path);
			 }else{
				 simpleDirFileServer.add(in, meta);
			 }
			 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}	

	// 编程方式获取spring目录存储bean
	private FileServer getDirFileServer(Map<String, Object> map) {
		String[] myDir1 = (String[]) map.get("myDir");
		if (myDir1 != null) {
			String myDir = myDir1[0];
			if (myDir != null && !"undefined".equals(myDir) && !"".equals(myDir)) {
				simpleDirFileServer = ApplicationContextManager.getContext().getBean(myDir, SimpleDirFileServer.class);
			} else {
				simpleDirFileServer = ApplicationContextManager.getContext().getBean("qybDirFileServer", SimpleDirFileServer.class);			
			}
		} else {
			simpleDirFileServer = ApplicationContextManager.getContext().getBean("qybDirFileServer", SimpleDirFileServer.class);			
		}
		return simpleDirFileServer;
	}
}
