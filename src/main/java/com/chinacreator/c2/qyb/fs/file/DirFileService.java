package com.chinacreator.c2.qyb.fs.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.chinacreator.c2.fs.FileInput;
import com.chinacreator.c2.fs.FileMetadata;
import com.chinacreator.c2.qyb.fs.dir.process.CommonUploadFileProcess;

public class DirFileService {
	@Autowired
	SimpleDirFileServer simpleDirFileServer;
	@Autowired
	CommonUploadFileProcess commonUploadFileProcess;

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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void addFileDir(File file, String path) {
		FileMetadata meta = new FileMetadata();
		meta.setFilesize(file.getTotalSpace());
		meta.setName(file.getName());
		meta.setPath(path);
		// meta.setMimetype(file.g);
		try {
			 InputStream in = new FileInputStream(file);
			 simpleDirFileServer.add(in, meta);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}	

}