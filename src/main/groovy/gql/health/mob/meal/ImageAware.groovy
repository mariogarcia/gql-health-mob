package gql.health.mob.ui

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ImageView
import groovy.transform.CompileDynamic

import java.text.SimpleDateFormat

trait ImageAware {

    static final String ALBUM_NAME = 'helthix'
    static final int IMAGE_KEY = 1

    private File getAlbumDir() {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            AlbumStorageDirFactory mAlbumStorageDirFactory = new FroyoAlbumDirFactory()
            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(ALBUM_NAME);

            if (storageDir != null) {
                if (! storageDir.mkdirs()) {
                    if (! storageDir.exists()){
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v(ALBUM_NAME, "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }

    File createImageFile() throws IOException {
        final String JPEG_FILE_PREFIX = "IMG_";
        final String JPEG_FILE_SUFFIX = ".jpg";

        // Create an imagePath file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
        return imageF;
    }

    void  loadPicFromPathInto(String path, ImageView imageView) {

        /* There isn't enough memory to open up more than a couple camera photos */
        /* So pre-scale the target bitmap into which the file is decoded */

        /* Get the size of the ImageView */
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        /* Get the size of the imagePath */
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        /* Figure out which way needs to be reduced less */
        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(
                    (photoW/targetW) as double,
                    (photoH/targetH) as double).intValue()
        }

        /* Set bitmap options to scale the imagePath decode target */
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        /* Decode the JPEG file into a Bitmap */
        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);

        /* Associate the Bitmap to the ImageView */
        imageView.setImageBitmap(bitmap)
        imageView.setVisibility(View.VISIBLE)
        imageView.invalidate()
    }

    void galleryAddPicFrom(String path) {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);

        //this.sendBroadcast(mediaScanIntent);
    }

    void paintImageOrDrawable(Context ctx, String imagePath, ImageView imageView, Drawable defaultDrawable) {
        if (imagePath) {
            loadPicFromPathInto(imagePath, imageView)
        } else {
            imageView.imageDrawable = defaultDrawable
        }
    }
}