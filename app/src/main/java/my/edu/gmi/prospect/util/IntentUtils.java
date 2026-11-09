package my.edu.gmi.prospect.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import my.edu.gmi.prospect.R;

/** Helper methods for opening external apps (browser, dialer, email, maps, share sheet). */
public final class IntentUtils {

    private IntentUtils() { }

    /** Opens the GMI Online Application portal. */
    public static void openOnlineApplication(Context context) {
        openUrl(context, context.getString(R.string.oa_url));
    }

    public static void openUrl(Context context, String url) {
        start(context, new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public static void dial(Context context, String telUri) {
        start(context, new Intent(Intent.ACTION_DIAL, Uri.parse(telUri)));
    }

    public static void email(Context context, String to, String subject, String body) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        start(context, intent);
    }

    public static void openMap(Context context, String query) {
        Uri uri = Uri.parse("geo:0,0?q=" + Uri.encode(query));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // No maps app installed: fall back to Google Maps in the browser
            openUrl(context, "https://www.google.com/maps/search/?api=1&query=" + Uri.encode(query));
        }
    }

    public static void shareText(Context context, String chooserTitle, String text) {
        Intent send = new Intent(Intent.ACTION_SEND);
        send.setType("text/plain");
        send.putExtra(Intent.EXTRA_TEXT, text);
        start(context, Intent.createChooser(send, chooserTitle));
    }

    private static void start(Context context, Intent intent) {
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, R.string.no_app_found, Toast.LENGTH_SHORT).show();
        }
    }
}
