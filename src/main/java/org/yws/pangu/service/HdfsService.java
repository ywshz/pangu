package org.yws.pangu.service;

import org.yws.pangu.domain.HdfsFile;

import java.util.List;

/**
* Created by ywszjut on 14-6-22.
*/
public interface HdfsService {

    List<HdfsFile> getFiles(String path);

    HdfsFile get(String fileName);

    void delete(String path);

}
