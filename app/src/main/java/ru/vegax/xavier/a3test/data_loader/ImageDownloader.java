/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.vegax.xavier.a3test.data_loader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;


public class ImageDownloader {
    private static final String LOG_TAG = "ImageDownloader";
    private static final int TIMEOUT = 1000;

    public void download(String url, ImageView imageView, ProgressBar progressBar) {
        resetPurgeTimer();
        Bitmap bitmap = getBitmapFromCache(url);

        if (bitmap == null) {
            forceDownload(url, imageView, progressBar);
        } else {
            cancelPotentialDownload(url, imageView);
            imageView.setImageBitmap(bitmap);
        }
    }

    /**
     * Same as download but the image is always downloaded and the cache is not used.
     * Kept private at the moment as its interest is not clear.
     */
    private void forceDownload(String url, ImageView imageView, ProgressBar progressBar) {
        // State sanity: mUrl is guaranteed to never be null in DownloadedDrawable and cache keys.
        if (url == null) {
            imageView.setImageDrawable(null);
            return;
        }
        BitmapDownloaderTask task = new BitmapDownloaderTask(imageView, progressBar,this);
        DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task);
        progressBar.setVisibility(View.VISIBLE);
        imageView.setImageDrawable(downloadedDrawable);
        // imageView.setMinimumHeight(156);
        task.execute(url);

    }

    private static void cancelPotentialDownload(String url, ImageView imageView) {
        BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);

        if (bitmapDownloaderTask != null) {
            String bitmapUrl = bitmapDownloaderTask.mUrl;
            if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
                bitmapDownloaderTask.cancel(true);
            }
        }
    }


    /**
     * @param imageView Any imageView
     * @return Retrieve the currently active download task (if any) associated with this imageView.
     * null if there is no such task.
     */
    private static BitmapDownloaderTask getBitmapDownloaderTask(ImageView imageView) {


        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof DownloadedDrawable) {
                DownloadedDrawable downloadedDrawable = (DownloadedDrawable) drawable;
                return downloadedDrawable.getBitmapDownloaderTask();
            }
        }
        return null;
    }

    private static Bitmap getContent(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setConnectTimeout(TIMEOUT);// 0.1 seconds
        con.setRequestMethod("GET");
        con.connect();
        Bitmap bitmap;

        try (InputStream is = con.getInputStream()) {
            bitmap = BitmapFactory.decodeStream(is);
        }
        return bitmap;
    }

    static class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {

        private final ImageDownloader mImageDownloader;
        private String mUrl;
        private final WeakReference<ImageView> mImageViewReference;
        private final WeakReference<ProgressBar> mProgressBarReference;
        private Exception mE;

        BitmapDownloaderTask(ImageView imageView, ProgressBar progressBar, ImageDownloader imageDownloader) {
            mImageViewReference = new WeakReference<>(imageView);
            mProgressBarReference = new WeakReference<>(progressBar);
            mImageDownloader = imageDownloader;
        }

        /**
         * Actual download method.
         */
        @Override
        protected Bitmap doInBackground(String... params) {
            mUrl = params[0];
            try {
                return getContent(mUrl);
            } catch (Exception e) {
                mE = e;
            }
            return null;
        }

        /**
         * Once the image is downloaded, associates it to the imageView
         */
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (!isCancelled() && mE == null) {
                mImageDownloader.addBitmapToCache(mUrl, bitmap);

                if (mImageViewReference != null) {
                    ImageView imageView = mImageViewReference.get();
                    ProgressBar progressBar = mProgressBarReference.get();
                    BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
                    if ((this == bitmapDownloaderTask) && imageView != null && bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                        if(progressBar != null){
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }
            }
        }
    }


    static class DownloadedDrawable extends ColorDrawable {
        private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;

        DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask) {
            super(Color.LTGRAY);
            bitmapDownloaderTaskReference =
                    new WeakReference<>(bitmapDownloaderTask);

        }

        BitmapDownloaderTask getBitmapDownloaderTask() {
            return bitmapDownloaderTaskReference.get();
        }
    }




    /*
     * A soft reference cache is too aggressively cleared by the
     * Garbage Collector.
     */

    private static final int HARD_CACHE_CAPACITY = 45;
    private static final int DELAY_BEFORE_PURGE = 10 * 1000; // in milliseconds

    private final HashMap<String, Bitmap> sHardBitmapCache =
            new LinkedHashMap<String, Bitmap>(HARD_CACHE_CAPACITY / 2, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Entry<String, Bitmap> eldest) {
                    if (size() > HARD_CACHE_CAPACITY) {
                        sSoftBitmapCache.put(eldest.getKey(), new SoftReference<>(eldest.getValue()));
                        return true;
                    } else
                        return false;
                }
            };


    private final static ConcurrentHashMap<String, SoftReference<Bitmap>> sSoftBitmapCache =
            new ConcurrentHashMap<>(HARD_CACHE_CAPACITY / 2);

    private final Handler purgeHandler = new Handler();

    private final Runnable purger = new Runnable() {
        public void run() {
            clearCache();
        }
    };


    private void addBitmapToCache(String url, Bitmap bitmap) {
        if (bitmap != null) {
            synchronized (sHardBitmapCache) {
                sHardBitmapCache.put(url, bitmap);
            }
        }
    }

    private Bitmap getBitmapFromCache(String url) {
        // First try the hard reference cache
        synchronized (sHardBitmapCache) {
            final Bitmap bitmap = sHardBitmapCache.get(url);
            if (bitmap != null) {
                // Bitmap found in hard cache
                // Move element to first position, so that it is removed last
                sHardBitmapCache.remove(url);
                sHardBitmapCache.put(url, bitmap);
                return bitmap;
            }
        }

        // Then try the soft reference cache
        SoftReference<Bitmap> bitmapReference = sSoftBitmapCache.get(url);
        if (bitmapReference != null) {
            final Bitmap bitmap = bitmapReference.get();
            if (bitmap != null) {
                // Bitmap found in soft cache
                return bitmap;
            } else {
                // Soft reference has been Garbage Collected
                sSoftBitmapCache.remove(url);
            }
        }

        return null;
    }

    private void clearCache() {
        sHardBitmapCache.clear();
        sSoftBitmapCache.clear();
    }

    private void resetPurgeTimer() {
        purgeHandler.removeCallbacks(purger);
        purgeHandler.postDelayed(purger, DELAY_BEFORE_PURGE);
    }
}
