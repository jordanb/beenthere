package com.beenthere.provider;

import java.io.File;
import java.util.ArrayList;

import com.beenthere.util.FileUtils;

public class FileAlbum extends PictureGroup {
	
	private File 		mFile;
	private boolean		mIsNew;
	
	public FileAlbum(File file, String name, ArrayList pictures) {
		super(Long.valueOf(file.getName()), name, pictures);
		mFile = file;
		mIsNew = false;
	}
	
	public FileAlbum(ArrayList pictures) {
		this( AlbumManager.createDirectory(),
			  "",
			  clonePictures(pictures) );
		mIsNew = true;
	}
	
	public FileAlbum newInstance() {
		return new FileAlbum(mFile, getName(), clonePictures(getPictures()));
	}
	
	public static ArrayList clonePictures(ArrayList<Picture> pictures) {
		final ArrayList newPictures = new ArrayList();
		final int size = pictures.size();
		for (int i = 0; i < size; ++i) {
			newPictures.add(pictures.get(i).newInstance());
		}
		return newPictures;
	}
	
	public void delete() {
		FileUtils.delete(mFile);
	}
	
	public boolean isNew() {
		return mIsNew;
	}
	
	public void setNew(boolean isNew) {
		mIsNew = isNew;
	}
	
	public File getFile() {
		return mFile;
	}
	
	public int getRemainingCount() {
		final ArrayList pictures = getPictures();
		final int size = pictures.size();
		int count = size;
		for (int i = 0; i < size; ++i) {
			if (((Picture) pictures.get(i)).getDelete()) {
				--count;
			}
		}
		return count;
	}
}
