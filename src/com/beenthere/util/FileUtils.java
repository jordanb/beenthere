package com.beenthere.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileUtils {

	/**
	 * Zip a directory into an outputstream.
	 * @param dir the directory to zip.
	 * @param zos the resulting stream.
	 * @throws IOException
	 */
	public static void zip(File dir, ZipOutputStream zos) throws IOException {
		zip(dir, null, zos);
	}
	
	/**
	 * Zip the current directory into an outputstream.
	 * @param dir the current directory.
	 * @param entry the name of the entry.
	 * @param zos the resulting stream.
	 * @throws IOException
	 */
	private static void zip(File dir, String entry, ZipOutputStream zos) throws IOException {
        final String[] dirList = dir.list(); 
        for (int i = 0; i < dirList.length; ++i) {
        	final String childName = dirList[i];
        	String childEntry = "";
        	if (entry != null && entry.length() > 0) {
        		childEntry += entry + "\\";
        	}
        	childEntry += childName;
            final File child = new File(dir, childName); 
        	if (child.isDirectory()) { 
        		zip(child, childEntry, zos); 
        		continue; 
            }
            final ZipEntry anEntry = new ZipEntry(childEntry); 
            zos.putNextEntry(anEntry);
            writeData(child, zos);
            zos.closeEntry();
        }
	}
	
	/**
	 * Unzip the passed stream to the given directory.
	 * @param zis the zip input stream.
	 * @param dir the output directory.
	 * @throws IOException
	 */
	public static void unzip(ZipInputStream zis, File dir) throws IOException {
		if (zis != null && dir != null) {
			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null) {
				final File newFile = new File(dir, entry.getName());
				if (entry.isDirectory()) {
					if (newFile.mkdirs()) {
						zis.closeEntry();
						unzip(zis, newFile);
						continue;
					}
				} 
				if (newFile.createNewFile()) {
					writeData(zis, newFile);
				}
				zis.closeEntry();
			}
		}
	}
	
	/**
	 * 
	 * @param src
	 * @param dst
	 * @throws IOException
	 */
	public static void copy(File src, File dst) throws IOException {
	    final FileInputStream fis = new FileInputStream(src);
	    writeData(fis, dst);
	    fis.close();
	}
	
	/**
	 * Write the contents into the destination File.
	 * @param dst
	 * @param contents
	 * @throws IOException
	 */
	public static void writeText(String contents, File dst) throws IOException {
		final FileOutputStream fos = new FileOutputStream(dst);
		final OutputStreamWriter out = new OutputStreamWriter(fos, "UTF8");
		out.write(contents);
		out.close();
		fos.close();
	}
	
	/**
	 * Write the content of the input stream into the destination File.
	 * @param dst
	 * @param is
	 * @throws IOException
	 */
	/*public static void writeText(InputStream is, File dst) throws IOException {
		final FileOutputStream fos = new FileOutputStream(dst);
		final OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF8");
		final InputStreamReader isr = new InputStreamReader(is, "UTF8");
		final char[] readBuffer = new char[1024]; 
	    int charsIn = 0; 
	    while ((charsIn = isr.read(readBuffer)) != -1) { 
	    	osw.write(readBuffer, 0, charsIn); 
        }
	    osw.close();
		fos.close();
	}*/
	
	/**
	 * Write the content of the input stream into the destination File.
	 * @param dst
	 * @param is
	 * @throws IOException
	 */
	public static void writeData(InputStream is, File dst) throws IOException {
		final FileOutputStream fos = new FileOutputStream(dst);
		writeData(is, fos);
		fos.close();
	}
	
	/**
	 * Write the content of the input file into the destination stream.
	 * @param src
	 * @param os
	 * @throws IOException
	 */
	public static void writeData(File src, OutputStream os) throws IOException {
		final FileInputStream fis = new FileInputStream(src);
		writeData(fis, os);
	    fis.close();
	}
	
	/**
	 * Write the content of the input stream into the destination stream.
	 * @param is
	 * @param os
	 * @throws IOException
	 */
	public static void writeData(InputStream is, OutputStream os) throws IOException {
		if (is != null && os != null) {
			final byte[] readBuffer = new byte[1024]; 
		    int bytesIn = 0; 
		    while ((bytesIn = is.read(readBuffer)) != -1) { 
		    	os.write(readBuffer, 0, bytesIn); 
	        }
		}
	}	
	
	
	/**
	 * Delete a file or a directory recursively.
	 * @param file the file or directory to delete.
	 */
	public static void delete(File file) {
		if (file != null) {
			if (file.isDirectory()) {
		        final String[] children = file.list();
		        final int size = children.length;
		        for (int i = 0; i < size; ++i) {
		            delete(new File(file, children[i]));
		        }
		    }
			file.delete();
		}
	}
	
	/**
	 * 
	 * @param absolutePath
	 */
	public static void delete(String absolutePath) {
		if (absolutePath != null) {
			final File file = new File(absolutePath);
			if (file != null) {
				delete(file);
			}
		}
	}
	
	public static long generateId() {
		return System.currentTimeMillis();
	}
	
	public static String generateStringId() {
		return Long.toString(generateId());
	}
	
}
