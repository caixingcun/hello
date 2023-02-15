package com.example.hello

import android.content.Intent
import android.net.Uri
import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import android.app.Activity
import java.util.*

/** HelloPlugin */
class HelloPlugin: FlutterPlugin, MethodCallHandler,ActivityAware{

  companion object {
    // 跳转浏览器
    const val  jumpUrlByNative = "jumpUrlByNative"
  }
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
//  private lateinit var activity: Activity

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "hello")
    channel.setMethodCallHandler(this)
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "getPlatformVersion") {
      result.success("Android ${android.os.Build.VERSION.RELEASE}")
    } else  if(call.method == jumpUrlByNative){
      var intent =  Intent()
      intent.action = Intent.ACTION_VIEW
      intent.data = Uri.parse(call.arguments.toString())
      mActivity?.startActivity(intent);
    }else{
      result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }


  private var mActivity: Activity? = null

  //首次绑定到Activity
  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    mActivity = binding.activity
  }

  //由于某些原因导致暂时解绑
  override fun onDetachedFromActivityForConfigChanges() {
    mActivity = null
  }

  //恢复绑定
  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    mActivity = binding.activity
  }

  //解绑
  override fun onDetachedFromActivity() {
    mActivity = null
  }
}
