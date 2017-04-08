package com.chinacreator.c2.qyb.fs.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chinacreator.c2.dao.Dao;
import com.chinacreator.c2.dao.DaoFactory;
import com.chinacreator.c2.dao.mybatis.enhance.Page;
import com.chinacreator.c2.dao.mybatis.enhance.Pageable;
import com.chinacreator.c2.fs.FileServer;
import com.chinacreator.c2.fs.dir.DirFileServer;
import com.chinacreator.c2.fs.exception.FileNotExsitException;
import com.chinacreator.c2.ioc.ApplicationContextManager;
import com.chinacreator.c2.qyb.fs.entity.UploadFile;

/**
 * 
 * 上传文件服务类
 * 
 * @author qiao.li
 * @version 1.0
 * @date 2015-5-29
 */

@Service
public class UploadFileService {
	private FileServer dirFileServer;

	/**
	 * 插入记录
	 * 
	 * @param file
	 */
	public void addUploadFile(UploadFile file) {
		Dao<UploadFile> dao = DaoFactory.create(UploadFile.class);
		dao.insert(file);
	}

	/**
	 * 查询数据库中是否有指定记录
	 * 
	 * @param businesstype
	 * @param name
	 * @param businesskey
	 * @param taskid
	 * @return 有则返回记录
	 */
	public UploadFile existFileWithNameBusinessKeys(String businesstype,
			String name, String businesskey, String businessKey1,
			String businessKey2,String businessKey3) {
		Dao<UploadFile> dao = DaoFactory.create(UploadFile.class);
		UploadFile file = new UploadFile();
		file.setFileName(name);
		file.setBusinessType(businesstype);
		file.setBusinessKey(businesskey);
		file.setBusinessKey2(businessKey2);
		file.setBusinessKey3(businessKey3);
		List<UploadFile> files = dao.select(file);
		if (files == null || files.size() == 0) {
			return null;
		} else {
			return files.get(0);
		}
	}

	/**
	 * 删除指定记录
	 * 
	 * @param file
	 */
	public int deleteFile(UploadFile file) {
		Dao<UploadFile> dao = DaoFactory.create(UploadFile.class);
		return dao.delete(file);
	}

	/**
	 * 更新文件
	 * 
	 * @param file
	 *            带主键属性file
	 */
	public void updateFile(UploadFile file) {
		Dao<UploadFile> dao = DaoFactory.create(UploadFile.class);
		dao.update(file);
	}

	/**
	 * 获取指定id 和 key 的记录条数
	 * 
	 * @param businessType
	 *            请必填
	 * @param businesskey
	 *            请必填
	 * @param taskid
	 * @return
	 */
	public List<UploadFile> getFiles(String businessType, String businessKey,
			String businessKey1, String businessKey2, String businessKey3) {
		Dao<UploadFile> dao = DaoFactory.create(UploadFile.class);
		UploadFile file = new UploadFile();
		file.setBusinessType(businessType);
		file.setBusinessKey(businessKey);
		file.setBusinessKey1(businessKey1);
		file.setBusinessKey2(businessKey2);
		file.setBusinessKey3(businessKey3);
		return dao.select(file);
	}

	/**
	 * 获取指定id 和 key 的记录条数
	 * 
	 * @param businessType
	 *            请必填
	 * @param businesskey
	 *            请必填
	 * @param taskid
	 * @return
	 */
	public int getTotal(String businessType, String businessKey,
			String businessKey1, String businessKey2) {
		Dao<UploadFile> dao = DaoFactory.create(UploadFile.class);
		UploadFile file = new UploadFile();
		file.setBusinessType(businessType);
		file.setBusinessKey(businessKey);
		file.setBusinessKey1(businessKey1);
		file.setBusinessKey2(businessKey2);
		return dao.count(file);
	}

	/**
	 * 获取分页数据
	 * 
	 * @param businessType
	 *            请必填
	 * @param businesskey
	 *            请必填
	 * @param taskid
	 * @param pageable
	 * @return
	 */
	public Page<UploadFile> getFilesPage(String businessType, String businessKey,
			String businessKey1, String businessKey2, String businessKey3, Pageable pageable) {
//		if (businessType == null || businessType == "undefined"
//				|| businessType == "" || businessKey == null
//				|| businessKey == "undefined" || businessKey == "") {
//			return new Page<UploadFile>(pageable, null);
//		}
		Dao<UploadFile> dao = DaoFactory.create(UploadFile.class);
		UploadFile file = new UploadFile();
		file.setBusinessType(businessType);
		file.setBusinessKey(businessKey);
		file.setBusinessKey1(businessKey1);
		file.setBusinessKey2(businessKey2);
		file.setBusinessKey3(businessKey3);
		return dao.selectByPage(file, pageable, null, true);
	}

	// 编程方式获取spring目录存储bean
	private FileServer getDirFileServer() {
		if (dirFileServer == null) {
			dirFileServer = ApplicationContextManager.getContext().getBean(
					"dirFileServer", FileServer.class);
		}
		return dirFileServer;
	}

	/**
	 * 文件处理器处理文件删除
	 */
	@Transactional
	public boolean processDelete(String path, Map<String, Object> arg1)
			throws Exception {
		try {
			Dao<UploadFile> dao = DaoFactory.create(UploadFile.class);
			FileServer server = this.getDirFileServer();
			if (server.delete(path, true)) {
				UploadFile uf = new UploadFile();
				uf.setFilePath(path);
				UploadFile con = dao.selectOne(uf);
				int r = dao.delete(con);
				if (r == 0) {
					throw new RuntimeException(); // 回滚
				}
				return true;
			} else if (existFileWithPath(path)) {
				UploadFile uf = new UploadFile();
				uf.setFilePath(path);

			}

		} catch (FileNotFoundException e) {
			throw new FileNotExsitException(path);
		}
		return false;
	}

	/**
	 * 查看数据库中是否有此path记录文件
	 * 
	 * @param path
	 * @return
	 */
	public boolean existFileWithPath(String path) {
		Dao<UploadFile> dao = DaoFactory.create(UploadFile.class);
		UploadFile file = new UploadFile();
		file.setFilePath(path);
		List<UploadFile> files = dao.select(file);
		if (files == null || files.size() == 0) {
			return false;
		} else {
			return true;
		}
	}
	/**
	 * 根据业务类型和key获取附件
	 * @param businessKey
	 * @param businessType
	 * @return
	 */
	public List<UploadFile> getFileList(String businessKey,String businessType){
		List<UploadFile> list = new ArrayList<UploadFile>();
		UploadFile con=new UploadFile();
		con.setBusinessType(businessType);
		con.setBusinessKey(businessKey);
		Dao<UploadFile> dao = DaoFactory.create(UploadFile.class);
		list=dao.select(con);
		return list;
	}
	
	/**
	 *  根据path fileservice 返回文件
	 * @param path
	 * @param dirFileServiceName
	 * @return
	 */
	public File getAttachFile(String path, String dirFileServiceName) {
		DirFileServer dirFileServer;
		if (dirFileServiceName == null) {
			dirFileServer = ApplicationContextManager.getContext()
					.getBean("dirFileServer", DirFileServer.class);
		} else {
			dirFileServer = ApplicationContextManager.getContext()
					.getBean(dirFileServiceName, DirFileServer.class);
		}
		return dirFileServer.getFile(path);
	}	
}
