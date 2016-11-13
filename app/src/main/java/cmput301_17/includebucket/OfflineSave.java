package cmput301_17.includebucket;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;

/**
 * Created by orlick on 11/12/16.
 *
 */
public class OfflineSave {
    //String FILENAME = "file.sav";

    public void loadFromFile(Context ctx, Request request, String FILENAME) {
        try {
            FileInputStream fis = ctx.openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            //Code taken from http://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt Sept.22,2016
           // Type listType = new TypeToken<ArrayList<NormalTweet>>(){}.getType();
           // tweetList = gson.fromJson(in, listType);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
           // tweetList = new ArrayList<NormalTweet>();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }


    public void saveRequestInFile(Context ctx, Request request, String FILENAME){
        try {
            FileOutputStream fos = ctx.openFileOutput(FILENAME, 0);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(request, writer);
            writer.flush();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

}


