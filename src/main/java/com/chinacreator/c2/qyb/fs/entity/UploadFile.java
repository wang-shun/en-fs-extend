package com.chinacreator.c2.qyb.fs.entity;

import java.io.Serializable;

import com.chinacreator.c2.annotation.Column;
import com.chinacreator.c2.annotation.ColumnType;
import com.chinacreator.c2.annotation.Entity;

/**
 * 上传的文件
 * @author 
 * @generated
 */
@Entity(id = "entity:com.chinacreator.c2.qyb.fs.entity.UploadFile", table = "KCOMP_FILE_UPLOAD", ds = "xzzx")
public class UploadFile implements Serializable {
	private static final long serialVersionUID = 1896977231134720L;
	/**
	 *
	 */
	@Column(id = "file_id", type = ColumnType.uuid, datatype = "string64")
	private java.lang.String fileId;

	/**
	 *
	 */
	@Column(id = "file_path", datatype = "string256")
	private java.lang.String filePath;

	/**
	 *
	 */
	@Column(id = "file_name", datatype = "string256")
	private java.lang.String fileName;

	/**
	 *
	 */
	@Column(id = "file_size", datatype = "long")
	private java.lang.Long fileSize;

	/**
	 *
	 */
	@Column(id = "file_type", datatype = "string64")
	private java.lang.String fileType;

	/**
	 *
	 */
	@Column(id = "business_type", datatype = "string20")
	private java.lang.String businessType;

	/**
	 *
	 */
	@Column(id = "business_key", datatype = "string64")
	private java.lang.String businessKey;

	/**
	 *
	 */
	@Column(id = "business_key1", datatype = "string64")
	private java.lang.String businessKey1;

	/**
	 *
	 */
	@Column(id = "store_type", datatype = "boolean")
	private java.lang.Boolean storeType;

	/**
	 *
	 */
	@Column(id = "file_src_path", datatype = "string256")
	private java.lang.String fileSrcPath;

	/**
	 *
	 */
	@Column(id = "file_src_name", datatype = "string256")
	private java.lang.String fileSrcName;

	/**
	 *
	 */
	@Column(id = "upload_time", datatype = "timestamp")
	private java.sql.Timestamp uploadTime;

	/**
	 *
	 */
	@Column(id = "org_id", datatype = "string64")
	private java.lang.String orgId;

	/**
	 *
	 */
	@Column(id = "user_id", datatype = "string64")
	private java.lang.String userId;

	/**
	 *
	 */
	@Column(id = "file_describe", datatype = "string256")
	private java.lang.String fileDescribe;

	/**
	 *
	 */
	@Column(id = "remark1", datatype = "string10")
	private java.lang.String remark1;

	/**
	 *
	 */
	@Column(id = "remark2", datatype = "string10")
	private java.lang.String remark2;

	/**
	 *
	 */
	@Column(id = "display_name", datatype = "string256")
	private java.lang.String displayName;

	/**
	 *
	 */
	@Column(id = "business_key2", datatype = "string256")
	private java.lang.String businessKey2;

	/**
	 *
	 */
	@Column(id = "business_key3", datatype = "string256")
	private java.lang.String businessKey3;

	/**
	 * 设置
	 */
	public void setFileId(java.lang.String fileId) {
		this.fileId = fileId;
	}

	/**
	 * 获取
	 */
	public java.lang.String getFileId() {
		return fileId;
	}

	/**
	 * 设置
	 */
	public void setFilePath(java.lang.String filePath) {
		this.filePath = filePath;
	}

	/**
	 * 获取
	 */
	public java.lang.String getFilePath() {
		return filePath;
	}

	/**
	 * 设置
	 */
	public void setFileName(java.lang.String fileName) {
		this.fileName = fileName;
	}

	/**
	 * 获取
	 */
	public java.lang.String getFileName() {
		return fileName;
	}

	/**
	 * 设置
	 */
	public void setFileSize(java.lang.Long fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * 获取
	 */
	public java.lang.Long getFileSize() {
		return fileSize;
	}

	/**
	 * 设置
	 */
	public void setFileType(java.lang.String fileType) {
		this.fileType = fileType;
	}

	/**
	 * 获取
	 */
	public java.lang.String getFileType() {
		return fileType;
	}

	/**
	 * 设置
	 */
	public void setBusinessType(java.lang.String businessType) {
		this.businessType = businessType;
	}

	/**
	 * 获取
	 */
	public java.lang.String getBusinessType() {
		return businessType;
	}

	/**
	 * 设置
	 */
	public void setBusinessKey(java.lang.String businessKey) {
		this.businessKey = businessKey;
	}

	/**
	 * 获取
	 */
	public java.lang.String getBusinessKey() {
		return businessKey;
	}

	/**
	 * 设置
	 */
	public void setBusinessKey1(java.lang.String businessKey1) {
		this.businessKey1 = businessKey1;
	}

	/**
	 * 获取
	 */
	public java.lang.String getBusinessKey1() {
		return businessKey1;
	}

	/**
	 * 设置
	 */
	public void setStoreType(java.lang.Boolean storeType) {
		this.storeType = storeType;
	}

	/**
	 * 获取
	 */
	public java.lang.Boolean isStoreType() {
		return storeType;
	}

	/**
	 * 设置
	 */
	public void setFileSrcPath(java.lang.String fileSrcPath) {
		this.fileSrcPath = fileSrcPath;
	}

	/**
	 * 获取
	 */
	public java.lang.String getFileSrcPath() {
		return fileSrcPath;
	}

	/**
	 * 设置
	 */
	public void setFileSrcName(java.lang.String fileSrcName) {
		this.fileSrcName = fileSrcName;
	}

	/**
	 * 获取
	 */
	public java.lang.String getFileSrcName() {
		return fileSrcName;
	}

	/**
	 * 设置
	 */
	public void setUploadTime(java.sql.Timestamp uploadTime) {
		this.uploadTime = uploadTime;
	}

	/**
	 * 获取
	 */
	public java.sql.Timestamp getUploadTime() {
		return uploadTime;
	}

	/**
	 * 设置
	 */
	public void setOrgId(java.lang.String orgId) {
		this.orgId = orgId;
	}

	/**
	 * 获取
	 */
	public java.lang.String getOrgId() {
		return orgId;
	}

	/**
	 * 设置
	 */
	public void setUserId(java.lang.String userId) {
		this.userId = userId;
	}

	/**
	 * 获取
	 */
	public java.lang.String getUserId() {
		return userId;
	}

	/**
	 * 设置
	 */
	public void setFileDescribe(java.lang.String fileDescribe) {
		this.fileDescribe = fileDescribe;
	}

	/**
	 * 获取
	 */
	public java.lang.String getFileDescribe() {
		return fileDescribe;
	}

	/**
	 * 设置
	 */
	public void setRemark1(java.lang.String remark1) {
		this.remark1 = remark1;
	}

	/**
	 * 获取
	 */
	public java.lang.String getRemark1() {
		return remark1;
	}

	/**
	 * 设置
	 */
	public void setRemark2(java.lang.String remark2) {
		this.remark2 = remark2;
	}

	/**
	 * 获取
	 */
	public java.lang.String getRemark2() {
		return remark2;
	}

	/**
	 * 设置
	 */
	public void setDisplayName(java.lang.String displayName) {
		this.displayName = displayName;
	}

	/**
	 * 获取
	 */
	public java.lang.String getDisplayName() {
		return displayName;
	}

	/**
	 * 设置
	 */
	public void setBusinessKey2(java.lang.String businessKey2) {
		this.businessKey2 = businessKey2;
	}

	/**
	 * 获取
	 */
	public java.lang.String getBusinessKey2() {
		return businessKey2;
	}

	/**
	 * 设置
	 */
	public void setBusinessKey3(java.lang.String businessKey3) {
		this.businessKey3 = businessKey3;
	}

	/**
	 * 获取
	 */
	public java.lang.String getBusinessKey3() {
		return businessKey3;
	}
}
