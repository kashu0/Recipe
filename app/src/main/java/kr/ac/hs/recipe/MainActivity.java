package kr.ac.hs.recipe;

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

import kr.ac.hs.recipe.ui.recipeDB.recipeData;

public class MainActivity extends AppCompatActivity {

    String key="1c74fe1f5913c684ec9bb14cc1dd45295904903af4c2012cb985cb757b1a322e";
    int v_RECIPE_ID, v_NATION_CODE, v_TY_CODE;
    String v_RECIPE_NM_KO, v_SUMRY, v_NATION_NM, v_TY_NM, v_COOKING_TIME, v_CALORIE, v_QNT, v_LEVEL_NM, v_IRDNT_CODE, v_IMG_URL, v_DET_URL;
    // Write a message to the database
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference recipeDBRef = myRef.child("recipeDB");
    kr.ac.hs.recipe.ui.recipeDB.recipeData recipeData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // DB 호출
        getData();

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


    // Open API Json Parsing
    // 레시피 기본정보
    public void JsonParse(String str) {
        StringBuffer buffer = new StringBuffer();
        try {
            JSONObject obj = new JSONObject(str);
            JSONArray rows = obj.getJSONObject("Grid_20150827000000000226_1").getJSONArray("row");
            for(int i=0; i < rows.length(); i ++){
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
        }catch (Exception e ){}
    }

    private void getData(){
        new Thread() {
            public void run() {
                String queryUrl="http://211.237.50.150:7080/openapi/" + key + "/json/Grid_20150827000000000226_1/1/537";
                try {
                    URL url = new URL(queryUrl);
                    StringBuilder sb = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
                    String result;
                    while((result = br.readLine())!=null){
                        sb.append(result+"\n");
                    }
                    result = sb.toString();
                    JsonParse(result);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}