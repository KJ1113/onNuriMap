package com.example.Onnuri_Alerts.Android_Class.menu_FragmentUse_Class;

import android.view.View;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Onnuri_Alerts.R;

public class ViewHolderPage extends RecyclerView.ViewHolder {
    private ImageView imageView;
    ImgDataPage data;
    public ViewHolderPage(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageView);
    }
    public void onBind(ImgDataPage data){
        this.data = data;
        imageView.setImageResource(data.getImg());
    }
}
