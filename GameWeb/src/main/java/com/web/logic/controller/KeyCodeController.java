package com.web.logic.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.web.db.bean.KeyAttrBean;
import com.web.db.bean.KeyBean;
import com.web.db.dao.KeyDao;
import com.web.logic.service.KeyCodeService;
import com.web.utils.GenerateExcel;

@Controller
public class KeyCodeController {

	private static final Logger logger = Logger
			.getLogger(KeyCodeController.class);

	@Autowired
	private KeyCodeService keyCodeService;

	@ResponseBody
	@RequestMapping(value = "generateKey", method = RequestMethod.POST)
	public String generateKey(@RequestParam Map<String, String> map) {
		String itemStr = map.get("itemStr");
		String resStr = map.get("resStr");
		String useServer = map.get("useServer");
		String fgi = map.get("fgi");
		String platform = map.get("platform");
		String deadTime = map.get("deadTime");
		String desc = map.get("desc");
		String generateNum = map.get("generateNum");
		String strKeyType = map.get("keyType");
		if ((itemStr.equals("") && resStr.equals("")) || useServer.equals("")
				|| fgi.equals("") || platform.equals("") || deadTime.equals("")
				|| desc.equals("") || strKeyType.equals("")) {
			logger.error("前端传来的参数错误，有空参数。");
			return "参数有为空的字段";
		}
		Map<String, String> rewardMap = new HashMap<String, String>();
		rewardMap.put("item", itemStr);
		rewardMap.put("resource", resStr);

		KeyAttrBean keyAttrBean = new KeyAttrBean();
		keyAttrBean.setReward(JSONObject.toJSONString(rewardMap));
		keyAttrBean.setDescStr(desc);
		keyAttrBean.setUseServer(Integer.valueOf(useServer));
		keyAttrBean.setFgi(Short.valueOf(fgi));
		keyAttrBean.setPlatform(platform);
		keyAttrBean.setGenerateNum(Short.valueOf(generateNum));
		keyAttrBean.setKeyType(Integer.valueOf(strKeyType));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date = null;
		try {
			date = format.parse(deadTime);
			keyAttrBean.setGenerateTime(System.currentTimeMillis());
			keyAttrBean.setDeadTime(date.getTime());
		} catch (ParseException e) {
			logger.error(e);
			return "error";
		}
		String result = keyCodeService.generateKey(keyAttrBean);

		return result;
	}

	@ResponseBody
	@RequestMapping(value = "getKeyAttrList", method = RequestMethod.GET)
	public String getKeyAttrList() {
		List<KeyAttrBean> list = keyCodeService.getKeyAttrBeanList();
		if (list != null && !list.isEmpty()) {
			return JSONArray.toJSONString(list);
		}
		return "error";
	}

	@RequestMapping(value = "exportExcel", method = RequestMethod.GET)
	public void exportExcel(HttpServletRequest request,
			HttpServletResponse response) {
		String strBatch = request.getParameter("batch");
		short nBatch = Short.valueOf(strBatch);

		String fileName = "激活码.xls";
		String result = "";
		OutputStream os = null;
		try {
			List<KeyBean> list = KeyDao.selectKeysByBatch(nBatch);
			if (list == null || list.isEmpty()) {
				logger.error("未找到激活码列表。batch=" + nBatch);
				response.sendError(500, "未找到激活码列表,batch:" + nBatch);
				return;
			}
			os = response.getOutputStream();
			response.reset();
			response.setHeader("Content-disposition", "attachment;filename="
					+ new String(fileName.getBytes("GBK"), "ISO8859-1"));
			response.setContentType("application/msexcel;charset=UTF-8");
			result = GenerateExcel.exporExcel(os, list);
			if (result == null || result.isEmpty()) {
				logger.error("激活码生成失败,batch:" + nBatch);
				return;
			}
			logger.info("导出激活码配置表成功。");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
