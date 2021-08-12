package kr.ac.hs.recipe.ui.search;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import kr.ac.hs.recipe.R;
import kr.ac.hs.recipe.recipeDB.ingredientsData;
import kr.ac.hs.recipe.recipeDB.recipeData;

public class Search extends Fragment {// implements AbsListView.OnScrollListener {

    EditText searchText;
    LinearLayout classification, searchList, searchLoading;
    ImageView backBtn;
    ListView listView;
    ArrayList<kr.ac.hs.recipe.ui.search.ListView> list;
    boolean lastItemVisibleFlag = false; // 화면에 리스트의 마지막 아이템이 보여지는지 체크
    CustomAdapter adapter;
    ToggleButton searchToggle;
    int[] lvBtn = {R.id.lv1, R.id.lv2, R.id.lv3}; // 난이도
    int[] nationBtn = {R.id.nation1, R.id.nation2, R.id.nation3, R.id.nation4, R.id.nation5, R.id.nation6, R.id.nation7}; // 유형분류
    int[] tyBtn =  {R.id.ty1, R.id.ty2, R.id.ty3, R.id.ty4, R.id.ty5, R.id.ty6, R.id.ty7, R.id.ty8, R.id.ty9, R.id.ty10, R.id.ty11, R.id.ty12, R.id.ty13, R.id.ty14, R.id.ty15, R.id.ty16, R.id.ty17}; // 음식분류
    Button[] lvBtnArr = new Button[3];
    Button[] nationBtnArr = new Button[7];
    Button[] tyBtnArr = new Button[17];
    String searchBtnText;
    int toggle = 0, page = 0;
    final int OFFSET = 10; // 데이터 개수
    //ProgressBar progressBar; // 목록 데이터 로딩중
    boolean mLockListView = false; // 데이터 불러올때 중복안되게 하기위한 변수

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference recipeDBRef = myRef.child("recipeDB");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search, container, false);
        LayoutInflater inf = getLayoutInflater();

        searchText = v.findViewById(R.id.searchText);
        classification = v.findViewById(R.id.classification_layout);
        searchList = v.findViewById(R.id.searchlist_layout);
        searchLoading = v.findViewById(R.id.searchLoading_layout);
        backBtn = v.findViewById(R.id.backtoSearchBtn);
        backBtn.setColorFilter(Color.parseColor("#80688A"));
        listView = v.findViewById(R.id.searchlist);
        //progressBar = v.findViewById(R.id.progressbar);
        adapter = new CustomAdapter();

        /*list = new ArrayList<kr.ac.hs.recipe.ui.search.ListView>();
        adapter = new CustomAdapter(getContext(), list);*/


        // 분류버튼 검색 기능
        // 난이도 버튼
        for (int i = 0; i < 3; i++) {
            final int INDEX;
            INDEX = i;
            lvBtnArr[INDEX] = v.findViewById(lvBtn[INDEX]);
            lvBtnArr[INDEX].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.clear(); // 검색 결과 목록 초기화
                    listView.setAdapter(adapter);
                    searchText.clearFocus();

                    classification.setVisibility(View.INVISIBLE);
                    searchLoading.setVisibility(View.VISIBLE);

                    searchBtnText = String.valueOf(lvBtnArr[INDEX].getText());
                    searchText.setText("난이도 분류 : " + searchBtnText);

                    recipeDBRef.child("recipe_ID").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                try {
                                    recipeData getResult = postSnapshot.getValue(recipeData.class);
                                    /*if(adapter.getCount() % 10 != 0 || adapter.getCount() == 0){
                                        if (getResult.LEVEL_NM.equals(searchBtnText)) { // 검색 내용이 포함된 메뉴만 반환
                                            adapter.addItem(getResult.IMG_URL, getResult.RECIPE_NM_KO, getResult.SUMRY);
                                        }
                                    }
                                    else {
                                        listView.setAdapter(adapter);
                                        //progressBar.setVisibility(View.GONE);
                                    }*/
                                    if (getResult.LEVEL_NM.equals(searchBtnText)) { // 검색 내용이 포함된 메뉴만 반환
                                        adapter.addItem(getResult.IMG_URL, getResult.RECIPE_NM_KO, getResult.SUMRY);
                                    }
                                } catch (Exception e) {
                                }
                            }
                            searchLoading.setVisibility(View.INVISIBLE);
                            searchList.setVisibility(View.VISIBLE);
                            listView.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                        }
                    });
                }
            });
            //progressBar.setVisibility(View.GONE);
            //listView.setOnScrollListener((AbsListView.OnScrollListener) this);
            //getItem();
        }

        // 유형분류 버튼
        for (int i = 0; i < 7; i++) {
            final int INDEX;
            INDEX = i;
            nationBtnArr[INDEX] = v.findViewById(nationBtn[i]);
            nationBtnArr[INDEX].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.clear(); // 검색 결과 목록 초기화
                    listView.setAdapter(adapter);
                    searchText.clearFocus();

                    classification.setVisibility(View.INVISIBLE);
                    searchLoading.setVisibility(View.VISIBLE);

                    searchBtnText = String.valueOf(nationBtnArr[INDEX].getText());
                    searchText.setText("유형분류 : " + searchBtnText);

                    recipeDBRef.child("recipe_ID").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                try {
                                    recipeData getResult = postSnapshot.getValue(recipeData.class);
                                    if (getResult.NATION_NAME.equals(searchBtnText)) { // 검색 내용이 포함된 메뉴만 반환
                                        adapter.addItem(getResult.IMG_URL, getResult.RECIPE_NM_KO, getResult.SUMRY);
                                    }
                                } catch (Exception e) {
                                }
                            }
                            searchLoading.setVisibility(View.INVISIBLE);
                            searchList.setVisibility(View.VISIBLE);
                            listView.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {

                        }
                    });
                }
            });
        }

        // 음식분류 버튼
        for (int i = 0; i < 17; i++) {
            final int INDEX;
            INDEX = i;
            tyBtnArr[INDEX] = v.findViewById(tyBtn[i]);
            tyBtnArr[INDEX].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.clear(); // 검색 결과 목록 초기화
                    listView.setAdapter(adapter);
                    searchText.clearFocus();

                    classification.setVisibility(View.INVISIBLE);
                    searchLoading.setVisibility(View.VISIBLE);

                    searchBtnText = String.valueOf(tyBtnArr[INDEX].getText());
                    searchText.setText("음식분류 : " + searchBtnText);

                    recipeDBRef.child("recipe_ID").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                try {
                                    recipeData getResult = postSnapshot.getValue(recipeData.class);
                                    if (getResult.TY_NM.equals(searchBtnText)) { // 검색 내용이 포함된 메뉴만 반환
                                        adapter.addItem(getResult.IMG_URL, getResult.RECIPE_NM_KO, getResult.SUMRY);
                                    }
                                } catch (Exception e) {
                                }
                            }
                            searchLoading.setVisibility(View.INVISIBLE);
                            searchList.setVisibility(View.VISIBLE);
                            listView.setAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {

                        }
                    });
                }
            });
        }


        // 검색창 검색 기능
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

        /*searchText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                searchText.getText().clear();
                classification.setVisibility(View.VISIBLE);
                searchList.setVisibility(View.INVISIBLE);
            }
        });*/

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    listView.setAdapter(adapter);
                    searchText.clearFocus();

                    classification.setVisibility(View.INVISIBLE);
                    searchLoading.setVisibility(View.VISIBLE);

                    if (toggle == 0) { //메뉴명으로 검색하기
                        adapter.clear(); // 검색 결과 목록 초기화
                        recipeDBRef.child("recipe_ID").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                    try {
                                        recipeData getResult = postSnapshot.getValue(recipeData.class);
                                        if (getResult.RECIPE_NM_KO.contains(String.valueOf(searchText.getText()))) { // 검색 내용이 포함된 메뉴만 반환
                                            adapter.addItem(getResult.IMG_URL, getResult.RECIPE_NM_KO, getResult.SUMRY);
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                                searchLoading.setVisibility(View.INVISIBLE);
                                searchList.setVisibility(View.VISIBLE);
                                listView.setAdapter(adapter);
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {

                            }
                        });
                    } else { //재료명으로 검색하기
                        adapter.clear(); // 검색 결과 목록 초기화
                        recipeDBRef.child("recipe_ID").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot snapshot) {
                                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                    try {
                                        recipeData getResult = postSnapshot.getValue(recipeData.class);
                                        for (DataSnapshot postSnapshot2 : postSnapshot.child("IRDNT_LIST").getChildren()) { // 재료 검색
                                            try {
                                                ingredientsData getIRDNT = postSnapshot2.getValue(ingredientsData.class);
                                                // equals가 나을지 contains가 나을지?????
                                                if (getIRDNT.IRDNT_NM.equals(String.valueOf(searchText.getText())) && getResult.RECIPE_ID == getIRDNT.RECIPE_ID) { // 검색 내용(재료)이 포함된 메뉴만 반환
                                                    adapter.addItem(getResult.IMG_URL, getResult.RECIPE_NM_KO, getResult.SUMRY);
                                                }
                                            } catch (Exception e) {
                                            }
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                                searchLoading.setVisibility(View.INVISIBLE);
                                searchList.setVisibility(View.VISIBLE);
                                listView.setAdapter(adapter);
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {

                            }
                        });
                    }
                }

                // 키보드 자동 숨기기
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);

                return true;
            }

        });

        // 목록 눌렀을 때 > 레시피 세부 페이지

        // 분류버튼 목록 페이지로 돌아가는 버튼
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText.getText().clear();
                searchText.clearFocus();

                adapter.clear(); // 검색 결과 목록 초기화
                listView.setAdapter(adapter);

                classification.setVisibility(View.VISIBLE);
                searchList.setVisibility(View.INVISIBLE);
            }
        });


        return v;
    }

/*    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag && mLockListView == false) {
           //progressBar.setVisibility(View.VISIBLE);
            getItem();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // firstVisibleItem : 화면에 보이는 첫번째 리스트의 아이템 번호.
        // visibleItemCount : 화면에 보이는 리스트 아이템의 갯수
        // totalItemCount : 리스트 전체의 총 갯수
        // 리스트의 갯수가 0개 이상이고, 화면에 보이는 맨 하단까지의 아이템 갯수가 총 갯수보다 크거나 같을때.. 즉 리스트의 끝일때. true
        lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
    }

    private void getItem() {
        // 리스트에 다음 데이터를 입력할 동안에 이 메소드가 또 호출되지 않도록 mLockListView 를 true로 설정한다.
        mLockListView = true;

        for(int i = 0; i < 10; i++){

        }

        // 1초 뒤 프로그레스바를 감추고 데이터를 갱신하고, 중복 로딩 체크하는 Lock을 했던 mLockListView변수를 풀어준다.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                page++;
                adapter.notifyDataSetChanged();
                //progressBar.setVisibility(View.GONE);
                mLockListView = false;
            }
        }, 1000);
    }*/

}