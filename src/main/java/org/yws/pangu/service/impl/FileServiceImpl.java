package org.yws.pangu.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yws.pangu.dao.mysql.MySqlFileDao;
import org.yws.pangu.dao.mysql.MysqlDebugHistoryDao;
import org.yws.pangu.domain.DebugHistory;
import org.yws.pangu.domain.FileBean;
import org.yws.pangu.domain.FileDescriptor;
import org.yws.pangu.domain.OutputRedirector;
import org.yws.pangu.enums.EFileType;
import org.yws.pangu.utils.DateRender;
import org.yws.pangu.utils.MemoryDebugHelper;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/*
 * 
 */
@Service
public class FileServiceImpl {
	private final String HIVE_HOME;
	private final String HADOOP_HOME;

	@Autowired
	private MySqlFileDao fileDao;
    @Autowired
    private MysqlDebugHistoryDao debugHistoryDao;


	public FileServiceImpl() {
		Properties props = new Properties();
		try {
			props.load(FileServiceImpl.class.getResourceAsStream("/pangu-config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		HIVE_HOME = (String) props.get("HIVE_HOME");
		HADOOP_HOME = (String) props.get("HADOOP_HOME");
	}

	public FileBean getFile(Integer id, String owner) {
		FileDescriptor fd = fileDao.getFile(id, owner);
		if (!fd.getOwner().equals(owner)) {
			return null;
		}

		if (EFileType.FOLDER.isEqual(fd.getType())) {
			return fileDao.fillFile(fd);
		} else {
			return new FileBean(fd);
		}
	}

    /**
     *
     * @param fileId
     * @param owner
     * @return history id : -2=is RUNNING error, -1=RUN FAILED , 0=execute success
     * @throws IOException
     */
	public int execute(Integer fileId,String owner) throws IOException {
        FileBean fd = getFile(fileId,owner);

        if(isRunning(fileId,owner)){
            return -2;
        }

        File file = generateTmpFile(fd);

		// execute file
		ProcessBuilder builder = new ProcessBuilder(HIVE_HOME + "/bin/hive", "-f",
				file.getAbsolutePath());
		builder.environment().put("HADOOP_HOME", HADOOP_HOME);
		builder.environment().put("HIVE_HOME", HIVE_HOME);
		Process process = builder.start();
		final InputStream inputStream = process.getInputStream();
		final InputStream errorStream = process.getErrorStream();

		// TODO:

        DebugHistory his = this.addDebugHistory(fileId,"hive");

        fd.getFileDescriptor().setHistory(his.getId());
        fileDao.update(fd.getFileDescriptor());

        MemoryDebugHelper.LOG_MAP.put(fileId.toString(), new StringBuffer("Job start...\n"));
        MemoryDebugHelper.JOB_STATUS_MAP.put(fileId.toString(), "RUNNING");

		OutputRedirector outRedirect = new OutputRedirector(inputStream, fileId.toString());
		OutputRedirector outToConsole = new OutputRedirector(errorStream, fileId.toString());
		outRedirect.start();
		outToConsole.start();

		int exitCode = -999;
		try {
			exitCode = process.waitFor();
			
			if(exitCode == 0){
				MemoryDebugHelper.LOG_MAP.get(fileId.toString()).append("Job run success!\n");
				MemoryDebugHelper.JOB_STATUS_MAP.put(fileId.toString(), "SUCCESS");
				his.setStatus("SUCCESS");
			}else{
				MemoryDebugHelper.LOG_MAP.get(fileId.toString()).append("Job run FAILED!\n");
				MemoryDebugHelper.JOB_STATUS_MAP.put(fileId.toString(), "FAILED");
				his.setStatus("FAILED");
			}
            his.setEndTime(new Date());
            his.setLog(MemoryDebugHelper.LOG_MAP.get(fileId.toString()).toString());

            updateDebugHistory(his);

		} catch (InterruptedException e) {
            MemoryDebugHelper.JOB_STATUS_MAP.put(fileId.toString(), "FAILED");
            MemoryDebugHelper.LOG_MAP.get(fileId.toString()).append("Job run failed!\n");
            his.setEndTime(new Date());
            his.setLog(MemoryDebugHelper.LOG_MAP.get(fileId.toString()).append(e.getMessage()).toString());
            his.setStatus("FAILED");
            updateDebugHistory(his);
		} finally {
            MemoryDebugHelper.JOB_STATUS_MAP.remove(fileId.toString());
            MemoryDebugHelper.LOG_MAP.remove(fileId.toString());
			process = null;
		}

		return exitCode;
	}

    private File generateTmpFile(FileBean fd) throws IOException {
        File file = File.createTempFile(UUID.randomUUID().toString(), ".hive");
        file.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        out.write(DateRender.render(fd.getFileDescriptor().getContent()));
        out.close();
        return file;
    }

    private boolean isRunning(Integer fileId,String owner) {
    	if("RUNNING".equals(MemoryDebugHelper.JOB_STATUS_MAP.get(fileId.toString()))){
    		return true;
    	}else{
    		return false;
    	}
    }

    public String getContent(Integer fileId, String owner) {
		return fileDao.getFile(fileId, owner).getContent();
	}

	public boolean updateContent(Integer fileId, String content, String owner) {
		FileDescriptor fd = fileDao.getFile(fileId, owner);
		fd.setGmtModified(new Date());
		fd.setContent(content);
		try {
			fileDao.update(fd);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public Integer save(FileDescriptor fd) {
		Date d = new Date();
		fd.setGmtCreate(d);
		fd.setGmtModified(d);
		return fileDao.save(fd);
	}

    public DebugHistory getDebugHistory(Long id){
        return  debugHistoryDao.get(id);
    }

    public boolean updateDebugHistoryLog(Long id,String newLog){
        DebugHistory his = getDebugHistory(id);
        if(his.getLog()==null){
            his.setLog(newLog);
        }else{
            his.setLog(his.getLog()+"/n"+newLog);
        }
        debugHistoryDao.update(his);
        return true;
    }

    public List<DebugHistory> getDebugHistoryList(Integer fileId) {
       return  debugHistoryDao.list(fileId);
    }

    public DebugHistory addDebugHistory(Integer fileId,String runType){
        DebugHistory his = new DebugHistory();
        his.setStartTime(new Date());
        his.setFileId(fileId);
        his.setRunType("hive");
        debugHistoryDao.save(his);
        return his;
    }

    public void updateDebugHistory(DebugHistory his){
        debugHistoryDao.update(his);
    }

	public boolean updateName(Integer id, String name, String owner) {
		FileDescriptor fd = fileDao.getFile(id, owner);
		fd.setName(name);
		fileDao.update(fd);
		return true;
	}

	public void delete(Integer id, String owner) {
		fileDao.delete(fileDao.getFile(id, owner));
	}

}
