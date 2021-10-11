package com.jachai.jachai_driver.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.format.Formatter
import android.util.DisplayMetrics
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.maps.android.SphericalUtil
import java.io.*
import java.text.*
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Matcher
import java.util.regex.Pattern

object Utils {
    private val IP_ADDRESS = Pattern.compile(
        "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]" + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]" + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}" + "|[1-9][0-9]|[0-9]))"
    )
    private val EMAIL_PATTERN = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    private val TAG = "EasyUtils"
    private val SECOND_MILLIS = 1000
    private val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private val DAY_MILLIS = 24 * HOUR_MILLIS
    var GP_PHONE_REGEX = "((?:(\\+|00)?88)?017\\d{8})"
    private var dialog: ProgressDialog? = null
    private var pattern: Pattern? = null
    private var matcher: Matcher? = null
    private val PHONE_REGEX = "^(?:\\+?88)?01[13-9]\\d{8}$"
    private val PASSWORD_REGEX = "(?=.*?[#?!@$%^&*-]).{8,}$"
    private val DIGIT_REGEX = "[0-9]*"
    private val DECIMAL_AMOUNT_REGEX = "\\d+(\\.\\d{1,2})?"
    private val NAME_REGEX = "^([a-zA-Z ]+(\\.[a-zA-Z ]+)?)"

    fun formatTime24(timeAmPm: String): String {
        var time: String = timeAmPm.toUpperCase()
        time = time.replace("PM", " PM").replace("AM", " AM").replace("  ", " ")
        val date24Format = SimpleDateFormat("HH:mm")
        return try {
            date24Format.format(date24Format.parse(time))
        } catch (e: java.lang.Exception) {
            time
        }
    }

    fun isNumeric(str: String): Boolean {
        try {
            str.toDouble()
            return true
        } catch (e: NumberFormatException) {
            return false
        }
    }

    fun cleanNumber(number: String?): String? {
        var number = number
        if (number == null) return ""
        number = number.replace("[^0-9]".toRegex(), "")
        number = number.replace("^88".toRegex(), "")
        return number
    }

    fun getTimeAgo(time: Long): String? {
        val curDate = currentDate()
        val now = curDate.time
        if (time > now || time <= 0) {
            return "a moment ago"
        }
        val timeDIM = getTimeDistanceInMinutes(time)
        var timeAgo: String? = null
        if (timeDIM == 0) {
            timeAgo = "Just now"
        } else if (timeDIM == 1) {
            return "1 minute ago"
        } else if (timeDIM >= 2 && timeDIM <= 44) {
            timeAgo = "$timeDIM minutes ago"
        } else if (timeDIM >= 45 && timeDIM <= 89) {
            timeAgo = "1 hour ago"
        } else if (timeDIM >= 90 && timeDIM <= 1439) {
            timeAgo = (Math.round((timeDIM / 60).toFloat())).toString() + " hours ago"
        } else if (timeDIM >= 1440) {
            val tdate = Date(time)
            val jdf = SimpleDateFormat("MMM dd 'at' h:mm a", Locale.getDefault())
            jdf.timeZone = TimeZone.getTimeZone("GMT-6")
            timeAgo = jdf.format(tdate)
        }
        return timeAgo
    }

    private fun getTimeDistanceInMinutes(time: Long): Int {
        val timeDistance = currentDate().time - time
        return Math.round(((Math.abs(timeDistance) / 1000) / 60).toFloat())
    }

    fun getReviewsCount(count: Int): String? {
        if (count == 0) return "No Reviews" else return if (count == 1) "1 Review" else "$count Reviews"
    }

    fun distanceBetweenLatLon(
        lat1: Double, lat2: Double, lon1: Double, lon2: Double, el1: Double, el2: Double
    ): Double {
        val R = 6371 // Radius of the earth
        val latDistance = Math.toRadians(lat2 - lat1)
        val lonDistance = Math.toRadians(lon2 - lon1)
        val a = (Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + (Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2)))
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        var distance = R * c * 1000
        val height = el1 - el2
        distance = Math.pow(distance, 2.0) + Math.pow(height, 2.0)
        return Math.sqrt(distance)
    }

    fun distanceBetweenLatLon(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        return SphericalUtil.computeDistanceBetween(LatLng(lat1, lon1), LatLng(lat2, lon2))
    }

    fun deg2rad(deg: Double): Double {
        return (deg * Math.PI / 180.0)
    }

    fun rad2deg(rad: Double): Double {
        return (rad * 180.0 / Math.PI)
    }

    fun capitalize(capString: String): String {
        val capBuffer = StringBuffer()
        val capMatcher =
            Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString)
        while (capMatcher.find()) {
            capMatcher.appendReplacement(
                capBuffer, capMatcher.group(1)?.toUpperCase() + capMatcher.group(2)?.toLowerCase()
            )
        }
        return capMatcher.appendTail(capBuffer).toString()
    }

    fun sortMapByValue(hm: HashMap<String, Int>): HashMap<String, Int>? {
        val list: List<Map.Entry<String, Int>> = LinkedList<Map.Entry<String, Int>>(hm.entries)
        Collections.sort(
            list
        ) { o1: Map.Entry<String, Int>, o2: Map.Entry<String, Int> ->
            (o1.value).compareTo(
                o2.value
            )
        }
        val temp: HashMap<String, Int> = LinkedHashMap()
        for (aa: Map.Entry<String, Int> in list) {
            temp[aa.key] = aa.value
        }
        return temp
    }

    fun convertDpToPixel(dp: Float, context: Context): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun convertPixelsToDp(px: Float, context: Context): Float {
        return px / (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun formatPrice(d: Double): String {
        return if (d % 1.0 == 0.0) String.format(
            Locale.ENGLISH,
            "%d",
            d.toInt()
        ) else String.format(
            Locale.ENGLISH, "%.2f", d
        )
    }

    fun formatPriceComma(d: Double): String {
        return if (d % 1.0 == 0.0) String.format(
            Locale.ENGLISH,
            "%,d",
            d.toInt()
        ) else String.format(
            Locale.ENGLISH, "%,.2f", d
        )
    }

    fun formatPrice(s: String): String? {
        val d = s.toDouble()
        return formatPrice(d)
    }

    fun formatPriceComma(s: String): String {
        val d = s.toDouble()
        return formatPriceComma(d)
    }

    fun formatPriceSymbol(s: String?): String? {
        return if (s == null) "৳ 0" else "৳ " + formatPriceComma(s)
    }


    fun formatPriceSymbol(s: Double): String {
        return "৳ " + formatPriceComma(s)
    }

    fun formattedDateFromString(
        inputFormat: String, outputFormat: String, inputDate: String?
    ): String? {
        var inputFormat = inputFormat
        var outputFormat = outputFormat
        if ((inputFormat == "")) { // if inputFormat = "", set a default input format.
            inputFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        }
        if ((outputFormat == "")) {
            outputFormat =
                "EEEE d',' MMMM  yyyy" // if inputFormat = "", set a default output format.
        }
        var parsed: Date? = null
        var outputDate: String = ""
        val df_input = SimpleDateFormat(inputFormat, Locale.ENGLISH)
        df_input.timeZone = TimeZone.getTimeZone("gmt")
        val df_output = SimpleDateFormat(outputFormat, Locale.ENGLISH)
        df_output.timeZone = TimeZone.getTimeZone("Asia/Dhaka")


        // You can set a different Locale, This example set a locale of Country Mexico.
        //SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, new Locale("es", "MX"));
        //SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, new Locale("es", "MX"));
        try {
            parsed = df_input.parse(inputDate)
            outputDate = df_output.format(parsed)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return outputDate
    }

    fun getValidPhoneNumber(phonNumber: String?): String? {
        if (phonNumber != null && phonNumber.length < 11) {
            return phonNumber
        }
        var phoneNumber = phonNumber!!.trim { it <= ' ' }
        if (phoneNumber.contains(" ")) {
            phoneNumber = phoneNumber.replace(" ", "")
        }
        if (phoneNumber.contains("+")) {
            phoneNumber = phoneNumber.replace("+", "")
        }
        if (phoneNumber.contains("-")) {
            phoneNumber = phoneNumber.replace("-", "")
        }
        if ((phoneNumber.substring(0, 2) == "88")) {
            phoneNumber = phoneNumber.substring(2)
        }
        return phoneNumber
    }


    fun timeAgoFromString(inputFormat: String, inputDate: String?): String? {
        var inputFormat = inputFormat
        val parsed: Date
        val outputDate = ""
        if ((inputFormat == "")) inputFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        val df_input = SimpleDateFormat(inputFormat, Locale.ENGLISH)
        df_input.timeZone = TimeZone.getTimeZone("UTC")
        try {
            parsed = df_input.parse(inputDate)
            if (parsed != null) {
                val time = getTimeDistance(parsed.time)
                if (time < MINUTE_MILLIS) {
                    return "a minute ago"
                } else if (time < 50 * MINUTE_MILLIS) {
                    return (time / MINUTE_MILLIS).toString() + " minutes ago"
                } else if (time < 90 * MINUTE_MILLIS) {
                    return "an hour ago"
                } else if (time < 24 * HOUR_MILLIS) {
                    return (time / HOUR_MILLIS).toString() + " hours ago"
                } else if (time < 48 * HOUR_MILLIS) {
                    return "yesterday"
                } else if (time / DAY_MILLIS < 30) {
                    return (time / DAY_MILLIS).toString() + " days ago"
                } else {
                    val jdf = SimpleDateFormat("MMM dd 'at' h:mm a", Locale.ENGLISH)
                    jdf.timeZone = TimeZone.getTimeZone("GMT-6")
                    return jdf.format(parsed)
                }
            }
        } catch (ignored: Exception) {
        }
        return outputDate
    }

    fun timeAgoInMinute(inputFormat: String, inputDate: String?): Int {
        var inputFormat = inputFormat
        val parsed: Date
        val outputDate = ""
        if ((inputFormat == "")) inputFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        val df_input = SimpleDateFormat(inputFormat, Locale.ENGLISH)
        df_input.timeZone = TimeZone.getTimeZone("UTC")
        try {
            parsed = df_input.parse(inputDate)
            if (parsed != null) {
                val time = getTimeDistance(parsed.time)
                return time.toInt() / MINUTE_MILLIS
            }
        } catch (ignored: Exception) {
        }
        return 0
    }

    fun captializeAllFirstLetter(name: String): String? {
        val array = name.toCharArray()
        array[0] = Character.toUpperCase(array[0])
        for (i in 1 until array.size) {
            if (Character.isWhitespace(array[i - 1])) array[i] = Character.toUpperCase(
                array[i]
            )
        }
        return String(array)
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager: ConnectivityManager? =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        assert(connectivityManager != null)
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun isValidName(name: String?): Boolean {
        return if (name == null) false else Pattern.matches(NAME_REGEX, name)
    }


    fun isValidURL(urlStr: String?): Boolean {
        return Patterns.WEB_URL.matcher(urlStr).matches()
    }

    fun isPasswordValid(password: String?): Boolean {
        return Pattern.matches(PASSWORD_REGEX, password)
    }


    fun getFileFromBitmap(context: Context?, bitmap: Bitmap): File? {
        val f = File(Objects.requireNonNull(context)!!.cacheDir, "image.jpg")
        try {
            f.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80 /*ignored for PNG*/, bos)
        val bitmapdata = bos.toByteArray()
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(f)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return f
    }


    fun getBitmapFromFile(path: String?): Bitmap? {
        val imageFile = File(path)
        var bitmap: Bitmap? = null
        if (imageFile.exists()) {
            bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
        }
        return bitmap
    }

    fun getBitmapFromURI(context: Context?, uri: Uri?): Bitmap? {
        var bm: Bitmap? = null
        var imageStream: InputStream? = null
        try {
            imageStream = Objects.requireNonNull(context)!!.contentResolver.openInputStream(
                (uri)!!
            )
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            bm = BitmapFactory.decodeStream(imageStream)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bm
    }

    fun getUriFromURL(url: String?): Uri? {
        var uri: Uri? = null
        if (isValidURL(url)) {
            uri = Uri.parse(url)
        }
        return uri
    }


    fun removeFromArrayListByName(arrayList: ArrayList<String>, name: String?) {
        for (i in arrayList.indices) {
            if (arrayList[i].equals(name, ignoreCase = true)) {
                arrayList.removeAt(i)
            }
        }
    }

    fun getValuesFromHashMap(hashMap: HashMap<String?, String?>): ArrayList<String?>? {
        val values = ArrayList<String?>()
        for (key: String? in hashMap.keys) {
            values.add(hashMap[key])
        }
        return values
    }

    fun getKeyFromValue(hm: Map<*, *>, value: Any): Any? {
        for (o: Any? in hm.keys) {
            if ((hm[o] == value)) {
                return o
            }
        }
        return null
    }


    fun getTagList(tagList: ArrayList<String>): ArrayList<String>? {

        //declare a ArrayList for storing tag
        val finalTagList = ArrayList<String>()
        for (i in tagList.indices) {

            //spliting the tagList element
            val tag = tagList[i].split(",".toRegex()).toTypedArray()
            for (j in tag.indices) {
                if (!finalTagList.contains(tag[j])) {
                    finalTagList.add(tag[j])
                }
            }
        }
        return finalTagList
    }

    fun showProgressDialog(context: Context?, title: String?, message: String?) {
        /**
         * Progress Dialog for User Interaction
         */
        if ((title == null) || (message == null) || (title == "") || (message == "")) {
            return
        }
        if (dialog == null) {
            dialog = ProgressDialog(context)
            dialog!!.setTitle(title)
            dialog!!.setMessage(message)
            dialog!!.show()
        }
    }

    fun hideProgressDialog() {
        if (dialog != null) {
            dialog!!.dismiss()
        }
    }

    fun showDialog(
        context: Context?,
        title: String?,
        message: String?,
        dissmiss: Boolean,
        listener: DialogListener
    ) {
        showDialog(context, title, message, null, "Cancel", dissmiss, listener)
    }


    fun showDialog(
        context: Context?,
        title: String?,
        message: String?,
        positiveButtonText: String?,
        negativeButtonText: String?,
        dissmiss: Boolean,
        listener: DialogListener
    ) {
        var positiveButtonText = positiveButtonText
        if (positiveButtonText == null) {
            positiveButtonText = "OK"
        }
        val builder = AlertDialog.Builder(
            (context)!!
        )
        builder.setTitle(title)
        builder.setCancelable(dissmiss)
        // builder.setIcon(R.drawable.warning);
        builder.setMessage(message)
        builder.setPositiveButton(positiveButtonText) { dialog: DialogInterface?, which: Int ->
            listener.onPositiveButtonClick(
                dialog
            )
        }
        if (negativeButtonText != null) {
            builder.setNegativeButton(negativeButtonText) { dialog: DialogInterface?, which: Int ->
                listener.onNegativeButtonClick(
                    dialog
                )
            }
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun shortToast(context: Context?, message: String?) {
        try {
            Toast.makeText(
                context, message, Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun longToast(context: Context?, message: String?) {
        try {
            Toast.makeText(
                context, message, Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun log(TAG: String?, message: String?) {
        Log.d(TAG, (message)!!)
    }


    fun getWifiIpAddress(context: Context): String? {
        val wifiMgr = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiMgr.connectionInfo
        val ip = wifiInfo.ipAddress
        return Formatter.formatIpAddress(ip)
    }

    fun writeToSDFile(string: String?) {
        val mExternalStorageAvailable: Boolean
        val mExternalStorageWritable: Boolean
        val state = Environment.getExternalStorageState()
        if ((Environment.MEDIA_MOUNTED == state)) {
            // Can read and write the media
            mExternalStorageWritable = true
            mExternalStorageAvailable = mExternalStorageWritable
        } else if ((Environment.MEDIA_MOUNTED_READ_ONLY == state)) {
            // Can only read the media
            mExternalStorageAvailable = true
            mExternalStorageWritable = false
        } else {
            // Can't read or write
            mExternalStorageWritable = false
            mExternalStorageAvailable = mExternalStorageWritable
        }

        // Find the root of the external storage.
        val root = Environment.getExternalStorageDirectory()
        val dir = File(root.absolutePath + "/SERVER")
        dir.mkdirs()
        val file = File(dir, "SERVER.txt")
        try {
            val f = FileOutputStream(file)
            val pw = PrintWriter(f)
            pw.println(string)
            pw.flush()
            pw.close()
            f.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getCurrentDateTime(format: String): String? {
        return getTimeByFormat(format)
    }

    fun getCurrentTime(format: String): String? {
        return getTimeByFormat(format)
    }

    fun getCurrentDateTime(): String? {
        return getTimeByFormat("dd-MMM-yyyy hh:mm:ss")
    }

    fun getCurrentDate(): String? {
        return getTimeByFormat("dd-MMM-yyyy")
    }

    fun getCurrentTime(): String? {
        return getTimeByFormat("hh:mm")
    }


    private fun getTimeByFormat(format: String): String? {
        val c = Calendar.getInstance()
        val df = SimpleDateFormat(format)
        df.timeZone = TimeZone.getTimeZone(TimeZone.getDefault().id)
        return df.format(c.time)
    }


    fun isValidIP(ip: String?): Boolean {
        val validate = false
        val matcher = IP_ADDRESS.matcher(ip)
        return if (matcher.matches()) {
            true
        } else validate
    }

    fun showMessageDialog(context: Context?, title: String?, message: String?, imageId: Int) {
        val builder = AlertDialog.Builder(
            (context)!!
        )
        builder.setTitle(title)
            .setMessage(message)
            .setCancelable(false)
            .setIcon(imageId)
            .setPositiveButton("Ok") { dialog, id -> }
            .setNegativeButton("Cancel") { dialog, id -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }


    fun isGPSEnable(context: Context): Boolean {
        val manager: LocationManager? =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        assert(manager != null)
        return manager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }


    fun showGPSEnableDialog(context: Context) {
        showGPSEnableDialog(
            context,
            "Your GPS seems to be disabled, do you want to enable it?",
            "GO TO SETTINGS",
            null
        )
    }

    fun showGPSEnableDialog(
        context: Context, message: String?, positiveButtonText: String?, negativeButtonText: String?
    ) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton(
                positiveButtonText
            ) { dialog: DialogInterface?, id: Int ->
                context.startActivity(
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                )
            }
        val alert = builder.create()
        alert.show()
    }


    fun getByteFromBitmap(bitmap: Bitmap): ByteArray? {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()
    }

    fun getBitmapFromByte(bytes: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }


    @SuppressLint("MissingPermission")
    fun getDeviceIMEI(context: Context): String? {
        var imei: String = ""
        try {
            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (Build.VERSION.SDK_INT >= 26) {
                imei = telephonyManager.imei
            } else {
                imei = telephonyManager.deviceId
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return imei
    }


    fun getMonthName(value: String): String {
        if ((value == "01") || (value == "1")) {
            return "Jan"
        }
        if ((value == "02") || (value == "2")) {
            return "Feb"
        }
        if ((value == "03") || (value == "3")) {
            return "Mar"
        }
        if ((value == "04") || (value == "4")) {
            return "Apr"
        }
        if ((value == "05") || (value == "5")) {
            return "May"
        }
        if ((value == "06") || (value == "6")) {
            return "Jun"
        }
        if ((value == "07") || (value == "7")) {
            return "Jul"
        }
        if ((value == "08") || (value == "8")) {
            return "Aug"
        }
        if ((value == "09") || (value == "9")) {
            return "Sep"
        }
        if ((value == "10")) {
            return "Oct"
        }
        if ((value == "11")) {
            return "Nov"
        }
        return if ((value == "12")) {
            "Dec"
        } else "Not Found"
    }

    fun getLocalFromUTC(utcTime: String): String? {
        /**  2017-10-22T18:22:37.000+06:00   */
        try {
            val newUtcTime = utcTime.substring(0, utcTime.indexOf("+"))
            val dateFormat: DateFormat =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH)
            val date = dateFormat.parse(newUtcTime)
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH)
            return simpleDateFormat.format(date)
        } catch (ex: Exception) {
            ex.printStackTrace()
            try {
                /** "created_at": "2017-08-15T10:29:20.000Z"  &&   */
                val dateFormat: DateFormat =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
                dateFormat.timeZone = TimeZone.getTimeZone("BST")
                val date = dateFormat.parse(utcTime)
                val simpleDateFormat =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
                return simpleDateFormat.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null
    }

    fun getFormattedTime(time: String?, format: String?): String? {
        try {
            val dateFormat: DateFormat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
            val date = dateFormat.parse(time)
            val simpleDateFormat = SimpleDateFormat(format, Locale.ENGLISH)
            return simpleDateFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            return time
        }
    }


    fun getLocalTimeFromUTCTime(utcTime: String): String? {
        var tempTime: String = ""
        try {
            val time = getLocalFromUTC(utcTime)
            val timePice = time!!.split("T".toRegex()).toTypedArray()
            tempTime = timePice[1].substring(0, 8)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return tempTime
    }

    fun getLocalDateFromUTCTime(utcTime: String): String? {
        var dateString: String = ""
        try {
            val time = getLocalFromUTC(utcTime)
            val timePice = time!!.split("T".toRegex()).toTypedArray()
            val tempDate = timePice[0]
            val tempTime = timePice[1].substring(0, 8)
            val date = tempDate.split("-".toRegex()).toTypedArray()
            val year = date[0]
            val month = date[1]
            val day = date[2]
            dateString = day + " " + getMonthName(month) + " " + year
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return dateString
    }


    private fun getTimeDistance(time: Long): Long {
        return currentDate().time - time
    }

    fun currentDate(): Date {
        val calendar = Calendar.getInstance()
        return calendar.time
    }

    fun showProgressDialog(context: Context?) {
        /**
         * Progress Dialog for User Interaction
         */
        if (isObjectNull(dialog) && !isObjectNull(context)) {
            dialog = ProgressDialog(context)
            dialog!!.setTitle("Please wait")
            dialog!!.setMessage("Loading...")
            dialog!!.show()
        }
    }

    fun showProgressDialog(context: Context?, message: String?) {
        /**
         * Progress Dialog for User Interaction
         */
        if (isObjectNull(dialog) && !isObjectNull(context)) dialog = ProgressDialog(context)
        dialog!!.setTitle("Please wait")
        dialog!!.setMessage(message)
        dialog!!.show()
    }


    fun nullifyProgressBar() {
        if (dialog != null) dialog = null
    }

    fun getFormatFromIntegerValue(value: Int): String? {
        var amount = value.toString()
        amount = amount.replace(",".toRegex(), "")
        val decimalFormat = DecimalFormat("#,###")
        return decimalFormat.format(java.lang.Double.valueOf(amount))
    }

    fun getFormatFromDecimalValue(value: Double): String? {
        var amount = value.toString()
        amount = amount.replace(",".toRegex(), "")
        val decimalFormat = DecimalFormat("#,###.##")
        return decimalFormat.format(java.lang.Double.valueOf(amount))
    }

    fun showExitDialog(context: Activity, title: String?, message: String?) {
        if (isObjectNull(context) || isObjectNull(title) || isObjectNull(message) || context.isFinishing) {
            return
        }
        showExitDialog(context, title, message, "Exit", "Cancel")
    }

    fun showExitDialog(
        context: Activity,
        title: String?,
        message: String?,
        exitButtonText: String?,
        cancelButtonText: String?
    ) {
        if (isObjectNull(context) || isObjectNull(title) || isObjectNull(message) || isObjectNull(
                exitButtonText
            ) || isObjectNull(cancelButtonText)
        ) {
            return
        }
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(exitButtonText) { dialog: DialogInterface, which: Int ->
                dialog.dismiss()
                context.finishAffinity()
            }
            .setNegativeButton(
                cancelButtonText
            ) { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }
            .show()
    }


    fun getCurrentTimeInMillis(): Long {
        val currentTimeMillis: Long
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        currentTimeMillis = calendar.timeInMillis
        return currentTimeMillis
    }


    fun getHoursDifferent(startTime: Long, currentTime: Long): Long {
        var hours: Long = 0
        val diff = currentTime - startTime
        hours = diff / (60 * 60 * 1000)
        return hours
    }


    fun isObjectNull(`object`: Any?): Boolean {
        return (`object` == null)
    }

    fun isPhoneValid(phone: String?): Boolean {
        //return true;
        return Pattern.matches(PHONE_REGEX, phone)
    }

    fun wordCapitalize(words: String): String? {
        var str: String = ""
        var isCap = false
        for (i in 0 until words.length) {
            if (isCap) {
                str += words.toUpperCase()[i]
            } else {
                if (i == 0) {
                    str += words.toUpperCase()[i]
                } else {
                    str += words.toLowerCase()[i]
                }
            }
            isCap = words[i] == ' '
        }
        return str
    }

    fun getDifferenceDays(d1: Date, d2: Date): Long {
        val diff = d2.time - d1.time
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
    }


    @JvmStatic
    fun isAtLeastVersion(version: Int): Boolean {
        return Build.VERSION.SDK_INT >= version
    }


    fun getRandomID(): String? {
        val random = Random()
        val randomNumber = random.nextInt(10000) + 123
        return randomNumber.toString() + "eFood"
    }

    fun truncateText(text: String?, maxLength: Int, endWith: String): String? {
        if (text == null) return ""
        if (text.length > maxLength) {
            val bi = BreakIterator.getWordInstance()
            bi.setText(text)
            if (bi.isBoundary(maxLength - 1)) {
                return text.substring(0, maxLength - 2) + " " + endWith
            } else {
                val preceding = bi.preceding(maxLength - 1)
                return text.substring(0, preceding - 1) + " " + endWith
            }
        } else {
            return text
        }
    }


    fun getRequestDateFormat(year: Int, month: Int, date: Int, dateFormate: String?): String? {
        var dateFormate = dateFormate
        if (dateFormate == null) {
            dateFormate = "yyyy-MM-dd"
        }
        return try {
            val calender = Calendar.getInstance()
            calender[Calendar.YEAR] = year
            calender[Calendar.MONTH] = month - 1
            calender[Calendar.DATE] = date
            val formatter = SimpleDateFormat(dateFormate)
            formatter.format(calender.time)
        } catch (exp: java.lang.Exception) {
            null
        }
    }

    fun fixDateFormat(str: String?): String? {
        return if (!str!!.contains("."))
            str.replace("Z", ".00Z")
        else
            str
    }

    fun localToGMT(date: Date?, format: String?): String? {
        val sdf = SimpleDateFormat(format)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(date)
    }

    fun formatLocal(date: Date?, format: String?): String? {
        val sdf = SimpleDateFormat(format)
        sdf.timeZone = TimeZone.getTimeZone("Asia/Dhaka")
        return sdf.format(date)
    }

    fun fromStringToDate(stringDate: String?, format: String?): Date? {
        val df = SimpleDateFormat(format)
        try {
            return df.parse(stringDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }


//    fun Context.showAlert(
//        msg: String,
//        onPositiveClick: () -> Unit = {},
//        cancelButtonText: String = "No",
//        positiveButtonText: String = "Yes"
//    ) {
//        MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_App_MaterialAlertDialog)
//            .setMessage(msg)
//            .setNegativeButton(cancelButtonText) { dialog, _ ->
//                dialog.dismiss()
//            }
//            .setPositiveButton(positiveButtonText) { dialog, _ ->
//                onPositiveClick()
//                dialog.dismiss()
//            }
//            .show()
//    }


    interface DialogListener {
        fun onPositiveButtonClick(dialog: DialogInterface?)
        fun onNegativeButtonClick(dialog: DialogInterface?)
    }
}