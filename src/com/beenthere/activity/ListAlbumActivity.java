package com.beenthere.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.zip.ZipOutputStream;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.beenthere.R;
import com.beenthere.provider.AlbumManager;
import com.beenthere.provider.FileAlbum;
import com.beenthere.provider.Picture;
import com.beenthere.provider.PictureBox;
import com.beenthere.provider.PictureGroup;
import com.beenthere.util.Constants;
import com.beenthere.util.FileUtils;
import com.beenthere.util.ThumbManager;
import com.beenthere.util.UIUtils;
import com.flurry.android.FlurryAgent;

public class ListAlbumActivity extends ListActivity implements OnScrollListener {
	
	// Dialogs
    private ProgressDialog 		mProgressDialog;
    
    // Tasks
    private DeleteAlbumTask		mDeleteTask;
    private ZipAlbumTask		mZipTask;
    
	// Views
	private ListAlbumAdapter 	mListAlbumAdapter;
	private final Handler 		mScrollHandler = new Handler();
	private final Runnable		mUpdateAlbumThumbs = new UpdateAlbumThumbnails();
	private Drawable			mDefaultThumb;
	
	// Model
	private PictureBox		 	mPictureBox;
	private ThumbManager		mThumbManager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listalbum);
   		
		mThumbManager = new ThumbManager(this);
		mPictureBox = PictureMapActivity.sPictureBox;
		
		setupViews();
		
		final Object task = getLastNonConfigurationInstance();
		if (task != null) {
			if (task instanceof DeleteAlbumTask) {
				mDeleteTask = (DeleteAlbumTask) task;
				restoreDeleteTask();
			}
			else if (task instanceof ZipAlbumTask) {
				mZipTask = (ZipAlbumTask) task;
				restoreZipTask();
			}
		}
	}
	
	private void setupViews() {		
		final ListView listView = getListView();
		listView.setOnScrollListener(this);
		registerForContextMenu(listView);
		mDefaultThumb = getResources().getDrawable(R.drawable.default_thumb);
		mListAlbumAdapter = new ListAlbumAdapter(this, mPictureBox, mThumbManager, mDefaultThumb);
		setListAdapter(mListAlbumAdapter);
	}
	
	private void updateList() {
		final ListAlbumAdapter albumAdapter = mListAlbumAdapter;
		final ArrayList groups = mPictureBox.getGroups();
		final int size = groups.size();
		albumAdapter.clear();
		for (int i = 0; i < size; ++i) {
			albumAdapter.add((PictureGroup) groups.get(i));
		}
		updateAlbumThumbnails(200);
	}
	
	@Override
	protected void onRestart() {
    	super.onRestart();
    }
	
	@Override
	protected void onStart() {
		super.onStart();
		// A new album might need to appear.
		updateList();
		FlurryAgent.onStartSession(this, "BJ5VYI12GXWB75IC5C1L");
	}	
	
	@Override
	protected void onResume() {
    	super.onResume();
    	updateAlbumThumbnails(200);
    }
	
	@Override
	protected void onPause() {
    	super.onPause();
    	mScrollHandler.removeCallbacks(mUpdateAlbumThumbs);
    	
    	// Save user preferences
		final SharedPreferences settings = getSharedPreferences(Constants.PREFS_NAME, 0);
		if (settings != null) {
			final SharedPreferences.Editor editor = settings.edit();
			mPictureBox.savePreferences(editor);
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
    	if (mDeleteTask != null) {
    		mDeleteTask.unbind();
		}
    	if (mZipTask != null) {
    		mZipTask.unbind();
		}
    }
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		if (mDeleteTask != null && mDeleteTask.getStatus() == AsyncTask.Status.RUNNING) {
			return mDeleteTask;
		}
		if (mZipTask != null && mZipTask.getStatus() == AsyncTask.Status.RUNNING) {
			return mZipTask;
		}
		return null;
	}

	@Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        final ViewHolder holder = (ViewHolder) ((AdapterContextMenuInfo) menuInfo).targetView.getTag();
        menu.setHeaderTitle(holder.group.getName());
        getMenuInflater().inflate(R.menu.album, menu);
        if (!(holder.group instanceof FileAlbum)) {
        	final MenuItem send = menu.findItem(R.id.send_album);
        	if (send != null) {
        		send.setVisible(false);
        	}
        	final MenuItem edit = menu.findItem(R.id.edit_album);
        	if (edit != null) {
        		edit.setVisible(false);
        	}
        	final MenuItem delete = menu.findItem(R.id.delete_album);
        	if (delete != null) {
        		delete.setVisible(false);
        	}
        }
    }

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		final ViewHolder holder = (ViewHolder) info.targetView.getTag();
		final boolean isFileAlbum = holder.group instanceof FileAlbum;
		switch (item.getItemId()) {
		case R.id.view_album:
			onView(holder.group);
			return true;
		case R.id.send_album:
			if (isFileAlbum) {
				onSend((FileAlbum) holder.group);
			}
			return true;
		case R.id.edit_album:
			if (isFileAlbum) {
				onEdit((FileAlbum) holder.group);
			}
			return true;
		case R.id.delete_album:
			if (isFileAlbum) {
				onDelete((FileAlbum) holder.group);
			}
			return true;
		}
		return super.onContextItemSelected(item);
	}
	
	private void onView(PictureGroup group) {
		final ArrayList pictures = group.getPictures();
    	if (pictures.size() > 0) {
    		final Intent viewGroup = new Intent(this, PictureViewerActivity.class);
    		viewGroup.putParcelableArrayListExtra(PictureViewerActivity.ITEM_LIST, pictures);
    		startActivity(viewGroup);
    	} else {
    		Toast.makeText(this, getResources().getText(R.string.no_pictures_in_album), Toast.LENGTH_SHORT).show();
    	}
	}
	
	private void onSend(FileAlbum album) {
		if (!UIUtils.showAccessWarning(this)) {
			if (mZipTask == null || mZipTask.getStatus() == AsyncTask.Status.FINISHED) {
				mZipTask = new ZipAlbumTask(album);
				mZipTask.bind(this);
				mZipTask.execute();
	        } 
		}
	}
	
	private void showZipDialog(FileAlbum album) {
		final ProgressDialog progressDialog = new ProgressDialog(this);
		final CharSequence message = getResources().getText(R.string.zipping) + " " + album.getName() + "...";
		progressDialog.setMessage(message);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setCancelable(false);
		progressDialog.setIndeterminate(true);
		progressDialog.show();
		mProgressDialog = progressDialog;
	}
	
	private void restoreZipTask() {
		if (mZipTask != null && mZipTask.getStatus() == AsyncTask.Status.RUNNING) {
			mZipTask.bind(this);
			showZipDialog(mZipTask.getAlbum());
		}
	}
	
	private void sendAlbum(File kmz) {
		final CharSequence subject = getResources().getText(R.string.email_subject);
		final CharSequence text = getResources().getText(R.string.email_text);
		final Intent sendIntent = new Intent(Intent.ACTION_SEND);
		sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		sendIntent.putExtra(Intent.EXTRA_TEXT, text); 
		sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		sendIntent.setType(PictureMapActivity.VND_KMZ_TYPE); 
		sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(kmz));
		final CharSequence title = getResources().getText(R.string.send_album);
		startActivity(Intent.createChooser(sendIntent, title)); 
	}
	
	private void onEdit(FileAlbum album) {
		final Intent createAlbum = new Intent(this, EditAlbumActivity.class);
		createAlbum.putExtra(EditAlbumActivity.ALBUM_ID, album.getId());
		startActivity(createAlbum);	
	}
	
	private void onDelete(FileAlbum album) {
		if (!UIUtils.showAccessWarning(this)) {
			if (mDeleteTask == null || mDeleteTask.getStatus() == AsyncTask.Status.FINISHED) {
				mDeleteTask = new DeleteAlbumTask(album);
				mDeleteTask.bind(this);
				mDeleteTask.execute();
	        } 
		}
	}
	
	private void showDeleteDialog(FileAlbum album) {
		final ProgressDialog progressDialog = new ProgressDialog(this);
		final CharSequence message = getResources().getText(R.string.deleting) + " " + album.getName() + "...";
		progressDialog.setMessage(message);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setCancelable(false);
		progressDialog.setMax(album.getPictures().size());
		if (mDeleteTask != null) {
			progressDialog.setProgress(mDeleteTask.progress);
		}
		progressDialog.show();
		mProgressDialog = progressDialog;
	}
	
	private void setDeleteProgress(int progress) {
		if (mProgressDialog != null) {
			mProgressDialog.setProgress(progress);
		}
	}
	
	private void restoreDeleteTask() {
		if (mDeleteTask != null && mDeleteTask.getStatus() == AsyncTask.Status.RUNNING) {
			mDeleteTask.bind(this);
			showDeleteDialog(mDeleteTask.getAlbum());
		}
	}
	
	private void hideProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}
	
	private void updateAlbumThumbnails(long delayMillis) {
		final Runnable updateAlbumThumbs = mUpdateAlbumThumbs;
		final Handler scrollHandler = mScrollHandler;
		scrollHandler.removeCallbacks(updateAlbumThumbs);
		scrollHandler.postDelayed(updateAlbumThumbs, delayMillis);
	}

    protected void onListItemClick(ListView listView, View view, int position, long id) {
    	onView(mListAlbumAdapter.getItem(position));
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// Called too often
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			updateAlbumThumbnails(0);	
		}
	}
    
    
    /*******************************************************************
	 * List Album Adapter
	 * @author Jordan Bonnet
	 *
	 ******************************************************************/ 
    private static class ListAlbumAdapter extends ArrayAdapter<PictureGroup> {
    	
    	private final PictureBox mPictureBox;
    	private final ThumbManager mThumbManager;
    	private final LayoutInflater mLayoutInflater;
    	private final Toast mToast;
    	
    	// Resources
    	private final Drawable mDefaultThumb;
    	private final CharSequence mPicturesStr;
    	private final CharSequence mPictureStr;
    	private final CharSequence mAlbumDisplayedStr;
    	private final CharSequence mAlbumNotDisplayedStr;
    	
    	public ListAlbumAdapter( Activity activity, 
    				 			 PictureBox pictureBox, 
    				 			 ThumbManager thumbManager, 
    				 			 Drawable defaultThumb ) {
    		super(activity, 0);
    		mPictureBox = pictureBox;
    		mThumbManager = thumbManager;
    		mLayoutInflater = LayoutInflater.from(activity);
    		mToast = Toast.makeText(activity, "", Toast.LENGTH_SHORT);
    		mDefaultThumb = defaultThumb;
    		mPicturesStr = activity.getResources().getText(R.string.pictures);
    		mPictureStr = activity.getResources().getText(R.string.picture);
    		mAlbumDisplayedStr = activity.getResources().getString(R.string.album_displayed);
    		mAlbumNotDisplayedStr = activity.getResources().getString(R.string.album_not_displayed);
    	}
    	
    	public View getView(int position, View convertView, ViewGroup parent) {  
    		ViewHolder holder; 		
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.image_list_item, parent, false);
                holder = new ViewHolder();
                holder.view = convertView;
                holder.thumbnail = (ImageView) convertView.findViewById(R.id.thumb_image_list_item);
                //holder.thumbnail.setLayoutParams(new LinearLayout.LayoutParams(Constants.IMG_DIM, Constants.IMG_DIM));
                holder.title = (TextView) convertView.findViewById(R.id.title_image_list_item);
                holder.info = (TextView) convertView.findViewById(R.id.info_image_list_item);
                holder.icon = (ImageView) convertView.findViewById(R.id.check_image_list_item);
                holder.icon.setTag(holder);
                convertView.setTag(holder);
            } 
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            
            final PictureGroup group = getItem(position);
        	final Picture picture = group.getImage();
        	if (picture != null) {
	        	final Bitmap thumbBitmap = mThumbManager.getMicroBitmap(picture, false);
	        	if (ThumbManager.bitmapExists(thumbBitmap)) {
	            	holder.thumbnail.setImageBitmap(thumbBitmap);
	            } else {
	            	holder.thumbnail.setImageDrawable(mDefaultThumb);
	            }
        	} else {
            	holder.thumbnail.setImageDrawable(mDefaultThumb);
            }

        	final int groupSize = group.size();
        	final CharSequence pictureStr = groupSize > 1 ? mPicturesStr : mPictureStr;
        	holder.group = group;
            holder.title.setText(group.getName());
            holder.info.setText(groupSize + " " + pictureStr);
            refreshView(holder, mPictureBox.isGroupEnabled(group));
    		
    		// Bind a Click Listener to update state in model
    		holder.icon.setOnClickListener(new OnClickListener() {
    			public void onClick(View view) {
    				final ViewHolder holder = (ViewHolder) view.getTag();
    				final boolean makeVisible = !mPictureBox.isGroupEnabled(holder.group);
    				refreshView(holder, makeVisible);
    				if (makeVisible) {
    					mPictureBox.showGroup(group);
    					final String msg = group.getName() + " " + mAlbumDisplayedStr;
    					mToast.setText(msg);
    					mToast.show();
    				} else {
    					mPictureBox.hideGroup(group);
    					final String msg = group.getName() + " " + mAlbumNotDisplayedStr;
    					mToast.setText(msg);
    					mToast.show();
    				}
    				view.postInvalidate();
    			}
    		});
    		
    		return convertView;
        }
    	
    	/**
    	 * Refresh the view look based on the visible status of 
    	 * the picture group.
    	 * @param holder
    	 */
    	private void refreshView(ViewHolder holder, boolean visible) {
    		if (visible) {
    			holder.view.setEnabled(true);
    			holder.title.setEnabled(true);
    			holder.icon.setImageState(new int[] { android.R.attr.state_checked }, true);
    		} else {
    			holder.view.setEnabled(false);
    			holder.title.setEnabled(false);
    			holder.icon.setImageState(new int[] { android.R.attr.state_empty }, true);
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
    	TextView title;
    	TextView info;
    	ImageView icon;
    	PictureGroup group;
    }
    
    /*******************************************************************
	 * Updates the pictures in the list view
	 * @author Jordan
	 *
	 ******************************************************************/
	private class UpdateAlbumThumbnails implements Runnable {
		
		public void run() {   
	        final ThumbManager thumbManager = mThumbManager;
	        final ListView listView = getListView();
	        final int count = listView.getChildCount();
	        for (int i = 0; i < count; ++i) {
	            final View view = listView.getChildAt(i);
	            final ViewHolder holder = (ViewHolder) view.getTag();
	            final PictureGroup group = holder.group;
	            if (group != null) {
	            	final Picture picture = group.getImage();
	            	if (picture != null) {
		            	final Bitmap thumbBitmap = thumbManager.getMicroBitmap(picture);
		            	if (ThumbManager.bitmapExists(thumbBitmap)) {
		            		holder.thumbnail.setImageBitmap(thumbBitmap);
		            	} else {
		            		holder.thumbnail.setImageDrawable(mDefaultThumb);
		            	}
	            	}
	            }
	        }
	        listView.invalidate();
		}
	}
	
	/*******************************************************************
	 * Delete Album Task
	 * @author Jordan
	 *
	 ******************************************************************/
	private static class DeleteAlbumTask extends AsyncTask {
		
		private ListAlbumActivity mActivity;
		private boolean mBind = true;
		int progress = 0;
		private FileAlbum mAlbum;
		
		public DeleteAlbumTask(FileAlbum album) {
			mAlbum = album;
		}
		
		public FileAlbum getAlbum() {
			return mAlbum;
		}
		
		public void bind(ListAlbumActivity activity) {
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
				mActivity.showDeleteDialog(mAlbum);
			}            
		}
		
		@Override
		protected Object doInBackground(Object... objects) {
			final FileAlbum album = mAlbum;
			final ArrayList pictures = album.getPictures();
			final int size = pictures.size();
			for (int i = 0; i < size; ++i) {
				((Picture) pictures.get(i)).delete();
				publishProgress(i + i);
			}
			album.delete();
			PictureMapActivity.sPictureBox.removeGroup(album);
			return null;
		}
		
		@Override
        public void onProgressUpdate(Object... values) {
			progress = ((Integer) values[0]).intValue();
			if (mBind && mActivity != null) {
				mActivity.setDeleteProgress(progress);
			}
        }
		
		@Override
		protected void onPostExecute(Object result) {
			if (mBind && mActivity != null) {
				mActivity.hideProgressDialog();
				mActivity.updateList();
			}
		}
		
		@Override
        public void onCancelled() {
			if (mBind && mActivity != null) {
				mActivity.hideProgressDialog();
			}
        }	
	}
	
	/*******************************************************************
	 * Zip Album Task
	 * @author Jordan
	 *
	 ******************************************************************/
	private static class ZipAlbumTask extends AsyncTask {
		
		private ListAlbumActivity mActivity;
		private boolean mBind = true;
		private FileAlbum mAlbum;
		
		public ZipAlbumTask(FileAlbum album) {
			mAlbum = album;
		}
		
		public FileAlbum getAlbum() {
			return mAlbum;
		}
		
		public void bind(ListAlbumActivity activity) {
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
				mActivity.showZipDialog(mAlbum);
			}
		}
		
		@Override
		protected Object doInBackground(Object... objects) {
			final FileAlbum album = mAlbum;
			final File albumFile = album.getFile();
			final File kmz = new File(AlbumManager.getMainDir(), album.getName() + Constants.KMZ);
			try {
				final ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(kmz));
				FileUtils.zip(albumFile, zos);
				zos.close();
				return kmz;
			} catch (Exception e) {
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(Object result) {
			if (mBind && mActivity != null) {
				mActivity.hideProgressDialog();
				final File kmz = (File) result;
				if (kmz != null) {
					mActivity.sendAlbum(kmz);
				} else {
					Toast.makeText(mActivity, mActivity.getResources().getText(R.string.zip_error), Toast.LENGTH_LONG).show();
				}
			}
		}
		
		@Override
        public void onCancelled() {
			if (mBind && mActivity != null) {
				mActivity.hideProgressDialog();
			}
        }	
	}
}
