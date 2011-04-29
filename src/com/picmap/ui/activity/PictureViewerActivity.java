package com.picmap.ui.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.ImageView;

import com.beenthere.provider.Picture;
import com.beenthere.provider.PictureBox;
import com.beenthere.provider.PictureGroup;
import com.beenthere.util.Constants;
import com.beenthere.util.ThumbManager;
import com.beenthere.util.UIUtils;
import com.beenthere.view.NoteView;
import com.beenthere.view.SafeImage;
import com.flurry.android.FlurryAgent;
import com.picmap.ui.R;

public class PictureViewerActivity extends Activity implements OnItemClickListener,
															   OnClickListener, 
															   OnTouchListener, 
															   OnItemSelectedListener {

	public static final String 	ITEM_ID = "item_id";
	public static final String 	ITEM_LIST = "item_list";
	
	// Preferences
	private static final String	VIEWER_IMG_SIZE = "viewer_img_size";
	
	// Gallery
	private final long 		    SHOW_GALLERY_TIME = 2500; // 2.5 seconds
	private Handler 			mHideGalleryHandler = new Handler();
	private Runnable 			mHideGallery;
	private Gallery 			mGallery;
	private GalleryAdapter 		mGalleryAdapter;
	private long				mStartTouchTime;
	private boolean				mResizeEnabled;
	private final Handler 		mFlingHandler = new Handler();
	private final Runnable		mUpdatePictures = new UpdatePictures();
	private ThumbManager 		mThumbManager;
	
	// Images
	private SafeImage 			mSafeImage;
	private long				mSelectedPictureId = -1;
	
	// Note
	private NoteView			mNoteView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pictureviewer);
		
		mThumbManager = new ThumbManager(this);
		setupViews();

		final Gallery gallery = mGallery;
		final GalleryAdapter galleryAdapter = mGalleryAdapter;
		
		final ArrayList<Picture> pictures = getIntent().getParcelableArrayListExtra(ITEM_LIST);
		final int size = pictures.size();
		for (int i = 0; i < size; ++i) {
			galleryAdapter.add((Picture) pictures.get(i));
		}

		int positionToSelect = 0;
		long itemId = getIntent().getLongExtra(ITEM_ID, -1);
		if (itemId != -1) {
			positionToSelect = galleryAdapter.getPosition(itemId);
		}
		selectImage(positionToSelect);
		gallery.setSelection(positionToSelect, true);

		mHideGallery = new Runnable() {
			public void run() {
				hideGallery();
			}
		};
	}
	
	private void setupViews() {
		int imageDipSize = Constants.GALLERY_IMG_SIZE_DEFAULT;
		final SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
		if (settings != null) {
			imageDipSize = settings.getInt(VIEWER_IMG_SIZE, Constants.GALLERY_IMG_SIZE_DEFAULT);
		}
		mNoteView = (NoteView) findViewById(R.id.note_view);
		final NoteView noteView = mNoteView;
		noteView.getBar().setOnClickListener(this);
		noteView.setVisibility(View.INVISIBLE);
		
		mSafeImage = (SafeImage) findViewById(R.id.picviewer_imageview);
		mSafeImage.setOnClickListener(this);	
		mGallery = (Gallery) findViewById(R.id.gallery);
		mGalleryAdapter = new GalleryAdapter(this, mThumbManager);
		final Gallery gallery = mGallery;
		final GalleryAdapter galleryAdapter = mGalleryAdapter;
		galleryAdapter.setImageDipSize(imageDipSize);
		gallery.setVisibility(View.INVISIBLE);
		gallery.setAdapter(galleryAdapter);
		gallery.setCallbackDuringFling(true);
		gallery.setOnItemSelectedListener(this);
		gallery.setOnItemClickListener(this);
		gallery.setOnTouchListener(this);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(this, "BJ5VYI12GXWB75IC5C1L");
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Save user preferences
		final SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
		if (settings != null) {
			SharedPreferences.Editor editor = settings.edit();
			editor.putInt(VIEWER_IMG_SIZE, mGalleryAdapter.getImageDipSize());
			editor.commit();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ThumbManager.releaseMemory();
	}

	private void selectImage(int position) {
		final GalleryAdapter galleryAdapter = mGalleryAdapter;
		final Picture currentPicture = (Picture) galleryAdapter.getItem(position);
		if (currentPicture != null) {
			mSelectedPictureId = currentPicture.getId();
			refreshImage(currentPicture);

			getIntent().putExtra(ITEM_ID, currentPicture.getId());
			Intent resultData = new Intent();
			resultData.putExtra(ITEM_ID, currentPicture.getId());
			setResult(Activity.RESULT_OK, resultData);
		}
	}

	private void refreshImage(Picture picture) {
		if (picture != null) {
			final Bitmap miniThumbBitmap = mThumbManager.getMiniBitmap(picture);
			if (ThumbManager.bitmapExists(miniThumbBitmap)) {
				mSafeImage.setImageBitmap(miniThumbBitmap);
			} else {
				mSafeImage.setImageBitmap(null);
			}
			final NoteView noteView = mNoteView;
			final PictureBox pictureBox = PictureMapActivity.sPictureBox;
			if (pictureBox != null) {
				final PictureGroup group = pictureBox.getGroup(picture.getGroupId());
				if (group != null) {
					noteView.getBar().setTitle(group.getName());
				}
			}
			noteView.getBar().setDate(picture.getDate());
			noteView.setNote(picture.getDescription());
		}
	}
	
	private void updateGalleryPictures() {
		final Runnable updatePictures = mUpdatePictures;
		final Handler flingHandler = mFlingHandler;
		flingHandler.removeCallbacks(updatePictures);
		flingHandler.post(updatePictures);
	}

	private void delayHideGallery() {
		final Handler hideGalleryHandler = mHideGalleryHandler;
		final Runnable hideGallery = mHideGallery;
		hideGalleryHandler.removeCallbacks(hideGallery);
		hideGalleryHandler.postDelayed(hideGallery, SHOW_GALLERY_TIME);
	}
	
	private void showGallery() {
		showView(mGallery);
		showView(mNoteView);
		updateGalleryPictures();
	}
	
	private void showView(View view) {
		if (view.getVisibility() == View.INVISIBLE) {
			view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
			view.setVisibility(View.VISIBLE);
		}
	}
	
	private void hideGallery() {
		hideView(mGallery);
		final NoteView noteView = mNoteView;
		if (!noteView.isOpened()) {
			hideView(noteView);
		}
	}
	
	private void hideView(View view) {
		if (view.getVisibility() == View.VISIBLE) {
			view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out));
			view.setVisibility(View.INVISIBLE);
		}
	}

	public void onItemClick(AdapterView<?> adapterView, View view, int position, long imageId) {
		selectImage(position);
	}

	public void onClick(View view) {
		if (view == mSafeImage) {
			showGallery();
			delayHideGallery();
		} else if (view == mNoteView.getBar()) {
			final NoteView noteView = mNoteView;
			if (noteView.isOpened()) {
				noteView.close();
			} else {
				showGallery();
				noteView.open();
			}
			delayHideGallery();
		}
	}
	
	/**
	 * Callback called when the gallery view is touched.
	 * If group mode is enabled and a specific group is selected, 
	 * used to go back to all the groups.
	 */
	public boolean onTouch(final View view, MotionEvent motion) {
		if (view == mGallery) {
			delayHideGallery();
			final int action = motion.getAction();
			if (action == MotionEvent.ACTION_DOWN) {
				mResizeEnabled = false;
				mStartTouchTime = System.currentTimeMillis();
			} else if (action == MotionEvent.ACTION_MOVE) {
				final long currentTouchTime = System.currentTimeMillis();
				if (currentTouchTime - mStartTouchTime > Constants.RESIZE_TIME_THRESHOLD) {
					final float currentY = motion.getY();
					if (mResizeEnabled) {
						int YDip = UIUtils.toDip(this, currentY);
						if (YDip > Constants.GALLERY_IMG_SIZE_MAX) {
							YDip = Constants.GALLERY_IMG_SIZE_MAX;
						} else if (YDip < Constants.GALLERY_IMG_SIZE_MIN) {
							YDip = Constants.GALLERY_IMG_SIZE_MIN;
						}
						final GalleryAdapter galleryAdapter = mGalleryAdapter;
						galleryAdapter.setImageDipSize(YDip);
						galleryAdapter.notifyDataSetChanged();
					} else {
						final float height = view.getHeight();
						final int deltaDip = UIUtils.toDip(this, height - currentY);
						if (Math.abs(deltaDip) < Constants.RESIZE_DELTA_THRESHOLD) {
							mResizeEnabled = true;
						}
					}
				}
			}
		}
		return false;
	}

	public void onItemSelected(AdapterView<?> galleryView, View imageView, int position, long id) {
		updateGalleryPictures();
		delayHideGallery();
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// Nothing to do
	}
	
	/*******************************************************************
	 * Updates the pictures in the gallery
	 * @author Jordan
	 *
	 ******************************************************************/
	private class UpdatePictures implements Runnable {
		
		public void run() {   
	        final long selectedPictureId = mSelectedPictureId;
	        final ThumbManager thumbManager = mThumbManager;
	        final Gallery gallery = mGallery;
	        final int count = gallery.getChildCount();
	        for (int i = 0; i < count; ++i) {
	            final ImageView imageView = (ImageView) gallery.getChildAt(i);
	            final Picture picture = (Picture) imageView.getTag();
	            // Update highlight
	            if (picture.getId() == selectedPictureId) {
	            	imageView.setImageState(new int[] { android.R.attr.state_checked }, true);
	            } else {
	            	imageView.setImageState(new int[] { android.R.attr.state_empty }, true);
	            }
	            // Update bitmap
	            final Bitmap bitmap = thumbManager.getMicroBitmap(picture);
	            if (ThumbManager.bitmapExists(bitmap)) {
	            	imageView.setImageBitmap(bitmap);
	            } else {
	            	imageView.setImageBitmap(null);
	            }
	            gallery.invalidate();
	        }			
		}
	}
}
