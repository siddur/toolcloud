package siddur.tool.sample;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.commons.io.FileUtils;

import siddur.tool.core.ConsoleTool;
import siddur.tool.core.TempFileUtil;


/**
 * 接受三个参数：
 * 一、由多个txt组成的zip文件
 * 二、一个txt文件
 * 三、一个字符串
 * 
 * 返回三个结果:
 * 一、一个txt文件
 * 二、一个文件夹生成的zip包
 * 三、一个字符串
 */
public class FileReadAndWrite extends ConsoleTool{

	@Override
	public String[] execute(String[] inputs) throws Exception {
		log("开始---->");
		File dir = TempFileUtil.findFile(inputs[0]);
		File file = TempFileUtil.findFile(inputs[1]);
		String s = inputs[2];
		
		File newDir = TempFileUtil.createEmptyDir();
		File newFile = TempFileUtil.createEmptyFile("txt");
		
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFile)));
		bw.write(s);
		
		if(dir.isDirectory()){
			for(File f : dir.listFiles()){
				readAndWrite(bw, f);
			}
		}
		if(file.isFile()){
			readAndWrite(bw, file);
		}
		
		bw.flush();
		bw.close();
		
		String[] results = new String[3];
		results[0] = TempFileUtil.file2String(newFile);
		
		FileUtils.copyFileToDirectory(newFile, newDir);
		results[1] = TempFileUtil.file2String(newDir);
		
		results[2] = s;
		
		log(results[0]);
		log(results[1]);
		log("----->结束");
		return results;
	}
	
	private void readAndWrite(BufferedWriter bw, File f)throws Exception{
		BufferedReader r = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
		String line = null;
		log("读取文件" + f.getCanonicalPath());
		while((line = r.readLine()) != null){
			//print to console
			log(line);
			
			//print to a file
			bw.append(line);
		}
		bw.flush();
		r.close();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
