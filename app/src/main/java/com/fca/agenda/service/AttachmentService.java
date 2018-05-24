package com.fca.agenda.service;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fca.agenda.R;
import com.fca.agenda.utils.ApplicationConstants;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by fabiano.alvarenga on 22/04/18.
 */

public class AttachmentService {

    private static ProgressDialog progressDialog = null;

    /**
     * Method responsible for opening a home page inside a webview.
     *
     * @param activity
     * @param urlOpen
     */
    public static void pageView(@NonNull AppCompatActivity activity, String urlOpen) {
        WebView webView = new WebView(activity);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(urlOpen);

        activity.setContentView(webView);
    }

    /**
     * Method responsible for downloading a google driver file through your ID.
     *
     * @param activity
     * @param idFile
     */
    public static void downloadFile(@NonNull final AppCompatActivity activity, final String idFile) {

        showProgress(activity);

        WebView webView = new WebView(activity);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                new DownloadAsyncTask(activity, new ResultCallback() {
                    @Override
                    public void onSuccess(Map<String, String> stringMap) {
                        AttachmentService.finishWebView(activity);
                        AttachmentService.showPDF(activity, stringMap);
                        AttachmentService.hideProgress();
                    }
                }).execute(url, userAgent, contentDisposition, mimetype);
            }
        });

        String url = activity.getString(R.string.google_driver_download_file) + idFile;
        webView.loadUrl(url);
        activity.setContentView(webView);
    }

    /**
     * Async Task Downloading Asynchronously
     */
    private static class DownloadAsyncTask extends AsyncTask<String, Void, Map<String, String>> {

        private ResultCallback callback;
        private Activity activity;

        public DownloadAsyncTask(final AppCompatActivity activity, final ResultCallback callback) {
            this.callback = callback;
            this.activity = activity;
        }

        @Override
        protected Map<String, String> doInBackground(String... parameters) {
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(parameters[0]));

            request.setMimeType(parameters[3]);
            String cookies = CookieManager.getInstance().getCookie(parameters[0]);
            request.addRequestHeader(ApplicationConstants.AttachmentService.COOKIE, cookies);
            request.addRequestHeader(ApplicationConstants.AttachmentService.USER_AGENT, parameters[1]);
            request.setDescription(activity.getString(R.string.processing_download));
            request.setTitle(URLUtil.guessFileName(parameters[0], parameters[2], parameters[3]));
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            String fileName = URLUtil.guessFileName(parameters[0], parameters[2], parameters[3]);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
            DownloadManager dm = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
            dm.enqueue(request);

            Map<String, String> stringMap = new LinkedHashMap<>();
            stringMap.put(ApplicationConstants.AttachmentService.FILE_NAME, fileName);
            stringMap.put(ApplicationConstants.AttachmentService.MIME_TYPE, parameters[3]);

            return stringMap;
        }

        @Override
        protected void onPostExecute(Map<String, String> returnValues) {
            super.onPostExecute(returnValues);
            callback.onSuccess(returnValues);
        }
    }

    /**
     * Method responsible for show progress dialog
     *
     * @param activity
     */
    private static void showProgress(Activity activity) {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
        progressDialog = ProgressDialog.show(activity, "", activity.getString(R.string.processing_download), true, false);
    }

    /**
     * Method responsible for hide progress dialog
     */
    private static void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    /**
     * Method responsible for finalizing the webview instance.
     *
     * @param activity
     */
    private static void finishWebView(final Activity activity) {
        Intent intent = activity.getIntent();
        activity.finish();
        activity.startActivity(intent);
    }

    /**
     * Method responsible for displaying the downloaded PDF.
     *
     * @param activity
     * @param stringMap
     */
    private static void showPDF(final Activity activity, final Map<String, String> stringMap) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                +"/"+stringMap.get(ApplicationConstants.AttachmentService.FILE_NAME));
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, stringMap.get(ApplicationConstants.AttachmentService.MIME_TYPE));
        activity.startActivity(intent);
    }
}
