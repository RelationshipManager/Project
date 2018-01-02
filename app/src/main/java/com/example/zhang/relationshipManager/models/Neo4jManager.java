package com.example.zhang.relationshipManager.models;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Neo4jManager extends DatabaseHelper {
    private static final String LOCAL_USER_ID = "local_user_id";
    private static final String PASSWORD = "password";
    private static final String RS_FRIENDS = "Friends";
    private static final String RS_COLLEAGUES = "Colleagues";
    private static final String NODE_CONTACT = "Contact";
    private static final String NODE_VIRTUAL_CONTACT = "VirtualContact";
    private static final String REQUEST_URL = "http://10.0.2.2:11001/db/data/cypher";
    private static final String POST_JSON_URL = "http://10.0.2.2:11001/db/data/transaction/commit";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static Neo4jManager sNeo4jManager;

    private ConnectivityManager mConnectivityManager;

    //获取单例
    static public Neo4jManager getInstance(Context context) {
        if (sNeo4jManager == null){
            sNeo4jManager = new Neo4jManager(context);
        }
        return sNeo4jManager;
    }

    //获取手机号对应用户的id，需要在子线程中运行
    public ArrayList<Integer> getUserId(String phoneNum) throws Exception{
        ArrayList<Integer> resultIds = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();
        String cypherOperator = "match(user:" + NODE_CONTACT +
                ") where user." + CONTACT_PHONE_NUM + "=" + phoneNum +
                " return ID(user)";
        RequestBody requestBody = new FormBody.Builder()
                .add("query", cypherOperator)
                .build();
        Request request = new Request.Builder()
                .url(REQUEST_URL)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        if (response.code() != 200)
            throw new Exception("rest error");
        JSONObject body = new JSONObject(response.body().string());
        JSONArray data = body.getJSONArray("data");
        for(int i=0; i<data.length(); i++)
            resultIds.add(data.getJSONArray(i).getInt(0));
        return resultIds;
    }

    //获取手机号对应用户的id，需要在子线程中运行
    public int logIn(String phoneNum, String passwd) throws Exception{
        int resultId = -1;
        OkHttpClient client = new OkHttpClient();
        String cypherOperator = "match(user:" + NODE_CONTACT +
                ") where user." + CONTACT_PHONE_NUM + "=" + phoneNum +
                " and user." + PASSWORD + "=\"" + passwd +
                "\" return ID(user)";
        RequestBody requestBody = new FormBody.Builder()
                .add("query", cypherOperator)
                .build();
        Request request = new Request.Builder()
                .url(REQUEST_URL)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        if (response.code() != 200)
            throw new Exception("rest error");
        JSONObject body = new JSONObject(response.body().string());
        JSONArray data = body.getJSONArray("data");
        if (data.length() >= 1)
            resultId = data.getJSONArray(0).getInt(0);
        return resultId;
    }

    //注册对应手机号的用户，会保存注册节点的id，如果失败则返回-1，需要在子线程中运行
    public void registerUser(Contact contact, String passwd)throws Exception{
        OkHttpClient client = new OkHttpClient();
        String cypherOperator = "create(user:" + NODE_CONTACT +"{" + CONTACT_PHONE_NUM + ":" + contact.getPhoneNumber() + "," +
                PASSWORD + ":\"" + passwd + "\"})" +
                "return ID(user)";
        RequestBody requestBody = new FormBody.Builder()
                .add("query", cypherOperator)
                .build();
        Request request = new Request.Builder()
                .url(REQUEST_URL)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        if (response.code() != 200)
            throw new Exception("rest error");
        JSONObject body = new JSONObject(response.body().string());
        JSONArray data = body.getJSONArray("data");
        int userId = data.getJSONArray(0).getInt(0);
        User.getInstance(null).setUserId(userId);

        //更新联系人本地手机号
        SQLiteDatabase db = getWritableDatabase();
        //构造联系人的值
        ContentValues values = new ContentValues();
        values.put(CONTACT_PHONE_NUM,contact.getPhoneNumber());
        db.update(CONTACT, values, CONTACT_ID + "=?", new String[]{String.valueOf(contact.getId())});

        //添加虚联系人
        addContact(contact);
    }

    //查询联系人的对应类型的关系,需要在子线程中调用
    public String searchRsByType(Contact contact, int type) throws IOException {
        if (!isConnected())
            return "";
        //获取关系类型
        String typeStr;
        switch (type){
            case RsType.FRIENDS:
                typeStr = RS_FRIENDS;break;
            case RsType.COLLEAGUES:
                typeStr = RS_COLLEAGUES;break;
            default:
                return "";
        }
        String result = "";
        String cypherOperator = "match path = (contact:" + NODE_VIRTUAL_CONTACT +
                ")-[:" + typeStr + "*]-(:" + NODE_VIRTUAL_CONTACT +
                ")where contact." + LOCAL_USER_ID + " = "+ User.getInstance(null).getUserId() +
                " and contact." + CONTACT_ID + "=" + contact.getId() +
                " return path";

        Response response = sendSearchPost(cypherOperator);
        if (response != null && response.code() == 200)
                result = response.body().string();

        return result;
    }

    //查询两个人之间的所有关系，需要在子线程中调用
    public String searchRsP2P(Contact contact1, Contact contact2) throws IOException {
        if (!isConnected())
            return "";

        String result = "";
        String cypherOperator = "match path = (a:" + NODE_VIRTUAL_CONTACT +
                ")-[*]-(b:" + NODE_VIRTUAL_CONTACT +
                ")where a." + LOCAL_USER_ID + " = "+ User.getInstance(null).getUserId() +
                " and b." + LOCAL_USER_ID + " = "+ User.getInstance(null).getUserId() +
                " and a." + CONTACT_ID + "=" + contact1.getId() +
                " and b." + CONTACT_ID + "=" + contact2.getId() +
                " return path";

        Response response = sendSearchPost(cypherOperator);
        if (response != null && response.code() == 200)
            result = response.body().string();

        return result;
    }

    //同步本地数据到服务器，不需要在子线程中调用
    public void synchronize(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db = getReadableDatabase();
                ArrayList<String> failedOperatorList = new ArrayList<>();
                Cursor cursor = db.query(CL, null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        String operator = cursor.getString(cursor.getColumnIndex(CL_OPERATOR));
                        if (!sendOperator(operator))
                            failedOperatorList.add(operator);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                db.delete(CL,null,null);
                ContentValues values = new ContentValues();
                for (String operator :
                        failedOperatorList) {
                    values.put(CL_OPERATOR,operator);
                    db.insert(CL,null,values);
                }
            }
        }).start();

    }

    //下面是添加、修改、删除联系人以及添加、删除关系，直接调用即可，不需要开子线程
    void addContact(Contact contact){
        String prop = LOCAL_USER_ID + ":" + User.getInstance(null).getUserId() + "," +
                CONTACT_ID + ":" + contact.getId() + "," +
                CONTACT_PHONE_NUM + ":" + contact.getPhoneNumber() + "," +
                CONTACT_NAME + ":\"" + contact.getName() + "\"," +
                CONTACT_AGE + ":" + contact.getAge() + "," +
                CONTACT_SEX + ":" + contact.getSex() + "," +
                CONTACT_NOTE + ":\"" + contact.getNotes() + "\"";
        String cypher = "create(:" + NODE_VIRTUAL_CONTACT + "{"+ prop +"})";
        execOperator(cypher);
    }

    void updateContact(Contact contact){
        String prop = "target." + CONTACT_NAME + "=\"" + contact.getName() + "\"," +
                "target." + CONTACT_AGE + "=" + contact.getAge() + "," +
                "target." + CONTACT_PHONE_NUM + "=" + contact.getPhoneNumber() + "," +
                "target." + CONTACT_SEX + "=" + contact.getSex() + "," +
                "target." + CONTACT_NOTE + "=\"" + contact.getNotes() + "\"";
        String cypher = "match(target:" + NODE_VIRTUAL_CONTACT + ") where target." + LOCAL_USER_ID + " = " +
                User.getInstance(null).getUserId() + " and target." + CONTACT_ID + "=" + contact.getId() +
                " set "+ prop;
        execOperator(cypher);
    }

    void removeContact(Contact contact){
        String cypher = "match(target:" + NODE_VIRTUAL_CONTACT + ") where target." + LOCAL_USER_ID + " = " +
                User.getInstance(null).getUserId() + " and target." + CONTACT_ID + "=" + contact.getId() +
                " delete target";
        execOperator(cypher);
    }

    void addRelationship(Contact startContact, Contact endContact, RsType rsType){
        String rsLabel = "Unknown";
        switch (rsType.getRelationshipType()){
            case RsType.COLLEAGUES:
                rsLabel = "Colleagues";
                break;
            case RsType.FRIENDS:
                rsLabel = "Friends";
                break;
        }

        if (!rsLabel.equals("Unknown")){
            String cypher = "match(startContact:" + NODE_VIRTUAL_CONTACT + "),(endContact:" + NODE_VIRTUAL_CONTACT + ")"+
                    " where startContact." + LOCAL_USER_ID + " = "+ User.getInstance(null).getUserId() +
                    " and endContact." + LOCAL_USER_ID + " = "+ User.getInstance(null).getUserId() +
                    " and startContact." + CONTACT_ID + "=" + startContact.getId() +
                    " and endContact." + CONTACT_ID + "=" + endContact.getId() +
                    " create(startContact)-[:" + rsLabel + "{start_role:\"" + rsType.getStartRole() +
                    "\", end_role:\"" + rsType.getEndRole() + "\"}]->(endContact)";
            execOperator(cypher);
        }
    }

    void removeRelationship(Relationship relationship){
        String rsLabel = "Unknown";
        switch (relationship.getRsType().getRelationshipType()){
            case RsType.COLLEAGUES:
                rsLabel = "Colleagues";
                break;
            case RsType.FRIENDS:
                rsLabel = "Friends";
                break;
        }

        if (!rsLabel.equals("Unknown")){
            String cypher = "match(startContact:" + NODE_VIRTUAL_CONTACT + ")-[target:" + rsLabel + "]->(endContact:" + NODE_VIRTUAL_CONTACT + ")" +
                    " where startContact." + LOCAL_USER_ID + " = "+ User.getInstance(null).getUserId() +
                    " and endContact." + LOCAL_USER_ID + " = "+ User.getInstance(null).getUserId() +
                    " and startContact." + CONTACT_ID + "=" + relationship.getStartContact().getId() +
                    " and endContact." + CONTACT_ID + "=" + relationship.getEndContact().getId() +
                    " delete target";
            execOperator(cypher);
        }
    }

    void removeRelationships(Contact contact){
        String cypher = "match(:" + NODE_VIRTUAL_CONTACT + ")-[target:]-(contact:" + NODE_VIRTUAL_CONTACT + ")" +
                " where contact." + LOCAL_USER_ID + " = "+ User.getInstance(null).getUserId() +
                " and contact." + CONTACT_ID + "=" + contact.getId() +
                " delete target";
        execOperator(cypher);
    }


    //构造函数
    private Neo4jManager(Context context) {
        super(context);
        mConnectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    //以json向transaction接口发送查找请求
    private Response sendSearchPost(String searchOperator){
        String requestJson = "{\"statements\" : [ {    \"statement\" : \"" + searchOperator + "\", " +
                "\"resultDataContents\":[\"graph\"]} ]}";
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, requestJson);
        Request request = new Request.Builder()
                .url(POST_JSON_URL)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //调用即可开启子线程执行操作
    private void execOperator(String cypherOperator){
        new Thread(() -> {
            if (!sendOperator(cypherOperator))
                saveOperator(cypherOperator);
        }).start();
    }

    //发送操作,必须在子线程中进行
    private boolean sendOperator(String cypherOperator){
        if (!isConnected())
            return false;
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("query", cypherOperator)
                .build();
        Request request = new Request.Builder()
                .url(REQUEST_URL)
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.code() != 200)
                return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //保存语句到本地数据库
    private void saveOperator(String cypherOperator){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CL_OPERATOR, cypherOperator);
        db.insert(CL, null, values);
    }

    //判断是否有网络链接
    private boolean isConnected(){
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP){//api小于23
            //获取WIFI连接的信息
            NetworkInfo wifiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            //获取移动数据连接的信息
            NetworkInfo dataNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            return wifiNetworkInfo.isConnected() || dataNetworkInfo.isConnected();

        }else {//api大于等于23
            NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
            return networkInfo != null;

        }
    }
}
