package com.beenthere.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;

import com.beenthere.R;
import com.beenthere.kml.Folder;
import com.beenthere.kml.Kml;
import com.beenthere.kml.Placemark;
import com.beenthere.provider.FileAlbum;
import com.beenthere.provider.Picture;
import com.beenthere.provider.PictureBox;
import com.beenthere.util.KmzUtils;
import com.beenthere.util.ThumbManager;
import com.beenthere.util.UIUtils;
import com.flurry.android.FlurryAgent;

public class EditAlbumActivity extends ListActivity implements OnScrollListener, 
															   OnClickListener {

	public static final String  ALBUM_ID = "album_id";
	public static final long    NEW_ALBUM = 0;
	
	// Dialogs
    private ProgressDialog 		mProgressDialog;
    
    // Tasks
    private SaveAlbumTask		mSaveTask;
    
	// Views
	private EditAlbumAdapter 	mEditAlbumAdapter;
	private Button				mSaveButton;
	private TextView			mEditView;
	private TextView			mAlbumNameEditor;
	private Drawable			mDefaultThumb;
	
	private final Handler 		mScrollHandler = new Handler();
	private final Runnable		mUpdatePictureThumbs = new UpdatePictureThumbnails();
	
	// Model
	private FileAlbum			mAlbum;
	private ThumbManager		mThumbManager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editalbum);
		mThumbManager = new ThumbManager(this);
		
		// Initialize views
		setupViews();

		// Retrieve the album so that if it is a new album
		// that has been modified, the notes are not lost
		// because a new instance is created from the screen.
		final Data data = (Data) getLastNonConfigurationInstance();
		if (data != null) {
			mAlbum = data.album;
			if (mAlbum != null) {
				final String note = data.text;
				final Picture picture = data.picture;
				if (note != null && picture != null) {
					onEditNote(note, picture);
				}
				mSaveTask = data.task;
				restoreSaveTask();
			}
		} 
		
		if (mAlbum == null) {
			// Retrieve album
			final Intent intent = getIntent();
	        if (intent != null) {
	        	final PictureBox pictureBox = PictureMapActivity.sPictureBox;
	        	final long albumId = intent.getLongExtra(ALBUM_ID, NEW_ALBUM);
	        	if (albumId == NEW_ALBUM) {
	        		mAlbum = new FileAlbum(pictureBox.getScreenPictures());
	        	} else {
	        		mAlbum = ((FileAlbum) pictureBox.getGroup(albumId)).newInstance();
	        		mAlbumNameEditor.setText(mAlbum.getName());
	        	}
	        }
		}
    
		updateList();
	}
	
	private void setupViews() {		
		mSaveButton = (Button) findViewById(R.id.editalbum_save_button);
		mSaveButton.setOnClickListener(this);
		mAlbumNameEditor = (TextView) findViewById(R.id.album_edit_item);
		final ListView listView = getListView();
		listView.setOnScrollListener(this);
		mDefaultThumb = getResources().getDrawable(R.drawable.default_thumb);
		mEditAlbumAdapter = new EditAlbumAdapter(this, mThumbManager, mDefaultThumb);
		setListAdapter(mEditAlbumAdapter);
	}
	
	private void updateList() {
		final FileAlbum album = mAlbum;
        final EditAlbumAdapter adapter = mEditAlbumAdapter;
        if (album != null && adapter != null) {
        	adapter.clear();
        	final ArrayList<Picture> pictures = album.getPictures();
        	final int size = pictures.size();
            for (int i = 0; i < size; ++i) {
                adapter.add(pictures.get(i));
            }
            updatePictureThumbnails(200);
        }
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
	protected void onResume() {
    	super.onResume();
    	updatePictureThumbnails(200);
    }
	
	@Override
	protected void onPause() {
    	super.onPause();
    	mScrollHandler.removeCallbacks(mUpdatePictureThumbs);
    }
    
	@Override
    protected void onStop() {
    	super.onStop();
    	FlurryAgent.onEndSession(this);
    }
    
	@Override
    protected void onDestroy() {
    	super.onDestroy();
    	if (mSaveTask != null) {
    		mSaveTask.unbind();
		}
    }

	@Override
	public Object onRetainNonConfigurationInstance() {
		final Data data = new Data();
		data.album = mAlbum;
		data.task = mSaveTask;
		if (mEditView != null) {
			data.text = mEditView.getText().toString();
			data.picture = (Picture) mEditView.getTag();
		} else {
			data.text = null;
			data.picture = null;
		}
		return data;
	}
	
	private FileAlbum getAlbum() {
		return mAlbum;
	}
	
	/**
	 * Shows a dialog to edit the selected field.
	 * If picture is null, consider that it should edit the album name.
	 * @param note
	 * @param picture
	 */
	private void onEditNote(final String note, final Picture picture) {
		final LayoutInflater inflater = LayoutInflater.from(this);
		final View view = inflater.inflate(R.layout.edit_note_dialog, null);
		mEditView = (TextView) view.findViewById(R.id.edit_note);
		mEditView.setText(note);
		mEditView.setTag(picture);
		
		final Builder builder = new AlertDialog.Builder(this);
		builder.setView(view);
		builder.setTitle(R.string.edit_note);
		builder.setPositiveButton(R.string.edit_note, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				final String note = mEditView.getText().toString();
				picture.setDescription(note);
				getListView().invalidateViews();
				mEditView = null;
			}
		});
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				mEditView = null;
			}
		});
		builder.create().show();
	}
	
	private void onSave() {
		if (mAlbumNameEditor.getText().length() == 0) {
			Toast.makeText(this, getResources().getText(R.string.empty_album_name), Toast.LENGTH_SHORT).show();
		} else if (mAlbum.getRemainingCount() == 0) {
			Toast.makeText(this, getResources().getText(R.string.empty_album), Toast.LENGTH_SHORT).show();
		} else if (mSaveTask == null || mSaveTask.getStatus() == AsyncTask.Status.FINISHED) {
			if (!UIUtils.showAccessWarning(this)) {
				// Set name of the albumm before launching the task, so 
				// that if the phone is rotated after launching but 
				// before the task is actually started, the name won't
				// be lost.
				mAlbum.setName(mAlbumNameEditor.getText().toString()); 
				mSaveTask = new SaveAlbumTask();
				mSaveTask.bind(this);
				mSaveTask.execute();
			}
        }
	}
	
	private void showSaveDialog() {
		final FileAlbum album = mAlbum;
		final ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setCancelable(false);
		final CharSequence message = getResources().getText(R.string.saving) + " " + album.getName() + "...";
		progressDialog.setMessage(message);
		progressDialog.setMax(album.getRemainingCount());
		if (mSaveTask != null) {
			progressDialog.setProgress(mSaveTask.progress);
		}
		progressDialog.show();
		mProgressDialog = progressDialog;
	}
	
	private void hideSaveDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}
	
	private void setSaveProgress(int progress) {
		if (mProgressDialog != null) {
			mProgressDialog.setProgress(progress);
		}
	}
	
	private void restoreSaveTask() {
		if (mSaveTask != null && mSaveTask.getStatus() == AsyncTask.Status.RUNNING) {
			mSaveTask.bind(this);
			showSaveDialog();
		}
	}
	
	private void updatePictureThumbnails(long delayMillis) {
		final Runnable updatePictureThumbs = mUpdatePictureThumbs;
		final Handler scrollHandler = mScrollHandler;
		scrollHandler.removeCallbacks(updatePictureThumbs);
		scrollHandler.postDelayed(updatePictureThumbs, delayMillis);
	}

    protected void onListItemClick(ListView listView, View view, int position, long id) {
    	final Picture picture = mEditAlbumAdapter.getItem(position);
    	if (!picture.getDelete()) {
    		onEditNote(picture.getDescription(), picture);
    	}
    }

	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// Called too often
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			updatePictureThumbnails(0);
		}
	}

	public void onClick(View view) {
		onSave();
	}
    
    
    /*******************************************************************
	 * Edit Album Adapter
	 * @author Jordan Bonnet
	 *
	 ******************************************************************/ 
    private static class EditAlbumAdapter extends ArrayAdapter<Picture> {
    	
    	private final ThumbManager mThumbManager;
    	private final LayoutInflater mLayoutInflater;
    	private final Drawable mDefaultThumb;
    	private final Locale mLocale;
    	
    	public EditAlbumAdapter(Activity activity, ThumbManager thumbManager, Drawable defaultThumb) {
    		super(activity, 0);
    		mThumbManager = thumbManager;
    		mLayoutInflater = LayoutInflater.from(activity);
    		mDefaultThumb = defaultThumb;
    		mLocale = activity.getResources().getConfiguration().locale;
    	}
    	
    	public View getView(int position, View convertView, ViewGroup parent) {  
    		ViewHolder holder; 		
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.image_list_item, parent, false);
                holder = new ViewHolder();
                holder.view = convertView;
                holder.thumbnail = (ImageView) convertView.findViewById(R.id.thumb_image_list_item);
                //holder.thumbnail.setLayoutParams(new LinearLayout.LayoutParams(Constants.IMG_DIM, Constants.IMG_DIM));
                holder.comment = (TextView) convertView.findViewById(R.id.title_image_list_item);
                holder.info = (TextView) convertView.findViewById(R.id.info_image_list_item);
                holder.info.setHint(R.string.no_date);
                holder.icon = (ImageView) convertView.findViewById(R.id.check_image_list_item);
                holder.icon.setTag(holder);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            
            final Picture picture = getItem(position);
            holder.picture = picture;  
            final Bitmap thumbBitmap = mThumbManager.getMicroBitmap(picture, false);
            if (ThumbManager.bitmapExists(thumbBitmap)) {
            	holder.thumbnail.setImageBitmap(thumbBitmap);
            } else {
            	holder.thumbnail.setImageDrawable(mDefaultThumb);
            }
            holder.comment.setText(picture.getDescription());
            holder.info.setText(UIUtils.getLocaleDate(mLocale, picture.getDate()));  
            refreshView(holder, picture.getDelete());
    		
    		// Bind a Click Listener to update state in model
    		holder.icon.setOnClickListener(new OnClickListener() {
    			public void onClick(View view) {
    				final ViewHolder holder = (ViewHolder) view.getTag();
    				holder.picture.switchDelete();
    				refreshView(holder, holder.picture.getDelete());
    				view.postInvalidate();
    			}
    		});
    		
    		return convertView;
        }
    	
    	/**
    	 * Refresh the view look based on the deletion status of 
    	 * the picture.
    	 * @param holder
    	 */
    	private void refreshView(ViewHolder holder, boolean delete) {
    		if (delete) {
				holder.view.setEnabled(false);
				holder.comment.setHint(R.string.exclude_picture);
				holder.comment.setEnabled(false);
            	holder.icon.setImageState(new int[] { android.R.attr.state_empty }, true);
			} else {
				holder.view.setEnabled(true);
				holder.comment.setHint(R.string.edit_note);
				holder.comment.setEnabled(true);
            	holder.icon.setImageState(new int[] { android.R.attr.state_checked }, true);
			}
    	}
    }
    
    /*******************************************************************
	 * View Holder
	 * @author Jordan Bonnet
	 *
	 ******************************************************************/ 
    private static class ViewHolder {
    	View view;
    	ImageView thumbnail;
    	TextView comment;
    	TextView info;
    	ImageView icon;
    	Picture picture;
    }
    
    /*******************************************************************
	 * Updates the pictures in the list view
	 * @author Jordan
	 *
	 ******************************************************************/
	private class UpdatePictureThumbnails implements Runnable {
		
		public void run() {   
	        final ThumbManager thumbManager = mThumbManager;
	        final ListView listView = getListView();
	        final int count = listView.getChildCount();
	        for (int i = 0; i < count; ++i) {
	            final View view = listView.getChildAt(i);
	            final ViewHolder holder = (ViewHolder) view.getTag();
	            final Picture picture = holder.picture;
	            if (picture != null) {
	            	final Bitmap thumbBitmap = thumbManager.getMicroBitmap(picture);
	            	if (ThumbManager.bitmapExists(thumbBitmap)) {
	            		holder.thumbnail.setImageBitmap(thumbBitmap);
	            	} else {
	            		holder.thumbnail.setImageDrawable(mDefaultThumb);
	            	}
	            }
	        }
	        listView.invalidate();
		}
	}
	
	/*******************************************************************
	 * Save Album Task
	 * @author Jordan
	 *
	 ******************************************************************/
	private static class SaveAlbumTask extends AsyncTask {
		
		private static final int ERROR_WRITING_KML = 1;
		private static final int ALBUM_SAVED = 0;
		
		private EditAlbumActivity mActivity;
		private boolean mBind = true;
		int progress = 0;
		
		public void bind(EditAlbumActivity activity) {
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
				mActivity.showSaveDialog();
			}
		}		
		
		@Override
		protected Object doInBackground(Object... objects) {
			int result = ALBUM_SAVED;
			if (mBind && mActivity != null) {
				final FileAlbum album = mActivity.getAlbum();
				final File albumFile = album.getFile();
				final Kml kml = KmzUtils.create(albumFile, album.getName());
				final Folder folder = kml.getFolder();
				if (folder != null && albumFile.exists()) {
					final ArrayList<Picture> pictures = album.getPictures();
					int i = 0;
					while (i < pictures.size()) {
						final Picture picture = pictures.get(i);
						if (picture.getDelete()) {
							if (!album.isNew()) {
								picture.delete();
							}
							album.removePicture(picture);
						} else {
							if (mActivity != null && mActivity.mThumbManager != null) {
								final Placemark placemark = KmzUtils.createPlacemark(albumFile, picture, mActivity.mThumbManager);
								if (placemark != null) {
									folder.addPlacemark(placemark);
								}
								++i;
								publishProgress(i);
							}
						}
					}
					try {
						KmzUtils.writeKml(albumFile, kml);
						// The addGroup() method is smart, so if there is an
						// album with the same id, it will be replaced.
						PictureMapActivity.sPictureBox.addGroup(album);
						// Either is not already not new, or has to become.
						album.setNew(false);
					} catch (IOException e) {
						result = ERROR_WRITING_KML;
					}
				}
			}
			return result;
		}
		
		@Override
        public void onProgressUpdate(Object... values) {
			progress = ((Integer) values[0]).intValue();
			if (mBind && mActivity != null) {
				mActivity.setSaveProgress(progress);
			}
        }
		
		@Override
		protected void onPostExecute(Object result) {
			if (mBind && mActivity != null) {
				mActivity.hideSaveDialog();
				int ret = ((Integer) result).intValue();
				if (ret == ALBUM_SAVED) {
					String msg = mActivity.getAlbum().getName();
					msg += " " + mActivity.getResources().getText(R.string.saved);
					Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
				} else if (ret == ERROR_WRITING_KML) {
					String msg = (String) mActivity.getResources().getText(R.string.save_error);
					Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
				}
				mActivity.updateList();
			}
		}
		
		@Override
        public void onCancelled() {
			if (mBind && mActivity != null) {
				mActivity.hideSaveDialog();
			}
        }	
	}
	
	/*******************************************************************
	 * Data
	 * @author Jordan
	 *
	 ******************************************************************/
	private static class Data {
		FileAlbum album;
		SaveAlbumTask task;
		Picture picture;
		String text;
	}
}

