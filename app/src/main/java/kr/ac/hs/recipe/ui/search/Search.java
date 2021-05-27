package kr.ac.hs.recipe.ui.search;

import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import kr.ac.hs.recipe.R;

public class Search extends Fragment {

    EditText searchText;
    LinearLayout classification, searchList;
    ListView listView;
    CustomAdapter adapter;
    Query sortbyKNM;
    List list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search, container, false);
        LayoutInflater inf = getLayoutInflater();

        // 검색
        searchText = v.findViewById(R.id.searchText);
        classification = v.findViewById(R.id.classification_layout);
        searchList = v.findViewById(R.id.searchlist_layout);
        sortbyKNM = FirebaseDatabase.getInstance().getReference().child("recipe_ID").orderByChild("RECIPE_NM_KO");

        // 검색목록
        listView = v.findViewById(R.id.searchlist);
        adapter = new CustomAdapter();
        listView.setAdapter(adapter);

        // 검색 기능
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
            }
        });

        // 검색 결과 표시 기능
        sortbyKNM.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                list.clear();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


        // for ~ 
        // 버튼 디자인
        /*LayoutInflater inf = getLayoutInflater();
        View layout = inf.inflate(R.layout.classification_button_layout, null);
        TextView classification_text = layout.findViewById(R.id.classification_text);
        classification_text.setText("DB에서 가져온 분류명... ");*/
        
        // 버튼 눌렀을 때 > 해당 목록 리스트

        // 검색 목록 내용 임시 리스트
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_mainimg), "레시피명1", "간략소개1") ;
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_mainimg), "레시피명2", "간략소개2") ;
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_mainimg), "레시피명3", "간략소개3") ;
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_mainimg), "레시피명3", "간략소개3") ;
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_mainimg), "레시피명3", "간략소개3") ;
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_mainimg), "레시피명3", "간략소개3") ;
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_mainimg), "레시피명3", "간략소개3") ;
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_mainimg), "레시피명3", "간략소개3") ;
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_mainimg), "레시피명3", "간략소개3") ;
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_mainimg), "레시피명3", "간략소개3") ;
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_mainimg), "레시피명3", "간략소개3") ;
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_mainimg), "레시피명3", "간략소개3") ;
        adapter.addItem(ContextCompat.getDrawable(getActivity(), R.drawable.ic_mainimg), "레시피명3", "간략소개3") ;

        // 목록 눌렀을 때 > 레시피 세부 페이지


        return v;
    }
}