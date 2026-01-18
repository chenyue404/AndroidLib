package com.chenyue404.androidlib.logcat

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

open class FileLogInterceptor(
    private val context: Context,
    private val pathToLogDirectory: String,
    private val filePrefix: String,
    private var appId: String? = null,
    private var versionName: String? = null,
    private var versionCode: String? = null,
    private val buildFlavor: String? = null,
    private val buildType: String? = null,
    private val shouldRoll: ((currentFile: File?) -> Boolean)? = null,
    private val isSensitive: (chain: Chain) -> Boolean = { false },
) : LogInterceptor, AutoCloseable {
    private val TAG = "FileLogInterceptor"
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val logChannel = Channel<String>(Channel.BUFFERED)
    private var logFile: File? = null
    private var printWriter: PrintWriter? = null
    private val fileDateFormatter = SimpleDateFormat("yyyyMMdd-HHmmss", Locale.getDefault())
    private val logDateFormatter = SimpleDateFormat("MM-dd HH:mm:ss:SSS", Locale.getDefault())

    init {
        autoFillAppInfo()

        scope.launch {
            for (fullLine in logChannel) {
                writeToDisk(fullLine)
            }
        }
    }

    private fun autoFillAppInfo() {
        if (appId == null) {
            appId = context.packageName
        }
        if (versionName == null || versionCode == null) {
            try {
                val pInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    context.packageManager.getPackageInfo(
                        context.packageName,
                        PackageManager.PackageInfoFlags.of(0)
                    )
                } else {
                    @Suppress("DEPRECATION")
                    context.packageManager.getPackageInfo(context.packageName, 0)
                }

                if (versionName == null) versionName = pInfo.versionName
                if (versionCode == null) {
                    versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        pInfo.longVersionCode.toString()
                    } else {
                        @Suppress("DEPRECATION")
                        pInfo.versionCode.toString()
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to auto-fill app info", e)
            }
        }
    }

    override fun intercept(chain: Chain) {
        val timestamp = System.currentTimeMillis()
        val level = chain.level
        val tag = chain.tag
        val originalMsg = chain.message
        val isSensitive = isSensitive(chain.copy())

        val message = if (isSensitive) "*** REDACTED ***" else originalMsg
        val dateStr = synchronized(logDateFormatter) {
            logDateFormatter.format(Date(timestamp))
        }
        val formattedLine = "[$dateStr] [$level] [$tag]: $message"

        logChannel.trySend(formattedLine)
    }

    private fun writeToDisk(text: String) {
        synchronized(this) {
            try {
                if (shouldRoll?.invoke(logFile) == true) {
                    internalClose()
                }

                if (printWriter == null || logFile?.exists() == false) {
                    internalClose()
                    createWriter(filePrefix)
                }

                printWriter?.let {
                    it.println(text)
                    it.flush()
                }
            } catch (e: Exception) {
                if (!isPermissionError(e)) {
                    Log.e(TAG, "Disk write failed", e)
                }
                internalClose()
            }
        }
    }

    private fun createWriter(prefix: String) {
        val dir = File(pathToLogDirectory)
        if (!dir.exists()) dir.mkdirs()

        val timeStamp = synchronized(fileDateFormatter) {
            fileDateFormatter.format(Date())
        }
        val target = File(dir, "${prefix}_$timeStamp.txt")

        this.logFile = target
        try {
            val isNew = target.createNewFile()
            this.printWriter = PrintWriter(target.outputStream().bufferedWriter())
            if (isNew) writeHeader()
        } catch (e: IOException) {
            Log.e(TAG, "Could not create log file", e)
        }
    }

    private fun writeHeader() {
        printWriter?.apply {
            val dm = context.resources.displayMetrics
            val dateStr = logDateFormatter.format(Date())

            // 1. logAppInfo
            println("Log ${context.packageName} $dateStr")
            println("------------------------------------------------------------------------------")
            println("Application Id:     ${appId ?: "unknown"}")
            buildFlavor?.let { println("Flavor:             $it") }
            buildType?.let { println("Build Type:         $it") }
            println("Version Name:       ${versionName ?: "unknown"}")
            println("Version Code:       ${versionCode ?: "unknown"}")
            println("")

            // 2. logDeviceInfo
            println("Device Information")
            println("==================")
            println("Brand:              ${Build.BRAND}")
            println("Model:              ${Build.MODEL}")
            println("Device:             ${Build.DEVICE}")
            println("Product:            ${Build.PRODUCT}")
            println("Screen Resolution:  ${dm.widthPixels}x${dm.heightPixels}@${dm.densityDpi}dpi")
            println("Fingerprint:        ${Build.FINGERPRINT}")
            println("")

            // 3. logOSInfo
            println("OS Information")
            println("==============")
            println("Locale:             ${Locale.getDefault()}")
            println("SDK:                ${Build.VERSION.SDK_INT}")
            println("Release:            ${Build.VERSION.RELEASE}")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                println("Security Patch:     ${Build.VERSION.SECURITY_PATCH}")
            }
            println("------------------------------------------------------------------------------\n")
            flush()
        }
    }

    private fun isPermissionError(e: Exception): Boolean {
        val m = e.message ?: ""
        return m.contains("EACCES") || m.contains("Permission denied")
    }

    fun rotateFile() {
        synchronized(this) {
            internalClose()
        }
    }

    private fun internalClose() {
        try {
            printWriter?.flush()
            printWriter?.close()
        } catch (e: Exception) {
            Log.e(TAG, "internalClose", e)
        } finally {
            printWriter = null
            logFile = null
        }
    }

    override fun close() {
        logChannel.close()
        scope.cancel()
        rotateFile()
    }
}