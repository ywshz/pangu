package org.yws.pangu.service;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.NoSuchObjectException;
import org.apache.hadoop.hive.metastore.api.Partition;
import org.apache.hadoop.hive.metastore.api.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.yws.pangu.exception.PanguException;
import org.yws.pangu.schedule.RunHiveJob;

@Repository
public class HiveService {

	private static Logger log = LoggerFactory.getLogger(HiveService.class);
	private HiveMetaStoreClient client;
	public static String DEFAULT_DB = "default";
	private final String HIVE_HOME;

	public HiveService() throws Exception {
		Properties props = new Properties();
		try {
			props.load(RunHiveJob.class.getResourceAsStream("/pangu-config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		HIVE_HOME = (String) props.get("HIVE_HOME");

		HiveConf conf = new HiveConf();
		File f = new File(HIVE_HOME + File.separator + "conf" + File.separator + "hive-site.xml");
		if (f.exists()) {
			conf.addResource(f.toURI().toURL());
		}
		client = new HiveMetaStoreClient(conf);
	}

	public List<String> getAllDatabases() throws PanguException {
		try {
			return client.getAllDatabases();
		} catch (Exception e) {
			throw new PanguException("获取所有数据库信息失败", e);
		}
	}

	public List<String> getAllTables(String db) throws PanguException {
		try {
			return client.getAllTables(db);
		} catch (Exception e) {
			throw new PanguException("获取所有表信息失败", e);
		}
	}

	public Table getTable(String db, String tableName) {

		try {
			return client.getTable(db, tableName);
		} catch (Exception e) {
			log.warn("找不到该表:" + tableName, e);
		}

		return null;
	}

	public List<Partition> getPartitions(String db, String tableName) throws PanguException {
		List<Partition> l = null;
		try {
			l = client.listPartitions(db, tableName, (short) -1);
			Collections.reverse(l);
		} catch (NoSuchObjectException e) {
			log.error("找不到该表:" + tableName, e);
			throw new PanguException("没找到这张表！", e);
		} catch (Exception e) {
			log.error("取所有分区失败:" + tableName, e);
			throw new PanguException("获取所有分区失败！", e);
		}
		return l;
	}

	public HiveMetaStoreClient getClient() {
		return client;
	}

	/**
	 * 指定HiveMetaStoreClient
	 * 
	 * @param client
	 */
	public void setClient(HiveMetaStoreClient client) {
		this.client = client;
	}
}
