package kolomyichuk.runly.di

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.Builder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import kolomyichuk.runly.MainActivity
import kolomyichuk.runly.R
import kolomyichuk.runly.domain.run.repository.RemoteRunRepository
import kolomyichuk.runly.domain.run.usecase.GetRunDisplayModelUseCase
import kolomyichuk.runly.service.RunTrackingService
import kolomyichuk.runly.service.manager.RunLocationManager
import kolomyichuk.runly.service.manager.RunNotificationManager
import kolomyichuk.runly.service.manager.RunTimerManager

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @ServiceScoped
    @Provides
    fun providesFusedLocationProvider(@ApplicationContext context: Context) =
        LocationServices.getFusedLocationProviderClient(context)

    @ServiceScoped
    @Provides
    fun provideTrackingNotificationBuilder(
        @ApplicationContext context: Context,
        pendingIntent: PendingIntent
    ) = NotificationCompat.Builder(context, RunTrackingService.TRACKING_CHANNEL_ID)
        .setAutoCancel(false)
        .setOngoing(true)
        .setOnlyAlertOnce(true)
        .setSmallIcon(R.mipmap.ic_runly)
        .setContentTitle(context.getString(R.string.app_name))
        .setContentText("00:00:00")
        .setContentIntent(pendingIntent)

    @ServiceScoped
    @Provides
    fun provideActivityPendingIntent(@ApplicationContext context: Context): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra(MainActivity.EXTRA_SCREEN, MainActivity.SCREEN_RUN)
        }
        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    @ServiceScoped
    @Provides
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    @ServiceScoped
    @Provides
    fun provideRunLocationManager(
        fusedLocationProviderClient: FusedLocationProviderClient,
        remoteRunRepository: RemoteRunRepository
    ): RunLocationManager {
        return RunLocationManager(
            fusedLocationProviderClient = fusedLocationProviderClient,
            remoteRunRepository = remoteRunRepository
        )
    }

    @ServiceScoped
    @Provides
    fun provideRunNotificationManager(
        @ApplicationContext context: Context,
        notificationManager: NotificationManager,
        trackingNotificationBuilder: Builder,
        getRunDisplayModelUseCase: GetRunDisplayModelUseCase
    ): RunNotificationManager {
        return RunNotificationManager(
            context = context,
            notificationManager = notificationManager,
            trackingNotificationBuilder = trackingNotificationBuilder,
            getRunDisplayModelUseCase = getRunDisplayModelUseCase
        )
    }

    @ServiceScoped
    @Provides
    fun provideRunTimerManager(
        remoteRunRepository: RemoteRunRepository
    ): RunTimerManager {
        return RunTimerManager(
            remoteRunRepository = remoteRunRepository
        )
    }
}
