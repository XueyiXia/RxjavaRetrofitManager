# RxjavaRetrofitManager

Kotlin 版本的 Retrofit + RxJava + OkHttp 网络封装库，适合 Android 项目快速接入网络请求、文件上传、文件下载、断点续传和生命周期绑定。

## 项目概览

本项目包含两部分：

- `app`：示例应用
- `framework_http`：网络请求封装库

支持能力：

- 通用 GET / POST / PUT / DELETE 请求
- JSON / 表单提交
- 文件上传
- 文件下载
- 断点续传
- 下载进度回调
- 通知栏下载状态
- Lifecycle 生命周期绑定
- 可扩展的 `BaseResponse<T>` 和回调接口

## 功能截图

下面为文档示例图，可替换为真实应用截图：

![使用示例](docs/images/usage-demo.svg)

## 目录结构

```text
RxjavaRetrofitManager/
├── app/
│   └── src/main/java/com/rxjava_retrofit
├── framework_http/
│   └── src/main/java/com/framework/http
├── README.md
├── build.gradle
├── settings.gradle
└── gradle.properties
```

## 快速开始

### 1. 引入模块

在项目的 `settings.gradle` 中，确保引入模块：

```gradle
include ':app', ':framework_http'
```

在 `app/build.gradle` 中添加依赖：

```gradle
dependencies {
    implementation project(':framework_http')
}
```

### 2. 基础配置

可在 Application 中初始化全局参数：

```kotlin
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        RxHttpConfigure.getInstance()
            .setBaseUrl("https://api.example.com/")
    }
}
```

## 使用方式

### 1. 普通请求示例

```kotlin
RxHttp.getInstance()
    .setContext(context)
    .setApiUrl("user/info")
    .setMethod(HttpMethod.GET)
    .build()
    .execute(object : SimpleResponseListener<UserInfo>() {
        override fun onStart() {
            // 开始请求
        }

        override fun onSucceed(data: UserInfo, method: String) {
            // 请求成功
        }

        override fun onError(e: Throwable?) {
            // 请求失败
        }

        override fun onCompleted() {
            // 请求完成
        }
    })
```

### 2. POST 请求示例

```kotlin
val params = TreeMap<String, Any>().apply {
    put("user_id", 123)
    put("token", "abc")
}

RxHttp.getInstance()
    .setContext(context)
    .setApiUrl("user/login")
    .setParameter(params)
    .post()
    .build()
    .execute(object : SimpleResponseListener<BaseResponse<LoginResult>>() {
        override fun onSucceed(data: BaseResponse<LoginResult>, method: String) {
            // 处理返回结果
        }

        override fun onError(e: Throwable?) {
            // 处理异常
        }
    })
```

### 3. JSON Body 请求

```kotlin
val jsonBody = "{\"name\":\"Tom\",\"age\":18}"

RxHttp.getInstance()
    .setContext(context)
    .setApiUrl("user/update")
    .setBodyString(jsonBody, true)
    .build()
    .execute(object : SimpleResponseListener<BaseResponse<UserInfo>>() {
        override fun onSucceed(data: BaseResponse<UserInfo>, method: String) {
            // 处理 JSON 请求返回
        }
    })
```

### 4. 文件上传示例

```kotlin
val fileList = mutableListOf<File>()
fileList.add(File(path))

NetworkRepository.getInstance().httpUpload(
    context = context,
    url = "upload/file",
    params = TreeMap<String, Any>().apply {
        put("type", "avatar")
    },
    fileList = fileList,
    onUpLoadFileListener = object : OnUpLoadFileListener<BaseResponse<String>>() {
        override fun onStart() {
            // 上传开始
        }

        override fun onSucceed(data: BaseResponse<String>, method: String) {
            // 上传成功
        }

        override fun onError(e: Throwable?) {
            // 上传失败
        }

        override fun progress(
            file: File?,
            currentSize: Long,
            totalSize: Long,
            progress: Float,
            currentIndex: Int,
            totalFile: Int
        ) {
            // 上传进度
        }
    }
)
```

### 5. 文件下载示例

```kotlin
NetworkRepository.getInstance().httpDownload(
    context = context,
    url = apkUrl,
    dir = dirPath,
    filename = "demo.apk",
    callback = object : DownloadCallback<DownloadInfo>() {
        override fun onStart() {
            // 开始下载
        }

        override fun onProgress(readBytes: Long, totalBytes: Long) {
            // 下载进度
        }

        override fun onNext(response: DownloadInfo?) {
            // 下载完成，处理文件
        }

        override fun onError(e: Throwable?) {
            // 下载失败
        }

        override fun onCompleted() {
            // 下载结束
        }
    },
    md5 = md5Value,
    breakPoint = false
)
```

### 6. 断点续传

```kotlin
NetworkRepository.getInstance().httpDownload(
    context = context,
    url = fileUrl,
    dir = downloadDir,
    filename = "big_file.zip",
    callback = callback,
    md5 = null,
    breakPoint = true
)
```

说明：

- `breakPoint = true` 时开启断点续传
- 若目标文件已存在且完整，则直接跳过重复下载
- 可在下载完成后通过 MD5 校验文件完整性

### 7. 生命周期绑定

如果当前 `Context` 是 `LifecycleOwner`，框架会自动绑定生命周期，页面销毁时自动取消请求：

```kotlin
RxHttp.getInstance()
    .setContext(this)
    .setApiUrl("user/list")
    .build()
    .execute(listener)
```

## 下载通知栏说明

下载大文件时会触发通知栏进度展示：

- 下载中显示进度百分比
- 下载成功时显示下载完成
- 下载失败时清除通知
- 下载完成后可执行 APK 安装

## 最佳实践

1. 请求 URL 建议统一管理，不要硬编码到业务层。
2. `BaseResponse<T>` 推荐统一服务端返回结构，方便解析。
3. 下载场景建议开启节流，避免大文件下载过程中 UI 频繁刷新。
4. 需要下载 APK 时，建议在 Android 8+ 上测试安装回调和 `FileProvider` 配置。
5. 网络请求可通过 `tag` 区分，便于取消指定请求。

## 常见问题

### Q1：请求回调拿不到数据？

检查：

- `setApiUrl()` 是否正确
- `setMethod()` 是否设置
- `BaseResponse<T>` 结构是否和后端一致
- `TypeUtils` 是否能正确解析泛型

### Q2：下载文件经常卡顿？

检查：

- 进度回调是否做了节流
- 下载进度更新是否频繁刷新 UI
- 文件写入是否在后台线程完成

### Q3：断点续传失效？

检查：

- `breakPoint` 是否开启
- 目标文件存在时是否正确判断已下载长度
- `Content-Length` 是否可获取

## 版本说明

当前版本：

- Kotlin 版本：适配最新项目结构
- RxJava 版本：RxJava3
- OkHttp 版本：稳定版
- Android：兼容常用 Android 版本

## License

本项目遵循 MIT License，详情请查看 LICENSE 文件。

## 贡献者

欢迎提 PR 和提交 issue，帮助完善网络封装库和示例代码。
```
