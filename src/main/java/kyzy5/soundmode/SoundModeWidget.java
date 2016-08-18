package kyzy5.soundmode;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class SoundModeWidget extends AppWidgetProvider {

    private static final String SOUND_MODE_WIDGET_CLICK = "SOUND_MODE_WIDGET_CLICK";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.sound_mode_widget);
        ComponentName watchWidget = new ComponentName(context, SoundModeWidget.class);

        Intent intent = new Intent(SOUND_MODE_WIDGET_CLICK);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.imageButton, pendingIntent);
        appWidgetManager.updateAppWidget(watchWidget, remoteViews);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.sound_mode_widget);
        if (SOUND_MODE_WIDGET_CLICK.equals(intent.getAction())) {
            AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (am.getRingerMode() == AudioManager.RINGER_MODE_SILENT
                    && am.getStreamVolume(AudioManager.STREAM_RING) == 0) {
                am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                am.setStreamVolume(AudioManager.STREAM_RING, 100, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                remoteViews.setTextViewText(R.id.textView, "Sound");
                remoteViews.setImageViewResource(R.id.imageButton, R.drawable.circle);
            } else if (am.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE) {
                am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                am.setStreamVolume(AudioManager.STREAM_RING, 0,  AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                remoteViews.setTextViewText(R.id.textView, "Priority");
                remoteViews.setImageViewResource(R.id.imageButton, R.drawable.cross);
            } else {
                am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                am.setStreamVolume(AudioManager.STREAM_RING, 0,  AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                remoteViews.setTextViewText(R.id.textView, "Vibrate");
                remoteViews.setImageViewResource(R.id.imageButton, R.drawable.triangle);
            }

            ComponentName widget = new ComponentName(context, SoundModeWidget.class);
            AppWidgetManager.getInstance(context).updateAppWidget(widget, remoteViews);
        }
    }
}

