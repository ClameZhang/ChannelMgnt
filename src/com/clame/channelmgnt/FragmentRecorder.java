package com.clame.channelmgnt;

import android.app.Application;

public class FragmentRecorder extends Application {
	private String fragmentname; 
    
    @Override  
    public void onCreate(){
        super.onCreate();  
    }

	public String getFragmentname() {
		return fragmentname;
	}

	public void setFragmentname(String fragmentname) {
		this.fragmentname = fragmentname;
	}
}
