import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.HttpURLConnection;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

public class Main {
    static String fromCurrency = null, toCurrency = null;
    static ArrayList<String> cachedData = new ArrayList<>();
    static PrintWriter cacheWriter;
    static URL url;
    static HttpURLConnection httpURLConnection;
    static BufferedReader httpReader, consoleReader, cacheReader;
    static ApiResponse apiResponse;
    static boolean isCachedFlag;


    public static void main(String[] args) throws IOException {

        initAndCheckDate();




        Gson gson = new GsonBuilder()
                .registerTypeAdapter(RateObject.class, new RatesDeserializer())
                .create();
        while (true) {

            url = printAndReadData();
            String s = fromCurrency + " => " + toCurrency + " : ";
            try {
                for (int i = 0; i < cachedData.size(); i++) {
                    if (cachedData.get(i).startsWith(s)){
                        System.out.println(cachedData.get(i));
                        isCachedFlag = true;
                    System.out.println("есть");
                    break;}
                }
                if (!isCachedFlag){
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                apiResponse = gson.fromJson(httpReader, ApiResponse.class);
                System.out.println(apiResponse.toString());
                if (!hasCache(apiResponse))
                cache(apiResponse);}
                isCachedFlag = false;
            } catch (NullPointerException e) {
                e.printStackTrace();
                System.out.println("bad url");
            } catch (IOException e){
                System.out.println("no such currency");
                break;
            }
        }

        consoleReader.close();
        cacheWriter.close();
        httpReader.close();
    }

    public static URL printAndReadData() {
        URL url = null;
        try {
            System.out.println("Enter from currency:");
            consoleReader = new BufferedReader(new InputStreamReader(System.in));
            fromCurrency = consoleReader.readLine().toUpperCase().replaceAll(" ","");
            System.out.println("Enter to currency:");
            toCurrency = consoleReader.readLine().toUpperCase().replaceAll(" ","");

        } catch (IOException e) {
            System.out.println("input data exception");
        }

        try {
            if (fromCurrency.equals(toCurrency))
                System.out.println("currency should be different");
            else
            url = new URL(" http://api.fixer.io/latest?base=" + fromCurrency + "&symbols=" + toCurrency + " ");
        } catch (MalformedURLException e) {
            System.out.println("incorrect currency");
        }
        return url;
    }
    public static void cache (ApiResponse obj){
        cacheWriter.println(obj.toString());
        cacheWriter.flush();
        Date date = new Date();
        System.out.println(date + "\n" + date.getTime());
    }
    public static boolean hasCache(ApiResponse obj){
        for (int i = 0; i < cachedData.size(); i++) {
            if (cachedData.get(i).equals(obj.toString()))
                return true;
        }
        return false;
    }
    public static void cleanCache(){

        File file = new File("cache.txt");
        if (file.exists())
            file.delete();
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("cant create new file");
        }
    }
    public static void initAndCheckDate() throws IOException {
        cacheReader = new BufferedReader(new FileReader("cache.txt"));
        String sTmp = null;
        Integer tmp = null;
        try {
            tmp = Integer.parseInt(sTmp = cacheReader.readLine());
        } catch (NumberFormatException e) {
        } catch (IOException e) {
        }
        while (cacheReader.ready())
            cachedData.add(cacheReader.readLine());
        cacheWriter = new PrintWriter("cache.txt");
        if (tmp==null||tmp>new Date().getDate()){
            cleanCache();
            cacheWriter.println(new Date().getDate());
        }
        cacheWriter.println(sTmp);
        for (int i = 0; i < cachedData.size(); i++) {
            cacheWriter.println(cachedData.get(i));
        }
        cacheWriter.flush();
    }
}
