package com.jm.gon.triphelper;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.jm.gon.triphelper.functionplan2.FunctionPlan2Adapter;
import com.jm.gon.triphelper.functionplan3.Fragment1Adapter;
import com.jm.gon.triphelper.functionplan3.Fragment2Adapter;
import com.jm.gon.triphelper.functionplan3.Fragment3Adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;
import java.util.concurrent.ExecutionException;

/**
 * Created by 김장민 on 2017-02-23.
 */

    //대부분의 기능이 있는 클래스입니다. 뷰와 엑티비티와 모델을 최대한 나누어보고자 했습니다.
public class HttpConnectControl {
    public final String TAG = "HttpConnectControl";
    public final String LOCATIONBASE = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/locationBasedList";
    public final String AREABASE = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList";
    public final String KEYWORD = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/searchKeyword";
    public final String FESTIVAL = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/searchFestival";
    public final String DETAIL = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailCommon";
    public final String DETAILIMAGE = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailImage";

    //결과값을 가지고 있는 리스트입니다.
    private ArrayList<DataModel> modelList;
    //생성자입니다.
    public HttpConnectControl() {
        modelList = new ArrayList<>();
    }
    //오버로딩된 두가지 메소드입니다. 파라메터에 따라 다르게 제공되어지지만 대부분 commonasynctask가 있는 메소드를 사용합니다.
    public void startAsync(Object adapter, String keyword, String[] mapxy) {
        new CommonAsync(adapter).execute(makeUrl(keyword, mapxy));
    }

    //계획을 세우는 화면에서 각각 관광지들 사이 최단거리를 구해 추천해주기 위해 알고리즘이 사용되어있는 메소드입니다.
    public void startAsync(FunctionPlan2Adapter adapter, int date, String city, String keyword, ArrayList<String> userInputSpotList) {
        new getLocation(adapter, date, city, userInputSpotList).execute(keyword);
    }


    //url을 만들기 위해 있는 메소드입니다.
    public String makeUrl(String url, String[] params) {
        StringBuilder urlBuilder = new StringBuilder();
        try {
            switch (url) {
                case LOCATIONBASE:
                    urlBuilder.append(LOCATIONBASE);
                    urlBuilder.append("?" + URLEncoder.encode("mapX", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8"));
                    urlBuilder.append("&" + URLEncoder.encode("mapY", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8"));
                    urlBuilder.append("&" + URLEncoder.encode("radius", "UTF-8") + "=" + URLEncoder.encode("5000", "UTF-8"));
                    break;
                case AREABASE:
                    urlBuilder.append(AREABASE);
                    urlBuilder.append("?" + URLEncoder.encode("areaCode", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8"));
                    break;
                case KEYWORD:
                    urlBuilder.append(KEYWORD);
                    urlBuilder.append("?" + URLEncoder.encode("keyword", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8"));
                    break;
                case FESTIVAL:
                    urlBuilder.append(FESTIVAL);
                    urlBuilder.append("?" + URLEncoder.encode("eventStartDate", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8"));
                    urlBuilder.append("&" + URLEncoder.encode("eventEndDate", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8"));
                    break;
                case DETAIL:
                    urlBuilder.append(DETAIL);
                    urlBuilder.append("?" + URLEncoder.encode("contentId", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8"));
                    urlBuilder.append("&" + URLEncoder.encode("defaultYN", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8"));
                    urlBuilder.append("&" + URLEncoder.encode("firstImageYN", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8"));
                    urlBuilder.append("&" + URLEncoder.encode("areacodeYN", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8"));
                    urlBuilder.append("&" + URLEncoder.encode("catcodeYN", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8"));
                    urlBuilder.append("&" + URLEncoder.encode("addrinfoYN", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8"));
                    urlBuilder.append("&" + URLEncoder.encode("mapinfoYN", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8"));
                    urlBuilder.append("&" + URLEncoder.encode("overviewYN", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8"));
                    urlBuilder.append("&" + URLEncoder.encode("transGuideYN", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8"));
                    break;
                case DETAILIMAGE:
                    urlBuilder.append(DETAILIMAGE);
                    urlBuilder.append("?" + URLEncoder.encode("contentId", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8"));
                    urlBuilder.append("&" + URLEncoder.encode("imageYN", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8"));
            }
            urlBuilder.append("&" + URLEncoder.encode("ServiceKey", "UTF-8") + "=atukOUcyFBF9HGzl%2BxiZLpNPMA9%2FbxkkXpPcyRIqQfXSs3JMNNEkQ3Eosc1aZsRz0u58DKzMDXCpdghsmYpiaQ%3D%3D");
            urlBuilder.append("&" + URLEncoder.encode("listYN", "UTF-8") + "=" + URLEncoder.encode("Y", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("MobileOS", "UTF-8") + "=" + URLEncoder.encode("AND", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("MobileApp", "UTF-8") + "=" + URLEncoder.encode("TripHelper", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("arrange", "UTF-8") + "=" + URLEncoder.encode("B", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("100", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return urlBuilder.toString();
    }

    //http연결을 하고 응답을 받아오는 메소드입니다.
    public String httpConnect(URL url) {
        String httpConnect_Result = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            String line;
            while ((line = rd.readLine()) != null) {
                httpConnect_Result = line;
            }
            rd.close();
            conn.disconnect();

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return httpConnect_Result;
    }
    //json응답중 공통적인부분을 파싱해주는 메소드입니다.
    public Object commonParsing(String parsingStr) {
        try {
            JSONObject jsonObject = new JSONObject(parsingStr);
            JSONObject jsonObject1 = new JSONObject(jsonObject.getString("response"));
            JSONObject jsonObject2 = new JSONObject(jsonObject1.getJSONObject("body").toString());
            JSONObject jsonObject3 = new JSONObject(jsonObject2.getJSONObject("items").toString());
            Object object = jsonObject3.get("item");
            return object;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    //parsing한 결과로 돌아오는 datamodel을 결과list에 추가해주는 메소드입니다.
    public ArrayList<DataModel> parsingJson(Object object, int count) {
        try {
            if (object instanceof JSONArray) {
                for (int cnt = 0; cnt < count; cnt++) {
                    modelList.add(doJson(((JSONArray) object).getJSONObject(cnt)));
                }
            } else {
                modelList.add(doJson((JSONObject) object));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return modelList;
    }

    //실제로 파싱을 하여 model의 setter로 추가하는 메소드입니다.
    public DataModel doJson(JSONObject jsonStr) {
        DataModel dataModel = new DataModel();


        try {
            dataModel.setTitle(jsonStr.getString("title"));
            if (jsonStr.has("addr1")) {
                dataModel.setAddr(jsonStr.getString("addr1"));
            } else {
                dataModel.setAddr("");
            }
            if (jsonStr.has("tel")) {
                dataModel.setTel(jsonStr.getString("tel"));
            } else {
                dataModel.setTel("");
            }
            dataModel.setMapx(jsonStr.getString("mapx"));
            dataModel.setMapy(jsonStr.getString("mapy"));
            if (jsonStr.has("firstimage")) {
                dataModel.setUrl(jsonStr.getString("firstimage"));
            } else {
                dataModel.setUrl("");
            }
            if (jsonStr.has("contenttypeid")) {
                Log.i(TAG, "content id = " + jsonStr.getString("contenttypeid"));
                dataModel.setContenttypeid(jsonStr.getString("contenttypeid"));
            } else {
                dataModel.setContenttypeid("");
            }
            if(jsonStr.has("contentid")) {
                dataModel.setContentid(jsonStr.getString("contentid"));
            }else {
                dataModel.setContentid("");
            }
            if (jsonStr.has("eventstartdate")) {
                dataModel.setStartdate(jsonStr.getString("eventstartdate"));
            } else {
                dataModel.setStartdate("");
            }
            if (jsonStr.has("eventenddate")) {
                dataModel.setEnddate(jsonStr.getString("eventenddate"));
            } else {
                dataModel.setEnddate("");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataModel;
    }

    //dfs를 이용하여 최단거리를 구했습니다. 우선 입력한 스팟의 갯수에 따라 다르게 움직입니다.
    //입력한스팟이 여행일정보다 숫자로서 작다면 하루에 2개씩을 추가하여 추천해주며 입력한 스팟이 없을경우에도 추천을 해줍니다.
    //입력한 스팟이 여행일정보다 숫자로서 크다면 여행일정*3에서 입력한 스팟의 갯수로 나눕니다. 스팟의 갯수로 나눈 나머지는 몫+1을 한만큼 추가로
    //여행지를 제공해주어야 하는 숫자입니다 예를들어 5일의 일정동안 10개를 입력하였다면 부족한 5개를 컴퓨터가 알아볼수 있게 하려면
    //15/10 = 몫은 1이고 나머지는 5입니다. 나머지의 갯수인 5개만큼은 몫+1인 2개씩 정보를 제공해주어야하고 나머지에서 먼저 센 갯수를 제외한 나머지 5개는
    //몫만큼 하루에 제공을해주면 됩니다.
    //이러한 구조로 돌아가는 형식이며 알고리즘은 입력한 스팟의 모든 좌표를 구합니다 그리고 그 좌표를 이용해 각좌표들 사이의 거리를 다구해 map을 완성합니다.
    //완성된 map을 이용하여 backtracking으로 최단거리를 구합니다.
    public class getLocation extends AsyncTask<String, Void, Boolean> {
        ArrayList<String> userInputSpotList;
        ArrayList<DataModel> keywordmodelList;
        String[][] locationXY;
        FunctionPlan2Adapter adapter;
        String city;
        double dfs_min;
        int userInputSpotCount, date;
        int[][] dfs_map;
        boolean[] dfs_visit;
        Stack<Integer> dfs_node;
        Stack<Integer> dfs_shortestNode;

        public getLocation(FunctionPlan2Adapter adapter, int date, String city, ArrayList<String> list) {
            this.adapter = adapter;
            this.date = date;
            this.city = city;
            keywordmodelList = new ArrayList<>();
            userInputSpotList = (ArrayList<String>) list.clone();
            userInputSpotCount = list.size();
            locationXY = new String[list.size()][2];
            dfs_map = new int[list.size()][list.size()];
            dfs_min = 999999;
            dfs_visit = new boolean[list.size()];
            Arrays.fill(dfs_visit, Boolean.FALSE);
            dfs_node = new Stack<>();
            dfs_shortestNode = new Stack<>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //
        @Override
        protected Boolean doInBackground(String... spot) {
            String strr;
            try {
                for (int i = 0; i < userInputSpotCount; i++) {
                    String[] keyword = new String[]{userInputSpotList.get(i)};
                    URL url = new URL(makeUrl(KEYWORD, keyword));
                    Object object = commonParsing(httpConnect(url));
                    //스팟리스트의 좌표 구하기.
                    if (object instanceof JSONArray) {
                        JSONArray jsonArray = (JSONArray) object;
                        for (int m = 0; m < jsonArray.length(); m++) {
                            JSONObject temp = jsonArray.getJSONObject(m);
                            if ((temp.getString("title")).equals(keyword[0])) {
                                keywordmodelList.add(doJson(temp));
                                locationXY[i][0] = temp.getString("mapx");
                                locationXY[i][1] = temp.getString("mapy");
                                break;
                            }
                        }
                    } else if (object instanceof JSONObject) {
                        JSONObject temp = (JSONObject) object;
                        if ((temp.getString("title")).equals(keyword[0])) {
                            keywordmodelList.add(doJson(temp));
                            locationXY[i][0] = temp.getString("mapx");
                            locationXY[i][1] = temp.getString("mapy");
                        }
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);

            //거리맵그리기
            for (int cnt = 0; cnt < userInputSpotCount; cnt++) {
                Location location = new Location("start");
                if (locationXY[cnt][0] != null) {
                    location.setLatitude(Double.parseDouble(locationXY[cnt][0]));
                    location.setLongitude(Double.parseDouble(locationXY[cnt][1]));
                }
                for (int j = 0; j < userInputSpotList.size(); j++) {
                    Location tmp = new Location("end");
                    if (locationXY[j][0] != null) {
                        tmp.setLatitude(Double.parseDouble(locationXY[j][0]));
                        tmp.setLongitude(Double.parseDouble(locationXY[j][1]));
                    }
                    dfs_map[cnt][j] = (int) location.distanceTo(tmp) / 1000;
                }
            }
            dfs_node.add(0);
            //최단거리찾기 시작
            dfs(0, 0);

            //입력한 거점 갯수가 날짜보다 작을때!
            if (userInputSpotCount <= date) {
                for (int cnt = 0; cnt < date; cnt++) {
                    //내 입력 목록갯수 * 3만큼 구하고 좌표기반으로 탐색..
                    if (cnt < userInputSpotCount) {
                        try {
                            //keywordmodellist => 원래의 관광지 모델.
                            //좌표탐색은 좌표를 기준으로 그주변이 나오는기능이라 원래모델을 먼저넣어주어야함.
                            //좌표가 저장되어있는 배열에서 최단거리로 여행할수 있는 순서대로 찾아서 파라미터로 넘겨준다.
                            modelList.add(keywordmodelList.get(dfs_shortestNode.get(cnt)));
                            String[] arr = new String[]{locationXY[dfs_shortestNode.get(cnt)][0], locationXY[dfs_shortestNode.get(cnt)][1]};
                            String result = new NetworkApi().execute(makeUrl(LOCATIONBASE, arr)).get();

                            parsingJson(commonParsing(result), 2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //입력하고 남으면
                        try {
                            String[] arr = new String[]{city};
                            //입력한 도시기반으로 검색.
                            String result = new NetworkApi().execute(makeUrl(AREABASE, arr)).get();
                            parsingJson(commonParsing(result), date - cnt);
                            break;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            adapter.update(modelList);
        }

        public class NetworkApi extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String result = null;
                try {
                    URL url = new URL(params[0]);
                    result = httpConnect(url);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }
        }

        private void dfs(int s, int weight) {
            if (dfs_node.size() == userInputSpotCount && dfs_min > weight) {
                dfs_min = weight;
                dfs_shortestNode = (Stack<Integer>) dfs_node.clone();
            }

            for (int i = 0; i < userInputSpotCount; i++) {
                if (dfs_map[s][i] != 0 && !dfs_visit[i]) {
                    dfs_visit[s] = true;
                    dfs_node.add(i);
                    dfs(i, weight + dfs_map[s][i]);
                    dfs_node.pop();
                    dfs_visit[s] = false;
                }
            }
        }

    }

    //공통적으로 사용되는 비동기처리 이며 공공api에 접근하여 데이터를 받아오는 일을합니다.
    public class CommonAsync extends AsyncTask<String, Void, String> {
        Object adapter;

        public CommonAsync(Object adapter) {
            this.adapter = adapter;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                return httpConnect(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Object object = commonParsing(s);
            if (object instanceof JSONArray) {
                parsingJson(object, ((JSONArray) object).length());
            } else {
                parsingJson(object, 0);
            }
            if (adapter instanceof MainActivityAdapter) {
                Log.i(TAG, "MainActivityAdapter");
                ((MainActivityAdapter) adapter).update(modelList);
            } else if (adapter instanceof Fragment1Adapter) {
                Log.i(TAG, "Fragment1Adapter");
                ((Fragment1Adapter) adapter).update(modelList);
            } else if (adapter instanceof Fragment2Adapter) {
                Log.i(TAG, "Fragment2Adapter");
                ((Fragment2Adapter) adapter).update(modelList);
            } else if (adapter instanceof Fragment3Adapter) {
                Log.i(TAG, "Fragment3Adapter");
                ((Fragment3Adapter) adapter).update(modelList);
            }
        }
    }
}
