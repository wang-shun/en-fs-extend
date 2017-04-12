package com.chinacreator.c2.qyb.fs.dir.process;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.chinacreator.c2.dao.Dao;
import com.chinacreator.c2.dao.DaoFactory;
import com.chinacreator.c2.fs.DownResult;
import com.chinacreator.c2.fs.FileInput;
import com.chinacreator.c2.fs.FileMetadata;
import com.chinacreator.c2.fs.FileServer;
import com.chinacreator.c2.fs.UploadProcess;
import com.chinacreator.c2.fs.exception.FileNotExsitException;
import com.chinacreator.c2.fs.util.Constants;
import com.chinacreator.c2.fs.util.Constants.HttpType;
import com.chinacreator.c2.fs.web.FileUploadResult;
import com.chinacreator.c2.fs.web.MultiFileUploadResult;
import com.chinacreator.c2.fs.web.Result;
import com.chinacreator.c2.ioc.ApplicationContextManager;
import com.chinacreator.c2.qyb.fs.entity.UploadFile;
import com.chinacreator.c2.qyb.fs.file.DynamicPathDirFileServer;
import com.chinacreator.c2.qyb.fs.service.UploadFileService;
import com.chinacreator.c2.util.UUIDUtil;

/**
 * 
 * 事件工单附件上传处理器
 * 
 * @author qiao.li
 * @version 1.0
 * @date 2015-5-29
 */
// @Service
public class CommonUploadFileProcess extends UploadProcess {
	private FileServer dirFileServer; // 目录存储
	private UploadFileService uploadfileservice;

	public final static String PARAM_TYPE = "businessType";
	public final static String PARAM_KEY = "businessKey";
	public final static String PARAM_KEY1 = "businessKey1";
	public final static String PARAM_KEY2 = "businessKey2";
	public final static String PARAM_KEY3 = "businessKey3";
	public final static String PARAM_SERVER = "myDir";
	public final static String PARAM_PATH = "path";
	
	public CommonUploadFileProcess(String processName) {
		super(processName);
	}

	// 编程方式获取spring目录存储bean
	private UploadFileService getUploadFileServer() {
		if (uploadfileservice == null) {
			uploadfileservice = ApplicationContextManager.getContext().getBean(
					UploadFileService.class);
		}

		return uploadfileservice;
	}

	// 编程方式获取spring目录存储bean
	private FileServer getDirFileServer(Map<String, Object> map) {
		String[] myDir1 = (String[]) map.get("myDir");
		if (myDir1 != null) {
			String myDir = myDir1[0];
			if (myDir != null && !"undefined".equals(myDir) && !"".equals(myDir)) {
				dirFileServer = ApplicationContextManager.getContext().getBean(myDir, FileServer.class);
			} else {
				dirFileServer = ApplicationContextManager.getContext().getBean("qybDirFileServer", FileServer.class);			
			}
		} else {
			dirFileServer = ApplicationContextManager.getContext().getBean("qybDirFileServer", FileServer.class);			
		}
		return dirFileServer;
	}
	
	// 编程方式获取spring目录存储bean
//	private FileServer getDirFileServer1(String myDir) {
//		dirFileServer = ApplicationContextManager.getContext().getBean(
//				myDir, FileServer.class);
//		return dirFileServer;
//	}
	

	/**
	 * 判断文件是否存在
	 */
	@Override
	public boolean exist(String arg0, Map<String, Object> map)
			throws Exception {

		FileServer server = this.getDirFileServer(map);
		return server.exsits(arg0);
	}

	/**
	 * 处理文件删除
	 */
	// @Transactional
//	public boolean processDelete(String path, Map<String, Object> arg1)
//			throws Exception {
//		return this.getUploadFileServer().processDelete(path, arg1);
//	}
	
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
	 * 文件处理器处理文件删除
	 */
//	@Transactional
	public boolean processDelete(String path, Map<String, Object> map)
			throws Exception {
		try {
			Dao<UploadFile> dao = DaoFactory.create(UploadFile.class);
			FileServer server = this.getDirFileServer(map);
			
			UploadFile uf = new UploadFile();
			uf.setFilePath(path);
			UploadFile con = dao.selectOne(uf);
			int r = dao.delete(con);
			if (r == 0) { //删除没成功
				throw new RuntimeException("删除附件表失败"); // 回滚
			}else{
				if (server.delete(path, true)) {
					return true;
				} else {
					//文件删除失败
				}				
			}
		} catch (FileNotFoundException e) {
			throw new FileNotExsitException(path);
		}
		return false;
	}

	/**
	 * 处理文件下载
	 */
	@Override
	// @Transactional
	public DownResult processDown(String fpath, Map<String, Object> map)
			throws Exception {
		try {
			FileServer server = this.getDirFileServer(map);
			DownResult downResult = new DownResult();
			InputStream is = server.get(fpath);
			FileMetadata fm = server.getMetaData(fpath);
			downResult.setInputStream(is);
			downResult.setFileMetadata(fm);
			return downResult;
		} catch (FileNotFoundException e) {
			throw new FileNotExsitException(fpath);
		}
	}

	/**
	 * @param map
	 *            map中key：businessType businessKey 必须！！！
	 *            处理文件上传，把businessType,businesskey... 存入uploadfile描述表
	 */
	@Override
	// @Transactional
	public Result processUpload(List<FileInput> fileList,
			Map<String, Object> map) throws Exception {
		String[] businessTypes = (String[]) map.get("businessType");
		String[] businessKeys = (String[]) map.get("businessKey");
		String[] businessKey1s = (String[]) map.get("businessKey1");
		String[] businessKey2s = (String[]) map.get("businessKey2");
		String[] businessKey3s = (String[]) map.get("businessKey3");
		String[] myDir1 = (String[]) map.get("myDir");

		String businessType = businessTypes[0];
		String businessKey = businessKeys[0];
		String businessKey1 = null;
		String businessKey2 = null;
		String businessKey3 = null;
		//业务模块要求的存储路径
		String[] paths = (String[]) map.get("path");
		String dynamicPath = null;
		if(paths != null){
			dynamicPath = paths[0];
		}
		
		if (businessKey1s != null) {
			businessKey1 = businessKey1s[0];
		}
		if (businessKey2s != null) {
			businessKey2 = businessKey2s[0];
		}
		if (businessKey3s != null) {
			businessKey3 = businessKey3s[0];
		}
		if (businessType == null || businessKey == null) {
			return new FileUploadResult(HttpType.ERROR.ordinal(),
					"businessType businessKey不能为空", null, null, null);
		}
		
		String filename = null;
		UploadFileService ufs = this.getUploadFileServer();

		Result uploadResult = null;

		try {

			if (fileList.size() <= 0)
				throw new Exception("上传文件不能为空");
			FileServer server = this.getDirFileServer(map);
			
			// 为了兼容以前，单附件和多附件格式不能统一
			if (fileList.size() > 1) {
				MultiFileUploadResult mfr = new MultiFileUploadResult(
						HttpType.SUCCESS.ordinal(), "成功");

				for (FileInput fileInput : fileList) {
					InputStream is = fileInput.getInputStream();
					FileMetadata fileMetadata = fileInput.getFileMetadata();
					filename = fileMetadata.getName();
					UploadFile fileexist = ufs.existFileWithNameBusinessKeys(
							businessType, filename, businessKey, businessKey1,
							businessKey2, businessKey3);
					if (fileexist == null) {
						//如果是动态路径文件存储器
						if(server instanceof DynamicPathDirFileServer){
							if(dynamicPath == null || "".equals(dynamicPath) || "undefined".equals(dynamicPath)){
								throw new RuntimeException("动态路径文件存储器 没有传入路径参数");
							}
							fileMetadata = ((DynamicPathDirFileServer)server).add(is,
									fileInput.getFileMetadata(),dynamicPath);							
						}else{
							fileMetadata = server.add(is,
									fileInput.getFileMetadata());							
						}

						// 将保存后的附件信息添加到结果集中
						FileUploadResult fr = new FileUploadResult(
								HttpType.SUCCESS.ordinal(), "成功",
								fileMetadata.getName(), fileMetadata.getPath(),
								Constants.IFRAME_FILE_PREFIX
										+ this.getProcessName() + "/"
										+ fileMetadata.getPath());
						fr.setFilesize(fileMetadata.getFilesize());
						fr.setMimetype(fileMetadata.getMimetype());
						setUploadFileDatabase(fr, businessType, businessKey,
								businessKey1, businessKey2, businessKey3);
						mfr.addFileUploadResult(fr);
					} else {
						this.processDelete(fileexist.getFilePath(), map);
						//如果是动态路径文件存储器
						if(server instanceof DynamicPathDirFileServer){
							if(dynamicPath == null || "".equals(dynamicPath) || "undefined".equals(dynamicPath)){
								throw new RuntimeException("动态路径文件存储器 没有传入路径参数");
							}
							fileMetadata = ((DynamicPathDirFileServer)server).add(is,
									fileInput.getFileMetadata(),dynamicPath);							
						}else{
							fileMetadata = server.add(is,
									fileInput.getFileMetadata());							
						}
						FileUploadResult fr = new FileUploadResult(
								HttpType.SUCCESS.ordinal(), "成功",
								fileMetadata.getName(), fileMetadata.getPath(),
								Constants.IFRAME_FILE_PREFIX
										+ this.getProcessName() + "/"
										+ fileMetadata.getPath());
						fr.setFilesize(fileMetadata.getFilesize());
						fr.setMimetype(fileMetadata.getMimetype());
						setUploadFileDatabase(fr, businessType, businessKey,
								businessKey1, businessKey2, businessKey3);
						uploadResult = fr;

					}
				}
				uploadResult = mfr;
			} else {
				FileInput fileInput = fileList.get(0);
				InputStream is = fileInput.getInputStream();
				FileMetadata fileMetadata = fileInput.getFileMetadata();
				filename = fileMetadata.getName();
				// 判断是否是用户更新同一份文件
				UploadFile fileexist = ufs.existFileWithNameBusinessKeys(
						businessType, filename, businessKey, businessKey1,
						businessKey2, businessKey3);
				if (fileexist == null) {
					//如果是动态路径文件存储器
					if(server instanceof DynamicPathDirFileServer){
						if(dynamicPath == null || "".equals(dynamicPath) || "undefined".equals(dynamicPath)){
							throw new RuntimeException("动态路径文件存储器 没有传入路径参数");
						}
						fileMetadata = ((DynamicPathDirFileServer)server).add(is,
								fileInput.getFileMetadata(),dynamicPath);							
					}else{
						fileMetadata = server.add(is,
								fileInput.getFileMetadata());							
					}
					FileUploadResult fr = new FileUploadResult(
							HttpType.SUCCESS.ordinal(), "成功",
							fileMetadata.getName(), fileMetadata.getPath(),
							Constants.IFRAME_FILE_PREFIX
									+ this.getProcessName() + "/"
									+ fileMetadata.getPath());
					fr.setFilesize(fileMetadata.getFilesize());
					fr.setMimetype(fileMetadata.getMimetype());
					uploadResult = fr;
					setUploadFileDatabase(fr, businessType, businessKey,
							businessKey1, businessKey2, businessKey3);
				} else {
					// 判断是用户更新同一份文件之后，fileserver的updata操作实现会出错，只好删除原来文件再添加。
					this.processDelete(fileexist.getFilePath(), map);
					//如果是动态路径文件存储器
					if(server instanceof DynamicPathDirFileServer){
						if(dynamicPath == null || "".equals(dynamicPath) || "undefined".equals(dynamicPath)){
							throw new RuntimeException("动态路径文件存储器 没有传入路径参数");
						}						
						fileMetadata = ((DynamicPathDirFileServer)server).add(is,
								fileInput.getFileMetadata(),dynamicPath);							
					}else{
						fileMetadata = server.add(is,
								fileInput.getFileMetadata());							
					}
					FileUploadResult fr = new FileUploadResult(
							HttpType.SUCCESS.ordinal(), "成功",
							fileMetadata.getName(), fileMetadata.getPath(),
							Constants.IFRAME_FILE_PREFIX
									+ this.getProcessName() + "/"
									+ fileMetadata.getPath());
					fr.setFilesize(fileMetadata.getFilesize());
					fr.setMimetype(fileMetadata.getMimetype());
					setUploadFileDatabase(fr, businessType, businessKey,
							businessKey1, businessKey2, businessKey3);
					uploadResult = fr;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("上传文件失败:" + e.getMessage());
		}
		// 返回文件上传统一结果集

		return uploadResult;
	}

	/**
	 * 更新数据库
	 * 
	 * @param fr
	 */
	// @Transactional
	private void setUploadFileDatabase(FileUploadResult fr,
			String businessType, String businessKey, String businessKey1,String businessKey2,
			String businessKey3) {
		UploadFileService ufs = this.getUploadFileServer();
		UploadFile uploadfile = new UploadFile();
		String filepath = fr.getPath();
		String filename = fr.getName();
		long filesize = fr.getFilesize();
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.###");
		// 转换成MB大小 作为displayName的一部分
		String sizemb = df.format(filesize / 1048576d) + "MB";
		String displayname = filename + "(" + sizemb + ")";
		String realfiletype;
		if (filename.lastIndexOf(".") == -1) {
			realfiletype = "unkown";
		} else {
			realfiletype = filename.substring(filename.lastIndexOf(".") + 1);
		}
		String xh1 = UUIDUtil.createUUID();
		uploadfile.setFileId(xh1);
		uploadfile.setBusinessKey(businessKey);
		uploadfile.setBusinessKey1(businessKey1);
		uploadfile.setBusinessKey2(businessKey2);
		uploadfile.setBusinessKey3(businessKey3);
		uploadfile.setFilePath(filepath);
		uploadfile.setFileName(filename);
		uploadfile.setFileSize(filesize);
		uploadfile.setFileType(realfiletype);
		uploadfile.setDisplayName(displayname);
		uploadfile.setBusinessType(businessType);
		uploadfile.setUploadTime(new Timestamp(System.currentTimeMillis()));
		ufs.addUploadFile(uploadfile);
		// }
	}

}
