package com.mrlolethan.nexgenkoths.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public enum FileUtils {;
	
	public static void copyInputStreamToFile(InputStream paramInputStream, File file) throws IOException {
		InputStream inputStream;
		int ch;
		
		if (paramInputStream instanceof BufferedInputStream) {
			inputStream = paramInputStream;
		} else {
			inputStream = new BufferedInputStream(paramInputStream);
		}
		
		try (FileOutputStream outputStream = new FileOutputStream(file)) {
			while ((ch = inputStream.read()) != -1) {
				outputStream.write(ch);
			}
		} finally {
			inputStream.close();
		}
	}
	
	
}
