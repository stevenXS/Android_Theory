package com.steven.camera;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.steven.camera.session.CameraDeviceManager;
import com.steven.camera.session.CameraSettings;
import com.steven.camera.session.PhotoSession;
import com.steven.camera.session.Session;
import com.steven.test_demo.R;

import java.io.File;

public class Camera2FragmentRefactor extends Fragment implements View.OnClickListener{
    private CameraDeviceManager cameraDeviceManager;
    private CameraSettings cameraSettings;
    private Session session;
    private ImageView showImgView;
    private AutoFitTextureView mTextureView;
    private String mCameraId;
    private File mFile;

    private final CameraDeviceManager.CameraEvent cameraEvent = new CameraDeviceManager.CameraEvent() {
        @Override
        public void onCameraOpened(CameraDevice device) {
            session.postRequest(Session.RQ_START_PREVIEW, mTextureView);
        }
    };

    private final TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
            CameraManager cameraManager = cameraDeviceManager.getCameraManager();
            try {
                for (String cameraId : cameraManager.getCameraIdList()) {
                    CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
                    Integer facing = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
                    if (facing != null && facing == CameraCharacteristics.LENS_FACING_BACK) {
                        // 本次仅打开后置摄像头
                        cameraDeviceManager.openCamera(Integer.parseInt(cameraId));
                        cameraSettings = new CameraSettings(getContext(), cameraCharacteristics);
                        session = new PhotoSession(getContext(), cameraDeviceManager, cameraCharacteristics);
                    }
                }
            } catch (CameraAccessException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {
            Log.d("camera2", "onSurfaceTextureSizeChanged");
            cameraSettings.configTransform(mTextureView.getWidth(), mTextureView.getHeight(), session.getPreviewSize(), mTextureView);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera2_basic, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.picture).setOnClickListener(this);
        view.findViewById(R.id.info).setOnClickListener(this);
        view.findViewById(R.id.switch_camera).setOnClickListener(this);
        showImgView = (ImageView) view.findViewById(R.id.show_img);
        showImgView.setOnClickListener(this);
        mTextureView = (AutoFitTextureView) view.findViewById(R.id.texture); // 视频播放视图
        mFile = new File(getActivity().getExternalFilesDir(null), "pic.jpg");
        cameraDeviceManager = new CameraDeviceManager(getContext(), cameraEvent);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mTextureView.isAvailable()) {
            CameraManager cameraManager = cameraDeviceManager.getCameraManager();
            try {
                for (String cameraId : cameraManager.getCameraIdList()) {
                    CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
                    Integer facing = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
                    if (facing != null && facing == CameraCharacteristics.LENS_FACING_BACK) {
                        // 本次仅打开后置摄像头
                        cameraDeviceManager.openCamera(Integer.parseInt(cameraId));
                        cameraSettings = new CameraSettings(getContext(), cameraCharacteristics);
                        session = new PhotoSession(getContext(), cameraDeviceManager, cameraCharacteristics);
                    }
                }
            } catch (CameraAccessException e) {
                throw new RuntimeException(e);
            }
        } else {
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.picture: {
                session.postRequest(Session.RQ_TAKE_PHOTO, mTextureView);
                break;
            }
            case R.id.info: {
                Activity activity = getActivity();
                if (null != activity) {
                    new AlertDialog.Builder(activity)
                            .setMessage("test message")
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                }
                break;
            }
            case R.id.show_img:
                showImgView.setVisibility(View.INVISIBLE);
                mTextureView.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onPause() {
        cameraDeviceManager.release();
        session.release();
        super.onPause();
    }
}
