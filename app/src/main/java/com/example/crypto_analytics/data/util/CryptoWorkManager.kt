package com.example.crypto_analytics.data.util

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.crypto_analytics.App
import com.example.crypto_analytics.R
import com.example.crypto_analytics.ui.view.MainActivity
import java.util.*
import kotlin.math.roundToInt
import kotlin.random.Random

class CryptoWorkManager(private val context: Context,
                        private val workerParams: WorkerParameters): CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        Log.v("work_timer", App.notificationPrice.toString())
        Log.v("work_timer", Calendar.getInstance().time.toString())
        val response = applicationContext.appComponent
            .getCryptoService()
            .getCryptoCurrency("bitcoin", "rub", "30", "daily")

        val lastPriceValue = response.body()?.prices?.last()?.last()?.toFloat() ?: 0F

        if (lastPriceValue >= App.notificationPrice)  {
            showNotification()
        }


        if(response.isSuccessful.not()) {
            return Result.failure(
                workDataOf(Constants.WORKER_KEY_ERROR to "Ошибка подключения")
            )
        }

        return Result.success(
            workDataOf(Constants.CRYPTO_WORKER_KEY to lastPriceValue)
        )
    }

    private suspend fun showNotification() {
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_ONE_SHOT)

        val notificationBuilder =
            NotificationCompat
                .Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_wallet)
                .setContentText("Биткоин вырос")
                .setContentTitle("ВРЕМЯ СКУПАТЬ")
                .setAutoCancel(false)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .build()

        val foregroundInfo = ForegroundInfo(Random.nextInt(), notificationBuilder)

        setForeground(foregroundInfo)
    }

    fun setPrice() {

    }
}