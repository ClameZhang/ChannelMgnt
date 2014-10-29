package com.clame.channelmgnt;

import com.clame.channelmgnt.widgets.BottomBarDelivery;
import com.clame.channelmgnt.widgets.BottomBarDelivery.OnItemChangedListener;
import com.clame.channelmgnt.widgets.BottomBarManagement;
import com.clame.channelmgnt.widgets.BottomBarManagement.OnManagementItemChangedListener;
import com.clame.channelmgnt.widgets.BottomBarPackage;
import com.clame.channelmgnt.widgets.BottomBarPackage.OnPackageItemChangedListener;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.os.Bundle;

public class MainActivity extends FragmentActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        int userAuth = 2;

        // declare the bottombar, and add the ItemChangedListener
        RelativeLayout llPackage = (RelativeLayout) findViewById(R.id.ll_bottom_bar_package);
        final BottomBarPackage bottomBarPackage = (BottomBarPackage) findViewById(R.id.main_bottom_bar_package);
        bottomBarPackage.setOnPackageItemChangedListener(new OnPackageItemChangedListener() {
			
			@Override
			public void onPackageItemChanged(int index) {
				// TODO Auto-generated method stub
				showDetails(index);
			}
		});
        bottomBarPackage.setSelectedState(0);

        RelativeLayout llDelivery = (RelativeLayout) findViewById(R.id.ll_bottom_bar_delivery);
        final BottomBarDelivery bottomBarDelivery = (BottomBarDelivery) findViewById(R.id.main_bottom_bar_delivery);
        bottomBarDelivery.setOnItemChangedListener(new OnItemChangedListener() {
            @Override
            public void onItemChanged(int index) {
                showDetails(index);
            }
        });
        bottomBarDelivery.setSelectedState(0);

        RelativeLayout llManagement = (RelativeLayout) findViewById(R.id.ll_bottom_bar_management);
        final BottomBarManagement bottomBarManagement = (BottomBarManagement) findViewById(R.id.main_bottom_bar_management);
        bottomBarManagement.setOnManagementItemChangedListener(new OnManagementItemChangedListener() {
            @Override
            public void onManagementItemChanged(int index) {
                showDetails(index);
            }
        });
        bottomBarManagement.setSelectedState(0);
        
        if (userAuth == 0) {
        	llPackage.setVisibility(View.GONE);
        	llDelivery.setVisibility(View.GONE);
		} else if (userAuth == 1) {
			llManagement.setVisibility(View.GONE);
			llDelivery.setVisibility(View.GONE);
		} else {
			llManagement.setVisibility(View.GONE);
			llPackage.setVisibility(View.GONE);
		}
    }

    /**
     * @FunName showDetails
     * @Description switch the fragment content according to the selected item on bottombar
     * @param index
     * @return N/A
     * 
     */
    private void showDetails(int index) {
        Fragment details = (Fragment) getSupportFragmentManager().findFragmentById(R.id.main_details);

        // set the target fragment according to the index
        switch (index) {
        case 0:
        //    details = new FragmentSuggest();
            break;
        case 1:
         //   details = new FragmentSearch();
            break;
        case 2:
         //   details = new FragmentMine();
            break;
        }

//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.main_details, details);
//        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        ft.commit();
    }
}
