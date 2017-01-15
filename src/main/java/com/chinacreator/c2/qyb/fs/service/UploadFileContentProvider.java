package com.chinacreator.c2.qyb.fs.service;

import java.util.ArrayList;
import java.util.Map;

import com.chinacreator.c2.dao.mybatis.enhance.Page;
import com.chinacreator.c2.dao.mybatis.enhance.Pageable;
import com.chinacreator.c2.ioc.ApplicationContextManager;
import com.chinacreator.c2.qyb.fs.entity.UploadFile;
import com.chinacreator.c2.web.ds.ArrayContentProvider;
import com.chinacreator.c2.web.ds.ArrayContext;

public class UploadFileContentProvider implements ArrayContentProvider {
	
	private UploadFileService uploadfileservice;
	private UploadFileService getUploadFileService(){
        if (uploadfileservice == null) {
        	uploadfileservice = ApplicationContextManager.getContext().getBean(
                    UploadFileService.class);
        }

        return uploadfileservice;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Page<UploadFile> getElements(ArrayContext context) {
		Page<UploadFile> page = new Page<UploadFile>(new Pageable(), new ArrayList());
		if(context != null){
			Map map = context.getCondition();
			String businesskey = (String) map.get("businessKey");
			String businessKey1 = (String) map.get("businessKey1");
			String businessKey2 = (String) map.get("businessKey2");
			String businessKey3 = (String) map.get("businessKey3");
			String businessType = (String) map.get("businessType");
			Pageable pageable = context.getPageable();
			return getUploadFileService().getFilesPage(businessType,businesskey, businessKey1, businessKey2, businessKey3, pageable);
		}
		return page;
	}

}
