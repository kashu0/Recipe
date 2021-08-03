package kr.ac.hs.recipe.ui.search;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kr.ac.hs.recipe.MainActivity;
import kr.ac.hs.recipe.R;
import kr.ac.hs.recipe.recipeDB.ingredientsData;
import kr.ac.hs.recipe.recipeDB.recipeData;

public class Search extends Fragment {

    EditText searchText;
    LinearLayout classification, searchList;
    ListView listView;
    CustomAdapter adapter;
    ToggleButton searchToggle;
    int toggle = 0;

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference recipeDBRef = myRef.child("recipeDB");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search, container, false);
        LayoutInflater inf = getLayoutInflater();

        // 검색
        searchText = v.findViewById(R.id.searchText);
        classification = v.findViewById(R.id.classification_layout);
        searchList = v.findViewById(R.id.searchlist_layout);

        // 검색목록
        listView = v.findViewById(R.id.searchlist);
        adapter = new CustomAdapter();
        listView.setAdapter(adapter);

        // 검색 토글 (메뉴 <> 재료)
        searchToggle = v.findViewById(R.id.searchToggle);

        searchToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchToggle.isChecked()){
                    toggle = 0; //메뉴명으로 검색하기
                } else {
                    toggle = 1; //재료명으로 검색하기
                }
            }
        });

        // 검색 기능
        // 1. 키보드 자판을 내려야 검색 결과가 출력되는 문제!! 해결하기!!
        // 2. 이미지 링크 > 이미지 (어케함?????????)
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                adapter.clear(); // 검색 결과 목록 초기화
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    if (toggle == 0) { //메뉴명으로 검색하기
                        Toast.makeText(getActivity(), "메뉴명에 " +  searchText.getText() + "(이)가 포함된 검색 결과입니다.", Toast.LENGTH_LONG).show();
                        recipeDBRef.child("recipe_ID").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                    try {
                                        recipeData getResult = postSnapshot.getValue(recipeData.class);
                                        if (getResult.RECIPE_NM_KO.contains(String.valueOf(searchText.getText()))) { // 검색 내용이 포함된 메뉴만 반환

                                            //String[] result = new String[]{getResult.RECIPE_NM_KO, getResult.SUMRY, getResult.IMG_URL};
                                            /*try {
                                                adapter.addItem(getResult.IMG_URL, getResult.RECIPE_NM_KO, getResult.SUMRY);
                                            } catch (Exception e) {
                                                //adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_mainimg), getResult.RECIPE_NM_KO, getResult.SUMRY);
                                            }*/
                                            adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_mainimg), getResult.RECIPE_NM_KO, getResult.SUMRY);
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {

                            }
                        });
                    }
                    else { //재료명으로 검색하기
                        Toast.makeText(getActivity(), "재료에 " +  searchText.getText() + "(이)가 포함된 검색 결과입니다.", Toast.LENGTH_LONG).show();
                        recipeDBRef.child("recipe_ID").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                    try {
                                        for (DataSnapshot postSnapshot2 : postSnapshot.child("IRDNT_LIST").getChildren()) { // 재료 검색
                                            try {
                                                ingredientsData getResult = postSnapshot2.getValue(ingredientsData.class);
                                                if (getResult.IRDNT_NM.contains(String.valueOf(searchText.getText()))) { // 검색 내용(재료)이 포함된 메뉴만 반환

                                                    // 해당 재료가 포함된 메뉴의 레시피 ID
                                                    recipeDBRef.child("recipe_ID").child(String.valueOf(getResult.RECIPE_ID)).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot snapshot) {
                                                            try {
                                                                recipeData getResult2 = postSnapshot.getValue(recipeData.class);

                                                                //String[] result = new String[]{getResult2.RECIPE_NM_KO, getResult2.SUMRY, getResult2.IMG_URL};
                                                                adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_mainimg), getResult2.RECIPE_NM_KO, getResult2.SUMRY);

                                                            } catch (Exception e) {
                                                            }

                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError error) {

                                                        }
                                                    });
                                                }
                                            } catch (Exception e) {
                                            }
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {

                            }


/*        // 검색 기능
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    Toast.makeText(getActivity(), "search : " + searchText.getText(), Toast.LENGTH_LONG).show();

                    // < 여기에 : if (검색 결과가 존재한다면) 추가하기
                    classification.setVisibility(View.INVISIBLE);
                    searchList.setVisibility(View.VISIBLE);

                    handled = true;
                }
                return handled;
            }*/
                        });
                    }

                    classification.setVisibility(View.INVISIBLE);
                    searchList.setVisibility(View.VISIBLE);

                    handled = true;
                }
                return handled;
            }

        });

        // 분류 버튼 눌렀을 때 > 해당 목록 리스트

        // 목록 눌렀을 때 > 레시피 세부 페이지


        return v;
    }
}