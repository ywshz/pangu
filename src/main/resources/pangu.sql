/*
Navicat MySQL Data Transfer

Source Server         : 192.168.100.61
Source Server Version : 50173
Source Host           : 192.168.100.61:3306
Source Database       : pangu

Target Server Type    : MYSQL
Target Server Version : 50173
File Encoding         : 65001

Date: 2014-07-29 13:32:54
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `pangu_debug_history`
-- ----------------------------
DROP TABLE IF EXISTS `pangu_debug_history`;
CREATE TABLE `pangu_debug_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `end_time` datetime DEFAULT NULL,
  `file_id` bigint(20) DEFAULT NULL,
  `log` mediumtext,
  `runtype` varchar(255) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pangu_debug_history
-- ----------------------------
INSERT INTO `pangu_debug_history` VALUES ('1', null, '4', 'pangu# 2014-05-12 11:31:03 进入任务队列\npangu# 2014-05-12 11:31:04 开始运行\n', 'shell', '2014-05-12 11:31:04', 'running');
INSERT INTO `pangu_debug_history` VALUES ('2', '2014-05-13 11:24:56', '4', 'pangu# 2014-05-13 11:24:55 进入任务队列\npangu# 2014-05-13 11:24:55 开始运行\npangu# 开始执行前置处理单元：DownloadJob\npangu# 前置处理单元：DownloadJob 处理完毕\npangu# 开始执行核心Job任务\npangu# DEBUG Command:chmod u+x c:\\run_job_dir\\2014-05-13\\debug-2\\1399951496211.sh\nCONSOLE# cygwin warning:\nCONSOLE#   MS-DOS style path detected: c:\\run_job_dir\\2014-05-13\\debug-2\\1399951496211.sh\nCONSOLE#   Preferred POSIX equivalent is: /cygdrive/c/run_job_dir/2014-05-13/debug-2/1399951496211.sh\nCONSOLE#   CYGWIN environment variable option \"nodosfilewarning\" turns off this warning.\nCONSOLE#   Consult the user\'s guide for more details about POSIX paths:\nCONSOLE#     http://cygwin.com/cygwin-ug-net/using.html#using-pathnames\npangu# DEBUG Command:dos2unix c:\\run_job_dir\\2014-05-13\\debug-2\\1399951496211.sh\nCONSOLE# \'dos2unix\' 不是内部或外部命令，也不是可运行的程序\nCONSOLE# 或批处理文件。\npangu# 核心Job任务处理完毕\npangu# exitCode=1\n', 'shell', '2014-05-13 11:24:55', 'failed');
INSERT INTO `pangu_debug_history` VALUES ('3', '2014-07-28 16:31:45', '84', 'Job start...\n\nLogging initialized using configuration in jar:file:/opt/hive-0.12.0/lib/hive-common-0.12.0.jar!/hive-log4j.properties\nAdded /opt/json-serde-1.1.9.3.jar to class path\nAdded resource: /opt/json-serde-1.1.9.3.jar\nOK\nTime taken: 3.096 seconds\n', 'hive', '2014-07-28 16:31:40', 'END');
INSERT INTO `pangu_debug_history` VALUES ('4', '2014-07-28 16:31:51', '84', 'Job start...\n\nLogging initialized using configuration in jar:file:/opt/hive-0.12.0/lib/hive-common-0.12.0.jar!/hive-log4j.properties\nAdded /opt/json-serde-1.1.9.3.jar to class path\nAdded resource: /opt/json-serde-1.1.9.3.jar\nOK\nTime taken: 3.11 seconds\n', 'hive', '2014-07-28 16:31:46', 'END');
INSERT INTO `pangu_debug_history` VALUES ('5', '2014-07-28 16:32:11', '84', 'Job start...\n\nLogging initialized using configuration in jar:file:/opt/hive-0.12.0/lib/hive-common-0.12.0.jar!/hive-log4j.properties\nAdded /opt/json-serde-1.1.9.3.jar to class path\nAdded resource: /opt/json-serde-1.1.9.3.jar\nOK\nTime taken: 3.14 seconds\n', 'hive', '2014-07-28 16:32:06', 'END');
INSERT INTO `pangu_debug_history` VALUES ('6', '2014-07-28 16:44:37', '84', 'Job start...\n\nLogging initialized using configuration in jar:file:/opt/hive-0.12.0/lib/hive-common-0.12.0.jar!/hive-log4j.properties\nAdded /opt/json-serde-1.1.9.3.jar to class path\nAdded resource: /opt/json-serde-1.1.9.3.jar\nOK\nTime taken: 3.111 seconds\n', 'hive', '2014-07-28 16:44:32', 'END');
INSERT INTO `pangu_debug_history` VALUES ('7', '2014-07-28 16:46:48', '84', 'Job start...\n\nLogging initialized using configuration in jar:file:/opt/hive-0.12.0/lib/hive-common-0.12.0.jar!/hive-log4j.properties\nAdded /opt/json-serde-1.1.9.3.jar to class path\nAdded resource: /opt/json-serde-1.1.9.3.jar\nOK\nTime taken: 3.156 seconds\n', 'hive', '2014-07-28 16:46:43', 'END');
INSERT INTO `pangu_debug_history` VALUES ('8', '2014-07-28 16:48:17', '84', 'Job start...\n\nLogging initialized using configuration in jar:file:/opt/hive-0.12.0/lib/hive-common-0.12.0.jar!/hive-log4j.properties\nAdded /opt/json-serde-1.1.9.3.jar to class path\nAdded resource: /opt/json-serde-1.1.9.3.jar\nOK\nTime taken: 3.119 seconds\n', 'hive', '2014-07-28 16:48:12', 'END');
INSERT INTO `pangu_debug_history` VALUES ('9', '2014-07-28 16:50:59', '84', 'Job start...\n\nLogging initialized using configuration in jar:file:/opt/hive-0.12.0/lib/hive-common-0.12.0.jar!/hive-log4j.properties\nAdded /opt/json-serde-1.1.9.3.jar to class path\nAdded resource: /opt/json-serde-1.1.9.3.jar\nOK\nTime taken: 3.096 seconds\n', 'hive', '2014-07-28 16:50:54', 'END');
INSERT INTO `pangu_debug_history` VALUES ('10', '2014-07-28 16:54:53', '84', 'Job start...\n\nLogging initialized using configuration in jar:file:/opt/hive-0.12.0/lib/hive-common-0.12.0.jar!/hive-log4j.properties\nAdded /opt/json-serde-1.1.9.3.jar to class path\nAdded resource: /opt/json-serde-1.1.9.3.jar\nOK\nTime taken: 3.089 seconds\n', 'hive', '2014-07-28 16:54:48', 'END');
INSERT INTO `pangu_debug_history` VALUES ('11', '2014-07-28 17:21:55', '84', 'Job start...\n\nLogging initialized using configuration in jar:file:/opt/hive-0.12.0/lib/hive-common-0.12.0.jar!/hive-log4j.properties\nAdded /opt/json-serde-1.1.9.3.jar to class path\nAdded resource: /opt/json-serde-1.1.9.3.jar\nOK\nTime taken: 3.113 seconds\nTotal MapReduce jobs = 1\nLaunching Job 1 out of 1\nNumber of reduce tasks not specified. Estimated from input data size: 1\nIn order to change the average load for a reducer (in bytes):\n  set hive.exec.reducers.bytes.per.reducer=<number>\nIn order to limit the maximum number of reducers:\n  set hive.exec.reducers.max=<number>\nIn order to set a constant number of reducers:\n  set mapred.reduce.tasks=<number>\nStarting Job = job_201407211343_1366, Tracking URL = http://master:50030/jobdetails.jsp?jobid=job_201407211343_1366\nKill Command = /opt/hadoop-1.2.1/libexec/../bin/hadoop job  -kill job_201407211343_1366\nHadoop job information for Stage-1: number of mappers: 2; number of reducers: 1\n2014-07-28 17:21:41,989 Stage-1 map = 0%,  reduce = 0%\n2014-07-28 17:21:44,009 Stage-1 map = 50%,  reduce = 0%, Cumulative CPU 1.45 sec\n2014-07-28 17:21:45,017 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.93 sec\n2014-07-28 17:21:46,025 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.93 sec\n2014-07-28 17:21:47,032 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.93 sec\n2014-07-28 17:21:48,040 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.93 sec\n2014-07-28 17:21:49,048 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.93 sec\n2014-07-28 17:21:50,056 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.93 sec\n2014-07-28 17:21:51,063 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.93 sec\n2014-07-28 17:21:52,069 Stage-1 map = 100%,  reduce = 33%, Cumulative CPU 2.93 sec\n2014-07-28 17:21:53,081 Stage-1 map = 100%,  reduce = 100%, Cumulative CPU 5.68 sec\n2014-07-28 17:21:54,089 Stage-1 map = 100%,  reduce = 100%, Cumulative CPU 5.68 sec\n2014-07-28 17:21:55,096 Stage-1 map = 100%,  reduce = 100%, Cumulative CPU 5.68 sec\nMapReduce Total cumulative CPU time: 5 seconds 680 msec\nEnded Job = job_201407211343_1366\nMapReduce Jobs Launched: \nJob 0: Map: 2  Reduce: 1   Cumulative CPU: 5.68 sec   HDFS Read: 514 HDFS Write: 41 SUCCESS\nTotal MapReduce CPU Time Spent: 5 seconds 680 msec\nOK\n1	yws	19	2014-07-26	1	zhaohui	2014-07-26\nTime taken: 20.077 seconds, Fetched: 1 row(s)\n', 'hive', '2014-07-28 17:21:29', 'END');
INSERT INTO `pangu_debug_history` VALUES ('12', '2014-07-28 17:28:30', '84', 'Job start...\n\nLogging initialized using configuration in jar:file:/opt/hive-0.12.0/lib/hive-common-0.12.0.jar!/hive-log4j.properties\nAdded /opt/json-serde-1.1.9.3.jar to class path\nAdded resource: /opt/json-serde-1.1.9.3.jar\nOK\nTime taken: 4.155 seconds\nTotal MapReduce jobs = 1\nLaunching Job 1 out of 1\nNumber of reduce tasks not specified. Estimated from input data size: 1\nIn order to change the average load for a reducer (in bytes):\n  set hive.exec.reducers.bytes.per.reducer=<number>\nIn order to limit the maximum number of reducers:\n  set hive.exec.reducers.max=<number>\nIn order to set a constant number of reducers:\n  set mapred.reduce.tasks=<number>\nStarting Job = job_201407211343_1370, Tracking URL = http://master:50030/jobdetails.jsp?jobid=job_201407211343_1370\nKill Command = /opt/hadoop-1.2.1/libexec/../bin/hadoop job  -kill job_201407211343_1370\nHadoop job information for Stage-1: number of mappers: 2; number of reducers: 1\n2014-07-28 17:27:50,380 Stage-1 map = 0%,  reduce = 0%\n2014-07-28 17:28:19,249 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.87 sec\n2014-07-28 17:28:19,527 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.92 sec\n2014-07-28 17:28:20,255 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.87 sec\n2014-07-28 17:28:20,534 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.92 sec\n2014-07-28 17:28:21,260 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.87 sec\n2014-07-28 17:28:21,540 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.92 sec\n2014-07-28 17:28:22,266 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.87 sec\n2014-07-28 17:28:22,546 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.92 sec\n2014-07-28 17:28:23,272 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.87 sec\n2014-07-28 17:28:23,551 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.92 sec\n2014-07-28 17:28:24,278 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.87 sec\n2014-07-28 17:28:24,557 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.92 sec\n2014-07-28 17:28:25,284 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.87 sec\n2014-07-28 17:28:25,562 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.92 sec\n2014-07-28 17:28:26,289 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.87 sec\n2014-07-28 17:28:26,568 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.92 sec\n2014-07-28 17:28:27,295 Stage-1 map = 100%,  reduce = 33%, Cumulative CPU 2.87 sec\n2014-07-28 17:28:27,574 Stage-1 map = 100%,  reduce = 33%, Cumulative CPU 2.92 sec\n2014-07-28 17:28:28,307 Stage-1 map = 100%,  reduce = 100%, Cumulative CPU 5.65 sec\n2014-07-28 17:28:28,585 Stage-1 map = 100%,  reduce = 100%, Cumulative CPU 5.69 sec\n2014-07-28 17:28:29,313 Stage-1 map = 100%,  reduce = 100%, Cumulative CPU 5.65 sec\n2014-07-28 17:28:29,591 Stage-1 map = 100%,  reduce = 100%, Cumulative CPU 5.69 sec\n2014-07-28 17:28:30,319 Stage-1 map = 100%,  reduce = 100%, Cumulative CPU 5.65 sec\nMapReduce Total cumulative CPU time: 5 seconds 650 msec\nEnded Job = job_201407211343_1369\nMapReduce Jobs Launched: \nJob 0: Map: 2  Reduce: 1   Cumulative CPU: 5.65 sec   HDFS Read: 514 HDFS Write: 41 SUCCESS\nTotal MapReduce CPU Time Spent: 5 seconds 650 msec\nOK\n1	yws	19	2014-07-26	1	zhaohui	2014-07-26\nTime taken: 68.64 seconds, Fetched: 1 row(s)\n', 'hive', '2014-07-28 17:27:14', 'END');
INSERT INTO `pangu_debug_history` VALUES ('13', '2014-07-28 17:28:30', '84', 'Job start...\n\nLogging initialized using configuration in jar:file:/opt/hive-0.12.0/lib/hive-common-0.12.0.jar!/hive-log4j.properties\nAdded /opt/json-serde-1.1.9.3.jar to class path\nAdded resource: /opt/json-serde-1.1.9.3.jar\nOK\nTime taken: 4.155 seconds\nTotal MapReduce jobs = 1\nLaunching Job 1 out of 1\nNumber of reduce tasks not specified. Estimated from input data size: 1\nIn order to change the average load for a reducer (in bytes):\n  set hive.exec.reducers.bytes.per.reducer=<number>\nIn order to limit the maximum number of reducers:\n  set hive.exec.reducers.max=<number>\nIn order to set a constant number of reducers:\n  set mapred.reduce.tasks=<number>\nStarting Job = job_201407211343_1370, Tracking URL = http://master:50030/jobdetails.jsp?jobid=job_201407211343_1370\nKill Command = /opt/hadoop-1.2.1/libexec/../bin/hadoop job  -kill job_201407211343_1370\nHadoop job information for Stage-1: number of mappers: 2; number of reducers: 1\n2014-07-28 17:27:50,380 Stage-1 map = 0%,  reduce = 0%\n2014-07-28 17:28:19,249 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.87 sec\n2014-07-28 17:28:19,527 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.92 sec\n2014-07-28 17:28:20,255 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.87 sec\n2014-07-28 17:28:20,534 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.92 sec\n2014-07-28 17:28:21,260 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.87 sec\n2014-07-28 17:28:21,540 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.92 sec\n2014-07-28 17:28:22,266 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.87 sec\n2014-07-28 17:28:22,546 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.92 sec\n2014-07-28 17:28:23,272 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.87 sec\n2014-07-28 17:28:23,551 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.92 sec\n2014-07-28 17:28:24,278 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.87 sec\n2014-07-28 17:28:24,557 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.92 sec\n2014-07-28 17:28:25,284 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.87 sec\n2014-07-28 17:28:25,562 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.92 sec\n2014-07-28 17:28:26,289 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.87 sec\n2014-07-28 17:28:26,568 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.92 sec\n2014-07-28 17:28:27,295 Stage-1 map = 100%,  reduce = 33%, Cumulative CPU 2.87 sec\n2014-07-28 17:28:27,574 Stage-1 map = 100%,  reduce = 33%, Cumulative CPU 2.92 sec\n2014-07-28 17:28:28,307 Stage-1 map = 100%,  reduce = 100%, Cumulative CPU 5.65 sec\n2014-07-28 17:28:28,585 Stage-1 map = 100%,  reduce = 100%, Cumulative CPU 5.69 sec\n2014-07-28 17:28:29,313 Stage-1 map = 100%,  reduce = 100%, Cumulative CPU 5.65 sec\n2014-07-28 17:28:29,591 Stage-1 map = 100%,  reduce = 100%, Cumulative CPU 5.69 sec\n2014-07-28 17:28:30,319 Stage-1 map = 100%,  reduce = 100%, Cumulative CPU 5.65 sec\nMapReduce Total cumulative CPU time: 5 seconds 650 msec\nEnded Job = job_201407211343_1369\nMapReduce Jobs Launched: \nJob 0: Map: 2  Reduce: 1   Cumulative CPU: 5.65 sec   HDFS Read: 514 HDFS Write: 41 SUCCESS\nTotal MapReduce CPU Time Spent: 5 seconds 650 msec\nOK\n1	yws	19	2014-07-26	1	zhaohui	2014-07-26\nTime taken: 68.64 seconds, Fetched: 1 row(s)\n2014-07-28 17:28:30,598 Stage-1 map = 100%,  reduce = 100%, Cumulative CPU 5.69 sec\nMapReduce Total cumulative CPU time: 5 seconds 690 msec\nEnded Job = job_201407211343_1370\nMapReduce Jobs Launched: \nJob 0: Map: 2  Reduce: 1   Cumulative CPU: 5.69 sec   HDFS Read: 514 HDFS Write: 41 SUCCESS\nTotal MapReduce CPU Time Spent: 5 seconds 690 msec\nOK\n1	yws	19	2014-07-26	1	zhaohui	2014-07-26\nTime taken: 49.447 seconds, Fetched: 1 row(s)\n', 'hive', '2014-07-28 17:27:34', 'END');
INSERT INTO `pangu_debug_history` VALUES ('14', '2014-07-28 17:45:25', '84', 'Job start...\n', 'hive', '2014-07-28 17:40:17', 'END');
INSERT INTO `pangu_debug_history` VALUES ('15', '2014-07-28 17:45:27', '84', 'Job start...\n', 'hive', '2014-07-28 17:40:40', 'END');
INSERT INTO `pangu_debug_history` VALUES ('16', '2014-07-28 17:50:40', '84', 'Job start...\n\nLogging initialized using configuration in jar:file:/opt/hive-0.12.0/lib/hive-common-0.12.0.jar!/hive-log4j.properties\nAdded /opt/json-serde-1.1.9.3.jar to class path\nAdded resource: /opt/json-serde-1.1.9.3.jar\nOK\nTime taken: 3.146 seconds\nTotal MapReduce jobs = 1\nLaunching Job 1 out of 1\nNumber of reduce tasks not specified. Estimated from input data size: 1\nIn order to change the average load for a reducer (in bytes):\n  set hive.exec.reducers.bytes.per.reducer=<number>\nIn order to limit the maximum number of reducers:\n  set hive.exec.reducers.max=<number>\nIn order to set a constant number of reducers:\n  set mapred.reduce.tasks=<number>\nStarting Job = job_201407211343_1377, Tracking URL = http://master:50030/jobdetails.jsp?jobid=job_201407211343_1377\nKill Command = /opt/hadoop-1.2.1/libexec/../bin/hadoop job  -kill job_201407211343_1377\nHadoop job information for Stage-1: number of mappers: 2; number of reducers: 1\n2014-07-28 17:50:26,899 Stage-1 map = 0%,  reduce = 0%\n2014-07-28 17:50:28,919 Stage-1 map = 50%,  reduce = 0%, Cumulative CPU 1.45 sec\n2014-07-28 17:50:29,927 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.92 sec\n2014-07-28 17:50:30,936 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.92 sec\n2014-07-28 17:50:31,949 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.92 sec\n2014-07-28 17:50:32,957 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.92 sec\n2014-07-28 17:50:33,965 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.92 sec\n2014-07-28 17:50:34,973 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.92 sec\n2014-07-28 17:50:35,981 Stage-1 map = 100%,  reduce = 0%, Cumulative CPU 2.92 sec\n2014-07-28 17:50:36,988 Stage-1 map = 100%,  reduce = 33%, Cumulative CPU 2.92 sec\n2014-07-28 17:50:38,000 Stage-1 map = 100%,  reduce = 100%, Cumulative CPU 5.65 sec\n2014-07-28 17:50:39,008 Stage-1 map = 100%,  reduce = 100%, Cumulative CPU 5.65 sec\n2014-07-28 17:50:40,015 Stage-1 map = 100%,  reduce = 100%, Cumulative CPU 5.65 sec\nMapReduce Total cumulative CPU time: 5 seconds 650 msec\nEnded Job = job_201407211343_1377\nMapReduce Jobs Launched: \nJob 0: Map: 2  Reduce: 1   Cumulative CPU: 5.65 sec   HDFS Read: 514 HDFS Write: 41 SUCCESS\nTotal MapReduce CPU Time Spent: 5 seconds 650 msec\nOK\n1	yws	19	2014-07-26	1	zhaohui	2014-07-26\nTime taken: 20.083 seconds, Fetched: 1 row(s)\n', 'hive', '2014-07-28 17:50:14', 'END');

-- ----------------------------
-- Table structure for `pangu_file`
-- ----------------------------
DROP TABLE IF EXISTS `pangu_file`;
CREATE TABLE `pangu_file` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `owner` varchar(255) NOT NULL,
  `parent` bigint(20) DEFAULT NULL,
  `type` int(11) NOT NULL,
  `history` bigint(20) DEFAULT NULL,
  `folder` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=87 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pangu_file
-- ----------------------------
INSERT INTO `pangu_file` VALUES ('1', null, '2014-05-12 11:29:40', '2014-05-12 11:29:40', '个人文档', '1', null, '1', null, '');
INSERT INTO `pangu_file` VALUES ('66', null, '2014-07-17 16:01:37', '2014-07-17 16:01:37', 'New Folder', '1', '1', '1', null, null);
INSERT INTO `pangu_file` VALUES ('67', null, '2014-07-17 16:01:39', '2014-07-17 16:01:39', 'New Folder', '1', '66', '1', null, null);
INSERT INTO `pangu_file` VALUES ('68', null, '2014-07-17 16:01:42', '2014-07-17 16:01:42', 'New Folder', '1', '66', '1', null, null);
INSERT INTO `pangu_file` VALUES ('69', null, '2014-07-17 16:01:44', '2014-07-17 16:01:44', 'New Folder', '1', '66', '1', null, null);
INSERT INTO `pangu_file` VALUES ('70', null, '2014-07-17 16:01:46', '2014-07-17 16:01:46', 'New Folder', '1', '66', '1', null, null);
INSERT INTO `pangu_file` VALUES ('71', null, '2014-07-17 16:01:48', '2014-07-17 16:01:48', 'New Folder', '1', '70', '1', null, null);
INSERT INTO `pangu_file` VALUES ('72', null, '2014-07-17 16:01:49', '2014-07-17 16:01:49', 'New Folder', '1', '70', '1', null, null);
INSERT INTO `pangu_file` VALUES ('73', null, '2014-07-17 16:01:50', '2014-07-17 16:01:50', 'New Folder', '1', '71', '1', null, null);
INSERT INTO `pangu_file` VALUES ('74', null, '2014-07-17 16:01:52', '2014-07-17 16:01:52', 'New Folder', '1', '72', '1', null, null);
INSERT INTO `pangu_file` VALUES ('75', null, '2014-07-17 16:01:53', '2014-07-17 16:01:53', 'New File', '1', '71', '2', null, null);
INSERT INTO `pangu_file` VALUES ('76', '额无法', '2014-07-17 16:01:55', '2014-07-17 17:30:35', 'New File', '1', '72', '2', null, null);
INSERT INTO `pangu_file` VALUES ('77', null, '2014-07-17 16:02:11', '2014-07-17 16:02:11', 'New Folder', '1', '72', '1', null, null);
INSERT INTO `pangu_file` VALUES ('78', null, '2014-07-17 16:02:13', '2014-07-17 16:02:13', 'New Folder', '1', '74', '1', null, null);
INSERT INTO `pangu_file` VALUES ('79', null, '2014-07-23 10:15:56', '2014-07-23 10:15:56', 'New File', '1', '69', '2', null, null);
INSERT INTO `pangu_file` VALUES ('80', null, '2014-07-23 10:20:12', '2014-07-23 10:20:12', 'New File', '1', '67', '2', null, null);
INSERT INTO `pangu_file` VALUES ('81', null, '2014-07-23 18:13:49', '2014-07-23 18:13:49', 'New File', '1', '66', '2', null, null);
INSERT INTO `pangu_file` VALUES ('82', null, '2014-07-23 18:13:59', '2014-07-23 18:13:59', 'New File', '1', '68', '2', null, null);
INSERT INTO `pangu_file` VALUES ('83', null, '2014-07-23 18:14:04', '2014-07-23 18:14:04', 'New File', '1', '67', '2', null, null);
INSERT INTO `pangu_file` VALUES ('84', 'add jar /opt/json-serde-1.1.9.3.jar;\nuse network;\n\nselect * from test.user join test.address on user.id=address.id;\n\n', '2014-07-24 17:39:50', '2014-07-28 17:50:14', 'New File', '1', '66', '2', '16', null);
INSERT INTO `pangu_file` VALUES ('85', null, '2014-07-25 13:11:17', '2014-07-25 13:11:17', 'New Folder', '1', '2', '1', null, null);
INSERT INTO `pangu_file` VALUES ('86', null, '2014-07-25 14:46:03', '2014-07-25 14:46:03', 'New Folder', '1', '2', '1', null, null);

-- ----------------------------
-- Table structure for `pangu_follow`
-- ----------------------------
DROP TABLE IF EXISTS `pangu_follow`;
CREATE TABLE `pangu_follow` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  `target_id` bigint(20) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `uid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pangu_follow
-- ----------------------------

-- ----------------------------
-- Table structure for `pangu_group`
-- ----------------------------
DROP TABLE IF EXISTS `pangu_group`;
CREATE TABLE `pangu_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `descr` varchar(255) DEFAULT NULL,
  `gmt_create` datetime NOT NULL,
  `gmt_modified` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  `owner` varchar(255) NOT NULL,
  `parent` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pangu_group
-- ----------------------------
INSERT INTO `pangu_group` VALUES ('1', null, '2014-05-12 11:22:21', '2014-05-12 11:22:21', '众神之神', '1', null);
INSERT INTO `pangu_group` VALUES ('2', null, '2014-07-24 19:25:10', '2014-07-24 19:25:12', 'test', '1', '1');
INSERT INTO `pangu_group` VALUES ('3', '11', '2014-07-24 20:30:35', '2014-07-24 20:30:38', '2222', '1', '1');

-- ----------------------------
-- Table structure for `pangu_job`
-- ----------------------------
DROP TABLE IF EXISTS `pangu_job`;
CREATE TABLE `pangu_job` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `auto` int(11) DEFAULT NULL,
  `cron_expression` varchar(255) DEFAULT NULL,
  `dependencies` varchar(255) DEFAULT NULL,
  `descr` varchar(255) DEFAULT NULL,
  `gmt_create` datetime NOT NULL,
  `gmt_modified` datetime NOT NULL,
  `group_id` int(11) NOT NULL,
  `history_id` bigint(20) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `owner` varchar(255) NOT NULL,
  `resources` varchar(255) DEFAULT NULL,
  `run_type` varchar(255) DEFAULT NULL,
  `schedule_type` int(11) DEFAULT NULL,
  `script` text,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pangu_job
-- ----------------------------
INSERT INTO `pangu_job` VALUES ('1', '1', '1', '1', '1', '2014-07-24 19:23:28', '2014-07-24 19:23:30', '1', '1', '1', '1', null, 'shell', '1', 'add jar /opt/json-serde-1.1.9.3.jar; \r\nuse network;\r\n\r\nload data inpath \'/kafka/mpt-liuliang/${zdt.addDay(-1).format(\"yyyy/MM/dd/\")}*\'\r\noverwrite into table tbl_fm_log_data partition(store_date=\'${zdt.addDay(-1).format(\"yyyy-MM-dd\")}\');\r\n\r\nload data inpath \'/kafka/mpt-session/${zdt.addDay(-1).format(\"yyyy/MM/dd/\")}*\'\r\noverwrite into table tbl_fm_log_session partition(store_date=\'${zdt.addDay(-1).format(\"yyyy-MM-dd\")}\');\r\n\r\nload data inpath \'/kafka/tbl_fm_base_mobile_imsi_ref/${zdt.addDay(-1).format(\"yyyy/MM/dd/\")}*\'\r\noverwrite into table tbl_fm_base_mobile_imsi_ref partition(store_date=\'${zdt.addDay(-1).format(\"yyyy-MM-dd\")}\');\r\n\r\nload data inpath \'/kafka/tbl_fm_log_ctrl_active/${zdt.addDay(-1).format(\"yyyy/MM/dd/\")}*\'\r\noverwrite into table tbl_fm_log_ctrl_active partition(store_date=\'${zdt.addDay(-1).format(\"yyyy-MM-dd\")}\');\r\n\r\nload data inpath \'/kafka/tbl_fm_log_failed_sms/${zdt.addDay(-1).format(\"yyyy/MM/dd/\")}*\'\r\noverwrite into table tbl_fm_log_failed_sms partition(store_date=\'${zdt.addDay(-1).format(\"yyyy-MM-dd\")}\');\r\n\r\nload data inpath \'/kafka/tbl_fm_log_user_flow/${zdt.addDay(-1).format(\"yyyy/MM/dd/\")}*\'\r\noverwrite into table tbl_fm_log_user_flow partition(store_date=\'${zdt.addDay(-1).format(\"yyyy-MM-dd\")}\');\r\n\r\nload data inpath \'/kafka/tbl_fm_log_app_flow/${zdt.addDay(-1).format(\"yyyy/MM/dd/\")}*\'\r\noverwrite into table tbl_fm_log_app_flow partition(store_date=\'${zdt.addDay(-1).format(\"yyyy-MM-dd\")}\');\r\n\r\n', '1');
INSERT INTO `pangu_job` VALUES ('2', '2', '0 0 0 * * ?', '39,29', '39,29', '2014-07-24 19:24:31', '2014-07-25 14:51:28', '2', '2', '测试', '1', '', 'hive', '1', 'add jar /opt/json-serde-1.1.9.3.jar; \nuse network;\n\nload data inpath \'/kafka/mpt-liuliang/${zdt.addDay(-1).format(\"yyyy/MM/dd/\")}*\'\noverwrite into table tbl_fm_log_data partition(store_date=\'${zdt.addDay(-1).format(\"yyyy-MM-dd\")}\');\n\nload data inpath \'/kafka/mpt-session/${zdt.addDay(-1).format(\"yyyy/MM/dd/\")}*\'\noverwrite into table tbl_fm_log_session partition(store_date=\'${zdt.addDay(-1).format(\"yyyy-MM-dd\")}\');\n\nload data inpath \'/kafka/tbl_fm_base_mobile_imsi_ref/${zdt.addDay(-1).format(\"yyyy/MM/dd/\")}*\'\noverwrite into table tbl_fm_base_mobile_imsi_ref partition(store_date=\'${zdt.addDay(-1).format(\"yyyy-MM-dd\")}\');\n\nload data inpath \'/kafka/tbl_fm_log_ctrl_active/${zdt.addDay(-1).format(\"yyyy/MM/dd/\")}*\'\noverwrite into table tbl_fm_log_ctrl_active partition(store_date=\'${zdt.addDay(-1).format(\"yyyy-MM-dd\")}\');\n\nload data inpath \'/kafka/tbl_fm_log_failed_sms/${zdt.addDay(-1).format(\"yyyy/MM/dd/\")}*\'\noverwrite into table tbl_fm_log_failed_sms partition(store_date=\'${zdt.addDay(-1).format(\"yyyy-MM-dd\")}\');\n\nload data inpath \'/kafka/tbl_fm_log_user_flow/${zdt.addDay(-1).format(\"yyyy/MM/dd/\")}*\'\noverwrite into table tbl_fm_log_user_flow partition(store_date=\'${zdt.addDay(-1).format(\"yyyy-MM-dd\")}\');\n\nload data inpath \'/kafka/tbl_fm_log_app_flow/${zdt.addDay(-1).format(\"yyyy/MM/dd/\")}*\'\noverwrite into table tbl_fm_log_app_flow partition(store_date=\'${zdt.addDay(-1).format(\"yyyy-MM-dd\")}\');\n', '1');

-- ----------------------------
-- Table structure for `pangu_job_history`
-- ----------------------------
DROP TABLE IF EXISTS `pangu_job_history`;
CREATE TABLE `pangu_job_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `end_time` datetime DEFAULT NULL,
  `illustrate` varchar(255) DEFAULT NULL,
  `job_id` bigint(20) DEFAULT NULL,
  `log` mediumtext,
  `operator` varchar(255) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `trigger_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pangu_job_history
-- ----------------------------
INSERT INTO `pangu_job_history` VALUES ('1', null, '1', '2', '123', '1', '2014-07-25 16:24:25', 'running', '1');
INSERT INTO `pangu_job_history` VALUES ('2', null, '1', '2', '122121', '1', '2014-07-25 16:24:40', 'complete', '1');

-- ----------------------------
-- Table structure for `pangu_lock`
-- ----------------------------
DROP TABLE IF EXISTS `pangu_lock`;
CREATE TABLE `pangu_lock` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  `host` varchar(255) DEFAULT NULL,
  `server_update` datetime DEFAULT NULL,
  `subgroup` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pangu_lock
-- ----------------------------
INSERT INTO `pangu_lock` VALUES ('1', '2014-05-12 11:22:21', '2014-05-12 11:22:21', '172.16.22.115', '2014-05-13 11:25:31', 'test-env');

-- ----------------------------
-- Table structure for `pangu_permission`
-- ----------------------------
DROP TABLE IF EXISTS `pangu_permission`;
CREATE TABLE `pangu_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  `target_id` bigint(20) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `uid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pangu_permission
-- ----------------------------

-- ----------------------------
-- Table structure for `pangu_profile`
-- ----------------------------
DROP TABLE IF EXISTS `pangu_profile`;
CREATE TABLE `pangu_profile` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  `hadoop_conf` varchar(255) DEFAULT NULL,
  `uid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pangu_profile
-- ----------------------------
INSERT INTO `pangu_profile` VALUES ('1', '2014-05-12 11:29:40', '2014-05-13 11:24:50', '{\"pangu.doc.lastopen\":\"3 4 4\"}', 'zhoufang');

-- ----------------------------
-- Table structure for `pangu_user`
-- ----------------------------
DROP TABLE IF EXISTS `pangu_user`;
CREATE TABLE `pangu_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `gmt_create` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `uid` varchar(255) DEFAULT NULL,
  `wangwang` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pangu_user
-- ----------------------------
INSERT INTO `pangu_user` VALUES ('1', 'zhoufang@taobao.com', '2014-05-12 11:25:16', '2014-05-13 11:23:31', '周方', '15068101234', 'zhoufang', null);
