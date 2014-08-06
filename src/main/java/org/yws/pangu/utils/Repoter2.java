package org.yws.pangu.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Repoter2 {

	public static void main(String[] args) throws IOException, IOException, ClassNotFoundException,
			SQLException {
		Class.forName("org.apache.hadoop.hive.jdbc.HiveDriver");
		Connection connection = DriverManager.getConnection("jdbc:hive://slave1:10000/network", "",
				"");

		Statement stmt = connection.createStatement();
		stmt.execute("use network");

		String yesterday = DateUtils.getNDaysTimeByPattern(-1, "yyyy-MM-dd");

		System.out.println(yesterday);

		Map<String, String> pdCodeMap = new HashMap<String, String>();
		pdCodeMap.put("mopote_12000016", "流量精灵(冒泡市场)");
		pdCodeMap.put("mopote_12000015", "流量精灵(京东APP市场)");
		pdCodeMap.put("mopote_12000014", "流量精灵(机锋市场)");
		pdCodeMap.put("mopote_12000013", "流量精灵(应用宝)");
		pdCodeMap.put("mopote_12000012", "流量精灵(百度)");
		pdCodeMap.put("mopote_12000011", "流量精灵(安卓市场)");
		pdCodeMap.put("mopote_12000010", "流量精灵(91商城)");
		pdCodeMap.put("mopote_12000009", "流量精灵(豌豆荚)");
		pdCodeMap.put("mopote_12000008", "流量精灵(360市场)");
		pdCodeMap.put("skymobi_12000004", "冒泡市场");

		XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream("f:/temple.xlsx"));

		XSSFCellStyle dateCellStyle = wb.createCellStyle();
		short df = wb.createDataFormat().getFormat("yyyy/mm/dd");
		dateCellStyle.setDataFormat(df);

		XSSFCellStyle percentageCellStyle = wb.createCellStyle();
		percentageCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));

		FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();

		Sheet sheet = wb.getSheetAt(0);
		// 总览
		Row row = sheet.getRow(0);
		// 总览--日期
		row.createCell(0).setCellValue(yesterday);
		row.getCell(0).setCellStyle(dateCellStyle);
		// 总览-启动数
		ResultSet rs = stmt.executeQuery("select * from total_startup_num where calc_date='"
				+ yesterday + "'");
		rs.next();
		row.createCell(1).setCellValue(Integer.valueOf(rs.getString("num")));

		// 总览-僵尸用户
		rs = stmt.executeQuery("select * from total_corpse_user_num where calc_date='"
				+ yesterday + "'");
		rs.next();
		row.createCell(2).setCellValue(Integer.valueOf(rs.getString("num")));

		// 总览-激活
		rs.close();
		rs = stmt.executeQuery("select * from total_active_success_num where calc_date='"
				+ yesterday + "'");
		rs.next();
		row.createCell(3).setCellValue(Integer.valueOf(rs.getString("num")));
		// 总览-启动激活率
		evaluator.evaluateFormulaCell(row.getCell(4));
		row.getCell(4).setCellStyle(percentageCellStyle);
		// 总览-留存数
		rs.close();
		rs = stmt.executeQuery("select * from total_user_left_num where calc_date='" + yesterday
				+ "'");
		rs.next();
		row.createCell(5).setCellValue(Integer.valueOf(rs.getString("num")));
		// 总览-总用户量
		rs.close();
		rs = stmt.executeQuery("select * from total_user_num where calc_date='" + yesterday + "'");
		rs.next();
		row.createCell(7).setCellValue(Integer.valueOf(rs.getString("num")));
		// 总览-日历史留存率
		evaluator.evaluateFormulaCell(row.getCell(6));
		row.getCell(6).setCellStyle(percentageCellStyle);

		// 助手
		row = sheet.getRow(1);
		// 助手--日期
		row.createCell(0).setCellValue(yesterday);
		row.getCell(0).setCellStyle(dateCellStyle);
		// 助手-装机-
		// 略过--无法获得
		// 助手-启动数
		rs.close();
		rs = stmt.executeQuery("select * from zhushou_startup_num where calc_date='" + yesterday
				+ "'");
		rs.next();
		row.createCell(2).setCellValue(Integer.valueOf(rs.getString("num")));
		// 助手-激活
		rs.close();
		rs = stmt.executeQuery("select * from zhushou_active_success_num where calc_date='"
				+ yesterday + "'");
		rs.next();
		row.createCell(3).setCellValue(Integer.valueOf(rs.getString("num")));
		// 助手-启动激活率
		evaluator.evaluateFormulaCell(row.getCell(4));
		row.getCell(4).setCellStyle(percentageCellStyle);
		// 助手-留存数
		rs.close();
		rs = stmt.executeQuery("select * from zhushou_user_left_num where calc_date='" + yesterday
				+ "'");
		rs.next();
		row.createCell(5).setCellValue(Integer.valueOf(rs.getString("num")));
		// 助手-总用户量
		rs.close();
		rs = stmt
				.executeQuery("select * from zhushou_user_num where calc_date='" + yesterday + "'");
		rs.next();
		row.createCell(7).setCellValue(Integer.valueOf(rs.getString("num")));
		// 助手-日历史留存率
		evaluator.evaluateFormulaCell(row.getCell(6));
		row.getCell(6).setCellStyle(percentageCellStyle);

		// 第三方渠道
		// 第三方--日期
		sheet.getRow(2).createCell(0).setCellValue(yesterday);
		sheet.getRow(2).getCell(0).setCellStyle(dateCellStyle);
		// 第三方--启动
		rs.close();
		rs = stmt.executeQuery("select * from third_startup_num where calc_date='" + yesterday
				+ "'");
		// 启动
		int i = 2;
		while (rs.next()) {
			String key = rs.getString("pdcode");
			String num = rs.getString("num");
			sheet.getRow(i).getCell(1).setCellValue(pdCodeMap.get(key));
			sheet.getRow(i).getCell(2).setCellValue(Integer.valueOf(num));
			i++;
		}
		rs.close();
		// 激活
		i = 2;
		rs = stmt
				.executeQuery("select * from third_active_num where calc_date='" + yesterday + "'");
		while (rs.next()) {
			String key = rs.getString("pdcode");
			String num = rs.getString("num");
			sheet.getRow(i).getCell(3).setCellValue(pdCodeMap.get(key));
			sheet.getRow(i).getCell(4).setCellValue(Integer.valueOf(num));
			i++;
		}
		rs.close();
		// 留存
		i = 2;
		rs = stmt.executeQuery("select * from third_user_left_num where calc_date='" + yesterday
				+ "'");
		while (rs.next()) {
			String key = rs.getString("pdcode");
			String num = rs.getString("num");
			sheet.getRow(i).getCell(5).setCellValue(pdCodeMap.get(key));
			sheet.getRow(i).getCell(6).setCellValue(Integer.valueOf(num));
			i++;
		}
		rs.close();
		// 埋点--具体指
		row = sheet.getRow(15);

		// 时间
		row.getCell(0).setCellValue(yesterday);
		row.getCell(0).setCellStyle(dateCellStyle);
		rs = stmt.executeQuery("select * from at_all_imsi_distinct_count where store_date='"
				+ yesterday + "'");
		Map<String, Integer> allAt = new HashMap<String, Integer>();
		while (rs.next()) {
			allAt.put(rs.getString("at"), rs.getInt("num"));
		}
		rs.close();
		// 101
		rs = stmt.executeQuery("select * from at_101_imsi_tm_count where store_date='" + yesterday
				+ "'");
		Map<String, Integer> map_101 = new HashMap<String, Integer>(4);
		while (rs.next()) {
			map_101.put(rs.getString("tm"), rs.getInt("num"));
		}
		rs.close();

		row.getCell(1).setCellValue(map_101.get("1"));
		row.getCell(2).setCellValue(map_101.get("2") == null ? 0 : map_101.get("2"));
		row.getCell(3).setCellValue(map_101.get("3") == null ? 0 : map_101.get("3"));
		row.getCell(4).setCellValue(map_101.get("4"));
		// 102
		Map<String, Integer> map_102 = new HashMap<String, Integer>(4);
		rs = stmt.executeQuery("select * from at_102_imsi_tm_count where store_date='" + yesterday
				+ "'");
		while (rs.next()) {
			map_102.put(rs.getString("tm"), rs.getInt("num"));
		}
		rs.close();
		row.getCell(5).setCellValue(map_102.get("1") == null ? 0 : map_102.get("1"));
		row.getCell(6).setCellValue(map_102.get("2") == null ? 0 : map_101.get("2"));
		row.getCell(7).setCellValue(map_102.get("3") == null ? 0 : map_101.get("3"));
		row.getCell(8).setCellValue(map_102.get("4") == null ? 0 : map_102.get("4"));
		// 103
		row.getCell(9).setCellValue(allAt.get("103") == null ? 0 : allAt.get("103"));
		// 104
		row.getCell(10).setCellValue(allAt.get("104") == null ? 0 : allAt.get("104"));
		// 105
		row.getCell(11).setCellValue(allAt.get("105") == null ? 0 : allAt.get("105"));
		// 106
		row.getCell(12).setCellValue(allAt.get("106") == null ? 0 : allAt.get("106"));
		// 107
		row.getCell(13).setCellValue(allAt.get("107") == null ? 0 : allAt.get("107"));
		// 108
		row.getCell(14).setCellValue(allAt.get("108") == null ? 0 : allAt.get("108"));
		// 201
		row.getCell(15).setCellValue(allAt.get("201") == null ? 0 : allAt.get("201"));
		// 202
		row.getCell(16).setCellValue(allAt.get("202") == null ? 0 : allAt.get("202"));
		// 203
		row.getCell(17).setCellValue(allAt.get("203") == null ? 0 : allAt.get("203"));
		// 204
		row.getCell(18).setCellValue(allAt.get("204") == null ? 0 : allAt.get("204"));
		// 301
		rs = stmt.executeQuery("select * from at_301_imsi_count where store_date='" + yesterday
				+ "'");
		rs.next();
		int at301 = rs.getInt("num");// 下面计算率用到
		rs.close();
		row.getCell(19).setCellValue(at301);
		// 302
		int at302 = allAt.get("302");// 下面计算率用到
		Map<String, Integer> map_302 = new HashMap<String, Integer>(4);

		rs = stmt.executeQuery("select * from at_302_imsi_nt_count where store_date='" + yesterday
				+ "'");
		while (rs.next()) {
			map_302.put(rs.getString("nt"), rs.getInt("num"));
		}
		rs.close();
		row.getCell(20).setCellValue(map_302.get("1"));
		row.getCell(21).setCellValue(map_302.get("2"));
		row.getCell(22).setCellValue(map_302.get("4"));
		// 401
		row.getCell(23).setCellValue(allAt.get("401"));
		// 402
		Map<String, Integer> map_402 = new HashMap<String, Integer>(4);
		rs = stmt.executeQuery("select * from at_402_imsi_et_count where store_date='" + yesterday
				+ "'");
		while (rs.next()) {
			map_402.put(rs.getString("et"), rs.getInt("num"));
		}
		rs.close();
		row.getCell(24).setCellValue(map_402.get("1"));
		row.getCell(25).setCellValue(map_402.get("2"));
		row.getCell(26).setCellValue(map_402.get("3"));
		row.getCell(27).setCellValue(map_402.get("4"));
		// 403
		row.getCell(28).setCellValue(allAt.get("403") == null ? 0 : allAt.get("403"));
		// 404
		Map<String, Integer> map_404 = new HashMap<String, Integer>(4);
		rs = stmt.executeQuery("select * from at_404_imsi_ut_us_count where store_date='"
				+ yesterday + "'");
		while (rs.next()) {
			map_404.put(rs.getString("ut") + "-" + rs.getString("us"), rs.getInt("num"));
		}
		rs.close();
		row.getCell(29).setCellValue(map_404.get("1-0") == null ? 0 : map_404.get("1-0"));
		row.getCell(30).setCellValue(map_404.get("1-1") == null ? 0 : map_404.get("1-1"));
		row.getCell(31).setCellValue(map_404.get("2-0") == null ? 0 : map_404.get("2-0"));
		row.getCell(32).setCellValue(map_404.get("2-1") == null ? 0 : map_404.get("2-1"));
		// 501
		row.getCell(33).setCellValue(allAt.get("501") == null ? 0 : allAt.get("501"));
		// 502
		row.getCell(34).setCellValue(allAt.get("502") == null ? 0 : allAt.get("502"));
		// 503
		row.getCell(35).setCellValue(allAt.get("503") == null ? 0 : allAt.get("503"));
		// 504
		row.getCell(36).setCellValue(allAt.get("504") == null ? 0 : allAt.get("504"));
		// 601
		row.getCell(37).setCellValue(allAt.get("601") == null ? 0 : allAt.get("601"));
		// 602
		row.getCell(38).setCellValue(allAt.get("602") == null ? 0 : allAt.get("602"));
		// 603
		row.getCell(39).setCellValue(allAt.get("603") == null ? 0 : allAt.get("603"));
		// 604
		row.getCell(40).setCellValue(allAt.get("604") == null ? 0 : allAt.get("604"));
		// 701
		Map<String, Integer> map_701 = new HashMap<String, Integer>(4);
		rs = stmt.executeQuery("select * from at_701_imsi_code_count where store_date='"
				+ yesterday + "'");
		while (rs.next()) {
			map_701.put(rs.getString("code"), rs.getInt("num"));
		}
		rs.close();

		row.getCell(41).setCellValue(map_701.get("2001") == null ? 0 : map_701.get("2001"));
		row.getCell(42).setCellValue(map_701.get("2002") == null ? 0 : map_701.get("2002"));
		row.getCell(43).setCellValue(map_701.get("2019") == null ? 0 : map_701.get("2019"));

		// 801
		row.getCell(44).setCellValue(allAt.get("801") == null ? 0 : allAt.get("801"));
		// 802
		Map<String, Integer> map_802 = new HashMap<String, Integer>(3);
		rs = stmt.executeQuery("select * from at_802_imsi_tp_count where store_date='" + yesterday
				+ "'");
		while (rs.next()) {
			map_802.put(rs.getString("tp"), rs.getInt("num"));
		}
		rs.close();

		row.getCell(45).setCellValue(map_802.get("1") == null ? 0 : map_802.get("1"));
		row.getCell(46).setCellValue(map_802.get("2") == null ? 0 : map_802.get("2"));
		row.getCell(47).setCellValue(map_802.get("3") == null ? 0 : map_802.get("3"));
		// 803
		row.getCell(48).setCellValue(allAt.get("803") == null ? 0 : allAt.get("803"));
		// 804
		row.getCell(49).setCellValue(allAt.get("804") == null ? 0 : allAt.get("804"));
		// 805
		Map<String, Integer> map_805 = new HashMap<String, Integer>(18);
		rs = stmt.executeQuery("select * from at_805_imsi_uspt_count where store_date='"
				+ yesterday + "'");
		while (rs.next()) {
			map_805.put(rs.getString("pt"), rs.getInt("num"));
		}
		rs.close();

		row.getCell(50).setCellValue(map_805.get("1") == null ? 0 : map_805.get("1"));
		row.getCell(51).setCellValue(map_805.get("2") == null ? 0 : map_805.get("2"));
		row.getCell(52).setCellValue(map_805.get("3") == null ? 0 : map_805.get("3"));
		row.getCell(53).setCellValue(map_805.get("4") == null ? 0 : map_805.get("4"));
		row.getCell(54).setCellValue(map_805.get("5") == null ? 0 : map_805.get("5"));
		row.getCell(55).setCellValue(map_805.get("6") == null ? 0 : map_805.get("6"));
		row.getCell(56).setCellValue(map_805.get("7") == null ? 0 : map_805.get("7"));
		row.getCell(57).setCellValue(map_805.get("8") == null ? 0 : map_805.get("8"));
		row.getCell(58).setCellValue(map_805.get("9") == null ? 0 : map_805.get("9"));
		// 806
		Map<String, Integer> map_806 = new HashMap<String, Integer>(3);
		rs = stmt.executeQuery("select * from at_806_imsi_tp_count where store_date='" + yesterday
				+ "'");
		while (rs.next()) {
			map_806.put(rs.getString("tp"), rs.getInt("num"));
		}
		rs.close();

		row.getCell(59).setCellValue(map_806.get("1") == null ? 0 : map_806.get("1"));
		row.getCell(60).setCellValue(map_806.get("2") == null ? 0 : map_806.get("2"));
		row.getCell(61).setCellValue(map_806.get("3") == null ? 0 : map_806.get("3"));
		// 807
		Map<String, Integer> map_807 = new HashMap<String, Integer>(2);
		rs = stmt.executeQuery("select * from at_807_imsi_tp_count where store_date='" + yesterday
				+ "'");
		while (rs.next()) {
			map_807.put(rs.getString("tp"), rs.getInt("num"));
		}
		rs.close();

		row.getCell(62).setCellValue(map_807.get("1") == null ? 0 : map_807.get("1"));
		row.getCell(63).setCellValue(map_807.get("2") == null ? 0 : map_807.get("2"));
		// 808
		Map<String, Integer> map_808 = new HashMap<String, Integer>(2);
		rs = stmt.executeQuery("select * from at_808_imsi_tp_count where store_date='" + yesterday
				+ "'");
		while (rs.next()) {
			map_808.put(rs.getString("tp"), rs.getInt("num"));
		}
		rs.close();

		row.getCell(64).setCellValue(map_808.get("1") == null ? 0 : map_808.get("1"));
		row.getCell(65).setCellValue(map_808.get("2") == null ? 0 : map_808.get("2"));
		row.getCell(66).setCellValue(map_808.get("3") == null ? 0 : map_808.get("3"));
		row.getCell(67).setCellValue(map_808.get("4") == null ? 0 : map_808.get("4"));
		row.getCell(68).setCellValue(map_808.get("9") == null ? 0 : map_808.get("5"));

		// 埋点--率
		row = sheet.getRow(16);
		// 时间
		row.getCell(0).setCellValue(yesterday);
		row.getCell(0).setCellStyle(dateCellStyle);

		evaluator.evaluateFormulaCell(row.getCell(1));
		evaluator.evaluateFormulaCell(row.getCell(2));
		evaluator.evaluateFormulaCell(row.getCell(3));
		evaluator.evaluateFormulaCell(row.getCell(4));
		evaluator.evaluateFormulaCell(row.getCell(5));
		evaluator.evaluateFormulaCell(row.getCell(6));
		evaluator.evaluateFormulaCell(row.getCell(7));
		evaluator.evaluateFormulaCell(row.getCell(8));
		evaluator.evaluateFormulaCell(row.getCell(9));
		evaluator.evaluateFormulaCell(row.getCell(10));
		// 计算301/302
		row.getCell(11).setCellValue(((double) at301) / at302);
		row.getCell(11).setCellStyle(percentageCellStyle);

		evaluator.evaluateFormulaCell(row.getCell(12));
		evaluator.evaluateFormulaCell(row.getCell(13));
		evaluator.evaluateFormulaCell(row.getCell(14));
		evaluator.evaluateFormulaCell(row.getCell(15));
		evaluator.evaluateFormulaCell(row.getCell(16));
		evaluator.evaluateFormulaCell(row.getCell(17));
		evaluator.evaluateFormulaCell(row.getCell(18));
		evaluator.evaluateFormulaCell(row.getCell(19));
		evaluator.evaluateFormulaCell(row.getCell(20));
		evaluator.evaluateFormulaCell(row.getCell(21));
		evaluator.evaluateFormulaCell(row.getCell(22));
		evaluator.evaluateFormulaCell(row.getCell(23));
		evaluator.evaluateFormulaCell(row.getCell(24));
		evaluator.evaluateFormulaCell(row.getCell(25));
		evaluator.evaluateFormulaCell(row.getCell(26));
		evaluator.evaluateFormulaCell(row.getCell(27));
		evaluator.evaluateFormulaCell(row.getCell(28));
		evaluator.evaluateFormulaCell(row.getCell(29));
		evaluator.evaluateFormulaCell(row.getCell(30));
		evaluator.evaluateFormulaCell(row.getCell(31));
		evaluator.evaluateFormulaCell(row.getCell(32));
		// 服务端校准
		row = sheet.getRow(17);
		// 时间
		row.getCell(0).setCellValue(yesterday);
		row.getCell(0).setCellStyle(dateCellStyle);
		// 失败
		rs = stmt.executeQuery("select * from server_validate_failed_user_num where calc_date='"
				+ yesterday + "'");
		rs.next();
		row.getCell(1).setCellValue(Integer.valueOf(rs.getString("num")));
		// 成功
		rs = stmt.executeQuery("select * from server_validate_success_user_num where calc_date='"
				+ yesterday + "'");
		rs.next();
		row.getCell(2).setCellValue(Integer.valueOf(rs.getString("num")));
		// 百分比
		evaluator.evaluateFormulaCell(row.getCell(3));

		FileOutputStream fileOut = new FileOutputStream("f:/" + yesterday + ".xlsx");
		wb.write(fileOut);
		fileOut.close();

	}

}
