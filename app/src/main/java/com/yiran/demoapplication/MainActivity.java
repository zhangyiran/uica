package com.yiran.demoapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private boolean isCopy;

    private View saveJsonButton;
    private String cachePath;
    private EditText robotId;
    private EditText serverIp;
    private EditText serverPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        robotId = (EditText)findViewById(R.id.robot_id);
        serverIp = (EditText) findViewById(R.id.server_ip);
        serverPort = (EditText)findViewById(R.id.server_port);
        
        saveJsonButton = findViewById(R.id.button);
        saveJsonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveJson();

            }
        });


    }

    private void saveJson() {
        JSONObject data = new JSONObject();
        String robotIdStr = null;
        String serverIpStr = null;
        String serverPortStr = null;
        if(!TextUtils.isEmpty(robotId.getText())) {
            robotIdStr = robotId.getText().toString().trim();
        }
        if(!TextUtils.isEmpty(serverIp.getText())) {
            serverIpStr = serverIp.getText().toString().trim();
        }
        if(!TextUtils.isEmpty(serverPort.getText())) {
            serverPortStr = serverPort.getText().toString().trim();
        }

        if (TextUtils.isEmpty(robotIdStr)) {
            Toast.makeText(this,"机器人ID不能为空",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(serverIpStr)) {
            Toast.makeText(this,"服务器IP不能为空",Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(serverPortStr)) {
            Toast.makeText(this,"服务端口不能为空",Toast.LENGTH_SHORT).show();
        } else {
            try {
                data.put("robot_id",robotIdStr);
                data.put("server_ip",serverIpStr);
                data.put("server_port",serverPortStr);

                CommonUtil.writeFileToSD("uica.json", data.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }



}

