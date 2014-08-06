package org.yws.pangu.web;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.metastore.api.Partition;
import org.apache.hadoop.hive.metastore.api.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yws.pangu.exception.PanguException;
import org.yws.pangu.service.HiveService;
import org.yws.pangu.utils.DateUtils;
import org.yws.pangu.web.webbean.ColumnWebBean;
import org.yws.pangu.web.webbean.TableWebBean;

@RequestMapping(value = "/hive")
@Controller
public class HiveController {

	@Autowired
	private HiveService hiveService;

	@RequestMapping(value = "databases.do")
	public @ResponseBody List<String> databases() {
		try {
			return hiveService.getAllDatabases();
		} catch (PanguException e) {
			return Collections.emptyList();
		}
	}

	@RequestMapping(value = "tables.do")
	public @ResponseBody List<String> tables(String db) {
		try {
			return hiveService.getAllTables(db);
		} catch (PanguException e) {
			return Collections.emptyList();
		}
	}

	@RequestMapping(value = "table.do")
	public @ResponseBody TableWebBean table(String db, String table) {
		Table t = hiveService.getTable(db, table);
		TableWebBean wb = new TableWebBean();

		wb.setDbName(t.getDbName());
		wb.setTableName(t.getTableName());
		wb.setTableType(t.getTableType());
		wb.setOwner(t.getOwner());
		wb.setLocation(t.getSd().getLocation());
		wb.setInputFormat(t.getSd().getInputFormat());
		wb.setOutputFormat(t.getSd().getOutputFormat());
		
	    
		wb.setCreateTime(DateUtils.format(new Date(t.getCreateTime()*1000L).getTime(), "yyyy/MM/dd HH:mm:ss"));
		wb.setLastAccess(DateUtils.format(new Date(t.getLastAccessTime()*1000L).getTime(), "yyyy/MM/dd HH:mm:ss"));

		for (FieldSchema fs : t.getSd().getCols()) {
			wb.getColumns().add(new ColumnWebBean(fs.getName(), fs.getType(), fs.getComment()));
		}

		for (FieldSchema fs : t.getPartitionKeys()) {
			wb.getPartitions().add(new ColumnWebBean(fs.getName(), fs.getType(), fs.getComment()));
		}

		return wb;
	}

	@RequestMapping(value = "partitions.do")
	public @ResponseBody List<Partition> partitions(String db, String table) {
		try {
			return hiveService.getPartitions(db, table);
		} catch (PanguException e) {
			return Collections.emptyList();
		}
	}
}