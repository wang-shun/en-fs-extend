package com.chinacreator.c2.qyb.fs.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chinacreator.c2.qyb.fs.entity.UploadFile;
import com.chinacreator.c2.qyb.fs.service.UploadFileService;

@RestController
public class UploadFileController {
	@Autowired
	UploadFileService ups;

	@RequestMapping("s/getuploadfiles")
	public List<UploadFile> getFiles(@RequestParam() String businessType,
			@RequestParam(required = false) String businessKey, @RequestParam(required = false) String businessKey1,
			@RequestParam(required = false) String businessKey2, @RequestParam(required = false) String businessKey3) {
		return ups.getFiles(businessType, businessKey, businessKey1, businessKey2, businessKey3);
	}
}
