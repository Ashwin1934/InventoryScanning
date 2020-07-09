package com.example.scanner;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ViewItems extends Fragment {

    TextView t1;
    TextView t2;
    TextView t3;
    TextView t4;
    Button returnButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_items, container, false);

        t1 = (TextView)view.findViewById(R.id.item1);
        t2 = (TextView)view.findViewById(R.id.item2);
        t3 = (TextView)view.findViewById(R.id.item3);
        t4 = (TextView)view.findViewById(R.id.item4);
        returnButton = (Button)view.findViewById(R.id.backButton);


        return view;
    }
}
