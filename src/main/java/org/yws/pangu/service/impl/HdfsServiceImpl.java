package org.yws.pangu.service.impl;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.yws.pangu.domain.HdfsFile;
import org.yws.pangu.service.HdfsService;
import org.yws.pangu.utils.DateUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
* Created by ywszjut on 14-6-22.
*/
public class HdfsServiceImpl implements HdfsService {

    Configuration conf=null;
    FileSystem hdfs=null;

    public HdfsServiceImpl(){
        conf=new Configuration();
        try {
            this.hdfs=FileSystem.get(conf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<HdfsFile> getFiles(String path) {
       List<HdfsFile> list = new ArrayList<HdfsFile>();
        try {
            for(FileStatus fs : hdfs.listStatus(new Path(path))){
                HdfsFile hf = new HdfsFile();
                hf.setModificationTime(DateUtils.format(fs.getModificationTime()));
                hf.setPath(fs.getPath().toString());
                hf.setName(fs.getPath().getName());
                hf.setOwner(fs.getOwner());
                hf.setSize(fs.getLen());
                hf.setType(fs.isDir()?"Dir":"File");
                list.add(hf);
            }
            return list;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public HdfsFile get(String path) {
        return null;
    }

    @Override
    public void delete(String path) {

    }

    public static void main(String[] args) {
        System.out.println( new HdfsServiceImpl().getFiles("/").get(0).getPath());
    }
}
