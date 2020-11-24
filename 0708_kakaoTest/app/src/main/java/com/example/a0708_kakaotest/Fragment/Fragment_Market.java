package com.example.a0708_kakaotest.Fragment;

import android.database.DataSetObserver;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.a0708_kakaotest.Android_Class.kakaoMapUse_Class.CustomPOIItem_Market;
import com.example.a0708_kakaotest.Android_Class.kakaoMapUse_Class.Make_Marker;
import com.example.a0708_kakaotest.Android_Class.menu_FragmentUse_Class.Return_Citys_Array;
import com.example.a0708_kakaotest.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.example.a0708_kakaotest.Android_Class.Init_Calss.Init_Data.get_useData;
import static com.example.a0708_kakaotest.Android_Class.Init_Calss.Init_GPS.getGPS;
import static net.daum.mf.map.api.MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading;

public class Fragment_Market extends Fragment implements MapView.MapViewEventListener, MapView.POIItemEventListener,MapView.CurrentLocationEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener {
    private SlidingUpPanelLayout slidview;
    private MapView mMapView;
    private View view;
    private ListView listview ;
    private List<String[]> maplist;
    private Spinner spinner_1;
    private Spinner spinner_2;
    private Button button_1;
    private Button button_2;
    private Button button_3;
    private Make_Marker make_marker;
    ArrayAdapter<String> arrayAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_market, container, false);
        slidview = view.findViewById(R.id.slidview);
        this.map_init();
        return view;
    }
    public void map_init(){
        button_1 = view.findViewById(R.id.button_1);
        button_2 = view.findViewById(R.id.button_2);
        button_3 = view.findViewById(R.id.button_3);
        spinner_1 = view.findViewById(R.id.spinner_1);

        spinner_2 = view.findViewById(R.id.spinner_2);
        slidview = view.findViewById(R.id.slidview);
        listview = view.findViewById(R.id.listView);
        mMapView = view.findViewById(R.id.map_view);
        cityArrayListinit();
        disArrayListinit();
        mMapView.setMapViewEventListener(this);
        mMapView.setPOIItemEventListener(this);
        button_1.setOnClickListener(new buttonOnclick_Select());
        button_2.setOnClickListener(new button2Onclick_Select());
        button_3.setOnClickListener(new button3Onclick_Select());

        slidview.setFadeOnClickListener(new SlidOnclick_Listener());
        spinner_1.setOnItemSelectedListener(new spinner_1_SelectListener());
        maplist = get_useData();
        make_marker =new Make_Marker(mMapView);
        make_marker.cur_pos(1);
        //test code


        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);


    }
    private void add_maker(int i , int zoomlv){
        make_marker.add_Market_marker(Integer.parseInt(maplist.get(i)[0]) ,maplist.get(i)[1], maplist.get(i)[2] ,
                Double.parseDouble(maplist.get(i)[3]),
                Double.parseDouble(maplist.get(i)[4]),
                maplist.get(i)[5] ,maplist.get(i)[6],zoomlv);
    }
    public void input_mapMaker(String city ,String dis){
        mMapView.removeAllPOIItems();
        make_marker.cur_pos(0);
        int num=0;
        if(dis.equals("")){
            for(int i = 1 ; i < maplist.size() ; i++){
                if(maplist.get(i)[5].equals(city+" ")) {
                    this.add_maker(i,8);
                    num ++ ;
                }
            }
        }
        else{
            for(int i = 1 ; i < maplist.size() ; i++){
                if(maplist.get(i)[5].equals(city+" ") && maplist.get(i)[6].equals(dis)) {
                    this.add_maker(i,6);
                    num ++ ;
                }
            }
        }
        if(num ==0 ){
            Toast.makeText(getActivity(),"검색 결과가 없습니다.",Toast.LENGTH_SHORT).show();
        }
    }
    public void disArrayListinit(){
        ArrayList disArrayList = new ArrayList<String>();
        disArrayList.add("시/군/구");

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(), R.layout.spinner_item,  disArrayList);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner_2.setAdapter(adapter);
    }
    private void cityArrayListinit(){
        ArrayList cityArrayList = new Return_Citys_Array().Market_cityArrayList();
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(), R.layout.spinner_item,  cityArrayList);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner_1.setAdapter(adapter);
    }
    private void inputdisArray(String city){
        ArrayList disArrayList = new Return_Citys_Array().Market_DisArrayList(city);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(), R.layout.spinner_item,  disArrayList);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner_2.setAdapter(adapter);
    }
    private class SlidOnclick_Listener implements SlidingUpPanelLayout.OnClickListener{
        @Override
        public void onClick(View view) {
            if(slidview.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED){
                slidview.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            }
        }
    }


    private class spinner_1_SelectListener implements Spinner.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String text = spinner_1.getSelectedItem().toString();
            if(!text.equals("시/도")){
                inputdisArray(text);
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
    private class buttonOnclick_Select implements Button.OnClickListener{
        @Override
        public void onClick(View view) {
            String city = spinner_1.getSelectedItem().toString();
            String dis  = spinner_2.getSelectedItem().toString();
            if(city.equals("시/도")){
                Toast.makeText(getActivity(),"시/도 를 먼저 설정해주세요",Toast.LENGTH_SHORT).show();
            }else if(dis.equals("시/군/구")) {
                input_mapMaker(city, "");
            }
            else {
                input_mapMaker(city, dis);
            }
        }
    }
    private class button2Onclick_Select implements Button.OnClickListener{
        @Override
        public void onClick(View view) {
            make_marker.cur_pos(1);
        }
    }
    private class button3Onclick_Select implements Button.OnClickListener{
        @Override
        public void onClick(View view) {
            if(mMapView.getCurrentLocationTrackingMode().equals(TrackingModeOnWithHeading)){
                mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);
            }
            else{
                mMapView.setCurrentLocationTrackingMode(TrackingModeOnWithHeading);
            }
        }
    }
    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {
        if(mapPOIItem instanceof CustomPOIItem_Market){
            CustomPOIItem_Market item = (CustomPOIItem_Market)mapPOIItem;
            String[] values = new String[] {"시장명 : " + item.name, "주소 : " + item.add,"시/도 : "+ item.city, "시/군/구 : " + item.dis};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, values);
            listview.setAdapter(adapter);
            slidview.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        }
    }
    @Override
    public void onMapViewInitialized(MapView mapView) { }
    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) { }
    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) { }
    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) { }
    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) { }
    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) { }
    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) { }
    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) { }
    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) { }
    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {}
    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {}
    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) { }
    @Override
    public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String s) { }
    @Override
    public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) { }
    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) { }
    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) { }
    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) { }
    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) { }
}