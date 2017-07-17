package com.kt.maps.demo;

import android.Manifest;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.kt.maps.GMapShared;
import com.kt.maps.internal.util.Logger;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;



public class MainActivity extends ListActivity {


    private static final int REQUEST_CODE_APP_DETAIL_SETTING = 10001;
    /**
     * 필요권한 리스트
     */
    private List<String> needPermissions;


    /**
     * 권한 승인여부 확인
     * 필요권한 중에 허용되지 않은 권한이 있는경우 요청 하며 false 반환
     * 없는 경우 true 반환
     *
     * @return 모든 권한 승인여부
     */
    private boolean checkPermissions() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        needPermissions = new ArrayList<>();
        for (String permission : permissions) {
            if (!hasPermission(permission)) {
                needPermissions.add(permission);
            }
        }

        if (needPermissions.isEmpty()) {
            return true;
        }

        requestPermissions(needPermissions);
        return false;
    }

    /**
     * 확인하는 권한이 승인이 되어있는지 여부 반환
     *
     * @param permission 권한 명칭
     * @return 승인여부
     */
    private boolean hasPermission(String permission) {
        return (PackageManager.PERMISSION_GRANTED ==
                ContextCompat.checkSelfPermission(this, permission));
    }

    /**
     * 승인이 필요한 권한 목록으로 권한 승인 요청
     *
     * @param needPermissions 필요 권한 목록
     */
    public void requestPermissions(List<String> needPermissions) {
        ActivityCompat.requestPermissions(this, needPermissions.toArray(
                new String[needPermissions.size()]), 0);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults == null || grantResults.length == 0) {
            return;
        }
        int resultCode = RESULT_OK;
        // 모든 필요한 권한이 다 있어야 한다.
        // 필요한 권한 리스트와 결과 리스트가 다르면 누락된 권한이 있다고 판단
        if (grantResults.length == needPermissions.size()) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    resultCode = RESULT_CANCELED;
                    break;
                }
            }
        } else {
            resultCode = RESULT_CANCELED;
        }

        if (resultCode == Activity.RESULT_OK) { //권한을 모두 허용
            makeList();
        } else { //권한 거부 항목이 있는경우
            //사용자가 '다시 보지 않음'(never ask again) 항목을 체크 하고 거부 했는지 확인
            for (int i = 0; i < permissions.length; i++) {
                //다시 보지 않음 항목이 있는경우 false 반환
                //권한 허용을 하는 경우 false를 반환하기 때문에 허용여부 한번 더 확인
                if (grantResults[i] != PermissionChecker.PERMISSION_GRANTED &&
                        !ActivityCompat.shouldShowRequestPermissionRationale(this,
                                permissions[i])) {
                    showPermissionAlertDialog(false);
                    return;
                }
            }
            //거부 항목만 있는경우
            showPermissionAlertDialog(true);
        }
    }

    /**
     * 권한 체크 결과 사용자가 허용하지 않은 권한이 있는경우
     * 권한 승인에 대한 알림 팝업
     * 권한확인 재시도, 앱종료
     *
     * @param isShowRationale 다시 보지 않음 체크 여부
     */
    private void showPermissionAlertDialog(final boolean isShowRationale) {

        new AlertDialog.Builder(this).setTitle(R.string.permission_dialog_title)

                .setMessage(isShowRationale ? R.string.permission_dialog_content :
                        R.string.permission_dialog_content_setting)
                .setCancelable(false)
                .setPositiveButton(isShowRationale ?
                                R.string.permission_dialog_button_positive :
                                R.string.permission_dialog_button_positive_setting,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                if (isShowRationale) {
                                    checkPermissions();
                                } else {
                                    startAppDetailSetting();
                                }
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(R.string.permission_dialog_button_negative,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                                dialog.dismiss();
                            }
                        }).show();
    }

    /**
     * 앱 상세설정화면 이동
     * 권한 확인시 다시 보지 않기 항목을 선택시 사용자가 직접 상세설정에서
     * 권한을 주어야하기 때문에 해당 화면으로 이동
     */
    private void startAppDetailSetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CODE_APP_DETAIL_SETTING);
    }

    /**
     * 앱 권한 확인후 리스트를 보여준다
     */
    private void makeList() {
        //리스트 작성
        ListAdapter adapter = new CustomArrayAdapter(this, demos);
        Logger.d("MainActivity", "==== SDK Tile data format : " + GMapShared.getSupportedTileFormat());
        setListAdapter(adapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != REQUEST_CODE_APP_DETAIL_SETTING) {
            return;
        }

        //퍼미션 재확인
        if (checkPermissions()) {
            makeList();
        }
    }

    private static class DemoDetails {

        private final int titleId;
        private final int descriptionId;

        private final Class<? extends Activity> activityClass;

        public DemoDetails(int titleId, int descriptionId, Class<? extends Activity> activityClass) {
            super();
            this.titleId = titleId;
            this.descriptionId = descriptionId;
            this.activityClass = activityClass;
        }
    }

    private static final DemoDetails[] demos = {
            new DemoDetails(R.string.map_demo, R.string.basic_description, BasicMapActivity.class),
            new DemoDetails(R.string.shapes, R.string.shapes, ShapeActivity.class),
            new DemoDetails(R.string.marker_demo, R.string.marker_demo,
                    MarkerActivity.class),
            new DemoDetails(R.string.ground_overlay, R.string.ground_overlay,
                    GroundOverlayActivity.class),
            new DemoDetails(R.string.infowindows, R.string.infowindows, InfoWindowActivity.class),
            new DemoDetails(R.string.navi_demo, R.string.navi_description,
                    NaviDemoActivity.class),
            new DemoDetails(R.string.fps_test, R.string.fps_test_description, FpsTestActivity.class),
            new DemoDetails(R.string.programmatic_map, R.string.programmatic_map_description,
                    ProgrammaticMapActivity.class),
            new DemoDetails(R.string.map_style, R.string.map_style, MapStyleActivity.class),
            new DemoDetails(R.string.hundreds_markers, R.string.hundreds_markers,
                    HundredsMarkersActivity.class),
            new DemoDetails(R.string.data_import, R.string.data_import, DataImportActivity.class),
            new DemoDetails(R.string.building_highlights, R.string.building_highlights,
                    BuildingHighlightsActivity.class),
            new DemoDetails(R.string.traffic_info, R.string.traffic_info_description, TrafficInfoActivity.class),
            new DemoDetails(R.string.multi_language, R.string.multi_language_description,
                    MultiLanguageActivity.class),
            new DemoDetails(R.string.layers, R.string.layers_description, LayersActivity.class)};



    private static class CustomArrayAdapter extends ArrayAdapter<DemoDetails> {

        public CustomArrayAdapter(Context context, DemoDetails[] demos) {
            super(context, R.layout.feature, R.id.title, demos);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FeatureView featureView;
            if (convertView instanceof FeatureView) {
                featureView = (FeatureView) convertView;
            } else {
                featureView = new FeatureView(getContext());
            }

            DemoDetails demo = getItem(position);

            featureView.setTitleId(demo.titleId);
            featureView.setDescriptionId(demo.descriptionId);

            return featureView;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());

        setContentView(R.layout.activity_main);

        if (checkPermissions()) {
            makeList();
        }

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        DemoDetails demo = (DemoDetails) getListAdapter().getItem(position);
        startActivity(new Intent(this, demo.activityClass));
    }
}
