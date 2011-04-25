package com.beenthere.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Set;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Thumbnails;
import android.widget.Toast;

import com.beenthere.provider.AlbumManager;
import com.beenthere.provider.Picture;

/**
 * Manages the thumbnails for all images.
 * Uses a cache of SoftReferences for fast retrieval.
 * If not found in cache, create the thumbnail.
 * If the thumbnail is generated for the first time, save it to either:
 *  - the album directory or thumbnails directory
 *  - or the image Content Provider if not local
 * 
 * @author Jordan Bonnet
 *
 */
public class ThumbManager  {
	
	private final Context mContext;
	private final ContentResolver mContentResolver;
	private final Toast mToast;
	
	private static HashMap<Long, SoftReference<Bitmap>> sMiniCache;	
	private static HashMap<Long, SoftReference<Bitmap>> sMicroCache;
    
	public ThumbManager(Context context) {
		mContext = context;
		mContentResolver = context.getContentResolver();
		mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
		if (sMiniCache == null) {
			sMiniCache = new HashMap<Long, SoftReference<Bitmap>>();
		}
		if (sMicroCache == null) {
			sMicroCache = new HashMap<Long, SoftReference<Bitmap>>();
		}
	}
	
	public void cleanUpCache() {
		releaseMemory();
		sMiniCache.clear();
		sMicroCache.clear();
	}
	
	public static void releaseMemory() {
		final HashMap<Long, SoftReference<Bitmap>> miniCache = sMiniCache;
		if (miniCache != null) {
			final Set<Long> keys = miniCache.keySet();
			for (long key : keys) {
				final SoftReference<Bitmap> ref = miniCache.get(key);
				if (ref != null) {
					final Bitmap bitmap = ref.get();
					if (bitmapExists(bitmap)) {
						bitmap.recycle();
					}
					ref.clear();
				}
			}			
		}
	}
	
	public static boolean bitmapExists(Bitmap bitmap) {
		return bitmap != null && !bitmap.isRecycled();
	}
	
	public Bitmap getMiniBitmap(Picture picture) {
		return getMiniBitmap(picture, true);
	}
	
	public Bitmap getMiniBitmap(Picture picture, boolean create) {
		Bitmap bitmap = null;
		final long imageId = picture.getId();
		final SoftReference<Bitmap> ref = sMiniCache.get(imageId);
		if (ref != null) {
			bitmap = ref.get();
		}
		if (!bitmapExists(bitmap) && create) {
			bitmap = createMiniThumbBitmap(picture);
			if (bitmapExists(bitmap)) {
				sMiniCache.put(imageId, new SoftReference<Bitmap>(bitmap));
			} else {
				// A OutOfMemoryError may have been fired, resulting
				// in a release of memory, so try again, it has better
				// chances to work this time.
				bitmap = createMiniThumbBitmap(picture);
				if (bitmapExists(bitmap)) {
					sMiniCache.put(imageId, new SoftReference<Bitmap>(bitmap));
				} else {
					UIUtils.showAccessWarning(mContext, mToast);
				}
			}
		}
		return bitmap;
	}
	
	public Bitmap getMicroBitmap(Picture picture) {
		return getMicroBitmap(picture, true);
	}

	public Bitmap getMicroBitmap(Picture picture, boolean create) {
		Bitmap bitmap = null;
		final long imageId = picture.getId();
		final SoftReference<Bitmap> ref = sMicroCache.get(imageId);
		if (ref != null) {
			bitmap = ref.get();
		}
		if (!bitmapExists(bitmap) && create) {
			bitmap = createMicroThumbBitmap(picture);
			if (bitmapExists(bitmap)) {
				sMicroCache.put(imageId, new SoftReference<Bitmap>(bitmap));
			} else {
				UIUtils.showAccessWarning(mContext, mToast);
			}
		}
		return bitmap;
	}
    
    private Bitmap createMiniThumbBitmap(Picture picture) {
    	Bitmap bitmap = createFromPath(picture.getMiniPath());
    	if (bitmap == null) {
    		bitmap = createFromFullAndSave(picture, Thumbnails.MINI_KIND);
    	}
    	return bitmap;
    }
    
    private Bitmap createMicroThumbBitmap(Picture picture) {
    	Bitmap bitmap = createFromPath(picture.getMicroPath());
    	if (bitmap == null) {
    		bitmap = createMicroFromMiniAndSave(picture);
    	}
    	if (bitmap == null) {
    		bitmap = createFromFullAndSave(picture, Thumbnails.MICRO_KIND);
    	}
    	return bitmap;
    }
    
    public static Bitmap createFromPath(String path) {
    	Bitmap bitmap = null;
    	if (path != null) {
    		try {
    			bitmap = BitmapFactory.decodeFile(path);
    		}
    		catch (OutOfMemoryError error) {
    			releaseMemory();
        		System.gc();
        	}
    	}
    	return bitmap;
    }
    
    private Bitmap createFromFullAndSave(Picture picture, int kind) {	
    	if (picture.isLocal()) {
    		// Pictures from albums have their own generated id.
    		// Thus, it will be impossible to retrieve the image
    		// from the database.
    		return null;
    	}
    	
    	Bitmap bitmap = null;
    	final long imageId = picture.getId();
    	Options options = new BitmapFactory.Options();
		options.inSampleSize = 1;
		options.inJustDecodeBounds = true;
    	Uri imageUri = ContentUris.withAppendedId(Images.Media.EXTERNAL_CONTENT_URI, imageId);
    	try {
			BitmapFactory.decodeStream(mContentResolver.openInputStream(imageUri), null, options);
			if (!options.mCancel && options.outWidth != -1 && options.outHeight != -1) {
    			options.inSampleSize = computeSampleSize( options.outWidth, 
    					                                  options.outHeight, 
    					                                  getDimension(kind, true) );
    			options.inJustDecodeBounds = false;
    			options.inDither = false;
    			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
    			bitmap = BitmapFactory.decodeStream( mContentResolver.openInputStream(imageUri), 
    					                             null, 
    					                             options );
    			if (bitmap != null) {
    				try {
    					saveThumb(picture, bitmap, options, kind);
    				} catch (IOException ioe) {
    					// Nothing to do
    	    		}
    			}
			}
    	} catch (IOException ioe) {
    		// Nothing to do
    	} catch (OutOfMemoryError error) {
    		releaseMemory();
    		System.gc();
    	}
    	return bitmap;
    }
    
    private Bitmap createMicroFromMiniAndSave(Picture picture) {	
    	Bitmap bitmap = null;
    	String miniThumbPath = picture.getMiniPath();
    	if (miniThumbPath != null) {
			Options options = new BitmapFactory.Options();
			options.inSampleSize = 1;
			options.inJustDecodeBounds = true;
	    	try {
    			BitmapFactory.decodeFile(miniThumbPath, options);
    			if (!options.mCancel && options.outWidth != -1 && options.outHeight != -1) {
	    			options.inSampleSize = computeSampleSize( options.outWidth, 
	    					                                  options.outHeight, 
	    					                                  getDimension(Thumbnails.MICRO_KIND, true) );
	    			options.inJustDecodeBounds = false;
	    			options.inDither = false;
	    			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
	    			bitmap = BitmapFactory.decodeFile(miniThumbPath, options);
	    			if (bitmap != null) {
	    				try {
	    					if (picture.isLocal()) {
	    						saveThumbToLocalDir(picture, bitmap);
	    					} else {
	    						saveThumb(picture, bitmap, options, Thumbnails.MICRO_KIND);
	    					}
	    				} catch (IOException ioe) {
	    					// Nothing to do
	    	    		}
	    			}
    			}
	    	} catch (OutOfMemoryError error) {
	    		releaseMemory();
	    		System.gc();
	    	}
    	}
    	return bitmap;
    }
    
    private void saveThumbToLocalDir(Picture picture, Bitmap bitmap) throws IOException {
    	long thumbId = picture.getId();
    	if (thumbId != -1) {
    		final File thumbFile = new File(AlbumManager.getThumbnailDir(), thumbId + Constants.JPG);
    		if (thumbFile.createNewFile()) {
    			final FileOutputStream fos = new FileOutputStream(thumbFile);
    			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos)) {
    				picture.setMicroPath(thumbFile.getAbsolutePath());
    			}
    			fos.close();
    		}
    	}
    }
    
    private void saveThumb(Picture picture, Bitmap bitmap, Options options, int kind) throws IOException {
    	if (picture.isLocal()) {
    		return;
    	}
    	
		ContentValues values = new ContentValues(4);
		values.put(Thumbnails.HEIGHT,   getDimension(kind, options.outHeight < options.outWidth));
		values.put(Thumbnails.WIDTH,    getDimension(kind, options.outWidth < options.outHeight));
		values.put(Thumbnails.IMAGE_ID, picture.getId());
		values.put(Thumbnails.KIND,     kind);
		Uri uri = mContentResolver.insert(Thumbnails.EXTERNAL_CONTENT_URI, values);
	    OutputStream outStream = mContentResolver.openOutputStream(uri);
	    if (bitmap.compress(Bitmap.CompressFormat.JPEG, 75, outStream)) {
	    	// Update Table of Path
	    	Cursor thumbCursor = mContentResolver.query(uri, null, null, null, null);
	    	if (thumbCursor == null) {
	    		throw new IOException();
	    	} else {
	    		if (thumbCursor.moveToFirst()) {
					int dataColumn = thumbCursor.getColumnIndex(Thumbnails.DATA);
					String thumbPath = thumbCursor.getString(dataColumn);
					if (kind == Thumbnails.MICRO_KIND) {
						picture.setMicroPath(thumbPath);
					} else if (kind == Thumbnails.MINI_KIND) {
						picture.setMiniPath(thumbPath);
					}
	    		}
	    		thumbCursor.close();
			}
	    }
	    outStream.close();
    }
    
    private static int getDimension(int thumbKind, boolean shortDim) {
    	int dimension = 100;
    	if (thumbKind == Thumbnails.MICRO_KIND) {
    		dimension = Constants.MICRO_DIM;
    	} else {
    		dimension = shortDim ? Constants.MINI_SHORT_DIM : Constants.MINI_LONG_DIM;
    	}
    	return dimension;
    }
    
    private static int computeSampleSize(float width, float height, float sizeTarget) {
    	return (width > height) ? Math.round(height / sizeTarget) : Math.round(width / sizeTarget);
    }
}
