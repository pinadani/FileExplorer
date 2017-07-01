package pinadani.filemanager.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ShareCompat;

/**
 * Intent utilities mainly gathering different purpose of user needs
 * Created on {18/12/15}
 **/
public class IntentUtils {
    public static final String TAG = IntentUtils.class.getName();

    public static void startDialActivity(Context ctx, String phone) {

        if (phone == null) {
            phone = "";
        }
        Intent dial = new Intent();
        dial.setAction(Intent.ACTION_DIAL);
        dial.setData(Uri.parse("tel:" + phone.trim()));
        if (dial.resolveActivity(ctx.getPackageManager()) != null) {
            ctx.startActivity(dial);
        }

    }

    public static void startEmailActivity(Context ctx, String email, String subject, String body) {
        ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder.from((Activity) ctx);
        builder.setType("message/rfc822");
        builder.addEmailTo(email);
        builder.setSubject(subject);
        builder.setText(body);
        builder.startChooser();
    }

    public static void startBrowserApp(Context ctx, String link) {
        if (link == null) {
            link = "";
        }
        if (!link.startsWith("http")) {
            link = "http://".concat(link);
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(link));
        if (i.resolveActivity(ctx.getPackageManager()) != null) {
            ctx.startActivity(i);
        }
    }

    public static void startMapApp(Context ctx, double latitude, double longitude, String label) {

        String uriBegin = "geo:" + latitude + "," + longitude;
        String query = latitude + "," + longitude + "(" + label + ")";
        String encodedQuery = Uri.encode(query);
        String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
        Uri uri = Uri.parse(uriString);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(ctx.getPackageManager()) != null) {
            ctx.startActivity(intent);
        }
    }

    public static void startSharing(Context context, String link) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, link);
        intent.setType("text/plain");
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }


    /**
     * Gets {@link android.content.Intent} for application, which navigates to
     * given location (e.g. Google Navigation).
     *
     * @param latitude  of navigated location.
     * @param longitude of navigated location.
     * @return
     */
    public static Intent getNavigateIntent(Double latitude, Double longitude) {
        return new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + latitude + "," + longitude));
    }

    public static void startNavigationApp(Context ctx, double lat, double lon) {
        Intent intent = getNavigateIntent(lat, lon);
        if (intent.resolveActivity(ctx.getPackageManager()) != null) {
            ctx.startActivity(intent);
        }
    }

}
