package org.yws.pangu.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.yws.pangu.service.impl.FileServiceImpl;
import org.yws.pangu.utils.DateRender;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = "classpath:spring-context.xml")
public class FileServiceTest {

	@Autowired
	FileServiceImpl fileService;
	
	@Test
	public void testLoadRoot(){
		Assert.assertNotNull(fileService.getFile(1, "1"));
	}

    @Test
    public void testHistory() throws IOException {

        String json="";
        FileReader reader = new FileReader("f:/123.txt");
        BufferedReader br = new BufferedReader(reader);
        String s1 = null;
        while ((s1 = br.readLine()) != null) {
                json+=s1;
        }
        br.close();
        reader.close();

        System.out.println(DateRender.render(json));

    }
}
