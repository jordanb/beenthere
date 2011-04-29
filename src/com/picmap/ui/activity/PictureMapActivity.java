package com.picmap.ui.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beenthere.provider.AlbumManager;
import com.beenthere.provider.FileAlbum;
import com.beenthere.provider.Picture;
import com.beenthere.provider.PictureBox;
import com.beenthere.provider.PictureGroup;
import com.beenthere.provider.PictureManager;
import com.beenthere.provider.Viewable;
import com.beenthere.util.Constants;
import com.beenthere.util.FileUtils;
import com.beenthere.util.ThumbManager;
import com.beenthere.util.UIUtils;
import com.beenthere.view.AnimationListenerAdapter;
import com.beenthere.view.BarView;
import com.flurry.android.FlurryAgent;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.picmap.ui.R;

public class PictureMapActivity extends MapActivity implements 	OnItemClickListener, 
																OnItemSelectedListener,
																ItemizedOverlay.OnFocusChangeListener,
																OnTouchListener {
																// ECLAIR OnZoomListener {

	// Style (Options)
	public static int				STYLE = 0; //Constants.NO_VIEW_ALBUMS 	| 
											// Constants.NO_CREATE_ALBUM 	| 
											// Constants.NO_STATUS_BAR;
	
	// Intents
	public  static final String     VND_KMZ_TYPE = "application/vnd.google-earth.kmz";
	private static final String 	SELECTED_ITEM_ID = "selected_item_id";
	private static final int 		VIEW_IMAGE_REQUEST = 0;

	// Dialogs
	private View					mLoadPanel;
	private TextView				mLoadLabel;
	private ProgressBar 			mLoadProgress;
	private ProgressDialog 			mProgressDialog;

	// Map elements
	private static final int 		ACTION_ZOOM 		= 5;
	private static final String 	MAP_MODE 			= "map_mode";
	private static final String 	SHOW_LOCATION_MODE 	= "location_mode";
	private static final int 		MAP 				= 1;
	private static final int 		SATELLITE 			= 2;
	private boolean					mShowLocationMode   = true;
	private MapView 				mMapView;
	private OverlayViewer 			mOverlayViewer;
	private MyLocationOverlay       mLocationOverlay;
	private Drawable 				mDot;
	private Drawable 				mDotSelected;

	// Gallery
	private static final String		MAP_IMG_SIZE = "map_img_size";
	private long					mStartTouchTime;
	private boolean					mResizeEnabled;
	private float					mOrigY;
	private float					mPreviousY;
	private BarView					mBarView;
	private Gallery 				mGallery;
	private GalleryAdapter 			mGalleryAdapter;
	private final Handler 			mFlingHandler = new Handler();
	private final Runnable			mUpdatePictures = new UpdatePictures();
	private ThumbManager			mThumbManager;
	private long					mSelectedItemId = -1;
	
	// Tasks
	private LoadPictureTask			mLoadTask;
	private ImportAlbumTask			mImportTask;

	// Model
	public static PictureBox 		sPictureBox = null;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.picturemap);
		
		mThumbManager = new ThumbManager(this);
		SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0); 
		
		// Initialize views
		setupViews(settings);
		
		if (sPictureBox == null) {
			sPictureBox = new PictureBox(settings);
		}	
		
		// Restore tasks
		boolean isLoadRunning = false;
		boolean isImportRunning = false;
		final Object task = getLastNonConfigurationInstance();
		if (task != null) {
			if (task instanceof LoadPictureTask) {
				mLoadTask = (LoadPictureTask) task;
				isLoadRunning = restoreLoadingTask();
			} else if (task instanceof ImportAlbumTask) {
				mImportTask = (ImportAlbumTask) task;
				isImportRunning = restoreImportTask();
			}
		} 
		
		// Load, import, or only update?
		final InputStream kmzStream = getKmzFromIntent();
		final boolean isKmzPresent = kmzStream != null;
		final boolean isModelLoaded = sPictureBox.isLoaded();
		
		if (!isModelLoaded && !isKmzPresent && !isLoadRunning && !isImportRunning) {
			onLoadPictures();
		}
		
		if (isLoadRunning && isKmzPresent) {
			cancelImportIntent();
			final CharSequence msg = getResources().getText(R.string.import_after_refresh);
			Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		}
		
		if (isKmzPresent && !isImportRunning && !isLoadRunning) {
			onImport(kmzStream);
		}
		
		if (isModelLoaded) {
			updateMap();
		}
	}
	
	private void setupViews(SharedPreferences settings) {
		// Restore user preferences
		// 1. Retrieve last selected image.
		// 2. Retrieve map mode.
		// 3. Retrieve location mode status.
		int mapMode = MAP;
		int imageDipSize = Constants.GALLERY_IMG_SIZE_DEFAULT;
		if (settings != null) {
			mSelectedItemId = settings.getLong(SELECTED_ITEM_ID, -1);
			mapMode = settings.getInt(MAP_MODE, MAP);
			mShowLocationMode = settings.getBoolean(SHOW_LOCATION_MODE, true);
			imageDipSize = settings.getInt(MAP_IMG_SIZE, Constants.GALLERY_IMG_SIZE_DEFAULT);
		}

		mMapView = (MapView) findViewById(R.id.mapview);
		final MapView mapView = mMapView;
		
		mapView.setBuiltInZoomControls(true);
		// ECLAIR mMapView.getZoomButtonsController().setOnZoomListener(this);
		if (mapMode == MAP) {
			mapView.setSatellite(false);
		} else if (mapMode == SATELLITE) {
			mapView.setSatellite(true);
		}

		final List<Overlay> overlays = mapView.getOverlays();
		
		mDot = this.getResources().getDrawable(R.drawable.ic_dot);
		mDotSelected = this.getResources().getDrawable(R.drawable.ic_dot_selected);
		mOverlayViewer = new OverlayViewer(this, mDot, mDotSelected);
		overlays.add(mOverlayViewer);
		
		mLocationOverlay = new MyLocationOverlay(this, mapView);
		overlays.add(mLocationOverlay);
		
		mBarView = (BarView) findViewById(R.id.bar_view);
		if ((STYLE & Constants.NO_STATUS_BAR) != 0) {
			mBarView.setVisibility(View.GONE);
		}
		
		mGallery = (Gallery) findViewById(R.id.gallery);
		mGalleryAdapter = new GalleryAdapter(this, mThumbManager);	
		final Gallery gallery = mGallery;
		final GalleryAdapter galleryAdapter = mGalleryAdapter;
		galleryAdapter.setImageDipSize(imageDipSize);
		gallery.setAdapter(galleryAdapter);
		gallery.setCallbackDuringFling(true);
		gallery.setOnItemClickListener(this);
		gallery.setOnItemSelectedListener(this);
		gallery.setOnTouchListener(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		FlurryAgent.onStartSession(this, "BJ5VYI12GXWB75IC5C1L");
		if (sPictureBox != null && sPictureBox.hasVisibleChanged()) {
			updateMap();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mShowLocationMode) {
			mLocationOverlay.enableMyLocation();
		}
		updateGalleryPictures(200);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mLocationOverlay.disableMyLocation();
		
		// Save user preferences
		SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
		if (settings != null) {
			SharedPreferences.Editor editor = settings.edit();
			editor.putLong(SELECTED_ITEM_ID, mSelectedItemId);
			editor.putBoolean(SHOW_LOCATION_MODE, mShowLocationMode);
			editor.putInt(MAP_MODE, mMapView.isSatellite() ? SATELLITE : MAP);
			editor.putInt(MAP_IMG_SIZE, mGalleryAdapter.getImageDipSize());
			if (sPictureBox != null) {
				sPictureBox.savePreferences(editor);
			}
			editor.commit();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mLoadTask != null) {
			mLoadTask.unbind();
		}
		if (mImportTask != null) {
			mImportTask.unbind();
		}
		mThumbManager.cleanUpCache();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.picturemap, menu);
		if ((STYLE & Constants.NO_VIEW_ALBUMS) != 0) {
			final MenuItem viewAlbums = menu.findItem(R.id.view_albums);
			if (viewAlbums != null) {
				viewAlbums.setVisible(false);
			}
		}
		if ((STYLE & Constants.NO_CREATE_ALBUM) != 0) {
			final MenuItem createAlbum = menu.findItem(R.id.create_album);
			if (createAlbum != null) {
				createAlbum.setVisible(false);
			}
		}
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		final MenuItem showLocationModeMenu = menu.findItem(R.id.show_location_mode);
		if (showLocationModeMenu != null) {
			showLocationModeMenu.setChecked(mShowLocationMode);
		}
		if (mMapView.isSatellite()) {
			final MenuItem sat = menu.findItem(R.id.satellite_mode);
			if (sat != null) {
				sat.setChecked(true);
			}
		} else {
			final MenuItem map = menu.findItem(R.id.map_mode);
			if (map != null) {
				map.setChecked(true);
			}
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.satellite_mode:
			mMapView.setSatellite(true);
			return true;
		case R.id.map_mode:
			mMapView.setSatellite(false);
			return true;
		case R.id.my_location:
			onGoToMyLocation();
			return true;
		case R.id.show_location_mode:
			if (mShowLocationMode) {
				mShowLocationMode = false;
				mLocationOverlay.disableMyLocation();
			} else {
				mShowLocationMode = true;
				mLocationOverlay.enableMyLocation();
			}
			return true;
		case R.id.refresh:
			onLoadPictures();
			return true;
		case R.id.view_albums:
			onViewAlbums();
			return true;
		case R.id.create_album:
			onCreateAlbum();
			return true;
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == VIEW_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
			mSelectedItemId = data.getLongExtra(PictureViewerActivity.ITEM_ID, -1);
			// Force a refresh in onStart() method, so that the picture selection
			// in the viewer is persisted.
			sPictureBox.setVisibleChanged();
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	protected boolean isLocationDisplayed() {
		return true;
	}
	
	/**
	 * 1. Populate the map with the overlays based on all the
	 *    visible pictures in the Picture Box.
	 * 2. Update the gallery accordingly.
	 * 3. Make sure the image selection is persisted.
	 */
	private void updateMap() {
		if (sPictureBox != null) {
			mOverlayViewer.initOverlays(sPictureBox.getVisiblePictures());
			mMapView.invalidate();
			updateGallery();
			
			final GalleryAdapter galleryAdapter = mGalleryAdapter;
			if (galleryAdapter.getCount() > 0) {
				int position = galleryAdapter.getPosition(mSelectedItemId);
				selectViewable(position, true);
				if (position >= 0) {
					selectOverlays(galleryAdapter.getItem(position));
				}
			}
		}
	}

	/**
	 * Updates the gallery with the pictures available on the 
	 * current map (screenshot).
	 * Called : 
	 *   - on finger up after moving the map
	 *   - when a zoom is performed
	 *   - in update map
	 */
	private void updateGallery() {
		final GalleryAdapter galleryAdapter = mGalleryAdapter;
		galleryAdapter.clear();
		final PictureBox pictureBox = sPictureBox;
		if (pictureBox != null) {
			final MapView mapView = mMapView;
			pictureBox.setMapInfo(mapView.getMapCenter(), mapView.getLatitudeSpan(), mapView.getLongitudeSpan());
			final ArrayList viewables = pictureBox.getScreenViewables();
			final int size = viewables.size();
			if (size > 0) {
				for (int i = 0; i < size; ++i) {
					galleryAdapter.add((Viewable) viewables.get(i));
				}
				updateGalleryPictures(0);
			} else {
				final BarView barView = mBarView;
				barView.setTitle("");
				barView.setInfo("");
			}
		}
	}
	
	private void updateGalleryPictures(long delayMillis) {
		final Runnable updatePictures = mUpdatePictures;
		final Handler flingHandler = mFlingHandler;
		flingHandler.removeCallbacks(updatePictures);
		flingHandler.postDelayed(updatePictures, delayMillis);
	}
	
	private void onCreateAlbum() {
		if (sPictureBox.getScreenPictures().isEmpty()) {
			Toast.makeText(this, getResources().getText(R.string.no_pictures_on_screen), Toast.LENGTH_SHORT).show();
		} else {
			final Intent createAlbum = new Intent(this, EditAlbumActivity.class);
			createAlbum.putExtra(EditAlbumActivity.ALBUM_ID, EditAlbumActivity.NEW_ALBUM);
			startActivity(createAlbum);
		}
	}

	private void onViewAlbums() {
		if (sPictureBox.getGroups().isEmpty()) {
			Toast.makeText(this, getResources().getText(R.string.no_albums), Toast.LENGTH_SHORT).show();
		} else {
			startActivity(new Intent(this, ListAlbumActivity.class));
		}
	}
	
	private void onGoToMyLocation() {
		if (!mShowLocationMode) {
			mShowLocationMode = true;
			mLocationOverlay.enableMyLocation();
		}
		final GeoPoint myLocationPoint = mLocationOverlay.getMyLocation();
		if (myLocationPoint != null) {
			mMapView.getController().animateTo(myLocationPoint);
		} else {
			final CharSequence msg = getResources().getText(R.string.no_location);
			Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		if (mLoadTask != null && mLoadTask.getStatus() == AsyncTask.Status.RUNNING) {
			return mLoadTask;
		}
		if (mImportTask != null && mImportTask.getStatus() == AsyncTask.Status.RUNNING) {
			return mImportTask;
		}
		return null;
	}
	
	private InputStream getKmzFromIntent() {
		InputStream kmzStream = null;
		final Intent intent = getIntent();
		if (intent != null) {
			final String type = intent.getType();
			final String action = intent.getAction();
			if (type != null && action != null) {
				if (type.equals(VND_KMZ_TYPE) && action.equals(Intent.ACTION_VIEW)) {
					final Uri data = (Uri) intent.getData();
					if (data != null) {
						try {
							kmzStream = getContentResolver().openInputStream(data);
						} catch (FileNotFoundException e) {
							// TO DO toast Problem blabla
							kmzStream = null;
						} 
					}
				}
			}
		} 
		return kmzStream;
	}
	
	private void cancelImportIntent() {
		final Intent intent = getIntent();
		if (intent != null) {
			final String type = intent.getType();
			if (type != null && type.equals(VND_KMZ_TYPE)) {
				intent.setType(null);
			}
		}
	}
	
	private void hideProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}
	
	private void onImport(InputStream kmzStream) {
		if (mImportTask == null || mImportTask.getStatus() == AsyncTask.Status.FINISHED) {
			mImportTask = new ImportAlbumTask(kmzStream);
			mImportTask.bind(this);
			mImportTask.execute();
        }
	}
	
	private void showImportDialog() {
		if (mProgressDialog == null) {
			final ProgressDialog progressDialog = new ProgressDialog(this);
			final CharSequence message = getResources().getText(R.string.importing_album);
			progressDialog.setMessage(message);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setCancelable(true);
			progressDialog.setIndeterminate(true);
			mProgressDialog = progressDialog;
		}
		mProgressDialog.show();
	}
	
	private boolean restoreImportTask() {
		if (mImportTask != null && mImportTask.getStatus() == AsyncTask.Status.RUNNING) {
			mImportTask.bind(this);
			showImportDialog();
			return true;
		}
		return false;
	}
	
	private void onLoadPictures() {
        if (mLoadTask == null || mLoadTask.getStatus() == AsyncTask.Status.FINISHED) {
        	mLoadTask = new LoadPictureTask();
        	mLoadTask.bind(this);
        	mLoadTask.execute();
        } else {
        	Toast.makeText(this, getResources().getText(R.string.refresh_running), Toast.LENGTH_SHORT).show();
        }
    }
	
	private void showLoadingDialog() {
		if (mLoadPanel == null) {
	        mLoadPanel = ((ViewStub) findViewById(R.id.stub_load)).inflate();
	        mLoadLabel = (TextView) mLoadPanel.findViewById(R.id.label_load);
	        mLoadProgress = (ProgressBar) mLoadPanel.findViewById(R.id.progress);
	        mLoadProgress.setIndeterminate(true);
		}
		if (mLoadTask != null) {
			mLoadLabel.setText(mLoadTask.textResourceId);
		}
		mLoadPanel.startAnimation(AnimationUtils.loadAnimation(this, R.anim.stub_slide_in));
		mLoadPanel.setVisibility(View.VISIBLE);
	}
	
	private void hideLoadingDialog() {
		if (mLoadPanel != null) {
			mLoadPanel.startAnimation(AnimationUtils.loadAnimation(this, R.anim.stub_slide_out));
			mLoadPanel.setVisibility(View.GONE);
		}
	}
	
	private void setLoadingText(int textResourceId) {
		if (mLoadLabel != null) {
			mLoadLabel.setText(textResourceId);
		}
	}
	
	private boolean restoreLoadingTask() {
		if (mLoadTask != null && mLoadTask.getStatus() == AsyncTask.Status.RUNNING) {
			mLoadTask.bind(this);
			showLoadingDialog();
			return true;
		}
		return false;
	}

	/**
	 * Shows the selected image in the picture viewer.
	 * @param position
	 */
	private void showImage(Picture picture) {
		long itemId = picture.getId();
		Intent viewPicture = new Intent(this, PictureViewerActivity.class);
		final ArrayList<Picture> pictures = sPictureBox.getScreenViewables();
		viewPicture.putParcelableArrayListExtra(PictureViewerActivity.ITEM_LIST, pictures);
		viewPicture.putExtra(PictureViewerActivity.ITEM_ID, itemId);
		this.startActivityForResult(viewPicture, VIEW_IMAGE_REQUEST);
	}

	/**
	 * Selects the viewable in the gallery at the given position.
	 * If animate is true, then force the gallery to move to the
	 * given position.
	 * @param position
	 * @param animate
	 */
	private void selectViewable(int position, boolean animate) {
		if (position < 0) {
			mSelectedItemId = -1;
			final BarView barView = mBarView;
			barView.setTitle("");
			barView.setInfo("");
		} else {
			final Viewable viewable = mGalleryAdapter.getItem(position);
			mSelectedItemId = viewable.getId();
			if (viewable instanceof Picture) {
				final Picture picture = (Picture) viewable;
				final PictureGroup group = sPictureBox.getGroup(picture.getGroupId());
				mBarView.setTitle(group.getName());
				mBarView.setDate(picture.getDate());
			}		
			if (animate) {
				mGallery.setSelection(position, true);
			} else {
				// Update the highlight (orange border).
				updateGalleryPictures(0);
			}
		}
	}

	/**
	 * Highlight the overlays corresponding to the viewable.
	 * @param viewable
	 */
	private void selectOverlays(Viewable viewable) {
		final OverlayViewer overlayViewer = mOverlayViewer;
		if (viewable instanceof PictureGroup) {
			overlayViewer.setSelection(((PictureGroup) viewable).getPictures());
		} else if (viewable instanceof Picture) {
			overlayViewer.setSelection(((Picture) viewable).getOverlay(), true);
		}	
		mMapView.invalidate();
	}
	
	/**
	 * Shows the gallery and the title bar with a fade 
	 * animation.
	 */
	private void showGallery() {
		final Gallery gallery = mGallery;
		if (gallery.getVisibility() == View.INVISIBLE) {
			gallery.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
			gallery.setVisibility(View.VISIBLE);
		}
    }

	/**
	 * Hides the gallery and the title bar with a fade 
	 * animation.
	 */
    private void hideGallery() {
    	final Gallery gallery = mGallery;
		if (gallery.getVisibility() == View.VISIBLE) {
			gallery.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out));
			gallery.setVisibility(View.INVISIBLE);
		}
    }
    
    /**
     * Show all groups with a cool animation.
     */
    private void showAllGroups() {
    	final Gallery gallery = mGallery;
    	final Context context = this;
    	final Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.gallery_slide_out);
		slideOut.setAnimationListener(new AnimationListenerAdapter() {
			public void onAnimationEnd(Animation anim) {
				mSelectedItemId = sPictureBox.getSelectedGroupId();
				sPictureBox.selectAllGroups();
				updateMap();
				gallery.startAnimation(AnimationUtils.loadAnimation(context, R.anim.gallery_slide_in));
			}
		});
		gallery.startAnimation(slideOut);
    }
    
    /**
     * Show the specified group with a cool animation.
     * @param groupId
     */
    private void showGroup(final long groupId) {
    	final Gallery gallery = mGallery;
    	final Context context = this;
    	final Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.gallery_slide_out);
		slideOut.setAnimationListener(new AnimationListenerAdapter() {
			public void onAnimationEnd(Animation anim) {
				sPictureBox.setSelectedGroupId(groupId);
				updateMap();
				gallery.startAnimation(AnimationUtils.loadAnimation(context, R.anim.gallery_slide_in));
			}
		});
		gallery.startAnimation(slideOut);
    }

	/**
	 * Callback called when an image in the gallery is clicked on.
	 * Highlight the right overlay(s).
	 * If Picture, show the image in the viewer if already selected.
	 * If FileAlbum, open album folder if already selected.
	 */
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long imageId) {
		final Viewable viewableClicked = mGalleryAdapter.getItem(position);
		if (viewableClicked.getId() == mSelectedItemId) {
			if (viewableClicked instanceof Picture) {
				showImage((Picture) viewableClicked);
			} else if (viewableClicked instanceof PictureGroup) {
				showGroup(viewableClicked.getId());
			}
		} else {
			selectViewable(position, false);
			selectOverlays(viewableClicked);
		}
	}

	/**
	 * Callback called when an overlay is clicked on.
	 * Highlight the right overlay, and selects the corresponding
	 * image in the gallery.
	 * @param overlay
	 * @param overlayItem
	 */
	public void onFocusChanged(ItemizedOverlay overlay, OverlayItem overlayItem) {
		if (overlayItem != null) {
			final PictureBox pictureBox = sPictureBox;
			mOverlayViewer.setSelection(overlayItem, false);
			mMapView.invalidate();
			if (pictureBox != null) {
				final Picture picture = pictureBox.getPicture(overlayItem);
				if (picture != null) {
					int position = -1;
					if (pictureBox.isGroupMode() && pictureBox.isAllGroups()) {
						position = mGalleryAdapter.getPosition(picture.getGroupId());
					} else {
						position = mGalleryAdapter.getPosition(picture.getId());
					}
					selectViewable(position, true);
				}
			}
		}
	}

	/**
	 * Callback called when the map is moved or when a zoom is 
	 * performed by clicking on an already selected overlay.
	 * @param action
	 * @param mapView
	 * @param overlay
	 */
	public void onMapTouchEvent(int action, MapView mapView, OverlayItem overlay) {
		if (action == MotionEvent.ACTION_DOWN) {
			mStartTouchTime = System.currentTimeMillis();
		} else if (action == MotionEvent.ACTION_UP) {
			updateGallery();
			showGallery();
		} else if (action == ACTION_ZOOM) {
			if (overlay != null) {
				final MapController mapController = mapView.getController();
				mapController.setCenter(overlay.getPoint());
				mapController.zoomIn();
			}
			updateGallery();
		} else if (action == MotionEvent.ACTION_MOVE) {
			final long currentTouchTime = System.currentTimeMillis();
			if (currentTouchTime - mStartTouchTime > Constants.HIDE_TIME_THRESHOLD) {
				hideGallery();
			}
		}
	}
	
	/**
	 * Callback called when the fling is finished or an item is
	 * selected. Set the pictures on the gallery.
	 */
	public void onItemSelected(AdapterView<?> adapterView, View view, int position, long arg3) {
		updateGalleryPictures(0);
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Callback called when the gallery view is touched.
	 * If group mode is enabled and a specific group is selected, 
	 * used to go back to all the groups.
	 */
	public boolean onTouch(final View view, MotionEvent motion) {
		final PictureBox pictureBox = sPictureBox;
		final int action = motion.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
			mResizeEnabled = false;
			mStartTouchTime = System.currentTimeMillis();
			if (pictureBox.isGroupMode() && !pictureBox.isAllGroups()) {
				mOrigY = motion.getY();
				mPreviousY = mOrigY;
			}
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
			if (pictureBox.isGroupMode()) {
				if (!pictureBox.isAllGroups()) {
					final float currentY = motion.getY();
					final int origDeltaDip = UIUtils.toDip(this, mOrigY - currentY);
					final int speedDip = UIUtils.toDip(this, mPreviousY - currentY);
					if (origDeltaDip > Constants.SLIDE_DELTA_THRESHOLD && 
							speedDip > Constants.SLIDE_SPEED_THRESHOLD) {
						showAllGroups();
					}
					mPreviousY = currentY;
				}
			}
		}
		return false;
	}

	/**
	 * -- ECLAIR ONLY --
	 * Callback called when a zoom is performed by clicking on
	 * the zoom buttons.
	 * @param zoomIn
	 */
	public void onZoom(boolean zoomIn) {
		if (zoomIn) {
			mMapView.getController().zoomIn();
		} else {
			mMapView.getController().zoomOut();
		}
		updateGallery();
	}

	/**
	 * -- ECLAIR ONLY --
	 * Callback called when the visibility of the zoom buttons
	 * changes.
	 * @param arg0
	 */
	public void onVisibilityChanged(boolean arg0) {
		// TODO Auto-generated method stub
	}
	
	/*******************************************************************
	 * Loading Task
	 * @author Jordan
	 *
	 ******************************************************************/
	private static class LoadPictureTask extends AsyncTask {
		
		private static final int ALBUMS = 1;
		private static final int PICTURES = 2;
		private static String sPhoneGroupStr = "";
		
		private PictureMapActivity mActivity;
		private boolean mBind = true;
		int textResourceId;
		
		public LoadPictureTask() {
			textResourceId = R.string.load_pictures;
		}
		
		public void bind(PictureMapActivity activity) {
			mActivity = activity;
			sPhoneGroupStr = (String) activity.getResources().getText(R.string.phone_pictures);
			mBind = true;
		}
		
		public void unbind() {
			mActivity = null;
			mBind = false;
		}

		@Override
		protected void onPreExecute() {	
			if (mBind && mActivity != null) {
				mActivity.showLoadingDialog();
			}
		}
		
		@Override
		protected Object doInBackground(Object... arg0) {
			try {
				// Load pictures
				final ArrayList pictures = PictureManager.loadPictures(mActivity.getContentResolver());
				publishProgress(PICTURES, pictures);
				
				if ((STYLE & Constants.NO_VIEW_ALBUMS) == 0) {
					// Load albums
					final ArrayList albums = AlbumManager.loadAlbums();
					publishProgress(ALBUMS, albums);
				}
			} catch (IOException e) {
				return Constants.NO_SD_ACCESS;
			}
			return null;
		}
		
		@Override
        public void onProgressUpdate(Object... values) {
			final int kind = ((Integer) values[0]).intValue();
			final ArrayList viewables = (ArrayList) values[1];
			if (kind == PICTURES) {
				if (!viewables.isEmpty()) {
					final PictureGroup phonePictures = new PictureGroup(Constants.PHONE_GROUP_ID, sPhoneGroupStr, viewables);
					sPictureBox.addGroup(phonePictures);
				}
				if ((STYLE & Constants.NO_VIEW_ALBUMS) == 0) {
					textResourceId = R.string.load_albums;
				} else {
					textResourceId = R.string.load_done;
				}
				if (mBind && mActivity != null) {
					mActivity.updateMap();
					mActivity.setLoadingText(textResourceId);
				}
			} else if (kind == ALBUMS) {
				if (!viewables.isEmpty()) {
					sPictureBox.addGroups(viewables);
				}
				textResourceId = R.string.load_done;
				if (mBind && mActivity != null) {
					mActivity.updateMap();
					mActivity.setLoadingText(textResourceId);
				}
			}
        }
		
		@Override
		protected void onPostExecute(Object result) {
			if (mBind) {
				if (result instanceof Integer && ((Integer)result).intValue() == Constants.NO_SD_ACCESS) {
					UIUtils.showAccessWarning(mActivity);			
				}
				if (mActivity != null) {
					mActivity.hideLoadingDialog();
					if (sPictureBox.getVisiblePictures().isEmpty()) {
						UIUtils.showNoPicturesVisibleWarning(mActivity, sPictureBox);	
					} 
				}
			}
		}
		
		@Override
        public void onCancelled() {
			if (mBind && mActivity != null) {
				mActivity.hideLoadingDialog();
			}
        }	
	}
	
	/*******************************************************************
	 * Import Album Task
	 * @author Jordan
	 *
	 ******************************************************************/
	private static class ImportAlbumTask extends AsyncTask {
		
		private PictureMapActivity mActivity;
		private boolean mBind = true;
		private InputStream mKmzStream;
		private File mNewDir;
		
		public ImportAlbumTask(InputStream kmzStream) {
			mKmzStream = kmzStream;
		}
		
		public void bind(PictureMapActivity activity) {
			mActivity = activity;
			mBind = true;
		}
		
		public void unbind() {
			mActivity = null;
			mBind = false;
		}

		@Override
		protected void onPreExecute() {
			if (mBind && mActivity != null) {
				mActivity.showImportDialog();
			}
		}
		
		@Override
		protected Object doInBackground(Object... objects) {
			String albumName = null;
			mNewDir = null;
			if (mKmzStream != null) {
				try {
					mNewDir = AlbumManager.createDirectory();
					if (mNewDir != null && mNewDir.mkdirs()) {
						// Unzip kmz
						final ZipInputStream zis = new ZipInputStream(mKmzStream);
						FileUtils.unzip(zis, mNewDir);
						zis.close();
						// Add the new album to the cache
						final FileAlbum album = AlbumManager.loadAlbum(mNewDir);
						if (album != null) {
							sPictureBox.addGroup(album);
							albumName = album.getName();
						}
					}
				} catch (IOException ioe) {
					if (mNewDir != null) {
						FileUtils.delete(mNewDir);
						mNewDir = null;
					}
					albumName = null;
				}
			}
			return albumName;
		}
		
		@Override
		protected void onPostExecute(Object result) {
			if (mBind && mActivity != null) {
				mActivity.hideProgressDialog();
				final String albumName = (String) result;
				if (albumName == null) {
					String msg = (String) mActivity.getResources().getText(R.string.import_error);
					Toast.makeText(mActivity, msg, Toast.LENGTH_LONG).show();
				} else {
					String msg = albumName;
					msg += " " + mActivity.getResources().getText(R.string.imported);
					Toast.makeText(mActivity, msg, Toast.LENGTH_LONG).show();
					mActivity.updateMap();
				}
				if (!sPictureBox.isLoaded()) {
					mActivity.onLoadPictures();
				}
				mActivity.cancelImportIntent();
			}
		}
		
		@Override
        public void onCancelled() {
			if (mBind && mActivity != null) {
				mActivity.hideProgressDialog();
			}
			if (mNewDir != null) {
				FileUtils.delete(mNewDir);
				mNewDir = null;
			}
        }
	}
	
	/*******************************************************************
	 * Updates the pictures in the gallery
	 * @author Jordan
	 *
	 ******************************************************************/
	private class UpdatePictures implements Runnable {
		
		public void run() {   
	        final long selectedPictureId = mSelectedItemId;
	        final ThumbManager thumbManager = mThumbManager;
	        final Gallery gallery = mGallery;
	        final int count = gallery.getChildCount();
	        for (int i = 0; i < count; ++i) {
	        	final ImageView view = (ImageView) gallery.getChildAt(i);
	        	final Viewable item = (Viewable) view.getTag();
	        	if (item != null) {
		            // Update highlight
		            if (item.getId() == selectedPictureId) {
		            	view.setImageState(new int[] { android.R.attr.state_checked }, true);
		            } else {
		            	view.setImageState(new int[] { android.R.attr.state_empty }, true);
		            }
		            // Update bitmap
		            final Picture picture = item.getImage();
		            if (picture != null) {
			            final Bitmap bitmap = thumbManager.getMicroBitmap(picture);
			            if (ThumbManager.bitmapExists(bitmap)) {
			            	view.setImageBitmap(bitmap);
			            } else {
			            	view.setImageBitmap(null);
			            }
		            }
		            //view.invalidate();
	            }
	            //gallery.invalidate();
	        }			
		}
	}
	
	/*******************************************************************
	 * Manages the overlays on the map.
	 * @author Jordan
	 *
	 ******************************************************************/
	private class OverlayViewer extends ItemizedOverlay<OverlayItem> {
		
		final private Drawable mFocusedMarker;
		final private ArrayList<OverlayItem> mSelected = new ArrayList<OverlayItem>();
		final private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
		final private OnFocusChangeListener mListener;
		
		public OverlayViewer(OnFocusChangeListener listener, Drawable defaultMarker, Drawable focusedMarker) {
			super(boundCenter(defaultMarker));
			mFocusedMarker = focusedMarker;
			mListener = listener;
			setOnFocusChangeListener(listener);
		}
		
		public void initOverlays(ArrayList<Picture> pictures) {
			clearSelection();
			final ArrayList<OverlayItem> overlays = mOverlays;
			overlays.clear();
			final int size = pictures.size();
			for (int i = 0; i < size; ++i) {
				overlays.add(pictures.get(i).getOverlay());
			}
			setLastFocusedIndex(-1);
			populate();
		}

		@Override
		protected OverlayItem createItem(int i) {
			return mOverlays.get(i);
		}
		
		@Override
		public void draw(Canvas canvas, MapView mapView, boolean pShadow) {
		    super.draw(canvas, mapView, false);
		} 
		
		@Override
		protected boolean onTap(int index) {
			return true;
		}
		
		@Override
		public boolean onTap(GeoPoint point, MapView mapView) {
			boolean hitOverlay = false;
			if (point != null && mapView != null) {
				try {
					final ArrayList<OverlayItem> selected = mSelected;
					final ArrayList<OverlayItem> preSelected = (ArrayList<OverlayItem>) selected.clone();
					hitOverlay = super.onTap(point, mapView);
					// The list of selected has been updated, by the callback
					// called in super.onTap, because onFocusChange is called,
					// which performs a setSelection.
					// 1. Can only tap on one at a time.
					// 2. We only want to zoom when only one was previously selected
					//    and is tapped twice.
					if (hitOverlay && preSelected.size() == 1 && selected.size() > 0) {
						final OverlayItem tappedOverlay = selected.get(0);
						if (preSelected.contains(tappedOverlay)) {
							onMapTouchEvent(ACTION_ZOOM, mapView, tappedOverlay);
						}
					}
				} catch (Exception e) {
					// Nothing else to do.
				}
			}
			return hitOverlay;
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent event, MapView mapView) {
			boolean ret = false;
			if (size() > 0 && event != null && mapView != null) {
				ret = super.onTouchEvent(event, mapView);
				onMapTouchEvent(event.getAction(), mapView, null);
			}
			return ret;
		}

		@Override
		public int size() {
			return mOverlays.size();
		}	
		
		public void setSelection(OverlayItem overlay, boolean forceFocus) {
			clearSelection();
			select(overlay);
			if (forceFocus) {
				setOnFocusChangeListener(null);
				setFocus(overlay);
				setOnFocusChangeListener(mListener);
			}
		}
		
		public void setSelection(ArrayList<Picture> pictures) {
			clearSelection();
			final ArrayList<OverlayItem> overlays = mOverlays;
			final int size = pictures.size();
			for (int i = 0; i < size; ++i) {
				final OverlayItem overlay = pictures.get(i).getOverlay();
				select(overlay);
				if (overlays.remove(overlay)) {
					overlays.add(overlay);
				}
			}
			setLastFocusedIndex(-1);
			populate();
		}
		
		private void select(OverlayItem overlay) {
			mSelected.add(overlay);
			overlay.setMarker(boundCenter(mFocusedMarker));
		}
		
		private void clearSelection() {
			final ArrayList<OverlayItem> selected = mSelected;
			final int size = selected.size();
			for (int i = 0; i < size; ++i) {
				selected.get(i).setMarker(null);
			}
			selected.clear();
		}
	}
}