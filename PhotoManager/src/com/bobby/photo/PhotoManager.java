package com.bobby.photo;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

public class PhotoManager {

	public static void main(String[] args) {
		
		DelRepeatFile.delRepeatFile();
		
		File source = new File("/Users/niebo/Downloads/十九大解析.mp3");
		Metadata metadata;
		try {
			metadata = com.drew.imaging.jpeg.JpegMetadataReader.readMetadata(source);

			Iterable<Directory> iter = metadata.getDirectories();
			iter.forEach((Directory directory)->{
				Collection<Tag> tags = directory.getTags();
				tags.forEach((Tag tag)->{
					System.out.println(tag.getTagName()+" = "+tag.getDescription());  
				});
			});
			
		} catch (JpegProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
