package kr.ac.hs.recipe.recipeDB;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ingredientsData {
    public int RECIPE_ID; // 레시피 코드
    public String IRDNT_NM; // 재료명
    public String IRDNT_CPCTY; // 재료용량
    public int IRDNT_TY_CODE; // 재료타입 코드
    public String IRDNT_TY_NM; // 재료타입명

    //firebaseDB는 객체 단위로 값을 넣을 때 반드시 매개변수가 비어있는 생성자를 요구함
    public ingredientsData() {
    }

    public ingredientsData(int RECIPE_ID, String IRDNT_NM, String IRDNT_CPCTY, int IRDNT_TY_CODE, String IRDNT_TY_NM) {
        this.RECIPE_ID = RECIPE_ID;
        this.IRDNT_NM = IRDNT_NM;
        this.IRDNT_CPCTY = IRDNT_CPCTY;
        this.IRDNT_TY_CODE = IRDNT_TY_CODE;
        this.IRDNT_TY_NM = IRDNT_TY_NM;
    }
}
