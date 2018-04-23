package com.web.utils;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.SheetSettings;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;

import com.web.db.bean.KeyBean;

public class GenerateExcel {

	private static final Logger logger = Logger.getLogger(GenerateExcel.class);

	public static String exporExcel(OutputStream os,List<KeyBean> list) {
		if (list == null || list.isEmpty()) {
			logger.error("激活码列表是个空的list。");
			return null;
		}
		
		List<KeyBean> useBeans = new ArrayList<>();
		List<KeyBean> notUseBeans = new ArrayList<>();
		for (KeyBean bean : list) {
			if (bean.isUse()) {
				useBeans.add(bean);
			} else {
				notUseBeans.add(bean);
			}
		}

		String path = "C:\\激活码.xls";
		try {
			// 打开文件
			WritableWorkbook book = Workbook.createWorkbook(os);
			// 生成名为“激活码”的工作表，参数0表示这是第一页
			WritableSheet sheet = book.createSheet("激活码 ", 0);
			SheetSettings ss = sheet.getSettings();
			// ss.setHorizontalFreeze(2); // 设置列冻结
			ss.setVerticalFreeze(1); // 设置行冻结前2行

			WritableFont font1 = new WritableFont(
					WritableFont.createFont("微软雅黑"), 12, WritableFont.BOLD);
			WritableFont font2 = new WritableFont(
					WritableFont.createFont("微软雅黑"), 10, WritableFont.NO_BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.BLUE);
			WritableFont font3 = new WritableFont(
					WritableFont.createFont("微软雅黑"), 10, WritableFont.NO_BOLD,false,UnderlineStyle.NO_UNDERLINE,Colour.RED);
			WritableCellFormat wcf = new WritableCellFormat(font1);
			WritableCellFormat wcf2 = new WritableCellFormat(font2);
			WritableCellFormat wcf3 = new WritableCellFormat(font3);// 设置样式，字体

			// wcf2.setBackground(Colour.LIGHT_ORANGE);
			wcf.setAlignment(Alignment.CENTRE); // 平行居中
			wcf.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直居中
			wcf3.setAlignment(Alignment.CENTRE); // 平行居中
			wcf3.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直居中
			wcf2.setAlignment(Alignment.CENTRE); // 平行居中
			wcf2.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直居中

			// sheet.mergeCells(1, 0, 13, 0); // 合并单元格

			// 在Label对象的构造子中指名单元格位置是第一列第一行(0,0)
			// 以及单元格内容为test
			// Label titleLabel = new Label(1, 0, " 采暖市场部收入、成本、利润明细表 ", wcf);
			// // 将定义好的单元格添加到工作表中
			// sheet.addCell(titleLabel);
			sheet.setRowView(0, 500); // 设置第一行的高度 20121111
			int[] headerArrHight = { 20, 20, 20};
			String headerArr[] = { "激活码编号", "是否已经使用", "使用者信息" };
			for (int i = 0; i < headerArr.length; i++) {
				sheet.addCell(new Label(i, 0, headerArr[i], wcf));
				sheet.setColumnView(i, headerArrHight[i]);
			}
			// 未使用的激活码
			int count = 1;
			for (int i = 0; i < notUseBeans.size(); i++) {
				sheet.addCell(new Label(0, count, notUseBeans.get(i).getKeyCode(), wcf2));
				sheet.addCell(new Label(1, count, "否", wcf2));
				sheet.addCell(new Label(2, count, notUseBeans.get(i).getUserInfo(), wcf2));
				count++;
			}
			// 已经使用的激活码
			for (int i = 0; i < useBeans.size(); i++) {
				sheet.addCell(new Label(0, count, useBeans.get(i).getKeyCode(), wcf3));
				sheet.addCell(new Label(1, count, "是", wcf3));
				sheet.addCell(new Label(2, count, useBeans.get(i).getUserInfo(), wcf3));
				count++;
			}

			// 写入数据并关闭文件
			book.write();
			book.close();
			return path;
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void main(String[] args) {
//		List<KeyBean> list = KeyDao.selectKeysByBatch((short)3);
//		GenerateExcel excel = new GenerateExcel();
//		excel.exporExcel(list);
	}
}
