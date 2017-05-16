package gql.health.mob.util

import android.os.Environment

public final class BaseAlbumDirFactory extends AlbumStorageDirFactory {

	// Standard storage location for digital camera files
	private static final String CAMERA_DIR = "/dcim/";

	@Override
	File getAlbumStorageDir(String albumName) {
		"${Environment.getExternalStorageDirectory()}${CAMERA_DIR}${albumName}" as File
	}
}
