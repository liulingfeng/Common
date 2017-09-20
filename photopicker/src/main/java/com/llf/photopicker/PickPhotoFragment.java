package com.llf.photopicker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.llf.basemodel.utils.FileUtil;
import com.llf.basemodel.utils.SettingUtil;
import com.llf.photopicker.adapter.FolderListAdapter;
import com.llf.photopicker.adapter.ImageListAdapter;
import com.llf.photopicker.bean.Folder;
import com.llf.photopicker.bean.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by llf on 2017/3/10.
 * 图片选择Fragment
 */

public class PickPhotoFragment extends Fragment implements View.OnClickListener, ImageListAdapter.OnItemClickListener {
    private ImgSelConfig config;
    private RecyclerView mRecyclerView;
    private Button btnAlbumSelected;
    private View rlBottom;
    private ListPopupWindow folderPopupWindow;
    private Callback callback;
    private FolderListAdapter mFolderListAdapter;
    private ImageListAdapter mImageListAdapter;
    private List<Folder> folderList = new ArrayList<>();
    private List<Image> imageList = new ArrayList<>();
    private static final int LOADER_ALL = 0;
    private static final int LOADER_CATEGORY = 1;
    private static final int REQUEST_CAMERA = 5;
    private File tempFile;

    public static PickPhotoFragment instance(ImgSelConfig config) {
        PickPhotoFragment fragment = new PickPhotoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("config", config);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            config = (ImgSelConfig) getArguments().getSerializable("config");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pick_photo, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        btnAlbumSelected = (Button) view.findViewById(R.id.btnAlbumSelected);
        btnAlbumSelected.setOnClickListener(this);
        rlBottom = view.findViewById(R.id.rlBottom);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        callback = (Callback) getActivity();
        mRecyclerView.setLayoutManager(new GridLayoutManager(mRecyclerView.getContext(), 3));
        if (config.needCamera) {
            imageList.add(new Image());
        }
        mImageListAdapter = new ImageListAdapter(getActivity(), imageList, config, this);
        mRecyclerView.setAdapter(mImageListAdapter);
        mFolderListAdapter = new FolderListAdapter(getActivity(), folderList);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ALL, null, mLoaderCallback);
    }

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media._ID};

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == LOADER_ALL) {
                CursorLoader cursorLoader = new CursorLoader(getActivity(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        null, null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            } else if (id == LOADER_CATEGORY) {
                CursorLoader cursorLoader = new CursorLoader(getActivity(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'", null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                folderList.clear();
                folderList.add(new Folder());
                int count = data.getCount();
                if (count > 0) {
                    List<Image> tempImageList = new ArrayList<>();
                    data.moveToFirst();
                    do {
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        Image image = new Image(path, name, dateTime);
                        if (!image.path.endsWith("gif"))
                            tempImageList.add(image);
                        File imageFile = new File(path);
                        File folderFile = imageFile.getParentFile();
                        Folder folder = new Folder();
                        folder.name = folderFile.getName();
                        folder.path = folderFile.getAbsolutePath();
                        folder.cover = image;
                        if (!folderList.contains(folder)) {
                            List<Image> imageList = new ArrayList<>();
                            imageList.add(image);
                            folder.images = imageList;
                            folderList.add(folder);
                        } else {
                            Folder f = folderList.get(folderList.indexOf(folder));
                            f.images.add(image);
                        }

                    } while (data.moveToNext());

                    imageList.clear();
                    if (config.needCamera)
                        imageList.add(new Image());
                    imageList.addAll(tempImageList);
                    mImageListAdapter.notifyDataSetChanged();
                    mFolderListAdapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == btnAlbumSelected.getId()) {
            if (folderPopupWindow == null) {
                createPopupFolderList(SettingUtil.getScreenWidth(getActivity()) / 3 * 2, SettingUtil.getScreenWidth(getActivity()) / 3 * 2);
            }

            if (folderPopupWindow.isShowing()) {
                folderPopupWindow.dismiss();
            } else {
                folderPopupWindow.show();
                int index = mFolderListAdapter.getSelectIndex();
                index = index == 0 ? index : index - 1;
                folderPopupWindow.getListView().setSelection(index);
            }
        }
    }

    private void createPopupFolderList(int width, int height) {
        folderPopupWindow = new ListPopupWindow(getActivity());
        folderPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        folderPopupWindow.setAdapter(mFolderListAdapter);
        folderPopupWindow.setContentWidth(width);
        folderPopupWindow.setWidth(width);
        folderPopupWindow.setHeight(height);
        folderPopupWindow.setAnchorView(rlBottom);
        folderPopupWindow.setModal(true);
        folderPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                folderPopupWindow.dismiss();
                mFolderListAdapter.setSelectIndex(position);
                if (position == 0) {
                    getActivity().getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);
                    btnAlbumSelected.setText("所有图片");
                } else {
                    imageList.clear();
                    if (config.needCamera)
                        imageList.add(new Image());
                    imageList.addAll(folderList.get(position).images);
                    mImageListAdapter.notifyDataSetChanged();
                    btnAlbumSelected.setText(folderList.get(position).name);
                }
            }
        });
    }

    private void showCameraAction() {
        if (config.maxNum <= Constant.imageList.size()) {
            Toast.makeText(getActivity(), "最多选择" + config.maxNum + "张图片", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            tempFile = new File(Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM + File.separator + "Camera" + File.separator + System.currentTimeMillis() + ".jpg");
            FileUtil.createFile(tempFile);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri contentUri = FileProvider.getUriForFile(getActivity(), "com.llf.common.provider666", tempFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
            } else {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
            }
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        } else {
            Toast.makeText(getActivity(), "打开相机失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (tempFile != null) {
                    if (callback != null) {
                        callback.onCameraShot(tempFile);
                        Intent storageUri = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        storageUri.setData(Uri.fromFile(tempFile));
                        getActivity().sendBroadcast(storageUri);
                    }
                }
            } else {
                if (tempFile != null && tempFile.exists()) {
                    tempFile.delete();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemClick(int position) {
        if (config.needCamera && position == 0) {
            showCameraAction();
        } else {
            if (config.multiSelect) {
                if (Constant.imageList.contains(imageList.get(position).path)) {
                    Constant.imageList.remove(imageList.get(position).path);
                    if (callback != null) {
                        callback.onImageUnselected(imageList.get(position).path);
                    }
                } else {
                    if (config.maxNum <= Constant.imageList.size()) {
                        Toast.makeText(getActivity(), "最多选择" + config.maxNum + "张图片", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Constant.imageList.add(imageList.get(position).path);
                    if (callback != null) {
                        callback.onImageSelected(imageList.get(position).path);
                    }
                }
                mImageListAdapter.select(imageList.get(position));
            } else {
                if (callback != null) {
                    callback.onSingleImageSelected(imageList.get(position).path);
                }
            }
        }
    }

    @Override
    public void onItemLongClick(int position) {

    }
}
