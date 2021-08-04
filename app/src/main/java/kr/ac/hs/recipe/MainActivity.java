package kr.ac.hs.recipe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import kr.ac.hs.recipe.recipeDB.ingredientsData;
import kr.ac.hs.recipe.recipeDB.recipeData;
import kr.ac.hs.recipe.recipeDB.stepData;

public class MainActivity extends AppCompatActivity {

    String key = "1c74fe1f5913c684ec9bb14cc1dd45295904903af4c2012cb985cb757b1a322e";
    int start, end;
    int v_RECIPE_ID, v_NATION_CODE, v_TY_CODE, v_IRDNT_TY_CODE, v_COOKING_NO;
    String v_RECIPE_NM_KO, v_SUMRY, v_NATION_NM, v_TY_NM, v_COOKING_TIME, v_CALORIE, v_QNT, v_LEVEL_NM, v_IRDNT_CODE, v_IMG_URL, v_DET_URL, v_IRDNT_NM, v_IRDNT_CPCTY, v_IRDNT_TY_NM, v_COOKING_DC, v_STRE_STEP_IMAGE_URL, v_STEP_TIP;

    // Write a message to the database
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference recipeDBRef = myRef.child("recipeDB");
    recipeData recipeData = null; // 레시피 기본정보
    ingredientsData ingredientsData = null; // 레시피 재료정보
    stepData stepData = null; // 레시피 과정정보

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //myRef.removeValue(); // drop all DB

        // 앱 최초 실행 여부 판단
        SharedPreferences pref = getSharedPreferences("isFirst", Activity.MODE_PRIVATE);
        boolean first = pref.getBoolean("isFirst", false);
        if (!first) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isFirst", true);
            editor.apply();
            updateData(); // 앱 최초 실행 시 데이터 갱신
        }

        // 최초 실행 이후, 일주일에 한 번만 데이터 갱신
        Calendar calendar = Calendar.getInstance(); // 오늘 날짜
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) { // 일요일에만 데이터 업데이트 (주기 설정)
            updateData();
        }

        // Navigation 구성
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_recipe, R.id.navigation_search, R.id.navigation_community)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    // 데이터 update
    public void updateData() {
        Thread getData = new getData();
        Thread getData_IRDNT = new getData_IRDNT();
        Thread getData_STEP = new getData_STEP();

        getData.start(); // 레시피 기본정보
        try {
            getData.join(); // getData가 종료될 때까지 기다림
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        getData_IRDNT.start(); // 레시피 재료정보
        try {
            getData_IRDNT.join(); // getData_IRDNT가 종료될 때까지 기다림
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        getData_STEP.start(); // 레시피 과정정보
    }

    // Open API Json Parsing
    // 레시피 기본정보
    public void JsonParse(String str) {
        try {
            JSONObject obj = new JSONObject(str);
            JSONArray rows = obj.getJSONObject("Grid_20150827000000000226_1").getJSONArray("row");
            for (int i = 0; i < rows.length(); i++) {
                JSONObject jObject = rows.getJSONObject(i);
                v_RECIPE_ID = jObject.getInt("RECIPE_ID");
                v_RECIPE_NM_KO = jObject.getString("RECIPE_NM_KO");
                v_SUMRY = jObject.getString("SUMRY");
                v_NATION_CODE = jObject.getInt("NATION_CODE");
                v_NATION_NM = jObject.getString("NATION_NM");
                v_TY_CODE = jObject.getInt("TY_CODE");
                v_TY_NM = jObject.getString("TY_NM");
                v_COOKING_TIME = jObject.getString("COOKING_TIME");
                v_CALORIE = jObject.getString("CALORIE");
                v_QNT = jObject.getString("QNT");
                v_LEVEL_NM = jObject.getString("LEVEL_NM");
                v_IRDNT_CODE = jObject.getString("IRDNT_CODE");
                v_IMG_URL = jObject.getString("IMG_URL");
                v_DET_URL = jObject.getString("DET_URL");

                //firebase에 data input
                recipeData = new recipeData(v_RECIPE_ID, v_RECIPE_NM_KO, v_SUMRY, v_NATION_CODE, v_NATION_NM, v_TY_CODE, v_TY_NM, v_COOKING_TIME, v_CALORIE, v_QNT, v_LEVEL_NM, v_IRDNT_CODE, v_IMG_URL, v_DET_URL);
                recipeDBRef.child("recipe_ID").child(String.valueOf(v_RECIPE_ID)).setValue(recipeData);
            }
        } catch (Exception e) {
        }
    }

    // 레시피 재료정보
    // 아니 어이가 없네!!!!!!! [ ] < 이거 json 파싱할때 특수문자 오류나나봄!!!!!! 해결하기
    // record 자르기 전에 "rows.getJSONObject(i);"를 replace 한 후에 jObject에 넣기 [대책1] 
    // replace 하거나... 다른 방법 생각해보기
    public void JsonParse_IRDNT(String str) {
        try {
            JSONObject obj = new JSONObject(str);
            JSONArray rows = obj.getJSONObject("Grid_20150827000000000227_1").getJSONArray("row");
            for (int i = 0; i < rows.length(); i++) {
                JSONObject jObject = rows.getJSONObject(i);
                v_RECIPE_ID = jObject.getInt("RECIPE_ID");
                v_IRDNT_NM = jObject.getString("IRDNT_NM");
                v_IRDNT_CPCTY = jObject.getString("IRDNT_CPCTY");
                v_IRDNT_TY_CODE = jObject.getInt("IRDNT_TY_CODE");
                v_IRDNT_TY_NM = jObject.getString("IRDNT_TY_NM");

                //firebase에 data input
                ingredientsData = new ingredientsData(v_RECIPE_ID, v_IRDNT_NM, v_IRDNT_CPCTY, v_IRDNT_TY_CODE, v_IRDNT_TY_NM);
                recipeDBRef.child("recipe_ID").child(String.valueOf(v_RECIPE_ID)).child("IRDNT_LIST").child(String.valueOf(v_IRDNT_NM)).setValue(ingredientsData);
            }
        } catch (Exception e) {
        }
    }

    // 레시피 과정정보
    public void JsonParse_STEP(String str) {
        try {
            JSONObject obj = new JSONObject(str);
            JSONArray rows = obj.getJSONObject("Grid_20150827000000000228_1").getJSONArray("row");
            for (int i = 0; i < rows.length(); i++) {
                JSONObject jObject = rows.getJSONObject(i);
                v_RECIPE_ID = jObject.getInt("RECIPE_ID");
                v_COOKING_NO = jObject.getInt("COOKING_NO");
                v_COOKING_DC = jObject.getString("COOKING_DC");
                v_STRE_STEP_IMAGE_URL = jObject.getString("STRE_STEP_IMAGE_URL");
                v_STEP_TIP = jObject.getString("STEP_TIP");

                //firebase에 data input
                stepData = new stepData(v_RECIPE_ID, v_COOKING_NO, v_COOKING_DC, v_STRE_STEP_IMAGE_URL, v_STEP_TIP);
                recipeDBRef.child("recipe_ID").child(String.valueOf(v_RECIPE_ID)).child("STEP").child(String.valueOf(v_COOKING_NO)).setValue(stepData);
            }
        } catch (Exception e) {
        }
    }

    // 레시피 기본정보
    class getData extends Thread {
        public void run() {
            //http://211.237.50.150:7080/openapi/1c74fe1f5913c684ec9bb14cc1dd45295904903af4c2012cb985cb757b1a322e/json/Grid_20150827000000000226_1/1/10
            String queryUrl = "http://211.237.50.150:7080/openapi/" + key + "/json/Grid_20150827000000000226_1/1/537";
            try {
                URL url = new URL(queryUrl);
                StringBuilder sb = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                String result;
                while ((result = br.readLine()) != null) {
                    sb.append(result + "\n");
                }
                result = sb.toString();
                JsonParse(result);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 레시피 재료정보
    class getData_IRDNT extends Thread {
        public void run() {
            start = 0;
            end = 0; // 초기화
            //http://211.237.50.150:7080/openapi/1c74fe1f5913c684ec9bb14cc1dd45295904903af4c2012cb985cb757b1a322e/json/Grid_20150827000000000227_1/1/6104
            for (start = 1; start < 6104; start += 1000) {
                end = start + 999; // 한 번에 1000개씩 호출 가능
                String queryUrl = "http://211.237.50.150:7080/openapi/" + key + "/json/Grid_20150827000000000227_1/" + start + "/" + end;
                try {
                    URL url = new URL(queryUrl);
                    StringBuilder sb = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                    String result;
                    while ((result = br.readLine()) != null) {
                        sb.append(result + "\n");
                    }
                    result = sb.toString();
                    JsonParse_IRDNT(result);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 레시피 과정정보
    class getData_STEP extends Thread {
        public void run() {
            start = 0;
            end = 0; // 초기화
            //http://211.237.50.150:7080/openapi/1c74fe1f5913c684ec9bb14cc1dd45295904903af4c2012cb985cb757b1a322e/json/Grid_20150827000000000228_1/1/5
            for (start = 1; start < 3022; start += 1000) {
                end = start + 999; // 한 번에 1000개씩 호출 가능
                String queryUrl = "http://211.237.50.150:7080/openapi/" + key + "/json/Grid_20150827000000000228_1/" + start + "/" + end;
                try {
                    URL url = new URL(queryUrl);
                    StringBuilder sb = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                    String result;
                    while ((result = br.readLine()) != null) {
                        sb.append(result + "\n");
                    }
                    result = sb.toString();
                    JsonParse_STEP(result);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}