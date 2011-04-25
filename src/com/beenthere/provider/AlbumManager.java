package com.beenthere.provider;

import java.io.File;
import java.util.ArrayList;
import android.os.Environment;

import com.beenthere.util.FileUtils;
import com.beenthere.util.KmzUtils;
import com.beenthere.util.KmzUtils.KmzParseResult;

public class AlbumManager {
	
	private static final String 	BEEN_THERE = ".beenthere";
	private static final String 	THUMBNAILS = ".thumbnails";
	
	private static File				sThumbnailDir;
	private static File				sMainDir;
	
	public static File getMainDir() {
		if (sMainDir == null) {
			File extStorage = Environment.getExternalStorageDirectory();
			if (extStorage != null) {
				sMainDir = retrieveDir(extStorage, BEEN_THERE);
			}
		}
		return sMainDir;
	}
	
	public static File getThumbnailDir() {
		if (sThumbnailDir == null) {
			File mainDir = getMainDir();
			if (mainDir != null) {
				sThumbnailDir = retrieveDir(sMainDir, THUMBNAILS);
			}
		}
		return sThumbnailDir;
	}
	
	public static ArrayList loadAlbums() {
		final ArrayList albums = new ArrayList();
		final File mainDir = getMainDir();
		if (mainDir != null) {
			final File[] files = mainDir.listFiles();
			for (int i = 0; i < files.length; ++i) {
				final File file = files[i];
				if (file.isDirectory() && !file.getName().equals(THUMBNAILS)) {
					final FileAlbum album = loadAlbum(file);
					if (album != null) {
						albums.add(album);
					}
				}
			}
		}
		return albums;
	}
	
	public static FileAlbum loadAlbum(File file) {
		FileAlbum album = null;
		if (file.isDirectory()) {
			final KmzParseResult result = KmzUtils.parse(file);
			if (result.isValid) {
				album = new FileAlbum(file, result.albumName, result.pictures);
			}
		}
		return album;
	}
	
	public static File createDirectory() {
		final File mainDir = getMainDir();
		if (mainDir != null) {
			return new File(mainDir, FileUtils.generateStringId());
		}
		return null;
	}
	
	private static File retrieveDir(File parent, String name) {
		File dir = null;
		if (parent != null) {
			if (parent.isDirectory()) {
				if (parent.canRead()) {
					dir = new File(parent, name);
					if (!dir.exists()) {
						dir.mkdirs();
					}
				}
			}
		}
		return dir;
	}
}
