package org.yws.pangu.service;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.yws.pangu.domain.FileDescriptor;
import org.yws.pangu.enums.EFileType;
import org.yws.pangu.service.impl.FileServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-context.xml")
public class FileServiceTest {

	@Autowired
	FileServiceImpl fileService;
	
	@Test
	public void testLoadRoot(){
		Assert.assertNotNull(fileService.getFile(1, "1"));
	}

    @Test
    public void testHistory(){
    }
}
