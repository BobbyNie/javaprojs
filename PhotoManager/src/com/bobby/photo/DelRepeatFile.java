package com.bobby.photo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;

public class DelRepeatFile {

	public static HashSet<String> md5Set = new HashSet<>();
	public static  HashSet<String> fileTypeSet = new HashSet<String>();
	
	 public static String bytes2hex(byte[] bytes)  
	    {  
	        BigInteger bigInteger = new BigInteger(1, bytes);  
	        return bigInteger.toString(16);  
	    }  
	
	public static void delRepeatFile(){
		
		Path srcPath = Paths.get("/Users/niebo/Downloads/test/from");
		Path repeatToPath = Paths.get("/Users/niebo/Downloads/test/to");
		
		try{ 
			Path filetypesfile = Paths.get("/Users/niebo/Downloads/test/filetypes.txt");
			final String srcRoot = srcPath.toFile().getAbsolutePath();
			final String repeatRoot = repeatToPath.toFile().getAbsolutePath();
			Files.walkFileTree(srcPath, new SimpleFileVisitor<Path>() {  
			    @Override  
			    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {  
			    	MessageDigest md5 = null;
					try {
						md5 = MessageDigest.getInstance("MD5");
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
					} 
			    	if(attrs.isDirectory()){
			    		
			    		return FileVisitResult.CONTINUE;  
			    	}else{
			    		File f = file.toFile();
			    		String[] tmpArr = f.getName().split("\\.");
			    		if(tmpArr.length > 1){
			    			fileTypeSet.add(tmpArr[tmpArr.length - 1]);
			    		};
						
			    		DigestInputStream md5in = new DigestInputStream(new FileInputStream(f), md5);
			    		byte[] buff = new byte[10240];
			    		while((md5in.read(buff, 0, 10240)) >=0);
			    		md5in.close();
			    		String md5str = bytes2hex(md5in.getMessageDigest().digest());
			    		
			    		if(md5Set.contains(md5str)){
			    			String srcpath = file.toFile().getAbsolutePath();
			    			String subpath = srcpath.substring(srcRoot.length());
			    			Path to = Paths.get(repeatRoot,subpath);
			    			to.getParent().toFile().mkdirs();
			    			Files.move(file, to,StandardCopyOption.ATOMIC_MOVE);
			    		}else{
			    			md5Set.add(md5str);
			    		}
			    		
			    	}
			        return FileVisitResult.CONTINUE;  
			    }  
			    @Override
			    public FileVisitResult postVisitDirectory(Path dir,IOException exc) {
			    	String[] sub = dir.toFile().list();
			        if(sub != null & sub.length == 0){
			        	try {
							Files.delete(dir);
						} catch (IOException e) {
							e.printStackTrace();
						}
			        }
			    	
			        return FileVisitResult.CONTINUE;
			    }
			    
			});
			
			Files.write(filetypesfile, fileTypeSet, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
