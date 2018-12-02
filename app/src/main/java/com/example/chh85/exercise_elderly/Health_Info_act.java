package com.example.chh85.exercise_elderly;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.tensorflow.Graph;
import org.tensorflow.Operation;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;

public class Health_Info_act extends AppCompatActivity {
    UserDB userdb;
    private static final String INPUT_NODE = "my_input/X"; // input tensor name
    private static final String OUTPUT_NODE = "my_output/Sigmoid"; // output tensor name
    private static final String[] OUTPUT_NODES = {"my_output/Sigmoid"};
    private static final int OUTPUT_SIZE = 8; // number of classes
    private static final int INPUT_SIZE = 4; // size of the input
    float[] result = new float[OUTPUT_SIZE]; // get the output probabilities for each class
    float gender, age, height, weight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health__info_act);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("건강 정보");
        TextView diseases_tv = (TextView) findViewById(R.id.health_info_disease_tv);
        TextView disease_info_tv = (TextView) findViewById(R.id.disease_info_tv);
        TextView badfood_info_tv = (TextView) findViewById(R.id.badfood_info_tv);
        TextView goodfood_info_tv2 = (TextView) findViewById(R.id.goodfood_info_tv2);
        ArrayList<String> user_info;
        userdb = new UserDB(this,"user_db",null,1);
        user_info=userdb.readDB(userdb);
        if(user_info.get(2).equals("m")){
            gender=1.0f;
        } else if(user_info.get(2).equals("f")){
            gender=2.0f;
        }
        int tmpage = Integer.parseInt(user_info.get(1));
        switch (tmpage/10){
            case 6: age=9.5f;
                    break;
            case 7: age= 11.5f;
                    break;
            case 8: age= 13.5f;
                    break;
            case 9: age=15.5f;
                     break;
        }

        height= Float.parseFloat(user_info.get(3));
        weight= Float.parseFloat(user_info.get(4));
        //float user_inputsrc[] = new float[] {1f,12f,173f,76f};
        float user_inputsrc[] = new float[] {gender,age,height,weight};
        int diseases [] ={0,0,0};   //0: 고지혈증, 1: 당뇨, 3: 고혈압
        int result = pyModel(user_inputsrc);
        System.out.println(result);
        String disease_tv_str = " ";
        switch (result){
            case 1:diseases[0]+=1;
            break;
            case 2: diseases[1]+=1;
            break;
            case 3: diseases[0] +=1;
                    diseases[1]+=1;
            break;
            case 4: diseases[2] +=1;
            break;
            case 5: diseases[2] +=1;
                    diseases[0] +=1;
                    break;
            case 6: diseases[2] +=1;
                    diseases[1] +=1;
                    break;
            case 7: diseases[1]+=1;
                    diseases[0]+=1;
                    diseases[2]+=1;
                    break;
            default: break;
        }

        if(diseases[0]>0){
            disease_tv_str+="고지혈증";
            disease_info_tv.setText("고지혈증은 혈중 지방이 필요 이상으로 높아진 상태입니다. " +
                    "고지혈증은 동맥경화, 협심증, 심근경색, 뇌졸중 등의 원인이 될 수 있습니다.");
            badfood_info_tv.setText("육류 기름, 프림, 라면, 마가린, 과자, 아이스크림, 계란 노른자, 새우, 오징어, 굴, 버터, 전복");
            goodfood_info_tv2.setText("버섯, 도라지, 당근, 계란 흰자, 연근, 미역, 다시마, 콩, 두부, 등 푸른 생선");
        }
        if(diseases[1]>0){
            disease_tv_str+="당뇨병";
        }
        if(diseases[2]>0){
            disease_tv_str+= "고혈압";
        }
        diseases_tv.setText(disease_tv_str);


    }

    public int pyModel(float inputsrc []){
        TensorFlowInferenceInterface tf = new TensorFlowInferenceInterface(getAssets(),"frozen_model.pb");
        Graph graph = tf.graph();
        for (Iterator<Operation> it = graph.operations(); it.hasNext(); ) {
            System.out.println(it.toString());
            System.out.println(it.next());
        }

        System.out.println(graph.operations().toString());

        tf.feed(INPUT_NODE,inputsrc,1,INPUT_SIZE);

        tf.run(OUTPUT_NODES);
        tf.fetch(OUTPUT_NODE, result);
        int maxResult = 0;
        for(int i=0; i<8;i++){
            if(result[i]>maxResult){
                maxResult = i;
            }
            System.out.println(result[i]);
        }
        return maxResult;
    }
}
