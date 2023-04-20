package com.rxjava_retrofit.fragment

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.framework.http.api.BaseResponse
import com.framework.http.interfac.OnUpLoadFileListener
import com.framework.http.manager.RxHttpTagManager
import com.framework.http.repository.NetworkRepository
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.camera.CustomCameraType
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.rxjava_retrofit.GlideEngine
import com.rxjava_retrofit.HttpApi
import com.rxjava_retrofit.R
import com.yalantis.ucrop.view.OverlayView
import java.io.File
import java.util.*

/**
 * @author: xiaxueyi
 * @date: 2022-12-30
 * @time: 10:47
 * @说明:
 */

class MallsFragment :Fragment(){

    private var mRootView: View? = null

    private val TAG = "Upload"

    private lateinit var mBtnUpload: Button

    private lateinit var mBtnOpenGallery: Button

    private lateinit var mTvResult: TextView

    val fileList: MutableList<File> = ArrayList()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(R.layout.fragment_malls, container, false)
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBtnUpload=view.findViewById(R.id.upload)
        mBtnOpenGallery=view.findViewById(R.id.open_gallery)
        mTvResult=view.findViewById(R.id.result)


        mBtnOpenGallery.setOnClickListener {

            openGallery(activity,  object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: MutableList<LocalMedia>?) {
                    val media = result!![0]

                    Log.i(TAG, "是否压缩:" + media.isCompressed)
                    Log.i(TAG, "压缩:" + media.compressPath)
                    Log.i(TAG, "原图:" + media.path)
                    Log.i(TAG, "绝对路径:" + media.realPath)
                    Log.i(TAG, "是否裁剪:" + media.isCut)
                    Log.i(TAG, "裁剪:" + media.cutPath)
                    Log.i(TAG, "是否开启原图:" + media.isOriginal)
                    Log.i(TAG, "原图路径:" + media.originalPath)
                    Log.i(TAG, "Android Q 特有Path:" + media.androidQToPath)
                    Log.i(TAG, "宽高: " + media.width + "x" + media.height)
                    Log.i(TAG, "Size: " + media.size)
                    val file: File = File(media.realPath)

                    fileList.add(file)
                }

                override fun onCancel() {

                }

            });
        }

        mBtnUpload.setOnClickListener {
            initRequestHttp()
        }
    }


    private fun initRequestHttp(){
        val params: TreeMap<String, Any> = TreeMap()
        val tag= RxHttpTagManager.generateRandomTag()
        NetworkRepository.getInstance().httpUpload(
            requireContext(),
            HttpApi.URL_UPLOAD_IMG,
            params,
            fileList,
            tag,
            object : OnUpLoadFileListener<BaseResponse<ImgUploadBean>>() {

                override fun progress(
                    file: File?,
                    currentSize: Long,
                    totalSize: Long,
                    progress: Float,
                    currentIndex: Int,
                    totalFile: Int
                ) {
                    Log.e(TAG, "progress  图片上传进度progress---->>$progress")
                }

                override fun onSucceed(data: BaseResponse<ImgUploadBean>, method: String) {
                    Log.e(TAG, "onSucceed 图片上传成功---->>${data.getData()}")

                    mTvResult.text = data?.getData().toString()
                }


                override fun onError(e: Throwable?) {
                    Log.e(TAG, "onError  图片上传错误---->>${e}")

                    mTvResult.text = e?.message
                }



                override fun onCompleted() {
                    Log.e(TAG, "onCompleted ")
                }


            })

    }


    /**
     * 打开相册
     * @param activity 上下文对象
     * @param requestCode 请求标识，会在onActivityResult 方法中回调
     * @param maxSelectNum 最大图片选择数量,默认为1
     * @param listener 回调监听
     */
    fun openGallery(
        activity: Activity?,
        listener: OnResultCallbackListener<LocalMedia>?
    ) {

        val pictureSelector = PictureSelector.create(activity)
        pictureSelector.openGallery(PictureMimeType.ofImage()) // 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
            .imageEngine(GlideEngine.createGlideEngine()) // 外部传入图片加载引擎，必传项
            //                .theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style v2.3.3后 建议使用setPictureStyle()动态方式
            .isWeChatStyle(true) // 是否开启微信图片选择风格
            .isUseCustomCamera(false) // 是否使用自定义相机
            //                .setLanguage(language)// 设置语言，默认中文
            //                .setPictureStyle(mPictureParameterStyle)// 动态自定义相册主题
            //                .setPictureCropStyle(mCropParameterStyle)// 动态自定义裁剪主题
            //                .setPictureWindowAnimationStyle(mWindowAnimationStyle)// 自定义相册启动退出动画
            .isWithVideoImage(false) // 图片和视频是否可以同选
            .maxSelectNum(5) // 最大图片选择数量
            .minSelectNum(1) // 最小选择数量
            //.minVideoSelectNum(1)// 视频最小选择数量，如果没有单独设置的需求则可以不设置，同用minSelectNum字段
            .maxVideoSelectNum(1) // 视频最大选择数量，如果没有单独设置的需求则可以不设置，同用maxSelectNum字段
            .imageSpanCount(4) // 每行显示个数
            .isReturnEmpty(true) // 未选择数据时点击按钮是否可以返回
            .isAndroidQTransform(true) // 是否需要处理Android Q 拷贝至应用沙盒的操作，只针对.isCompress(false); && .isEnableCrop(false);有效,默认处理
            .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) // 设置相册Activity方向，不设置默认使用系统
            .isOriginalImageControl(false) // 是否显示原图控制按钮，如果设置为true则用户可以自由选择是否使用原图，裁剪功能将会失效
            .isDisplayOriginalSize(true) // 是否显示原文件大小，isOriginalImageControl true有效
            .isEditorImage(false) //是否编辑图片
            //.cameraFileName("test.png")    // 重命名拍照文件名、注意这个只在使用相机时可以使用，如果使用相机又开启了压缩或裁剪 需要配合压缩和裁剪文件名api
            //.renameCompressFile("test.png")// 重命名压缩文件名、 注意这个不要重复，只适用于单张图压缩使用
            //.renameCropFileName("test.png")// 重命名裁剪文件名、 注意这个不要重复，只适用于单张图裁剪使用
            .selectionMode(PictureConfig.MULTIPLE) // 多选 or 单选
            .isSingleDirectReturn(false) // 单选模式下是否直接返回，PictureConfig.SINGLE模式下有效
            .isPreviewImage(true) // 是否可预览图片
            .isPreviewVideo(true) // 是否可预览视频
            //.querySpecifiedFormatSuffix(PictureMimeType.ofJPEG())// 查询指定后缀格式资源
            .isEnablePreviewAudio(true) // 是否可播放音频
            .isCamera(true) // 是否显示拍照按钮
            //.isMultipleSkipCrop(false)// 多图裁剪时是否支持跳过，默认支持
            .isZoomAnim(true) // 图片列表点击 缩放效果 默认true
            //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
            .setCameraImageFormat(PictureMimeType.PNG) // 相机图片格式后缀,默认.jpeg
            .setCameraVideoFormat(PictureMimeType.MP4) // 相机视频格式后缀,默认.mp4
            .setCameraAudioFormat(PictureMimeType.AMR) // 录音音频格式后缀,默认.amr
            .isEnableCrop(false) // 是否裁剪
            .isCompress(true) // 是否压缩
            .compressQuality(80) // 图片压缩后输出质量 0~ 100
            .synOrAsy(true) //同步false或异步true 压缩 默认同步
            //.queryMaxFileSize(10)// 只查多少M以内的图片、视频、音频  单位M
            //.compressSavePath(getPath())//压缩图片保存地址
            .withAspectRatio(3, 4) // 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
            .hideBottomControls(false) // 是否显示uCrop工具栏，默认不显示
            .isGif(false) // 是否显示gif图片
            .freeStyleCropEnabled(true) // 裁剪框是否可拖拽
            .setCustomCameraFeatures(CustomCameraType.BUTTON_STATE_BOTH) // 设置自定义相机按钮状态
            .freeStyleCropMode(OverlayView.DEFAULT_FREESTYLE_CROP_MODE) // 裁剪框拖动模式
            .isCropDragSmoothToCenter(false) // 裁剪框拖动时图片自动跟随居中
            .circleDimmedLayer(false) // 是否圆形裁剪
            //.setCircleDimmedColor(ContextCompat.getColor(this, R.color.app_color_white))// 设置圆形裁剪背景色值
            //.setCircleDimmedBorderColor(ContextCompat.getColor(getApplicationContext(), R.color.app_color_white))// 设置圆形裁剪边框色值
            //.setCircleStrokeWidth(3)// 设置圆形裁剪边框粗细
            .showCropFrame(false) // 是否显示裁剪矩形边框 圆形裁剪时建议设为false
            .showCropGrid(false) // 是否显示裁剪矩形网格 圆形裁剪时建议设为false
            .isOpenClickSound(false) // 是否开启点击声音
            //                .selectionData(mAdapter.getData())// 是否传入已选图片
            //.isDragFrame(false)// 是否可拖动裁剪框(固定)
            //.videoMaxSecond(15)
            //.videoMinSecond(10)
            .isPreviewEggs(false) // 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
            //.cropCompressQuality(90)// 注：已废弃 改用cutOutQuality()
            .cutOutQuality(90) // 裁剪输出质量 默认100
            .minimumCompressSize(100) // 小于100kb的图片不压缩
            //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
            //.rotateEnabled(true) // 裁剪是否可旋转图片
            //.scaleEnabled(true)// 裁剪是否可放大缩小图片
            //.videoQuality()// 视频录制质量 0 or 1
            //.recordVideoSecond()//录制视频秒数 默认60s
            //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径  注：已废弃
            //                .forResult(activity);//结果回调onActivityResult code
            .forResult(0, listener)
    }


    class ImgUploadBean {
        var success = false
        var code: Any? = null
        var msg: String? = null
        var data: String? = null
    }
}