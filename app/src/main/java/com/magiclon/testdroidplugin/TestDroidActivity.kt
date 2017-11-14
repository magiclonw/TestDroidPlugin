package com.magiclon.testdroidplugin

import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.morgoo.droidplugin.pm.PluginManager
import com.morgoo.helper.compat.PackageManagerCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class TestDroidActivity : AppCompatActivity() {
    var path = "${Environment.getExternalStorageDirectory()}/app-debug.apk"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        install.setOnClickListener {
            Log.e("-------","${"${File(path).absolutePath}".equals("$path")}-------")
            PluginManager.getInstance().installPackage(path, PackageManagerCompat.INSTALL_REPLACE_EXISTING)
        }
        unstall.setOnClickListener {
            PluginManager.getInstance().deletePackage("com.magiclon.pocketdoctor",0)
        }
        start.setOnClickListener {
//            val info = packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES)
//            Log.e("---/----",info.packageName)
//            val intent = Intent(Intent.ACTION_MAIN)
//            intent.addCategory(Intent.CATEGORY_LAUNCHER)
//            val cn = ComponentName("com.magiclon.huatuodrug", "com.magiclon.huatuodrug.activity.FlashActivity")
//            intent.component = cn
//            startActivity(intent)
            doStartApplicationWithPackageName("com.magiclon.pocketdoctor")
        }
    }

    private fun doStartApplicationWithPackageName(packagename: String) {

        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        var packageinfo: PackageInfo? = null
        try {
            packageinfo = packageManager.getPackageInfo(packagename, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        if (packageinfo == null) {
            return
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        val resolveIntent = Intent(Intent.ACTION_MAIN, null)
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        resolveIntent.`package` = packageinfo.packageName

        // 通过getPackageManager()的queryIntentActivities方法遍历
        val resolveinfoList = packageManager
                .queryIntentActivities(resolveIntent, 0)

        val resolveinfo = resolveinfoList.iterator().next()
        if (resolveinfo != null) {
            // packagename = 参数packname
            val packageName = resolveinfo.activityInfo.packageName
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            val className = resolveinfo.activityInfo.name
            // LAUNCHER Intent
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)

            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            val cn = ComponentName(packageName, className)

            intent.component = cn
            startActivity(intent)
        }
    }

}
