package com.example.chh85.exercise_elderly;

import android.widget.CheckBox;
import android.widget.RadioGroup;

/*
    성별, 병력은 라디오 체크, 체크 박스를 사용.
    저장 버튼을 클릭했을 때 체크되어 있는 항목을 문자열로 반환.
 */

public class ischecked {
    public static String RG_checked(RadioGroup rgtemp) {
        String gender_tmp;
        switch (rgtemp.getCheckedRadioButtonId()) {
            case R.id.male:
                gender_tmp = "m";
                break;
            case R.id.female:
                gender_tmp = "f";
                break;
            default:
                gender_tmp = "n";
        }
        return gender_tmp;
    }


    public static String CB_checked(CheckBox hb, CheckBox gj, CheckBox di, CheckBox goji) {
        String disease_tmp="";
        if(hb.isChecked()){
            disease_tmp = disease_tmp.concat("h");
        }
        if(gj.isChecked()){
            disease_tmp=disease_tmp.concat("g");
        }
        if(di.isChecked()){
            disease_tmp=disease_tmp.concat("d");
        }
        if (goji.isChecked()) {

            disease_tmp=disease_tmp.concat("j");
        }

        return disease_tmp;
    }


}
